package com.github.fictology.gensokyoontology.common.entiy.ai.goal;

import com.github.fictology.gensokyoontology.common.entiy.monster.YoukaiEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.Random;

public class BattlePhaseGoal<Y extends YoukaiEntity> extends Goal {
    protected final int mainPhase;
    protected final int subPhase;
    protected final int maxTicks;
    protected final Y youkai;
    protected int ticksExecuted;
    protected Random random;

    public BattlePhaseGoal(Y youkai, int mainPhase, int subPhase, int maxTicks) {
        this.youkai = youkai;
        this.maxTicks = maxTicks;
        this.mainPhase = mainPhase;
        this.subPhase = subPhase;
        this.random = new Random();
    }

    @Override
    public boolean canUse() {
        var target = this.youkai.getTarget();
        return target != null && target.isAlive();
    }

    @Override
    public boolean canContinueToUse() {
        return this.ticksExecuted < this.maxTicks;
    }

    @Override
    public void tick() {
        super.tick();
        var target = this.youkai.getTarget();
        if (target == null) return;
        this.youkai.getLookControl().setLookAt(target, 30.0F, 30.0F);
        if (!this.youkai.getSensing().hasLineOfSight(target)) {
            this.youkai.getNavigation().stop();
        }
    }


}
