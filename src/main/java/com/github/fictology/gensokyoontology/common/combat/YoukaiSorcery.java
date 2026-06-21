package com.github.fictology.gensokyoontology.common.combat;

import com.github.fictology.gensokyoontology.common.entiy.monster.YoukaiEntity;
import net.minecraft.world.level.Level;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface YoukaiSorcery extends BiConsumer<Level, YoukaiEntity> {
    @Override
    void accept(Level level, YoukaiEntity youkai);
}