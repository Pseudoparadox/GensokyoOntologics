package com.github.fictology.gensokyoontology.common.entiy.ai.goal;

import com.github.fictology.gensokyoontology.common.entiy.monster.YoukaiEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.Random;

public class BattleBasedGoal extends Goal {
    protected final YoukaiEntity entity;
    protected Random random;

    public BattleBasedGoal(YoukaiEntity entity) {
        this.entity = entity;
        this.random = new Random();
    }

    @Override
    public boolean canUse() {
        var target = this.entity.getTarget();
        return target != null && target.isAlive();
    }

    @Override
    public void tick() {
        super.tick();
        var target = this.entity.getTarget();
        if (target == null) return;
        this.entity.getLookControl().setLookAt(target, 30.0F, 30.0F);
        if (!this.entity.getSensing().hasLineOfSight(target)) {
            this.entity.getNavigation().stop();
        }
    }


}
