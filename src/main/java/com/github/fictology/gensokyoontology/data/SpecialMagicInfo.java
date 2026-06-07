package com.github.fictology.gensokyoontology.data;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public record SpecialMagicInfo(int selectedIndex, List<Pair<Integer, ItemStack>> enhancements) {
    public static final Codec<SpecialMagicInfo> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("selectedIndex").forGetter(info -> info.selectedIndex),
            Codec.list(Codec.pair(Codec.INT, ItemStack.CODEC)).fieldOf("enhancements").forGetter(info -> info.enhancements)
    ).apply(instance, SpecialMagicInfo::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, SpecialMagicInfo> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, info -> info.selectedIndex,
            ByteBufCodecs.fromCodec(Codec.list(Codec.pair(Codec.INT, ItemStack.CODEC))), info -> info.enhancements,
            SpecialMagicInfo::new);

    public static final SpecialMagicInfo EMPTY = new SpecialMagicInfo(0, List.of(
            Pair.of(0, ItemStack.EMPTY), Pair.of(0, ItemStack.EMPTY),
            Pair.of(0, ItemStack.EMPTY), Pair.of(0, ItemStack.EMPTY)));

    public int getStoredIntValue(int slotIndex){
        return this.enhancements().get(slotIndex).getFirst();
    }

    public Item getItem(int slotIndex){
        return this.enhancements().get(slotIndex).getSecond().getItem();
    }

    public void insertItem(int slotIndex, ItemStack stack){
        this.enhancements().set(0, Pair.of(0, stack));
    }

    public SpecialMagicInfo updateValue(int slotIndex, int cooldown){
        this.enhancements().set(slotIndex, Pair.of(cooldown, this.enhancements.get(slotIndex).getSecond()));
        return new SpecialMagicInfo(0, this.enhancements);
    }

    public SpecialMagicInfo updateStack(int slotIndex, ItemStack stack){
        this.enhancements().set(slotIndex, Pair.of(0, stack));
        return new SpecialMagicInfo(0, this.enhancements);
    }
}
