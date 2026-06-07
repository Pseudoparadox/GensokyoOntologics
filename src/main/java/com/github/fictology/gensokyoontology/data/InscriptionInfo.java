package com.github.fictology.gensokyoontology.data;

import com.github.fictology.gensokyoontology.registry.ItemRegistry;
import com.github.fictology.gensokyoontology.util.GSKOUtil;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record InscriptionInfo(List<Identifier> inscriptions,
                              Map<ResourceKey<Item>, ResourceKey<Item>> itemSpells) {

    public static final List<Identifier> INSCRIPTIONS = List.of(
            GSKOUtil.key("inscription_seal"),
            GSKOUtil.key("inscription_nether"),
            GSKOUtil.key("inscription_youkai"),
            GSKOUtil.key("inscription_possess"),
            GSKOUtil.key("inscription_masquerading"),
            GSKOUtil.key("inscription_tsukumogami")
    );

    public static final Map<ResourceKey<Item>, ResourceKey<Item>> ITEM_SPELLS = Util.make(() -> {
        var map = new HashMap<ResourceKey<Item>, ResourceKey<Item>>();
        map.put(ItemRegistry.HAKUREI_GOHEI.getKey(), ItemRegistry.SPELL_CARD_BLANK.getKey());
        return map;
    });

    public static final Codec<InscriptionInfo> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.list(Identifier.CODEC).fieldOf("inscriptions").forGetter(i -> i.inscriptions),
            Codec.unboundedMap(ResourceKey.codec(Registries.ITEM), ResourceKey.codec(Registries.ITEM)).fieldOf("itemSpells").forGetter(inscriptionInfo -> inscriptionInfo.itemSpells)
    ).apply(instance, InscriptionInfo::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, InscriptionInfo> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public @NotNull InscriptionInfo decode(RegistryFriendlyByteBuf buffer) {
            return new InscriptionInfo(buffer.readList(FriendlyByteBuf::readIdentifier), buffer.readMap(
                    buf -> buf.readResourceKey(Registries.ITEM),
                    (buf, itemResourceKey) -> buf.readResourceKey(Registries.ITEM)
            ));
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, InscriptionInfo info) {
            buf.writeCollection(info.inscriptions, FriendlyByteBuf::writeIdentifier);
            buf.writeMap(info.itemSpells, FriendlyByteBuf::writeResourceKey, FriendlyByteBuf::writeResourceKey);
        }
    };
}
