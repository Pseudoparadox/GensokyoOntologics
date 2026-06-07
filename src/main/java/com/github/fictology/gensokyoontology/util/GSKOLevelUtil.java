package com.github.fictology.gensokyoontology.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

public class GSKOLevelUtil {

    public static ResourceKey<Level> getLevelDimension(String dimension) {
        return ResourceKey.create(Registries.DIMENSION, Identifier.parse(dimension));
    }

    public static boolean isEntityInDimension(Entity entity, ResourceKey<Level> dimensionKey) {
        return entity.level().dimension() == dimensionKey;
    }

    public static boolean isEntityInBiome(Player player, ResourceKey<Biome> biomeKey) {
        return true;
    }
}
