package com.github.fictology.gensokyoontology.common.entiy.monster;


import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class LilyWhiteEntity extends YoukaiEntity {


    public LilyWhiteEntity(EntityType<? extends TamableAnimal> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    public void nextSubPhase() {

    }

    @Override
    public int[] getMaxPhases() {
        return new int[0];
    }

    @Override
    protected void registerGoals() {
        // GSKOBossGoal.Stage stage = new GSKOBossGoal.Stage(GSKOBossGoal.Type.NON_SPELL, 500, true);
        // stages.put(GSKOBossGoal.Type.SPELL_CARD_BREAKABLE, Pair.of(50f, 2000));

        // this.goalSelector.addGoal(0, new SwimGoal(this));
        // this.goalSelector.addGoal(1, new LilyWhiteBossBattleGoal(this, stage, 0.4f));
        this.goalSelector.addGoal(2, new MoveTowardsRestrictionGoal(this, 1.0));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0f));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    public void danmakuAttack(LivingEntity target) {

    }

    @Override
    public boolean isFood(ItemStack itemStack) {
        return false;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return null;
    }
}
