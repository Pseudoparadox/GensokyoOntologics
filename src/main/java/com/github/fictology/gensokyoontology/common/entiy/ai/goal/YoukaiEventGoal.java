package com.github.fictology.gensokyoontology.common.entiy.ai.goal;

import com.github.fictology.gensokyoontology.common.combat.YoukaiCombat;
import com.github.fictology.gensokyoontology.common.entiy.monster.YoukaiEntity;
import com.github.fictology.gensokyoontology.data.EventCallbackInfo;
import com.github.fictology.gensokyoontology.util.GSKOUtil;
import net.minecraft.world.entity.LivingEntity;

import java.util.concurrent.atomic.AtomicInteger;

/** 要想使用这个 Goal 必须要重写 {@link YoukaiEntity#subscribeAIEvent()}，否则什么都不会执行。
 * @param <Y> 妖怪实体
 */
public class YoukaiEventGoal<Y extends YoukaiEntity> extends BattlePhaseGoal<Y>{
    protected final YoukaiCombat.EventAction<Y> action;
    protected AtomicInteger timer = new AtomicInteger(0);
    public YoukaiEventGoal(Y youkai, YoukaiCombat.EventAction<Y> action, int mainPhase, int subPhase, int maxTicks) {
        super(youkai, mainPhase, subPhase, maxTicks);
        this.action = action;
    }

    @Override
    public boolean canUse() {
        return super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse();
    }

    @Override
    public void tick() {
        super.tick();
        this.timer.incrementAndGet();
        this.action.invoke(this.youkai, this.youkai.subscribeAIEvent(), this.timer);
    }
}
