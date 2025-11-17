package github.thelawf.gensokyoontology.common.entity.monster;

import com.google.common.collect.ImmutableList;
import github.thelawf.gensokyoontology.api.entity.ISpellCardUser;
import github.thelawf.gensokyoontology.common.entity.ai.goal.GSKOBossGoal;
import github.thelawf.gensokyoontology.common.entity.ai.goal.LaserSpiralGoal;
import github.thelawf.gensokyoontology.common.entity.ai.goal.YoukaiCombatGoal;
import github.thelawf.gensokyoontology.common.entity.combat.AbstractSpellCardEntity;
import github.thelawf.gensokyoontology.common.entity.combat.BossBattle;
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
    public boolean shouldEnterNextMainPhase() {
        return false;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(2, new SitGoal(this));
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1D, true));
        this.goalSelector.addGoal(3, new YoukaiCombatGoal<>(this, BossBattle.THOUSAND_KNIVES, 1, 1, 1000));
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
