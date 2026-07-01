package github.thelawf.gensokyoontology.data;

import github.thelawf.gensokyoontology.api.INBTWriter;
import github.thelawf.gensokyoontology.api.util.CircularList;
import github.thelawf.gensokyoontology.api.util.Maybe;
import github.thelawf.gensokyoontology.common.nbt.GSKONBTUtil;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

public class TrackInfo extends WorldSavedData implements INBTWriter {
    private Map<Long, CircularList<HermiteSpineInfo>> tracks = new HashMap<>();
    private boolean isLooped;
    public static final String NAME = "TrackInfo";

    public TrackInfo(){
        super(NAME);
    }

    public Map<Long, CircularList<HermiteSpineInfo>> tracks() {
        return this.tracks;
    }

    public CircularList<HermiteSpineInfo> addTrack(BlockPos blockPos) {
        CircularList<HermiteSpineInfo> splines = new CircularList<>();
        this.tracks.put(blockPos.toLong(), splines);
        return splines;
    }

    public boolean isLooped() {
        return this.isLooped;
    }

    public TrackInfo setLooped(boolean looped) {
        isLooped = looped;
        return this;
    }

    public static Maybe<TrackInfo> tryGetInstance(World world){
        Maybe<TrackInfo> maybe = Maybe.empty();
        if (!(world instanceof ServerWorld)) {
            return maybe;
        }
        ServerWorld serverWorld = (ServerWorld) world;
        DimensionSavedDataManager storage = serverWorld.getSavedData();
        maybe.set(storage.getOrCreate(TrackInfo::new, NAME));
        return maybe;
    }

    @Override
    public void read(CompoundNBT nbt) {
        this.isLooped = nbt.getBoolean("isLooped");
        List<HermiteSpineInfo> list = this.readList("splines", nbt, HermiteSpineInfo.IDENTITY, inbt -> (CompoundNBT) inbt);

    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        Stream<CompoundNBT> stream = this.tracks.entrySet().stream().map(entry -> {
            CompoundNBT tag = new CompoundNBT();
            tag.putLong("firstPositions", entry.getKey());
            this.writeList(tag, "splines", entry.getValue().toList(), GSKONBTUtil.NBTType.COMPOUND);
            return tag;
        });

        compound.putBoolean("isLooped", this.isLooped);
        this.writeCompoundList(compound, "tracks", stream);
        return compound;
    }
}
