package github.thelawf.gensokyoontology.data;

import github.thelawf.gensokyoontology.api.INBTSynchornizable;
import github.thelawf.gensokyoontology.common.util.math.CurveUtil;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.common.util.INBTSerializable;

public class HermiteSpineInfo implements INBTSynchornizable<CompoundNBT, HermiteSpineInfo> {
    private long endPosOffset;
    private Vector3f startFacing;
    private Vector3f endFacing;

    public static final HermiteSpineInfo IDENTITY = new HermiteSpineInfo(0, Vector3f.XP, Vector3f.XP);

    private HermiteSpineInfo(long endPosOffset, Vector3f startFacing, Vector3f endFacing) {
        this.endPosOffset = endPosOffset;
        this.startFacing = startFacing;
        this.endFacing = endFacing;
    }

    public static HermiteSpineInfo of(BlockPos startPos, BlockPos endPos, Vector3f startFacing, Vector3f endFacing){
        return new HermiteSpineInfo(endPos.subtract(startPos).toLong(), startFacing, endFacing);
    }

    public Vector3d getPosOnCurve(float progress){
        return CurveUtil.hermite3(Vector3d.ZERO, this.getEndPosVec(), this.startFacing, this.endFacing, progress);
    }

    public Vector3d getNormalOnCurve(float progress){
        return CurveUtil.hermiteNormal(Vector3d.ZERO, this.getEndPosVec(),
                new Vector3d(this.startFacing),
                new Vector3d(this.endFacing), progress);
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

    public Vector3f startFacing(){
        return this.startFacing;
    }
    public Vector3f endFacing(){
        return this.endFacing;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putLong("endPosOffset", this.endPosOffset);
        nbt.putFloat("startFacingX", this.startFacing.getX());
        nbt.putFloat("startFacingY", this.startFacing.getY());
        nbt.putFloat("startFacingZ", this.startFacing.getZ());
        nbt.putFloat("endFacingX", this.endFacing.getX());
        nbt.putFloat("endFacingY", this.endFacing.getY());
        nbt.putFloat("endFacingZ", this.endFacing.getZ());
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.endPosOffset = nbt.getLong("endPosOffset");
        this.startFacing = new Vector3f(
                nbt.getFloat("startFacingX"),
                nbt.getFloat("startFacingY"),
                nbt.getFloat("startFacingZ"));
        this.endFacing = new Vector3f(
                nbt.getFloat("endFacingX"),
                nbt.getFloat("endFacingY"),
                nbt.getFloat("endFacingZ"));
    }

    @Override
    public HermiteSpineInfo copy(){
        return new HermiteSpineInfo(this.endPosOffset, this.startFacing, this.endFacing);
    }
}
