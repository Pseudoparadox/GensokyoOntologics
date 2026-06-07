package com.github.fictology.gensokyoontology.common.entiy.ai.goal;

import com.github.fictology.gensokyoontology.common.entiy.monster.YoukaiEntity;
import com.github.fictology.gensokyoontology.util.api.BossSpell;
import net.minecraft.world.entity.Entity;

public class SpellCardAttackGoal extends BattleBasedGoal {
    public BossSpell<Entity> bossSpell;

    public SpellCardAttackGoal(YoukaiEntity entity, BossSpell<Entity> bossSpell) {
        super(entity);
        this.bossSpell = bossSpell;
    }

    @Override
    public void tick() {
        super.tick();
        var target = this.entity.getTarget();
        if (target == null) return;
        if (this.entity.getSensing().hasLineOfSight(target)) {
            this.bossSpell.accept(this.entity.level(), this.entity);
            this.entity.getNavigation().moveTo(this.entity.getTarget(), 0.7);
        }
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void stop() {
        super.stop();
        this.entity.nextPhase();
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }
}
