package com.github.fictology.gensokyoontology.common.entiy.monster;


import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
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
//        this.goalSelector.addGoal(1, new SwimGoal(this));
//        this.goalSelector.addGoal(2, new SitGoal(this));
//
//        this.goalSelector.addGoal(3, new YoukaiTargetGoal<>(this, BossBattle.WALL_SHOOT_RUMIA, 1, 1, 800));
//        this.goalSelector.addGoal(3, new YoukaiTimerGoal<>(this, BossBattle.DARK_BORDER_LINE, 1, 2, 800));
//        this.goalSelector.addGoal(3, new YoukaiTargetGoal<>(this, BossBattle.DARK_SPHERE, 1, 3, 800));
//        this.goalSelector.addGoal(4, new YoukaiSkillGoal<>(this, BossBattle.BLANK_PHASE, 1, 4, 100));
        this.goalSelector.addGoal(4, new FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F));
//        this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 0.4f));
//        this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 0.8f));
//        this.goalSelector.addGoal(6, new LookRandomlyGoal(this));

//        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, CreatureEntity.class)).setCallsForHelp());
//        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, TsumiBukuroEntity.class, true));
//        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
//        this.targetSelector.addGoal(4, new ResetAngerGoal<>(this, true));
    }

    @Override
    public int[][] getMaxPhases() {
        return new int[][]{new int[]{4}};
    }

    @Override
    public void tick() {
        super.tick();
        int light = this.level().getLightEngine().getRawBrightness(BlockPos.containing(this.getPosition(0)), 0);
        this.setInvulnerable(light < 10);
    }

    @Override
    public void nextPhase() {
        this.nextRandomPhase();
    }

    private void nextRandomPhase() {
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
