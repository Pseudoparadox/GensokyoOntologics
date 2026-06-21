package com.github.fictology.gensokyoontology.common.entiy.ai.goal;

import com.github.fictology.gensokyoontology.common.combat.YoukaiCombat;
import com.github.fictology.gensokyoontology.common.entiy.monster.YoukaiEntity;

public class YoukaiTargetGoal<Y extends YoukaiEntity> extends BattlePhaseGoal<Y> {
    public YoukaiCombat.TargetAction<Y> bossSpell;

    public YoukaiTargetGoal(Y youkai, YoukaiCombat.TargetAction<Y> bossSpell, int mainPhase, int subPhase, int maxTicks) {
        super(youkai, mainPhase, subPhase, maxTicks);
        this.bossSpell = bossSpell;
    }

    @Override
    public void tick() {
        super.tick();
        var target = this.youkai.getTarget();
        if (target == null) return;
        if (this.youkai.getSensing().hasLineOfSight(target)) {
            this.bossSpell.invoke(this.youkai.level(), this.youkai, target);
            this.youkai.getNavigation().moveTo(this.youkai.getTarget(), 0.7);
        }
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void stop() {
        super.stop();
        this.youkai.nextPhase();
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }
}
