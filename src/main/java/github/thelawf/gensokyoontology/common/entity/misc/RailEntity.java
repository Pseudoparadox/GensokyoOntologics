package github.thelawf.gensokyoontology.common.entity.misc;

import github.thelawf.gensokyoontology.api.Color4i;
import github.thelawf.gensokyoontology.api.util.Maybe;
import github.thelawf.gensokyoontology.common.util.math.CurveUtil;
import github.thelawf.gensokyoontology.common.util.math.RotMatrix;
import github.thelawf.gensokyoontology.core.init.EntityRegistry;
import github.thelawf.gensokyoontology.common.util.math.DerivativeInfo;
import github.thelawf.gensokyoontology.data.GSKOSerializers;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.system.CallbackI;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
//data get entity @e[type=gensokyoontology:rail,limit=1]
public class RailEntity extends Entity {
    public static final int SEGMENTS = 32;

    public static final DataParameter<Optional<UUID>> DATA_PREV_UUID = EntityDataManager.createKey(
            RailEntity.class, DataSerializers.OPTIONAL_UNIQUE_ID);
    public static final DataParameter<Optional<UUID>> DATA_TARGET_UUID = EntityDataManager.createKey(
            RailEntity.class, DataSerializers.OPTIONAL_UNIQUE_ID);

    public static final DataParameter<Quaternion> DATA_ROT = EntityDataManager.createKey(
            RailEntity.class, GSKOSerializers.QUATERNION);
    public static final DataParameter<BlockPos> DATA_NEXT_POS = EntityDataManager.createKey(
            RailEntity.class, DataSerializers.BLOCK_POS);
    public static final DataParameter<Integer> DATA_INFO = EntityDataManager.createKey(
            RailEntity.class, DataSerializers.VARINT);
    public static final DataParameter<Integer> DATA_EXIT = EntityDataManager.createKey(
            RailEntity.class, DataSerializers.VARINT);
    public static final DataParameter<Integer> DATA_ENTER = EntityDataManager.createKey(
            RailEntity.class, DataSerializers.VARINT);
    public static final DataParameter<Boolean> DATA_AUTO = EntityDataManager.createKey(
            RailEntity.class, DataSerializers.BOOLEAN);


    public int prevId;
    public int nextId;

    public RailEntity(EntityType<RailEntity> entityType, World worldIn) {
        super(entityType, worldIn);
        this.ignoreFrustumCheck = true;
        this.setNoGravity(true);
    }

    public RailEntity(World worldIn) {
        this(EntityRegistry.RAIL_ENTITY.get(), worldIn);
    }

    public static RailEntity place(World world, BlockPos pos) {
        RailEntity railEntity = new RailEntity(world);
        railEntity.setPosition(pos.getX(), pos.getY(), pos.getZ());
        world.addEntity(railEntity);
        return railEntity;
    }

    @Override
    protected void registerData() {
        this.dataManager.register(DATA_PREV_UUID, Optional.empty());
        this.dataManager.register(DATA_TARGET_UUID, Optional.empty());
        this.dataManager.register(DATA_ROT, new Quaternion(0f, 0f, 0f, 1f));
        this.dataManager.register(DATA_NEXT_POS, new BlockPos(0,0,0));
        this.dataManager.register(DATA_INFO, Info.UNIFORM.ordinal());
        this.dataManager.register(DATA_EXIT, 10);
        this.dataManager.register(DATA_ENTER, 10);
        this.dataManager.register(DATA_AUTO, true);
    }

    @Override
    public @NotNull IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void readAdditional(@NotNull CompoundNBT nbt) {
        float qx = nbt.getFloat("qx");
        float qy = nbt.getFloat("qy");
        float qz = nbt.getFloat("qz");
        float qw = nbt.getFloat("qw");

        this.setInfo(nbt.getInt("info"));
        this.setExit(nbt.getInt("exit"));
        this.setEnter(nbt.getInt("enter"));
        this.setAutoScale(nbt.getBoolean("auto"));
        this.setRotation(new Quaternion(qx, qy, qz, qw));

        if (nbt.contains("prevID")) this.setPrevId(nbt.getUniqueId("prevID"));
        if (nbt.contains("nextID")) this.setNextId(nbt.getUniqueId("nextID"));

        if (nbt.contains("targetX") && nbt.contains("targetY") && nbt.contains("targetZ")){
            this.setNextPos(new BlockPos(nbt.getInt("targetX"), nbt.getInt("targetY"), nbt.getInt("targetZ")));
        }
    }

