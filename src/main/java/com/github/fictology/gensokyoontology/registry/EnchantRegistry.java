package com.github.fictology.gensokyoontology.registry;

import com.github.fictology.gensokyoontology.GensokyoOntology;
import com.github.fictology.gensokyoontology.data.CooldownHaste;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class EnchantRegistry {
    // In some registration class
    public static final DeferredRegister.DataComponents ENCHANTMENT_COMPONENT_TYPES =
            DeferredRegister.createDataComponents(Registries.ENCHANTMENT_EFFECT_COMPONENT_TYPE, GensokyoOntology.MODID);

    public static final Supplier<DataComponentType<CooldownHaste>> COOLDOWN_HASTE =
            ENCHANTMENT_COMPONENT_TYPES.registerComponentType(
                    "cooldown_haste",
                    builder -> builder.persistent(CooldownHaste.CODEC)
            );
}
