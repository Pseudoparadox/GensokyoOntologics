package com.github.fictology.gensokyoontology.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public record GapInfo(ResourceKey<Level> departureWorld, BlockPos departurePos, boolean firstPlacement) {
    public static final GapInfo EMPTY = new GapInfo(null, null, false);

    public static final Codec<GapInfo> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Level.RESOURCE_KEY_CODEC.fieldOf("depatureWorld").forGetter(GapInfo::departureWorld),
            BlockPos.CODEC.fieldOf("departurePos").forGetter(GapInfo::departurePos),
            Codec.BOOL.fieldOf("firstPlacement").forGetter(GapInfo::firstPlacement)
    ).apply(instance, GapInfo::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, GapInfo> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.fromCodec(Level.RESOURCE_KEY_CODEC), GapInfo::departureWorld,
            ByteBufCodecs.fromCodec(BlockPos.CODEC), GapInfo::departurePos,
            ByteBufCodecs.BOOL, GapInfo::firstPlacement, GapInfo::new);

}