    @Override
    public void writeAdditional(@NotNull CompoundNBT compound) {
        compound.putFloat("qx", this.getRotation().getX());
        compound.putFloat("qy", this.getRotation().getY());
        compound.putFloat("qz", this.getRotation().getZ());
        compound.putFloat("qw", this.getRotation().getW());

        this.getPrevId().ifPresent(id -> compound.putUniqueId("prevID", id));
        this.getNextId().ifPresent(id -> compound.putUniqueId("nextID", id));

        compound.putInt("exit", this.getExit());
        compound.putInt("enter", this.getEnter());
        compound.putBoolean("auto", this.isAutoScale());
        compound.putInt("info", this.getInfo().ordinal());

        compound.putInt("targetX", this.getNextPos().getX());
        compound.putInt("targetY", this.getNextPos().getY());
        compound.putInt("targetZ", this.getNextPos().getZ());

    }

    public Maybe<Entity> tryGetPrevRail(Maybe<Entity> ref) {
        if (this.world instanceof ClientWorld) {
            return ref;
        }
        ServerWorld serverWorld = (ServerWorld) this.world;
        Maybe.from(this.getPrevId())
                .map(serverWorld::getEntityByUuid)
                .ifPresent(ref::set);
        return ref;
    }

    public Maybe<Entity> tryGetNextRail(Maybe<Entity> ref) {
        if (this.world instanceof ClientWorld) {
            return ref;
        }
        ServerWorld serverWorld = (ServerWorld) this.world;
        Maybe.from(this.getNextId())
                .map(serverWorld::getEntityByUuid)
                .ifPresent(ref::set);
        return ref;
    }

    @OnlyIn(Dist.CLIENT)
    public void setPrevRailClient(RailEntity prevRail) {
        this.prevId = prevRail.getEntityId();
        this.setPrevId(prevRail.getUniqueID());
    }

    @OnlyIn(Dist.CLIENT)
    public void setNextRailClient(RailEntity nextRail) {
        this.nextId = nextRail.getEntityId();
        this.setNextId(nextRail.getUniqueID());
        this.setNextPos(nextRail.getPosition());
    }

    @OnlyIn(Dist.CLIENT)
    public Maybe<Entity> getNextRailClient(Maybe<Entity> reference){
        if (this.world.getEntityByID(this.nextId) == null){
            return reference;
        }
        reference.set(this.world.getEntityByID(this.nextId));
        return reference;
    }

    public Optional<UUID> getPrevId() {
        return this.dataManager.get(DATA_PREV_UUID);
    }
    public void setPrevId(UUID uuid) {
        this.dataManager.set(DATA_PREV_UUID, Optional.of(uuid));
    }

    public Optional<UUID> getNextId() {
        return this.dataManager.get(DATA_TARGET_UUID);
    }
    public void setNextId(UUID uuid) {
        this.dataManager.set(DATA_TARGET_UUID, Optional.of(uuid));
    }

    public void setRotation(float qx, float qy, float qz, float qw) {
        this.dataManager.set(DATA_ROT, new Quaternion(qx, qy, qz, qw));
    }

    public int getExit(){
        return this.dataManager.get(DATA_EXIT);
    }public int getEnter(){
        return this.dataManager.get(DATA_ENTER);
    }

    public void setExit(int exitScale){
        this.dataManager.set(DATA_EXIT, exitScale);
    }
    public void setEnter(int enterScale){
        this.dataManager.set(DATA_ENTER, enterScale);
    }

    public void setRotation(Quaternion rotation) {
        this.dataManager.set(DATA_ROT, rotation);
    }

    public Quaternion getRotation() {
        return this.dataManager.get(DATA_ROT);
    }

    public BlockPos getNextPos() {
        return this.dataManager.get(DATA_NEXT_POS);
    }

    public void setNextPos(BlockPos targetRailPos) {
        this.dataManager.set(DATA_NEXT_POS, targetRailPos);
    }

    public Vector3f getOrientation() {
        return new RotMatrix(this.getRotation()).tangent();
    }

    public Optional<RailEntity> getNextRail() {
        if (world.isRemote) return Optional.empty();

        ServerWorld serverWorld = (ServerWorld) world;
        AtomicReference<Optional<RailEntity>> nextRail = new AtomicReference<>();

        this.dataManager.get(DATA_TARGET_UUID).ifPresent(uuid ->
                nextRail.set(Optional.ofNullable((RailEntity) serverWorld.getEntityByUuid(uuid))));
        return nextRail.get();
    }

