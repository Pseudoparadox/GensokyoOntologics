package com.github.fictology.gensokyoontology.data;


import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record CooldownHaste(int level) {
    public static final Codec<CooldownHaste> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("level").forGetter(CooldownHaste::level)
    ).apply(instance, CooldownHaste::new));
}
