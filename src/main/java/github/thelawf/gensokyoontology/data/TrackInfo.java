package github.thelawf.gensokyoontology.data;

import com.github.tartaricacid.touhoulittlemaid.mclib.math.functions.utility.HermiteBlend;
import github.thelawf.gensokyoontology.api.INBTWriter;
import github.thelawf.gensokyoontology.api.util.CircularList;
import github.thelawf.gensokyoontology.common.nbt.GSKONBTUtil;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.storage.WorldSavedData;

public class TrackInfo extends WorldSavedData implements INBTWriter {
    public final CircularList<HermiteSpineInfo> positions;
    public final boolean isLooped;

    public TrackInfo(CircularList<HermiteSpineInfo> positions, boolean isLooped) {
        super("track_info");
        this.positions = positions;
        this.isLooped = isLooped;
    }

    public void saveToWorld(){

    }

    @Override
    public void read(CompoundNBT nbt) {
        this.readList("splines", nbt, HermiteSpineInfo.IDENTITY, inbt -> (CompoundNBT) inbt);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        this.writeList(compound, "splines", positions.toList(), GSKONBTUtil.NBTType.COMPOUND);
        return compound;
    }
}
