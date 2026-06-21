package com.github.fictology.gensokyoontology.common.entiy.monster;


import com.github.fictology.gensokyoontology.common.combat.BossBattle;
import com.github.fictology.gensokyoontology.common.entiy.ai.goal.YoukaiTargetGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;


public class RumiaEntity extends YoukaiEntity{
    public RumiaEntity(EntityType<? extends TamableAnimal> type, Level worldIn) {
        super(type, worldIn);
        this.setBattlePhase(1, 1);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(2, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1D, true));
        this.goalSelector.addGoal(4, new YoukaiTargetGoal<>(this, BossBattle.WALL_SHOOT_RUMIA, 1,1, 800));
        this.goalSelector.addGoal(5, new FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 0.4f));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 0.8f));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, Mob.class)));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, TsumiBukuroEntity.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    public int[] getMaxPhases() {
        return new int[]{4};
    }

    @Override
    public void tick() {
        super.tick();
        int light = this.level().getRawBrightness(BlockPos.containing(this.getPosition(0)), 0);
        this.setInvulnerable(light < 10);
    }

    @Override
    public void nextPhase() {
        this.nextRandomPhase();
    }

    @Override
    public void nextSubPhase() {

    }


    @Override
    public void danmakuAttack(LivingEntity target) {
    }

    @Override
    public boolean isFood(ItemStack itemStack) {
        return false;
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) {
        return null;
    }
}
