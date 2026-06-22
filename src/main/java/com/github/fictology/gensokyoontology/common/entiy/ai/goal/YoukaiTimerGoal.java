package com.github.fictology.gensokyoontology.common.entiy.ai.goal;

import com.github.fictology.gensokyoontology.common.combat.YoukaiCombat;
import com.github.fictology.gensokyoontology.common.entiy.monster.YoukaiEntity;

import java.util.concurrent.atomic.AtomicInteger;

public class YoukaiTimerGoal<Y extends YoukaiEntity> extends BattlePhaseGoal<Y>{
    protected final YoukaiCombat.TimerAction<Y> action;
    private final AtomicInteger timer = new AtomicInteger(0);
    public YoukaiTimerGoal(Y youkai, YoukaiCombat.TimerAction<Y> action, int mainPhase, int subPhase, int maxTicks) {
        super(youkai, mainPhase, subPhase, maxTicks);
        this.action = action;
    }

    @Override
    public void tick() {
        super.tick();
        action.invoke(this.youkai.level(), youkai, youkai.getTarget(), this.timer);
    }

    public int getCurrentGoalTick() {
        return this.timer.get();
    }
}
