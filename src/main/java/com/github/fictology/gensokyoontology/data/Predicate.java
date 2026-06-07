package com.github.fictology.gensokyoontology.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record Predicate(boolean condition) {
    public static final Codec<Predicate> CODEC = RecordCodecBuilder.create(predicateInstance -> predicateInstance.group(
            Codec.BOOL.fieldOf("predicate").forGetter(predicate -> predicate.condition)
    ).apply(predicateInstance, Predicate::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, Predicate> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, Predicate::condition, Predicate::new);
}
