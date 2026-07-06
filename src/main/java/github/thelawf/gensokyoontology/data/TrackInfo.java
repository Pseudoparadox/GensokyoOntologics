package github.thelawf.gensokyoontology.data;

import com.mojang.datafixers.util.Pair;
import github.thelawf.gensokyoontology.api.INBTWriter;
import github.thelawf.gensokyoontology.api.util.CircularList;
import github.thelawf.gensokyoontology.api.util.Maybe;
import github.thelawf.gensokyoontology.common.entity.misc.RailEntity;
import github.thelawf.gensokyoontology.common.nbt.GSKONBTUtil;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;

import java.util.*;
import java.util.stream.Stream;

public class TrackInfo extends WorldSavedData implements INBTWriter {
    private Map<UUID, CircularList<HermiteNodeInfo>> tracks = new HashMap<>();
    private boolean isLooped;
    public static final String NAME = "TrackInfo";

    public TrackInfo(){
        super(NAME);
    }

    public Map<UUID, CircularList<HermiteNodeInfo>> tracks() {
        return this.tracks;
    }

    public CircularList<HermiteNodeInfo> addTrack(RailEntity rail) {
        CircularList<HermiteNodeInfo> splines = new CircularList<>();
        this.tracks.put(rail.getUniqueID(), splines);
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
        List<Pair<UUID, List<HermiteNodeInfo>>> entries = this.readCompoundList("tracks", nbt, compound -> {
            HermiteNodeInfo info = HermiteNodeInfo.EMPTY;
            info.deserializeNBT(compound);
            return Pair.of(compound.getUniqueId("startRail"), this.readList("splines", compound, info, inbt -> (CompoundNBT) inbt));
        });
        if (entries.isEmpty()) return;
        this.tracks.clear();
        entries.forEach(entry -> this.tracks.put(entry.getFirst(), CircularList.from(entry.getSecond())));
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        Stream<CompoundNBT> stream = this.tracks.entrySet().stream().map(entry -> {
            CompoundNBT tag = new CompoundNBT();
            tag.putUniqueId("startRail", entry.getKey());
            this.writeList(tag, "splines", entry.getValue().toList(), GSKONBTUtil.NBTType.COMPOUND);
            return tag;
        });

        compound.putBoolean("isLooped", this.isLooped);
        this.writeCompoundList(compound, "tracks", stream);
        return compound;
    }
}
