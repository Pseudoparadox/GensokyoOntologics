package com.github.fictology.gensokyoontology.common.entiy.ai.goal;


import com.github.fictology.gensokyoontology.common.combat.YoukaiCombat;
import com.github.fictology.gensokyoontology.common.entiy.monster.YoukaiEntity;

public class YoukaiSorceryGoal<Y extends YoukaiEntity> extends BattlePhaseGoal<Y> {
    protected int mainPhase;
    protected int subPhase;
    protected YoukaiCombat.SorceryAction<YoukaiEntity> youkaiSkill;

    public YoukaiSorceryGoal(Y youkai, YoukaiCombat.SorceryAction<YoukaiEntity> youkaiSkill, int mainPhase, int subPhase, int maxTicks) {
        super(youkai, mainPhase, subPhase, maxTicks);
        this.mainPhase = mainPhase;
        this.subPhase = subPhase;
        this.youkaiSkill = youkaiSkill;
    }

    @Override
    public boolean canUse() {
        super.canUse();
        return this.youkai.isPhaseMatches(mainPhase, subPhase);
    }

    @Override
    public void tick() {
        super.tick();
        var target = this.youkai.getTarget();
        if (target == null) return;
        if (this.youkai.getSensing().hasLineOfSight(target)) {
            this.youkaiSkill.invoke(this.youkai.level(), this.youkai);
        }
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }
}
