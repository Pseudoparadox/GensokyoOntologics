package github.thelawf.gensokyoontology.data;

import com.mojang.datafixers.util.Pair;
import github.thelawf.gensokyoontology.api.INBTWriter;
import github.thelawf.gensokyoontology.api.util.CircularList;
import github.thelawf.gensokyoontology.api.util.Maybe;
import github.thelawf.gensokyoontology.common.entity.misc.RailEntity;
import github.thelawf.gensokyoontology.common.nbt.GSKONBTUtil;
import github.thelawf.gensokyoontology.common.util.GSKOUtil;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;

import java.util.*;
import java.util.stream.Stream;

public class TrackInfo extends WorldSavedData implements INBTWriter {
    private final Map<UUID, CircularList<HermiteNodeInfo>> tracks = new HashMap<>();
    private boolean isLooped;
    public static final String NAME = "TrackInfo";

    public TrackInfo(){
        super(NAME);
    }

    public Map<UUID, CircularList<HermiteNodeInfo>> tracks() {
        return this.tracks;
    }
    public void addTracks(RailEntity startRail){
        this.tracks.put(startRail.getUniqueID(), new CircularList<>());
        this.markDirty();
    }

    public void addRailNode(ServerWorld serverWorld, UUID startRailId, HermiteNodeInfo newNode){
        this.tracks.get(startRailId).addValue(newNode);
        this.markDirty();
    }

    public void replaceNode(BlockPos nodePos, HermiteNodeInfo newValue){
        boolean b = this.tracks.values().stream().anyMatch(list ->
                list.replaceIf(node -> {
                    GSKOUtil.log(node.getStartPos().toLong() == nodePos.toLong());
                    return node.getStartPos().toLong() == nodePos.toLong();
                }, newValue));
        this.markDirty();
    }

    public Optional<CircularList<HermiteNodeInfo>> findTracksBy(HermiteNodeInfo nodeInfo){
        return this.tracks.values().stream().filter(list -> {
            return list.toNodeList().stream().anyMatch(node ->{
                boolean b = node.value().getStartPos().toLong() == nodeInfo.getStartPos().toLong();
                        GSKOUtil.log(b);
                        return b;
                    }
                    );
        }).findAny();
    }
    public Optional<CircularList<HermiteNodeInfo>> findTracksBy(BlockPos nodePos){
        return this.tracks.values().stream().filter(list ->
                list.tryFind(node -> node.getStartPos().toLong() == nodePos.toLong()).isPresent()).findFirst();
    }

    public Maybe<RailEntity> tryFindFirst(World world, HermiteNodeInfo node){
        if (!(world instanceof ServerWorld)) return Maybe.empty();
        ServerWorld serverWorld = (ServerWorld) world;
        Maybe<UUID> maybe = Maybe.empty();
        Maybe<RailEntity> entity = Maybe.empty();
        this.findTracksBy(node).ifPresent(list ->
        {
            this.tracks.forEach((key, value) -> {
                GSKOUtil.log(value.equals(list));
                if (value.equals(list)) maybe.set(key);
            });
        });

        maybe.ifPresent(uuid -> entity.set((RailEntity) serverWorld.getEntityByUuid(uuid)));
        return entity;
    }


    public Maybe<HermiteNodeInfo> tryFindNext(HermiteNodeInfo prevNode){
        Maybe<HermiteNodeInfo> maybe = Maybe.empty();
        this.findTracksBy(prevNode.getStartPos()).ifPresent(list ->
        {
            GSKOUtil.log("Prev: " + prevNode);
            list.tryFind(node -> node.getStartPos().toLong() == prevNode.getStartPos().toLong())
                    .ifPresent(current -> current.tryGetNext().ifPresent(next -> {
                        GSKOUtil.log("next: " + current.value().getStartPos());
                        maybe.set(HermiteNodeInfo.of(current.value().getRailType(),
                                        current.value().getStartPos(), next.value().getStartPos().subtract(current.value().getStartPos()),
                                        current.value().rotation0(), next.value().rotation0())
                                .setPrevScale(current.value().scale0())
                                .setNextScale(next.value().scale0())
                                .setAutoSmooth(current.value().shouldAutoSmooth())
                                .setFlipNormal(current.value().shouldFlipNormal()));
                    }));
        });
        return maybe;
    }


    public void removeNode(BlockPos removedPos){
        this.findTracksBy(removedPos).ifPresent(blockPos -> this.tracks.forEach(
                (uuid, circularList) -> circularList.removeWhen(
                        node -> node.getStartPos() == removedPos)));
        this.markDirty();
    }

    /**
     * 泡利不相容原理，一个BlockPos不允许两个轨道实体出现，如果设置了同一处，则后设置的轨道覆盖前一个轨道
     */

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
            return Pair.of(compound.getUniqueId("startRail"),
                    this.readList("splines", compound, info, inbt -> (CompoundNBT) inbt));
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
