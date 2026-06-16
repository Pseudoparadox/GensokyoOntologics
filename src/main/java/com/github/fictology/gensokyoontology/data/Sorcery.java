package com.github.fictology.gensokyoontology.data;

import com.github.fictology.gensokyoontology.util.GSKOUtil;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;

import java.util.List;

public record Sorcery(Identifier id, CompoundTag properties) {
    public static final Codec<Sorcery> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Identifier.CODEC.fieldOf("id").forGetter(Sorcery::id),
            CompoundTag.CODEC.fieldOf("properties").forGetter(Sorcery::properties)
    ).apply(instance, Sorcery::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, Sorcery> STREAM_CODEC = StreamCodec.composite(
            Identifier.STREAM_CODEC, Sorcery::id,
            ByteBufCodecs.fromCodec(CompoundTag.CODEC), Sorcery::properties,
            Sorcery::new);

    public static Sorcery create(Identifier id){
        return new Sorcery(id, new CompoundTag());
    }

    public record Storage(int selectedIndex, List<Sorcery> sorceries){
        public static final Codec<Sorcery.Storage> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.intRange(0, 3).fieldOf("selected_index").forGetter(Storage::selectedIndex),
                Codec.list(Sorcery.CODEC).fieldOf("sorceries").forGetter(Storage::sorceries)
        ).apply(instance, Storage::new));

        public static final Sorcery.Storage EMPTY = new Sorcery.Storage(0, List.of(
                Sorcery.create(GSKOUtil.key("empty")),
                Sorcery.create(GSKOUtil.key("empty")),
                Sorcery.create(GSKOUtil.key("empty")),
                Sorcery.create(GSKOUtil.key("empty"))));
    }
}
