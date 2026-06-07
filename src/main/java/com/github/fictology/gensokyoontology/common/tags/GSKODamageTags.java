package com.github.fictology.gensokyoontology.common.tags;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;

public class GSKODamageTags {
    public static final TagKey<DamageType> REAL = create("real");

    private static TagKey<DamageType> create(String name) {
        return TagKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace(name));
    }
}
