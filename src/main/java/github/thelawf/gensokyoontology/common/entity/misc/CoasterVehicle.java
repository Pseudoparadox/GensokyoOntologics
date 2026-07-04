package github.thelawf.gensokyoontology.common.entity.misc;

import github.thelawf.gensokyoontology.common.util.math.GSKOMathUtil;
import github.thelawf.gensokyoontology.core.init.EntityRegistry;
import github.thelawf.gensokyoontology.core.init.ItemRegistry;
import github.thelawf.gensokyoontology.common.util.math.DerivativeInfo;
import github.thelawf.gensokyoontology.common.util.math.TimeDifferential;
import github.thelawf.gensokyoontology.data.CoasterPhysics;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CoasterVehicle extends Entity {

    public static final float GRAVITY = 0.04f;
    public static final float FRICTION = 1.0F;

    private int positionInterpSteps;
    private int progInterpSteps;


    private final Quaternion lastClientOrientation = Quaternion.ONE;
    private final Quaternion clientOrientation = Quaternion.ONE;

    public static final BlockPos NAN_POS = new BlockPos(Double.NaN, Double.NaN, Double.NaN);
    public static final BlockState AIR = Blocks.AIR.getDefaultState();

    public static final DataParameter<CoasterPhysics> DATA_PHYSICS = EntityDataManager.createKey(
            CoasterVehicle.class, CoasterPhysics.INERTIAL_STD);

    public static final DataParameter<Float> DATA_PROGRESS = EntityDataManager.createKey(
            CoasterVehicle.class, DataSerializers.FLOAT);
    public static final DataParameter<Integer> DATA_PREV_RAIL = EntityDataManager.createKey(
            CoasterVehicle.class, DataSerializers.VARINT);
    public static final DataParameter<Integer> DATA_NEXT_RAIL = EntityDataManager.createKey(
            CoasterVehicle.class, DataSerializers.VARINT);
    public static final DataParameter<Boolean> DATA_MOVING_FLAG = EntityDataManager.createKey(
            CoasterVehicle.class, DataSerializers.BOOLEAN);
    public static final DataParameter<Integer> DATA_MOTION_TICKER = EntityDataManager.createKey(
            CoasterVehicle.class, DataSerializers.VARINT);

    public CoasterVehicle(EntityType<?> type, World world) {
        super(type, world);
    }

    public CoasterVehicle(World world) {
        this(EntityRegistry.COASTER_VEHICLE.get(), world);
    }

    @Override
    protected void registerData() {
        this.dataManager.register(DATA_PROGRESS, 0.0F);
        this.dataManager.register(DATA_PREV_RAIL, 0);
        this.dataManager.register(DATA_NEXT_RAIL, 0);
        this.dataManager.register(DATA_MOVING_FLAG, false);
        this.dataManager.register(DATA_MOTION_TICKER, 0);
    }

    public void setPrevRail(RailEntity rail) {
        this.dataManager.set(DATA_PREV_RAIL, rail.getEntityId());
    }
    public void setNextRail(RailEntity rail) {
        this.dataManager.set(DATA_NEXT_RAIL, rail.getEntityId());
    }
    public void setPrevRail(int prevRailEntityId) {
        this.dataManager.set(DATA_PREV_RAIL, prevRailEntityId);
    }
    public void setNextRail(int nextRailEntityId) {
        this.dataManager.set(DATA_NEXT_RAIL, nextRailEntityId);
    }
    public Optional<RailEntity> getPrevRail() {
        Entity entity = this.world.getEntityByID(this.dataManager.get(DATA_PREV_RAIL));
        return entity instanceof RailEntity ? Optional.of((RailEntity) entity) : Optional.empty();
    }
    public Optional<RailEntity> getNextRail() {
        return this.getPrevRail().flatMap(RailEntity::getNextRail);
    }

    public void setShouldMove(boolean flag) {
        this.dataManager.set(DATA_MOVING_FLAG, flag);
    }
    public boolean shouldMove() {
        return this.dataManager.get(DATA_MOVING_FLAG);
    }

    public int getMotionTicker() {
        return this.dataManager.get(DATA_MOTION_TICKER);
    }

    public void setMotionTicker(int tick) {
        this.dataManager.set(DATA_MOTION_TICKER, tick);
    }

    public double getAcceleration() {
        return 0;
    }

    public void setAcceleration(double acceleration) {}

    public float getProgress() {
        return this.dataManager.get(DATA_PROGRESS);
    }
    public void setProgress(float progress) {
        this.dataManager.set(DATA_PROGRESS, progress);
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
    public void tick() {
        super.tick();

        if (!this.shouldMove() || this.getPassengers().isEmpty()) {
            this.setMotion(Vector3d.ZERO);
            this.setShouldMove(false);
            this.setMotionTicker(-1);
            return;
        }

        this.tickOnServer();
//        if (this.world.isRemote) {
//            Entity passenger = this.getPassengers().get(0);
//            if (this.positionInterpSteps > 0) {
////                this.interpPosOnly(this.positionInterpSteps);
//                this.positionInterpSteps--;
//            } else {
//                this.resetPositionToBB();
//            }
//
////            this.lastClientTrackProgress = this.clientTrackProgress;
//            this.lastClientOrientation.set(this.clientOrientation.getX(), this.clientOrientation.getY(),
//                    this.clientOrientation.getZ(), this.clientOrientation.getW());
//
//            float progInterpDelta = 1;
//            if (this.progInterpSteps > 0) {
//                progInterpDelta = 1 / (float) progInterpSteps;
//
//                this.progInterpSteps--;
//            }
//
//            Vector3d clientPos = new Vector3d(this.getPosX(), this.getPosY(), this.getPosZ());
//
////            boolean updatePos = this.clientTrackProgress.getOrientation(this.lastClientTrackProgress, progInterpDelta, clientPos, this.clientOrientation);
////            if (updatePos) {
////                this.setPosition(clientPos.x(), clientPos.y(), clientPos.z());
////            }
//        }
        if (!this.world.isRemote) this.tickOnServer();

//        Optional<Entity> nextRailOpt = this.getPrevRail().flatMap(RailEntity::getNextRail);
//        if (!nextRailOpt.isPresent()) return;
//        Entity entity = nextRailOpt.get();
//        if (!(entity instanceof RailEntity)) return;
//
//        RailEntity nextRail = (RailEntity) entity;
//        List<TimeDifferential> integral = this.getIntegralOfDistanceAndTime(nextRail);
//
//        // 查找当前时间对应的轨道段
//        DerivativeInfo derivative = null;
//
//        if (!this.getPassengers().isEmpty() && this.getPassengers().get(0) instanceof ServerPlayerEntity) {
//            GSKONetworking.sendToClientPlayer(
//                    new SInteractCoasterPacket(SInteractCoasterPacket.RIDING, this.getEntityId()),
//                    (ServerPlayerEntity) this.getPassengers().get(0));
//        }
//
//        for (TimeDifferential dt : integral) {
//            if (this.getMotionTicker() <= dt.timePartial) {
//                derivative = dt.derivativeInfo;
//                break;
//            }
//        }
//
//        if (derivative == null) {
//            // 已到达轨道末端
//            this.setShouldMove(false);
//            return;
//        }
//
//        Vector3d velocity = derivative.tangent;
//        this.moveCoaster(velocity);
//
//        // 更新朝向
//        this.rotationYaw = (float)Math.toDegrees(Math.atan2(velocity.z, velocity.x)) - 90;
//        this.rotationPitch = (float)Math.toDegrees(Math.atan2(velocity.y,
//                Math.sqrt(velocity.x*velocity.x + velocity.z*velocity.z)));
//        // 增加运动计时器（模拟时间流逝）
//        int currentTicker = this.getMotionTicker() + 1;
//        this.setMotionTicker(currentTicker);
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

        if (key.equals(DATA_PROGRESS)) {

        }
    }

    public void tickOnServer(){
        for (Entity passenger : this.getPassengers()) passenger.fallDistance = 0;
        Entity passenger = this.getPassengers().get(0);
        if (passenger == null) return;

        Vector3d motion;

        RailEntity start = this.getPrevRail().isPresent() ? this.getPrevRail().get(): null;
        RailEntity end = this.getNextRail().isPresent() ? this.getNextRail().get() : null;

        if (start == null) return;
        if (end == null) return;

        // 计算位置和导数
        int index = this.getMotionTicker() / 3;
        if (this.getMotionTicker() >= start.getDerivatives(end).size() * 3 - 1) return;
        DerivativeInfo derivative = start.getDerivatives(end).get(this.getMotionTicker() / 3 + 1);
        if (derivative == null) return;

        // 更新进度
        double arcLength = derivative.tangent.length();
        double motionScale = (arcLength > 1e-5) ? 1.0 / arcLength : 0;
        this.setProgress((float) (this.getMotion().length() * motionScale));

        // 轨道段切换
        if (this.getProgress() >= 1.0) {
            Optional<RailEntity> next = end.getNextRail();
            if (next.isPresent()) {
                this.setPrevRail(end);
                this.setNextRail(next.get());
                this.setProgress(this.getProgress() - 1);
            }
        }

        // 物理计算
        Vector3d tangent = derivative.tangent.normalize();
//        double velocity = -tangent.y * GRAVITY; // 每tick重力分量

        // 应用运动
        motion = tangent.scale(motionScale);
        this.setMotionTicker(this.getMotionTicker() + 1);
        this.setPositionAndUpdate(derivative.position.x,  derivative.position.y, derivative.position.z);
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

    /**
     * 获取路程对时间的积分，即通过分段曲线的长度除以瞬时速度来获取载具在通过某个分段时所需的时间，将其累加来确定过山车在哪个游戏刻抵达哪个区段。
     * @return 路程对时间的积分
     */
    public List<TimeDifferential> getIntegralOfDistanceAndTime(@NotNull RailEntity nextRail) {
        List<TimeDifferential> integral = new ArrayList<>();
        this.getPrevRail().ifPresent(prev -> {
            List<Double> lengths = prev.getSegmentsLength(nextRail);
//            List<Vector3d> tangents = prev.getTangents(nextRail);
//
//            double accumulatedTime = 0;
//            for (Vector3d tangent : tangents) {
//                double segTime = tangent.length() / 200;
//                accumulatedTime += segTime;
//                integral.add(new TimeDifferential(segTime, accumulatedTime, new DerivativeInfo(
//                        Vector3d.ZERO, tangent, Vector3d.ZERO)));
//            }
//            for (int i = 0; i < tangents.size() - 1; i++) {
//                // 避免除以零
//                double segmentTime = tangents.get(i).length() / 10;
//                accumulatedTime += segmentTime;
//
//                integral.add(new TimeDifferential(segmentTime, accumulatedTime, new DerivativeInfo(Vector3d.ZERO, tangents.get(i), Vector3d.ZERO)));
//            }
            List<DerivativeInfo> derivatives = prev.getDerivatives(nextRail);

            double accumulatedTime = 0;
            for (int i = 0; i < derivatives.size() - 1; i++) {
                DerivativeInfo derivative = derivatives.get(i);
                double segmentLength = lengths.get(i);
                // 避免除以零
                double speed = Math.max(0.01, derivative.tangent.length());
                double segmentTime = segmentLength / speed;
                accumulatedTime += segmentTime;

                integral.add(new TimeDifferential(segmentTime, accumulatedTime, derivative));
            }
        });
        return integral;
    }

    @Override
    protected void readAdditional(@NotNull CompoundNBT compound) {
        this.setPrevRail(compound.getInt("prevId"));
        this.setNextRail(compound.getInt("nextId"));
        this.setShouldMove(compound.getBoolean("shouldMove"));
        this.setMotionTicker(compound.getInt("movingTick"));
    }

    @Override
    protected void writeAdditional(@NotNull CompoundNBT compound) {
        compound.putInt("prevId", this.dataManager.get(DATA_PREV_RAIL));
        compound.putInt("nextId", this.dataManager.get(DATA_NEXT_RAIL));
        compound.putBoolean("shouldMove", this.dataManager.get(DATA_MOVING_FLAG));
        compound.putInt("movingTick", this.dataManager.get(DATA_MOTION_TICKER));
    }

    @Override
    public @NotNull IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public void moveCoaster(Vector3d velocity) {
        this.setBoundingBox(this.getBoundingBox().offset(velocity));
        this.resetPositionToBB();
    }

    public static class Progress{
        public final RailEntity startRail;
        public final RailEntity endRail;
        public final boolean orientationOnly;
        public final double t;

        public Progress(RailEntity endRail, RailEntity startRail, boolean orientationOnly, double t) {
            this.endRail = endRail;
            this.startRail = startRail;
            this.orientationOnly = orientationOnly;
            this.t = t;
        }

        public boolean getOrientation(Progress last, float delta,
                                      Vector3d posOut, Quaternion orientOut) {
            // 实现轨道位置和朝向插值计算
            return false;
        }

    }

}
