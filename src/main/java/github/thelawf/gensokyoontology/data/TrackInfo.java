package github.thelawf.gensokyoontology.data;

import com.mojang.datafixers.util.Pair;
import github.thelawf.gensokyoontology.api.INBTWriter;
import github.thelawf.gensokyoontology.api.util.CircularList;
import github.thelawf.gensokyoontology.api.util.CircularNode;
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

    public void addRailNode(UUID startRailId, HermiteNodeInfo newNode){
        this.pauliExclude(newNode.getStartPos());
        this.tracks.get(startRailId).add(newNode);
        this.markDirty();
    }

    public void replaceNodeValue(BlockPos nodePos, HermiteNodeInfo newValue){
        this.tracks.values().stream().anyMatch(list ->
                list.replaceIf(node -> node.getStartPos() == nodePos, newValue));
        this.markDirty();
    }

    public Maybe<HermiteNodeInfo> tryGetNextNode(UUID first, BlockPos prevNode){
        return this.tracks.get(first).tryFind(node -> node.getStartPos().toLong() == prevNode.toLong())
                .map(CircularNode::value);
    }
    public Maybe<CircularNode<HermiteNodeInfo>> tryGetNextNode(BlockPos prevPos){
        return Maybe.from(this.tracks.entrySet().stream().flatMap(entry -> entry.getValue().toNodeList().stream())
                .filter(node -> node.value().getStartPos().toLong() == prevPos.toLong()).findFirst());
    }

    public void removeNode(BlockPos removedPos){
        this.getAllRegisteredPos().stream().filter(this::containsPosition).findFirst()
                .ifPresent(blockPos -> this.tracks.forEach((uuid, circularList) ->
                        circularList.removeIf(node -> node.getStartPos() == removedPos)));
        this.markDirty();
    }

    private boolean containsPosition(BlockPos pos){
        return this.getAllRegisteredPos().stream().anyMatch(blockPos -> blockPos == pos);
    }


    private List<BlockPos> getAllRegisteredPos(){
        List<BlockPos> list = new ArrayList<>();
        this.tracks.forEach((uuid, circularList) -> circularList.forEach(node -> list.add(node.getStartPos())));
        return list;
    }

    /**
     * 泡利不相容原理，一个BlockPos不允许两个轨道实体出现，如果设置了同一处，则后设置的轨道覆盖前一个轨道
     */
    public void pauliExclude(BlockPos newPos){
        this.removeNode(newPos);
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
