package com.github.fictology.gensokyoontology.common.entiy.monster;

import com.github.fictology.gensokyoontology.common.combat.YoukaiCombat;
import com.github.fictology.gensokyoontology.common.entiy.ai.goal.YoukaiEventGoal;
import com.github.fictology.gensokyoontology.data.EventCallbackInfo;
import com.mojang.datafixers.util.Pair;
import com.github.fictology.gensokyoontology.common.combat.Sorceries;
import com.github.fictology.gensokyoontology.common.entiy.ai.goal.YoukaiSorceryGoal;
import com.github.fictology.gensokyoontology.common.entiy.SpellCardEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.behavior.Swim;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FlandreScarletEntity extends YoukaiEntity{

    public static final int[][] MAX_PHASES = {{1,2,3},{1,2,3},{1,2,3}};
    public static final List<Pair<String, YoukaiCombat.SorceryAction<YoukaiEntity>>> BATTLE_PHASES = List.of(
            Pair.of("1.1", Sorceries.DESTRUCTIVE_EYE));

    public FlandreScarletEntity(EntityType<? extends TamableAnimal> type, Level worldIn) {
        super(type, worldIn);
        this.favorability = -10;
        // this.stage = new GSKOBossGoal.Stage(GSKOBossGoal.Type.SPELL_CARD_BREAKABLE,
        //         new ScarletPrisoner(worldIn, this), 500, true);
        // this.setHeldItem(Hand.MAIN_HAND, new ItemStack(ItemRegistry.CLOCK_HAND_ITEM.codec()));
    }

    @Override
    public void nextSubPhase() {

    }

    @Override
    public int[] getMaxPhases() {
        return new int[]{3};
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 0.9, true));
        this.goalSelector.addGoal(3, new YoukaiSorceryGoal<>(this, Sorceries.DESTRUCTIVE_EYE, 1, 1, 800));
        this.goalSelector.addGoal(3, new YoukaiEventGoal<>(this, Sorceries.BECOME_BAT, 2, 2, 1200));
        this.goalSelector.addGoal(4, new FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.4f));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 0.8f));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, Mob.class)));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, TsumiBukuroEntity.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, true));
        // this.targetSelector.addGoal(5, new Grief<>(this, true));
    }


    @Override
    public void tick() {
        super.tick();
    }

    // @Override
    // public void danmakuAttack(LivingEntity target) {
    //     ImmutableList<Consumer<FlandreScarletEntity>> list = ImmutableList.of(FlandreSpellAttack::laser, FlandreSpellAttack::sphere);
    //     GSKOUtil.rollFrom(list).accept(this);
    // }

    public void spellCardAttack(SpellCardEntity spellCard, int ticksIn) {
        if (spellCard == null) return;
        spellCard.onTick(this.level(), this, ticksIn);
        spellCard.setOldPosAndRot(new Vec3(this.getX(), this.getY(), this.getZ()), this.yHeadRot, this.xRotO);
        this.level().addFreshEntity(spellCard);
    }

    public boolean isDoppelganger() {
        return false;
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

    @Override
    public EventCallbackInfo subscribeAIEvent() {
        return EventCallbackInfo.onExit("exit_bat_state", 0, 1200);
    }

    @Override
    public void nextPhase() {
        if (this.getHealth() <= this.getMaxHealth()){
            this.setBattlePhase(2, 1);
        }else {
            super.nextRandomPhase();
        }
    }

    /*
    public static class Doppelganger extends FlandreScarletEntity {
        public static final int LIFE = 1000;
        public final FullCherryBlossomEntity SPELL_CARD = new FullCherryBlossomEntity(world, this);
        // public final GSKOBossGoal.Stage stage = new GSKOBossGoal.Stage(GSKOBossGoal.Type.SPELL_CARD_BREAKABLE,
        //         SPELL_CARD, 1200, true);

        public Doppelganger(EntityType<? extends TameableEntity> type, World worldIn) {
            super(EntityRegistry.FLANDRE_DOPPELDANGER.codec(), worldIn);
        }

        @Override
        protected void registerGoals() {
            this.goalSelector.addGoal(1, new SwimGoal(this));
            this.goalSelector.addGoal(2, new SitGoal(this));
            this.goalSelector.addGoal(3, new FlandreSpellAttackGoal(this, new GSKOBossGoal.Stage(GSKOBossGoal.Type.SPELL_BREAKABLE, 500, false)));
            this.goalSelector.addGoal(4, new FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F, false));
            this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 0.4F));
            this.goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 0.8F));
            this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
        }

        @Override
        public void tick() {
            super.tick();
            if (this.ticksExisted >= LIFE) this.remove();
        }

        @Override
        public boolean isDoppelganger() {
            return true;
        }
    }

 */
}
