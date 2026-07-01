package github.thelawf.gensokyoontology.data;

import github.thelawf.gensokyoontology.api.INBTSynchornizable;
import github.thelawf.gensokyoontology.common.util.math.CurveUtil;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;

public class HermiteSpineInfo implements INBTSynchornizable<CompoundNBT, HermiteSpineInfo> {
    private long endPosOffset;
    private Vector3f exitOrientation;
    private Vector3f enterOrientation;

    public static final HermiteSpineInfo IDENTITY = new HermiteSpineInfo(0, Vector3f.XP, Vector3f.XP);

    private HermiteSpineInfo(long endPosOffset, Vector3f exitOrientation, Vector3f enterOrientation) {
        this.endPosOffset = endPosOffset;
        this.exitOrientation = exitOrientation;
        this.enterOrientation = enterOrientation;
    }

    public static HermiteSpineInfo of(BlockPos startPos, BlockPos endPos, Vector3f startFacing, Vector3f endFacing){
        return new HermiteSpineInfo(endPos.subtract(startPos).toLong(), startFacing, endFacing);
    }

    public Vector3d getPosOnCurve(float progress){
        return CurveUtil.hermite3(Vector3d.ZERO, this.getEndPosVec(), this.exitOrientation, this.enterOrientation, progress);
    }

    public Vector3d getNormalOnCurve(float progress){
        return CurveUtil.hermiteNormal(Vector3d.ZERO, this.getEndPosVec(),
                new Vector3d(this.exitOrientation),
                new Vector3d(this.enterOrientation), progress);
    }

    public BlockPos getEndBlockPos(){
        return BlockPos.fromLong(endPosOffset);
    }

    public Vector3d getEndPosVec(Vector3d offset){
        return Vector3d.copy(this.getEndBlockPos()).add(offset);
    }

    public Vector3d getEndPosVec(){
        return Vector3d.copy(this.getEndBlockPos());
    }

    public Vector3f currentOrientation(){
        return this.exitOrientation;
    }
    public Vector3f nextOrientaion(){
        return this.enterOrientation;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putLong("endPosOffset", this.endPosOffset);
        nbt.putFloat("startFacingX", this.exitOrientation.getX());
        nbt.putFloat("startFacingY", this.exitOrientation.getY());
        nbt.putFloat("startFacingZ", this.exitOrientation.getZ());
        nbt.putFloat("endFacingX", this.enterOrientation.getX());
        nbt.putFloat("endFacingY", this.enterOrientation.getY());
        nbt.putFloat("endFacingZ", this.enterOrientation.getZ());
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.endPosOffset = nbt.getLong("endPosOffset");
        this.exitOrientation = new Vector3f(
                nbt.getFloat("startFacingX"),
                nbt.getFloat("startFacingY"),
                nbt.getFloat("startFacingZ"));
        this.enterOrientation = new Vector3f(
                nbt.getFloat("endFacingX"),
                nbt.getFloat("endFacingY"),
                nbt.getFloat("endFacingZ"));
    }

    @Override
    public HermiteSpineInfo copy(){
        return new HermiteSpineInfo(this.endPosOffset, this.exitOrientation, this.enterOrientation);
    }
}
