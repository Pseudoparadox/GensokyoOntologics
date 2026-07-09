package github.thelawf.gensokyoontology.data;

import github.thelawf.gensokyoontology.api.ISynchornizable;
import github.thelawf.gensokyoontology.api.INBTWriter;
import github.thelawf.gensokyoontology.api.util.CircularNode;
import github.thelawf.gensokyoontology.api.util.Maybe;
import github.thelawf.gensokyoontology.common.entity.misc.RailEntity;
import github.thelawf.gensokyoontology.common.util.EnumUtil;
import github.thelawf.gensokyoontology.common.util.math.CurveUtil;
import github.thelawf.gensokyoontology.common.util.math.RotMatrix;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class HermiteNodeInfo implements INBTWriter, ISynchornizable<CompoundNBT, HermiteNodeInfo>{
    private long startPos = 0;
    private long endPosOffset = 0;
    private boolean flipNormal = false;
    private boolean autoSmooth = true;
    private int scale0 = 10;
    private int scale1 = 10;
    private RailEntity.Type type;
    private Quaternion prevRotation;
    private Quaternion nextRotation;

    public static final HermiteNodeInfo EMPTY = new HermiteNodeInfo(RailEntity.Type.DECELERATION,
            0, 0, Quaternion.ONE, Quaternion.ONE);

    public static HermiteNodeInfo of(RailEntity.Type type, BlockPos start, BlockPos end, Quaternion startRot, Quaternion endRot){
        return new HermiteNodeInfo(type, start.toLong(), end.toLong(), startRot, endRot);
    }
    public HermiteNodeInfo(){

    }

    public static HermiteNodeInfo from(@NotNull RailEntity rail){
        return of(rail.getRailType(), rail.getPosition(), rail.getNextPos().subtract(rail.getPosition()), rail.getRotation(),
                rail.getNextRail().isPresent() ?
                        rail.getNextRail().get().getRotation() : Quaternion.ONE)
                .setPrevScale(rail.getScale0())
                .setNextScale(rail.getScale1())
                .setAutoSmooth(rail.isAutoScale())
                .setFlipNormal(rail.isFlipNormal());
    }

    public static HermiteNodeInfo copyFrom(HermiteNodeInfo node){
        return of(node.getRailType(), BlockPos.fromLong(node.startPos), BlockPos.fromLong(node.startPos),
                node.rotation0(), node.rotation1())
                .setPrevScale(node.scale0)
                .setNextScale(node.scale1)
                .setAutoSmooth(node.autoSmooth)
                .setFlipNormal(node.flipNormal);
    }

    private HermiteNodeInfo(RailEntity.Type type, long startPos, long endPosOffset, Quaternion prevRot, Quaternion nextRot) {
        this.type = type;
        this.startPos = startPos;
        this.endPosOffset = endPosOffset;
        this.prevRotation = prevRot;
        this.nextRotation = nextRot;
    }

    public Vector3d getPosOnCurve(float progress){
        return CurveUtil.hermite3(Vector3d.ZERO, this.getEndPosVec(), this.prevOrientation(), this.nextOrientation(), progress);
    }

    public Vector3d getNormalOnCurve(float progress){
        return CurveUtil.hermiteNormal(Vector3d.ZERO, this.getEndPosVec(),
                new Vector3d(this.prevOrientation()),
                new Vector3d(this.nextOrientation()), progress);
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT compound = new CompoundNBT();
        compound.putLong("endPosOffset", this.endPosOffset);

        compound.putFloat("prev_qx", this.prevRotation.getX());
        compound.putFloat("prev_qy", this.prevRotation.getY());
        compound.putFloat("prev_qz", this.prevRotation.getZ());
        compound.putFloat("prev_qw", this.prevRotation.getW());

        compound.putFloat("next_qx", this.nextRotation.getX());
        compound.putFloat("next_qy", this.nextRotation.getY());
        compound.putFloat("next_qz", this.nextRotation.getZ());
        compound.putFloat("next_qw", this.nextRotation.getW());

        compound.putInt("scale0", this.scale0);
        compound.putInt("scale1", this.scale1);
        compound.putInt("info", this.type.ordinal());
        compound.putBoolean("flip", this.flipNormal);
        compound.putBoolean("smooth", this.autoSmooth);
        return compound;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.startPos = nbt.getLong("start");
        this.endPosOffset = nbt.getLong("end_offset");

        float pqx = nbt.getFloat("prev_qx");
        float pqy = nbt.getFloat("prev_qy");
        float pqz = nbt.getFloat("prev_qz");
        float pqw = nbt.getFloat("prev_qw");

        float nqx = nbt.getFloat("next_qx");
        float nqy = nbt.getFloat("next_qy");
        float nqz = nbt.getFloat("next_qz");
        float nqw = nbt.getFloat("next_qw");

        this.prevRotation = new Quaternion(pqx, pqy, pqz, pqw);
        this.nextRotation = new Quaternion(nqx, nqy, nqz, nqw);

        this.setPrevScale(nbt.getInt("scale0"));
        this.setNextScale(nbt.getInt("scale1"));
        this.setFlipNormal(nbt.getBoolean("flip"));
        this.setAutoSmooth(nbt.getBoolean("smooth"));
        this.type = EnumUtil.readEnum(RailEntity.Type.class, nbt.getInt("info"));
    }

    @Override
    public HermiteNodeInfo copy(){
        return new HermiteNodeInfo(this.type, this.startPos, this.endPosOffset, this.prevRotation, this.nextRotation)
                .setPrevScale(this.scale0)
                .setNextScale(this.scale1)
                .setAutoSmooth(this.autoSmooth)
                .setFlipNormal(this.flipNormal);
    }

    @Override
    public void write(PacketBuffer buf, HermiteNodeInfo value) {
        buf.writeCompoundTag(value.serializeNBT());
    }

    @Override
    public HermiteNodeInfo read(PacketBuffer buf) {
        Maybe<CompoundNBT> maybe = Maybe.ofNullable(buf.readCompoundTag());
        maybe.ifPresent(this::deserializeNBT);
        return this;
    }

    @Override
    public HermiteNodeInfo copyValue(HermiteNodeInfo value) {
        return this.copy();
    }

    public BlockPos getStartPos(){
        return BlockPos.fromLong(this.startPos);
    }
    public Vector3d getStartVec(Vector3d offset){
        return Vector3d.copy(this.getStartPos()).add(offset);
    }
    public BlockPos getEndPos(){
        return BlockPos.fromLong(this.endPosOffset).add(this.getStartPos());
    }

    public Vector3d getEndOffset(){
        return Vector3d.copy(BlockPos.fromLong(this.endPosOffset));
    }
    public Vector3d getEndVec(Vector3d offset){
        return Vector3d.copy(this.getEndPos()).add(offset);
    }

    public Vector3d getEndPosVec(){
        return Vector3d.copy(this.getEndPos());
    }
    public RailEntity.Type getRailType(){
        return this.type;
    }

    public Quaternion rotation0(){
        return this.prevRotation;
    }
    public Quaternion rotation1(){
        return this.nextRotation;
    }
    public boolean shouldAutoSmooth(){
        return this.autoSmooth;
    }
    public boolean shouldFlipNormal(){
        return this.flipNormal;
    }
    public int scale0(){
        return this.scale0;
    }
    public int scale1(){
        return this.scale1;
    }

    public HermiteNodeInfo setEndOffset(long endPosOffset) {
        this.endPosOffset = endPosOffset;
        return this;
    }

    public Vector3f prevOrientation(){
        Vector3f v = RotMatrix.from(this.prevRotation).tangent();
        v.mul(this.scale0);
        return v;
    }
    public Vector3d orientation0(){
        Vector3f v = RotMatrix.from(this.prevRotation).tangent();
        v.mul(this.scale0);
        return new Vector3d(v);
    }
    public Vector3f nextOrientation(){
        Vector3f v = RotMatrix.from(this.nextRotation).tangent();
        v.mul(this.scale1);
        return v;
    }
    public Vector3d orientation(float progress){
        return CurveUtil.hermiteTangent(this.getStartVec(Vector3d.ZERO), this.getEndPosVec(), this.orientation0(), this.orientation1(), progress);
    }
    public Vector3d orientation1(){
        Vector3f v = RotMatrix.from(this.nextRotation).tangent();
        v.mul(this.scale1);
        return new Vector3d(v);
    }

    public HermiteNodeInfo setFlipNormal(boolean flipNormal) {
        this.flipNormal = flipNormal;
        return this;
    }

    public HermiteNodeInfo setAutoSmooth(boolean autoSmooth) {
        this.autoSmooth = autoSmooth;
        return this;
    }

    public HermiteNodeInfo setPrevScale(int scale0) {
        this.scale0 = scale0;
        return this;
    }

    public HermiteNodeInfo setNextScale(int scale1) {
        this.scale1 = scale1;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        HermiteNodeInfo that = (HermiteNodeInfo) obj;
        return Objects.equals(this.startPos, that.startPos) &&
                Objects.equals(this.getEndOffset(), that.getEndOffset()) &&
                Objects.equals(this.prevRotation, that.prevRotation) &&
                Objects.equals(this.nextRotation, that.nextRotation) &&
                this.getRailType() == that.getRailType();
    }

    @Override
    public int hashCode() {
        super.hashCode();
        return Objects.hash(this.startPos, this.endPosOffset, this.prevRotation, this.nextRotation, this.type);
    }
}
