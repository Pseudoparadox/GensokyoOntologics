package github.thelawf.gensokyoontology.common.entity.misc;

import com.mojang.datafixers.util.Pair;
import github.thelawf.gensokyoontology.api.Functions;
import github.thelawf.gensokyoontology.api.INBTWriter;
import github.thelawf.gensokyoontology.api.util.Maybe;
import github.thelawf.gensokyoontology.common.entity.AffiliatedEntity;
import github.thelawf.gensokyoontology.common.util.math.CurveUtil;
import github.thelawf.gensokyoontology.core.init.ItemRegistry;
import github.thelawf.gensokyoontology.common.util.math.DerivativeInfo;
import github.thelawf.gensokyoontology.data.CoasterPhysics;
import github.thelawf.gensokyoontology.data.HermiteNodeInfo;
import github.thelawf.gensokyoontology.data.TrackInfo;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.FloatNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class CoasterVehicle extends AffiliatedEntity implements INBTWriter {

    public static final DataParameter<CoasterPhysics> DATA_PHYSICS = EntityDataManager.createKey(
            CoasterVehicle.class, CoasterPhysics.INERTIAL_STD);
    public static final DataParameter<HermiteNodeInfo> DATA_SPLINE = EntityDataManager.createKey(
            CoasterVehicle.class, HermiteNodeInfo.EMPTY);

    // 轨道物理参数
    private static final float ACCEL_GRAVITY = 0.05f;      // 重力加速度
    private static final float FRICTION_COEFFICIENT = 0.98f; // 摩擦系数
    private static final float AIR_RESISTANCE = 0.99f;     // 空气阻力
    private static final float CONSTANT_SPEED_THRESHOLD = 0.01f; // 匀速轨道速度保持阈值


    public CoasterVehicle(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(DATA_SPLINE, HermiteNodeInfo.EMPTY);
        this.dataManager.register(DATA_PHYSICS, CoasterPhysics.INERTIAL_STD);
    }

    @Override
    protected void readAdditional(@NotNull CompoundNBT compound) {
        super.readAdditional(compound);
        HermiteNodeInfo node = HermiteNodeInfo.EMPTY.copy();
        node.deserializeNBT(compound.getCompound("node"));
        this.setPhysics(CoasterPhysics.from(compound.getCompound("physics")));
        this.setCurrentNode(node);
    }

    @Override
    protected void writeAdditional(@NotNull CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.put("node", this.getCurrentNode().serializeNBT());
        compound.put("physics", this.getPhysics().serializeNBT());
    }

    @Override
    public @NotNull IPacket<?> createSpawnPacket() {
        return super.createSpawnPacket();
    }

    @SafeVarargs
    public final <T extends INBT> void physicsSetter(Pair<String, T>... setter){
        this.setPhysics(this.getPhysics().copyAndSet(setter));
    }

    public void setPhysics(CoasterPhysics physics){
        this.dataManager.set(DATA_PHYSICS, physics);
    }
    public CoasterPhysics getPhysics(){
        return this.dataManager.get(DATA_PHYSICS);
    }
    public HermiteNodeInfo getCurrentNode(){
        return this.dataManager.get(DATA_SPLINE);
    }
    public void setCurrentNode(HermiteNodeInfo node){
        this.dataManager.set(DATA_SPLINE, node);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.world.isRemote) {
            return;
        }

        this.updatePhysics();
        this.updatePosition();
        this.checkNextRail();
    }

    private void updatePhysics() {
        switch (this.getCurrentNode().getRailType()) {
            case ACCELERATION:
                this.updateAccelerateRail();
                break;
            case DECELERATION:
                this.updateDecelerateRail();
                break;
            case INERTIAL:
                this.updateInertiaRail();
                break;
            case UNIFORM:
                this.updateConstantSpeedRail();
                break;
        }
    }

    public void setMotionSpeed(float speed){
        this.setMotion(this.getMotion().normalize().scale(speed));
    }
    public void accelerate(float acceleration){
        Vector3d unit = new Vector3d(1,1,1).scale(acceleration);
        this.setMotion(this.getMotion().add(unit));
    }
    public float progress(){
        return this.readFloatOr(CoasterPhysics.KEY_PROGRESS, this.getPhysics().serializeNBT(), 0F);
    }
    public float motionSpeed(){
        return (float) this.getMotion().length();
    }
    public float velocity(){
        return this.readFloatOr(CoasterPhysics.KEY_VELOCITY, this.getPhysics().serializeNBT(), 0F);
    }

    /**
     * 加速轨道：按加速度加速直到达到最高限速
     */
    private void updateAccelerateRail() {
        // 获取轨道的加速参数
        float trackAcceleration = 0.1f; // 可以从轨道实体获取
        float maxSpeed = 2.0f;         // 可以从轨道实体获取

        this.accelerate(trackAcceleration);

        // 限制最大速度
        if (this.getMotion().length() > maxSpeed) {
            this.setMotionSpeed(maxSpeed);
        }

        // 应用摩擦和空气阻力
        this.setMotionSpeed(maxSpeed * FRICTION_COEFFICIENT);
        this.setMotionSpeed(maxSpeed * AIR_RESISTANCE);
    }

    /**
     * 减速轨道：根据轨道长度衰减速度，使载具停在末端
     */
    private void updateDecelerateRail() {
        float remainingDistance = (1.0f - this.progress()) * getRailLength();

        // 计算需要的减速度，使载具刚好停在末端
        if (remainingDistance > 0.1f) {
            float deceleration = -(this.motionSpeed() * this.motionSpeed()) / (2 * remainingDistance);
            this.accelerate(deceleration);
        } else {
            // 接近末端，强制减速到0
            this.setMotionSpeed(this.motionSpeed() * 0.9f);
            if (Math.abs(this.motionSpeed()) < CONSTANT_SPEED_THRESHOLD) {
                this.setMotionSpeed(0);
            }
        }
    }

    /**
     * 惯性轨道：根据真实物理效果加速和减速
     */
    private void updateInertiaRail() {
        Vector3d startPos = this.getCurrentNode().getStartVec(Vector3d.ZERO);
        Vector3d endPos = this.getCurrentNode().getEndPosVec();
        Vector3d direction = endPos.subtract(startPos);

        // 计算坡度（y方向变化）
        float slope = (float) (direction.y / direction.length());

        // 重力影响：下坡加速，上坡减速
        float acceleration = ACCEL_GRAVITY * slope;
        float velocity = (this.motionSpeed() + acceleration) * AIR_RESISTANCE * FRICTION_COEFFICIENT;
        this.setMotionSpeed(velocity);

        // 防止反向滑动（在平坦轨道上）
        if (Math.abs(slope) < 0.1f && Math.abs(this.motionSpeed()) < CONSTANT_SPEED_THRESHOLD) {
            this.setMotionSpeed(0);
        }
    }

    /**
     * 匀速轨道：保持进入时的速度不变
     */
    private void updateConstantSpeedRail() {
        if (this.getCurrentNode().getRailType() == RailEntity.Type.UNIFORM) {
            this.physicsSetter(Pair.of(CoasterPhysics.KEY_VELOCITY, FloatNBT.valueOf(this.motionSpeed())));
        }

        // 保持进入时的速度不变
        this.setMotionSpeed(this.velocity());
    }

    private void updatePosition() {
        float delta = this.motionSpeed() / this.getRailLength();
        float progress = this.progress() + delta;

        // 限制进度在 [0, 1] 范围内
        if (progress < 0) progress = 0;
        if (progress > 1) progress = 1;

        // 计算Hermite曲线上的位置
        Vector3d newPosition = this.calculatePositionOnRail(progress);
        this.setPosition(newPosition.x, newPosition.y, newPosition.z);

        // 计算并应用旋转（使载具面向运动方向）
        this.updateRotation();
    }

    private Vector3d calculatePositionOnRail(float t) {
        Vector3d startPos = this.getCurrentNode().getStartVec(Vector3d.ZERO);
        Vector3d endPos = this.getCurrentNode().getEndPosVec();

        // 获取方向向量
        Vector3f startDir = this.getCurrentNode().prevOrientation().copy();
        Vector3f endDir = this.getCurrentNode().nextOrientation().copy();

        // 使用Hermite插值
        this.physicsSetter(Pair.of(CoasterPhysics.KEY_PROGRESS, FloatNBT.valueOf(t)));
        return CurveUtil.hermite3(startPos, endPos, startDir, endDir, t);
    }

    private void updateRotation() {
        CoasterPhysics physics = this.getPhysics();
        CompoundNBT tag = physics.serializeNBT();
        float progress = this.getPhysics().readFloatOr(CoasterPhysics.KEY_PROGRESS, tag, 0F);
        Vector3d tangent = this.calculateTangentOnRail(progress);

        // 将切线转换为旋转
        float yaw = (float) Math.toDegrees(Math.atan2(tangent.z, tangent.x));
        float pitch = (float) Math.toDegrees(Math.asin(tangent.y / tangent.length()));

        this.rotationYaw = yaw;
        this.rotationPitch = pitch;

    }

    private Vector3d calculateTangentOnRail(float t) {

        Vector3d startPos = this.getCurrentNode().getStartVec(Vector3d.ZERO);
        Vector3d endPos = this.getCurrentNode().getEndPosVec();

        // 获取方向向量
        Vector3f startDir = this.getCurrentNode().prevOrientation().copy();
        Vector3f endDir = this.getCurrentNode().nextOrientation().copy();

        return CurveUtil.hermiteTangent(startPos, endPos, new Vector3d(startDir), new Vector3d(endDir), t).normalize();
    }

    private float getRailLength() {

        Vector3d startPos = this.getCurrentNode().getStartVec(Vector3d.ZERO);
        Vector3d endPos = this.getCurrentNode().getEndPosVec();
        return (float) startPos.distanceTo(endPos);
    }

    public Maybe<Float> tryGetProgress(){
        Maybe<Float> maybe = Maybe.empty();
        CoasterPhysics physics = this.getPhysics();
        CompoundNBT tag = physics.serializeNBT();
        physics.tryGetValue(CoasterPhysics.KEY_PROGRESS, tag, Functions.NBT_2_FLOAT)
                .ifPresent(may -> may.ifPresent(maybe::set));
        return maybe;
    }

    private void checkNextRail() {
        this.tryGetProgress().ifPresent(progress -> {
            if (progress < 1.0F) return;
            TrackInfo.tryGetInstance(this.world).ifPresent(trackInfo -> this.tryGetOwnerId().ifPresent(uuid ->
                    trackInfo.tracks().get(uuid).tryFindValue(node ->
                            node.getStartPos() == this.getCurrentNode().getStartPos()).ifPresent(node -> {
                        this.setCurrentNode(node);
                        this.adjustVelocityForNewRail();
                        this.physicsSetter(Pair.of(CoasterPhysics.KEY_PROGRESS, FloatNBT.valueOf(0F)));
                    })));
        });
    }

    private void adjustVelocityForNewRail() {
        RailEntity.Type newType = this.getCurrentNode().getRailType();

        switch (newType) {
            case ACCELERATION:
                this.updateAccelerateRail();
                break;
            case DECELERATION:
                this.updateDecelerateRail();
                break;
            case INERTIAL:
                this.updateInertiaRail();
                break;
            case UNIFORM:
                this.updateConstantSpeedRail();
                break;
        }
    }
    @Override
    public boolean hitByEntity(Entity entityIn) {
        if (!(entityIn instanceof ServerPlayerEntity)) return false;
        ServerPlayerEntity player = (ServerPlayerEntity) entityIn;
        if (!player.isSneaking()) return false;
        if (player.isCreative()) return false;

        Block.spawnAsEntity(world, this.getPosition(), ItemRegistry.COASTER_ITEM.get().getDefaultInstance());
        this.remove();
        return true;
    }

    @Override
    public ActionResultType processInitialInteract(PlayerEntity player, Hand hand) {
        if (this.world.isRemote) return ActionResultType.PASS;
        ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
        serverPlayer.startRiding(this);
        return ActionResultType.SUCCESS;
    }


    @Override
    public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
        super.setPositionAndRotationDirect(x, y, z, yaw, pitch, posRotationIncrements, teleport);
    }

    @Override
    public void updatePassenger(Entity passenger) {
        super.updatePassenger(passenger);
    }

    @Override
    public void notifyDataManagerChange(DataParameter<?> key) {
        super.notifyDataManagerChange(key);
    }

    private DerivativeInfo interpolate(RailEntity start, RailEntity end,
                             double t) {
        Vector3d p0 = start.getPositionVec();
        Vector3d p1 = end.getPositionVec();
        Vector3d m0 = new Vector3d(start.getOrientation());
        Vector3d m1 = new Vector3d(end.getOrientation());

        // 使用Hermite插值
        double t2 = t * t;
        double t3 = t2 * t;

        double h1 = 2*t3 - 3*t2 + 1;
        double h2 = -2*t3 + 3*t2;
        double h3 = t3 - 2*t2 + t;
        double h4 = t3 - t2;

        // 位置计算
        Vector3d pos = new Vector3d(
                h1 * p0.x + h2 * p1.x + h3 * m0.x + h4 * m1.x,
                h1 * p0.y + h2 * p1.y + h3 * m0.y + h4 * m1.y,
                h1 * p0.z + h2 * p1.z + h3 * m0.z + h4 * m1.z
        );

        // 导数计算（一阶）
        double dh1 = 6*t2 - 6*t;
        double dh2 = -6*t2 + 6*t;
        double dh3 = 3*t2 - 4*t + 1;
        double dh4 = 3*t2 - 2*t;

        Vector3d deriv = new Vector3d(
                dh1 * p0.x + dh2 * p1.x + dh3 * m0.x + dh4 * m1.x,
                dh1 * p0.y + dh2 * p1.y + dh3 * m0.y + dh4 * m1.y,
                dh1 * p0.z + dh2 * p1.z + dh3 * m0.z + dh4 * m1.z
        );

        return new DerivativeInfo(pos, deriv, Vector3d.ZERO);
    }

}
