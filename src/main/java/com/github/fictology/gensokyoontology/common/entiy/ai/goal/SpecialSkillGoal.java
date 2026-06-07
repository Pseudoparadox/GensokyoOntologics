package com.github.fictology.gensokyoontology.common.entiy.ai.goal;


import com.github.fictology.gensokyoontology.common.combat.SpecialSkill;
import com.github.fictology.gensokyoontology.common.entiy.monster.YoukaiEntity;

public class SpecialSkillGoal extends BattleBasedGoal {
    protected int mainPhase;
    protected int subPhase;
    protected SpecialSkill.YoukaiSkill youkaiSkill;

    public SpecialSkillGoal(YoukaiEntity entity, int mainPhase, int subPhase, SpecialSkill.YoukaiSkill youkaiSkill) {
        super(entity);
        this.mainPhase = mainPhase;
        this.subPhase = subPhase;
        this.youkaiSkill = youkaiSkill;
    }

    @Override
    public boolean canUse() {
        super.canUse();
        return this.entity.isPhaseMatches(mainPhase, subPhase);
    }

    @Override
    public void tick() {
        super.tick();
        var target = this.entity.getTarget();
        if (target == null) return;
        if (this.entity.getSensing().hasLineOfSight(target)) {
            this.entity.getNavigation().moveTo(this.entity.getTarget(), 0.7);
            this.youkaiSkill.accept(this.entity.level(), this.entity);
        }
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }
}