    public void setAutoScale(boolean autoScale){
        this.dataManager.set(DATA_AUTO, autoScale);
    }
    public boolean isAutoScale(){
        return this.dataManager.get(DATA_AUTO);
    }
    public void setInfo(Info info) {
        this.dataManager.set(DATA_INFO, info.ordinal());
    }

    public void setInfo(int infoOrdinal) {
        this.dataManager.set(DATA_INFO, infoOrdinal);
    }

    public Info getInfo() {
        return Info.values()[this.dataManager.get(DATA_INFO)];
    }

    public List<Vector3d> getSegmentPositions(@NotNull RailEntity nextRail) {
        List<Vector3d> railPositions = new LinkedList<>();
        railPositions.add(this.getPositionVec());
        for (float t = 0F; t < 1F; t += 1F / SEGMENTS) {
            Vector3d nextPos = CurveUtil.hermite3(this.getPositionVec(), nextRail.getPositionVec(),
                    this.getOrientation(), nextRail.getOrientation(), t);
            railPositions.add(nextPos);
        }
        return railPositions;
    }

    public List<Vector3d> getTangents(@NotNull RailEntity nextRail){
        List<Vector3d> positions = new LinkedList<>();
        List<Vector3d> tangents = new LinkedList<>();
        Vector3d pos = this.getPositionVec();
        for (float t = 0F; t < 1F; t += 1F / SEGMENTS) {
            Vector3d nextSegPos = CurveUtil.hermite3(pos, nextRail.getPositionVec(),
                    this.getOrientation(), nextRail.getOrientation(), t);
            positions.add(nextSegPos);
        }
        for (int i = 0; i < positions.size() - 1; i++) {
            Vector3d currentPos = positions.get(i);
            Vector3d nextPos = positions.get(i + 1);
            tangents.add(nextPos.subtract(currentPos));
        }
        return positions;
    }
// https://naxx.org/?utm_source=MV
    public List<DerivativeInfo> getDerivatives(@NotNull RailEntity nextRail){
        List<DerivativeInfo> derivativeMap = new LinkedList<>();
        Vector3d pos = this.getPositionVec();
        for (int i = 0; i < SEGMENTS; i++) {
            float t = (float) i / SEGMENTS;
            Vector3d nextSegPos = CurveUtil.hermite3(pos, nextRail.getPositionVec(),
                    this.getOrientation(), nextRail.getOrientation(), t);
            Vector3d tangent = CurveUtil.hermiteTangent(pos, nextRail.getPositionVec(),
                    new Vector3d(this.getOrientation()), new Vector3d(nextRail.getOrientation()), t);
            Vector3d curvature = CurveUtil.hermiteDerivative(this.getPositionVec(), nextRail.getPositionVec(),
                    new Vector3d(this.getOrientation()), new Vector3d(nextRail.getOrientation()), t);
            derivativeMap.add(new DerivativeInfo(nextSegPos, tangent, curvature));
        }

        return derivativeMap;
    }

    public double getRailLength(@NotNull RailEntity nextRail){
        List<Double> segmentsLength = this.getSegmentsLength(nextRail);
        double result = 0;
        for (double length : segmentsLength) {
            result += length;
        }
        return result;
    }

    public List<Double> getSegmentsLength(@NotNull RailEntity nextRail){
        List<Double> lengths = new LinkedList<>();
        List<Vector3d> railPositions = this.getSegmentPositions(nextRail);
        Vector3d pos = this.getPositionVec();
        for (Vector3d nextpos : railPositions) {
            if (nextpos.equals(pos)) continue;
            lengths.add(pos.distanceTo(nextpos));
            pos = nextpos;
        }
        return lengths;
    }

    /**
     * 递归获取所有连接的轨道实体
     * (等一下！需要判断是否形成环！)
     */
    public List<RailEntity> getConnectedRails(List<RailEntity> rails) {
//        if (this.world.isRemote) return rails;
//        RailEntity prevRail = (RailEntity) this.world.getEntityByID(this.getPrevId());
//        if (prevRail != null && rails.isEmpty()) prevRail.getConnectedRails(rails);
//        else {
//            if (this.getNextRail().isPresent()) {
//                this.getNextRail().ifPresent(nextRail -> {
//                    rails.add((RailEntity) nextRail);
//                    ((RailEntity) nextRail).getConnectedRails(rails);
//                });
//            }
//        }
        return rails;
    }


    public enum Info{
        ACCELERATION(Color4i.GREEN),
        DECELERATION(Color4i.RED),
        UNIFORM(Color4i.CYAN),
        INERTIAL(Color4i.YELLOW);

        public final Color4i color;
        Info(Color4i color) {
            this.color = color;
        }
    }

}
