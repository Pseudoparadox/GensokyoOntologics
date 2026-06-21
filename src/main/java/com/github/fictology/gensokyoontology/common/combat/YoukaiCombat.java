package com.github.fictology.gensokyoontology.common.combat;

import com.github.fictology.gensokyoontology.common.entiy.monster.YoukaiEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

public final class YoukaiCombat {

    @FunctionalInterface
    public interface SorceryAction<Y extends YoukaiEntity>{
        void invoke(Level level, Y youkai);
    }

    @FunctionalInterface
    public interface TargetAction<Y extends YoukaiEntity> {
        void invoke(Level world, Y youkai, LivingEntity target);
    }

    @FunctionalInterface
    public interface TimerAction<Y extends YoukaiEntity> {
        void invoke(Level world, Y youkai, LivingEntity target, AtomicInteger currentTick);
    }

    @FunctionalInterface
    public interface KeyframeAction<Y extends YoukaiEntity> {
        void invoke(Map<Integer, TimerAction<Y>> map);
    }
}
