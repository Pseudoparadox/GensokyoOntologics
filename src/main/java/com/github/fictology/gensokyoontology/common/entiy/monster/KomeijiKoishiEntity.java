package com.github.fictology.gensokyoontology.common.entiy.monster;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class KomeijiKoishiEntity extends RetreatableEntity{

    public static final String KEY_FAVORABILITY = "favourability";
    private int angerTime;
    private UUID angerTarget;

    public KomeijiKoishiEntity(EntityType<? extends TamableAnimal> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(2, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1D, true));
        // this.goalSelector.addGoal(3, new SummonEyeGoal(this));
        // this.goalSelector.addGoal(4, new SpellCardAttackGoal(this, flandreSpell.bossSpell));
        this.goalSelector.addGoal(4, new FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.4f));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 0.8f));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, Mob.class)));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, TsumiBukuroEntity.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, true));
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
