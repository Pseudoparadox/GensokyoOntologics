package com.github.fictology.gensokyoontology.common.entiy.ai.goal;

import com.github.fictology.gensokyoontology.common.entiy.monster.YoukaiEntity;
import com.github.fictology.gensokyoontology.util.GSKOUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

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
        return this.tryGetTarget(GSKOUtil.atomic());
    }

    @Override
    public boolean canContinueToUse() {
        return this.ticksExecuted < this.maxTicks;
    }

    @Override
    public void tick() {
        super.tick();
        this.ticksExecuted++;
        var ref = GSKOUtil.<LivingEntity>atomic();
        if (!this.tryGetTarget(ref)) return;

        this.youkai.getLookControl().setLookAt(ref.get(), 30.0F, 30.0F);
        if (!this.youkai.getSensing().hasLineOfSight(ref.get())) {
            this.youkai.getNavigation().stop();
        }
    }
    public boolean tryGetTarget(AtomicReference<LivingEntity> ref){
        if (this.youkai.getTarget() == null) return false;
        ref.set(this.youkai.getTarget());
        return true;
    }

    @Override
    public void stop() {
        super.stop();
        this.youkai.nextPhase();
    }
}
