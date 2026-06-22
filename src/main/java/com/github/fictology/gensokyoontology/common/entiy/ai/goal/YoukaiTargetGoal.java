package com.github.fictology.gensokyoontology.common.entiy.ai.goal;

import com.github.fictology.gensokyoontology.common.combat.YoukaiCombat;
import com.github.fictology.gensokyoontology.common.entiy.monster.YoukaiEntity;
import com.github.fictology.gensokyoontology.util.GSKOUtil;
import net.minecraft.world.entity.LivingEntity;

public class YoukaiTargetGoal<Y extends YoukaiEntity> extends BattlePhaseGoal<Y> {
    public YoukaiCombat.TargetAction<Y> bossSpell;

    public YoukaiTargetGoal(Y youkai, YoukaiCombat.TargetAction<Y> bossSpell, int mainPhase, int subPhase, int maxTicks) {
        super(youkai, mainPhase, subPhase, maxTicks);
        this.bossSpell = bossSpell;
    }

    @Override
    public void tick() {
        super.tick();
        var ref = GSKOUtil.<LivingEntity>atomic();

        if (!this.tryGetTarget(ref)) return;
        if (this.youkai.getSensing().hasLineOfSight(ref.get())) {
            this.bossSpell.invoke(this.youkai.level(), this.youkai, ref.get());
            this.youkai.getNavigation().moveTo(ref.get(), 0.7);
        }
    }

    @Override
    public void start() {
        super.start();
    }


    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }
}
