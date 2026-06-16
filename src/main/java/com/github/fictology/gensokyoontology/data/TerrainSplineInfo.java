package com.github.fictology.gensokyoontology.data;

import com.github.fictology.gensokyoontology.api.FloatPair;
import com.github.fictology.gensokyoontology.api.FloatRange;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public record TerrainSplineInfo(List<Pair<Float, Float>> terrainX, List<Pair<Float, Float>> terrainZ) {
    public static final Codec<TerrainSplineInfo> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Codec.list(Codec.pair(Codec.FLOAT, Codec.FLOAT)).fieldOf("terrainX").forGetter(TerrainSplineInfo::terrainX),
            Codec.list(Codec.pair(Codec.FLOAT, Codec.FLOAT)).fieldOf("terrainY").forGetter(TerrainSplineInfo::terrainZ)
    ).apply(inst, TerrainSplineInfo::new));

    public static final FloatPair ZERO_X_EXAMPLE = FloatPair.of("position", 0, "height", 0);
    public FloatRange getXPosRange(float position){
        var min = new AtomicReference<>(0f);
        var max = new AtomicReference<>(0f);

        for (int i = 0; i < this.terrainX.size(); i++) {
            if (i+1 > this.terrainX.size()) {
                min.set(this.terrainX.get(i-1).getFirst());
                max.set(this.terrainX.get(i).getFirst());
                break;
            }
            min.set(this.terrainX.get(i).getFirst() < position ? 0 : position);
            max.set(this.terrainX.get(i+1).getFirst());

        }

        return new FloatRange(min.get(), max.get());
    }
}
