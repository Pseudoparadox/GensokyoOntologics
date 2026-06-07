package com.github.fictology.gensokyoontology.data;

import com.github.fictology.gensokyoontology.util.GSKOUtil;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record Incidents(List<Pair<String, Boolean>> incidentList) implements ValueIOSerializable {
    public static final String SCARLET_MIST = GSKOUtil.identifier("scarlet_mist");
    public static final List<Pair<String, Boolean>> DATA = List.of(
            Pair.of(SCARLET_MIST, true),
            Pair.of("geyser_ghosts", true),
            Pair.of("imperishable_night", true),

            Pair.of("spring_snows", false),
            Pair.of("eternal_summer", false),
            Pair.of("fake_lunar", false),

            Pair.of("nightmare", false),
            Pair.of("lost_in_gensokyo", false),
            Pair.of("reality_or_fantasy", false));
//
//    public static final Map<String, List<ResourceKey<Biome>>> SCARLET_MIST_INFLUENCED = new ImmutableMap.Builder<String, List<ResourceKey<Biome>>>()
//            .put("scarlet_mist", List.of(GSKOBiomes.SCARLET_MANSION_PRECINCTS_KEY)).build();


    public static final Codec<Incidents> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Codec.list(Codec.pair(Codec.STRING, Codec.BOOL)).fieldOf("incidentList").forGetter(Incidents::incidentList)
    ).apply(inst, Incidents::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, Incidents> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.fromCodec(Codec.list(Codec.pair(Codec.STRING, Codec.BOOL))), Incidents::incidentList,
            Incidents::new);

    public Pair<String, Boolean> getInstance(String key) {
        return this.toInstanceMap().get(key);
    }
    public boolean isActivate(String key) {
        return this.toMap().get(key);
    }

    public void setActivate(String key, boolean isActivate) {
        this.incidentList.replaceAll(p -> {
            if (p.getFirst().equals(key)) return Pair.of(key, isActivate);
            return p;
        });
    }

    public Map<String, Boolean> toMap() {
        var map = new HashMap<String, Boolean>();
        this.incidentList.forEach(pair -> map.put(pair.getFirst(), pair.getSecond()));
        return map;
    }

    public Map<String, Pair<String, Boolean>> toInstanceMap(){
        var map = new HashMap<String, Pair<String, Boolean>>();
        this.incidentList.forEach(pair -> map.put(pair.getFirst(), pair));
        return map;
    }

    public Pair<String, Boolean> getIncident(String key){
        return toInstanceMap().get(key);
    }

    @Override
    public void serialize(ValueOutput output) {
        output.store("incidentList", CODEC, this);
    }

    @Override
    public void deserialize(ValueInput input) {
    }
}
