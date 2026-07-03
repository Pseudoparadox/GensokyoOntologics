package github.thelawf.gensokyoontology.common.entity;

import github.thelawf.gensokyoontology.common.entity.misc.RailEntity;
import github.thelawf.gensokyoontology.common.util.math.CurveUtil;
import github.thelawf.gensokyoontology.common.util.math.GeometryUtil;
import github.thelawf.gensokyoontology.data.CoasterPhysics;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class CoasterVehicleEntity extends Entity {

    // 当前所在的轨道实体
    private RailEntity currentRail;
    private float progress = 0.0f;
    private float velocity = 0.0f;
    private float maxVelocity = 2.0f;
    // 当前加速度
    private float acceleration = 0.0f;

    public static final DataParameter<CoasterPhysics> DATA_PHYSICS = EntityDataManager.createKey(
            CoasterVehicleEntity.class, CoasterPhysics.INERTIAL_STD);

    // 轨道物理参数
    private static final float ACCEL_GRAVITY = 0.05f;      // 重力加速度
    private static final float FRICTION_COEFFICIENT = 0.98f; // 摩擦系数
    private static final float AIR_RESISTANCE = 0.99f;     // 空气阻力


    public CoasterVehicleEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    protected void registerData() {
        this.dataManager.register(DATA_PHYSICS, CoasterPhysics.INERTIAL_STD);
    }

    @Override
    protected void readAdditional(CompoundNBT compound) {
        CompoundNBT properties = compound.getCompound("physics");
        this.dataManager.set(DATA_PHYSICS, CoasterPhysics.from(properties));
    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {
        compound.put("physics", this.dataManager.get(DATA_PHYSICS).serializeNBT());
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public CoasterPhysics getPhysics(){
        return this.dataManager.get(DATA_PHYSICS);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.currentRail == null || this.world.isRemote) {
            return;
        }
        this.updatePhysics();
        this.updatePosition();
        this.checkNextRail();
    }

    private void updatePhysics() {
        switch (this.currentRail.getInfo()) {
            case ACCELERATION:
                updateAccelerateRail();
                break;
            case DECELERATION:
                updateDecelerateRail();
                break;
            case INERTIAL:
                updateInertiaRail();
                break;
        }
    }

    /**
     * 加速轨道：按加速度加速直到达到最高限速
     */
    private void updateAccelerateRail() {
        // 获取轨道的加速参数
        float trackAcceleration = 0.1f; // 可以从轨道实体获取
        float maxSpeed = 2.0f;         // 可以从轨道实体获取

        // 应用加速度
        this.acceleration = trackAcceleration;
        this.velocity += this.acceleration;

        // 限制最大速度
        if (this.velocity > maxSpeed) {
            this.velocity = maxSpeed;
        }

        // 应用摩擦和空气阻力
        this.velocity *= FRICTION_COEFFICIENT;
        this.velocity *= AIR_RESISTANCE;
    }

    /**
     * 减速轨道：根据轨道长度衰减速度，使载具停在末端
     */
    private void updateDecelerateRail() {
        if (this.currentRail == null) return;

        // 计算剩余距离
        float remainingDistance = (1.0f - this.progress) * getRailLength();

        // 计算需要的减速度，使载具刚好停在末端
        // 使用运动学公式：v^2 = u^2 + 2as
        // 这里我们需要 a = -(v^2)/(2s)
        if (remainingDistance > 0.1f) {
            this.acceleration = -(this.velocity * this.velocity) / (2 * remainingDistance);
            this.velocity += this.acceleration;
        } else {
            // 接近末端，强制减速到0
            this.velocity *= 0.9f;
            if (Math.abs(this.velocity) < 0.01f) {
                this.velocity = 0.0f;
            }
        }

        // 确保速度不为负
        if (this.velocity < 0) {
            this.velocity = 0;
        }
    }

    /**
     * 惯性轨道：根据真实物理效果加速和减速
     */
    private void updateInertiaRail() {
        if (this.currentRail == null) return;

        // 计算轨道斜率（用于重力加速/减速）
        Vector3d startPos = this.currentRail.getPositionVec();
        Vector3d endPos = Vector3d.copyCentered(this.currentRail.getNextPos());
        Vector3d direction = endPos.subtract(startPos);

        // 计算坡度（y方向变化）
        float slope = (float) (direction.y / direction.length());

        // 重力影响：下坡加速，上坡减速
        this.acceleration = ACCEL_GRAVITY * slope;

        // 应用加速度
        this.velocity += this.acceleration;

        // 应用摩擦和空气阻力
        this.velocity *= FRICTION_COEFFICIENT;
        this.velocity *= AIR_RESISTANCE;

        // 防止反向滑动（在平坦轨道上）
        if (Math.abs(slope) < 0.1f && Math.abs(this.velocity) < 0.01f) {
            this.velocity = 0;
        }
    }

    private void updatePosition() {
        if (this.currentRail == null) return;

        // 更新进度
        float deltaProgress = this.velocity / getRailLength();
        this.progress += deltaProgress;

        // 限制进度在 [0, 1] 范围内
        if (this.progress < 0) this.progress = 0;
        if (this.progress > 1) this.progress = 1;

        // 计算Hermite曲线上的位置
        Vector3d newPosition = calculatePositionOnRail(this.currentRail, this.progress);
        this.setPosition(newPosition.x, newPosition.y, newPosition.z);

        // 计算并应用旋转（使载具面向运动方向）
        updateRotation();
    }

    private Vector3d calculatePositionOnRail(RailEntity rail, float t) {
        Vector3d startPos = rail.getPositionVec();
        Vector3d endPos = rail.getNextPosVec();

        // 获取方向向量
        Vector3f startDir = rail.getOrientation().copy();
        Vector3f endDir = rail.getNextRail()
                .map(r -> r.getOrientation().copy())
                .orElse(startDir);

        return CurveUtil.hermite3(startPos, endPos, startDir, endDir, t);
    }

    private void updateRotation() {
        if (this.currentRail == null || this.velocity == 0) return;

        // 计算切线方向
        Vector3d tangent = calculateTangentOnRail(this.currentRail, this.progress);

        // 将切线转换为旋转
        float yaw = (float) Math.toDegrees(Math.atan2(tangent.z, tangent.x));
        float pitch = (float) Math.toDegrees(Math.asin(tangent.y / tangent.length()));

        this.rotationYaw = yaw;
        this.rotationPitch = pitch;
    }

    private Vector3d calculateTangentOnRail(RailEntity rail, float t) {
        Vector3d startPos = rail.getPositionVec();
        Vector3d endPos = rail.getNextPosVec();

        Vector3f startDir = rail.getOrientation().copy();
        Vector3f endDir = rail.getNextRail()
                .map(r -> r.getOrientation().copy())
                .orElse(startDir);

        // 使用Hermite曲线的导数作为切线
        return CurveUtil.hermiteTangent(startPos, endPos, new Vector3d(startDir), new Vector3d(endDir), t).normalize();
    }

    private float getRailLength() {
        if (this.currentRail == null) return 1.0f;

        Vector3d startPos = this.currentRail.getPositionVec();
        Vector3d endPos = this.currentRail.getNextRail().map(Entity::getPositionVec).orElse(startPos);
        return (float) startPos.distanceTo(endPos);
    }

    private void checkNextRail() {
        if (this.progress >= 1.0f && this.currentRail != null) {
            // 尝试切换到下一个轨道
            this.currentRail.getNextRail().ifPresent(nextRail -> {
                this.currentRail = nextRail;
                this.progress = 0.0f;
                this.updatePhysics();
            });
        }
    }

    private void adjustVelocityForNewRail() {
        switch (this.currentRail.getInfo()) {
            case ACCELERATION:
                // 加速轨道，保持或增加速度
                break;
            case DECELERATION:
                // 减速轨道，准备减速
                break;
            case INERTIAL:
                // 惯性轨道，保持当前速度
                break;
        }
    }

    // 公共方法：设置当前轨道
    public void setCurrentRail(RailEntity rail) {
        this.currentRail = rail;
        this.progress = 0.0f;
    }

    // 公共方法：获取当前速度
    public float getVelocity() {
        return this.velocity;
    }

    // 公共方法：设置速度
    public void setVelocity(float velocity) {
        this.velocity = velocity;
    }
}