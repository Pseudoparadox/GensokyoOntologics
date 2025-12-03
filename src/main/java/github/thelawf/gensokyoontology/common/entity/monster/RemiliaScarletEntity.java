package github.thelawf.gensokyoontology.common.entity.monster;

import com.google.common.collect.ImmutableList;
import github.thelawf.gensokyoontology.api.entity.ISpellCardUser;
import github.thelawf.gensokyoontology.common.entity.ai.goal.GSKOBossGoal;
import github.thelawf.gensokyoontology.common.entity.ai.goal.LaserSpiralGoal;
import github.thelawf.gensokyoontology.common.entity.ai.goal.YoukaiSkillGoal;
import github.thelawf.gensokyoontology.common.entity.combat.AbstractSpellCardEntity;
import github.thelawf.gensokyoontology.common.entity.combat.BossBattle;
import github.thelawf.gensokyoontology.common.entity.combat.RemiliaBattle;
import github.thelawf.gensokyoontology.common.entity.combat.spell.RemiliaSpellAttack;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

import java.util.List;

public class RemiliaScarletEntity extends YoukaiEntity implements ISpellCardUser {
    public RemiliaScarletEntity(EntityType<? extends TameableEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    public void nextSubPhase() {

    }

    /**
     * flowchart TD
     *     Click["点击气球图标"] --> Display["显示Ballon Rise滚动面板"]
     *     Display --> TickAnimation["循环播放时钟指针动画"]
     *     Display --> X["点击面板右上角的叉"] --> Dispose["关闭Ballon Rise面板"]
     *     Display --> Check{"检测气球爬升等级是否>0"}
     *     Check -->|"是"|PlayAnimation["播放狗狗爬升动画"]
     *     Check -->|"否"|DONOTHING["保持不变"]
     *     PlayAnimation --> Progress["狗狗爬升的同时更新进度条进度"]
     *     Progress --> SetClaimed["当进度条的进度到达下一等级时显示Claimed标签"]
     *     SetClaimed --> OnFinishRise["在爬升结束后，播放狗狗嗅嗅动画"]
     *     OnFinishRise --> ShowFinished["显示一个勾"]
     *     ShowFinished --> CanGetReward{{"检测当前状态是否能获取奖励"}}
     *     CanGetReward --> |"是"|ShouldMoveScroll{{"检测当前滚动面板位置是否需要移动"}}
     *     CanGetReward --> |"否"|Keep["保持不变"]
     *     ShouldMoveScroll -->|"是"| MoveScrollView["移动Ballon Rise滚动面板"]
     *     MoveScrollView --> QueryReward
     *     ShouldMoveScroll  --> |"否"|QueryReward["在 <楼层:奖励> 的字典中查找奖励"]
     *     QueryReward  --> DisplayReward["播放获得奖励动画"]
     *     DisplayReward --> ShowFinished
     * @return 是否应该进入下一个主要阶段
     */
    @Override
    public boolean shouldEnterNextMainPhase() {
        return false;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(2, new SitGoal(this));
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1D, true));
        this.goalSelector.addGoal(3, new YoukaiSkillGoal<>(this, RemiliaBattle.THOUSAND_KNIVES, 1, 1, 1000));
        // this.goalSelector.addGoal(3, new LaserSpiralGoal(this, new GSKOBossGoal.Stage(GSKOBossGoal.Type.NON_SPELL, 500, false)));

        this.goalSelector.addGoal(4, new FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F, false));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 0.4f));
        this.goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 0.8f));
        this.goalSelector.addGoal(8, new LookRandomlyGoal(this));

        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, CreatureEntity.class)).setCallsForHelp());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, TsumiBukuroEntity.class, true));
    }

    @Override
    public int[] getMaxPhases() {
        return new int[]{3};
    }

    @Override
    public void danmakuAttack(LivingEntity target) {
    }

    @Override
    public void spellCardAttack(AbstractSpellCardEntity spellCard, int ticksIn) {
        List<Runnable> runnables = ImmutableList.of(
                () -> RemiliaSpellAttack.tickLaserSpiral(this)
        );

        runnables.get(this.getRNG().nextInt(runnables.size())).run();
    }
}
