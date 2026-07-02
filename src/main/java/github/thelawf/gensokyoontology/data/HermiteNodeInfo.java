package github.thelawf.gensokyoontology.data;

import github.thelawf.gensokyoontology.api.ISynchornizable;
import github.thelawf.gensokyoontology.api.INBTWriter;
import github.thelawf.gensokyoontology.api.util.Maybe;
import github.thelawf.gensokyoontology.common.entity.misc.RailEntity;
import github.thelawf.gensokyoontology.common.util.EnumUtil;
import github.thelawf.gensokyoontology.common.util.math.CurveUtil;
import github.thelawf.gensokyoontology.common.util.math.RotMatrix;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.server.ServerWorld;

import java.util.UUID;

public class HermiteNodeInfo implements INBTWriter, ISynchornizable<CompoundNBT, HermiteNodeInfo>{
    private long startPos;
    private long endPosOffset;
    private boolean flipNormal = false;
    private boolean autoSmooth = true;
    private int scale0 = 10;
    private int scale1 = 10;
    private RailEntity.Info info;
    private Quaternion prevRotation;
    private Quaternion nextRotation;

    public static final HermiteNodeInfo EMPTY = new HermiteNodeInfo(RailEntity.Info.DECELERATION,
            0, 0, Quaternion.ONE, Quaternion.ONE);

    private HermiteNodeInfo(RailEntity.Info info, long startPos, long endPosOffset, Quaternion prevRot, Quaternion nextRot) {
        this.info = info;
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
        compound.putInt("info", this.info.ordinal());
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
        this.info = EnumUtil.readEnum(RailEntity.Info.class, nbt.getInt("info"));
    }

    @Override
    public HermiteNodeInfo copy(){
        return new HermiteNodeInfo(this.info, this.startPos, this.endPosOffset, this.prevRotation, this.nextRotation)
                .setPrevScale(this.scale0)
                .setNextScale(this.scale1)
                .setAutoSmooth(this.autoSmooth)
                .setFlipNormal(this.flipNormal);
    }

    @Override
    public void write(PacketBuffer buf, HermiteNodeInfo value) {
        buf.writeCompoundTag(value.serializeNBT());
    }

    // TODO: Read from bytes
    @Override
    public HermiteNodeInfo read(PacketBuffer buf) {
        return null;
    }

    @Override
    public HermiteNodeInfo copyValue(HermiteNodeInfo value) {
        return this.copy();
    }

    public BlockPos getStartPos(){
        return BlockPos.fromLong(this.startPos);
    }
    public BlockPos getEndBlockPos(){
        return BlockPos.fromLong(this.endPosOffset).add(this.getStartPos());
    }

    public Vector3d getEndPosVec(Vector3d offset){
        return Vector3d.copy(this.getEndBlockPos()).add(offset);
    }

    public Vector3d getEndPosVec(){
        return Vector3d.copy(this.getEndBlockPos());
    }

    public Vector3f prevOrientation(){
        return RotMatrix.from(this.prevRotation).tangent();
    }
    public Vector3f nextOrientation(){
        return RotMatrix.from(this.nextRotation).tangent();
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
}
