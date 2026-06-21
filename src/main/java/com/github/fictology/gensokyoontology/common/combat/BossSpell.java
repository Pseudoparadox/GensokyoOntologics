package com.github.fictology.gensokyoontology.common.combat;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface BossSpell<E extends Entity> extends BiConsumer<Level, E> {
}
