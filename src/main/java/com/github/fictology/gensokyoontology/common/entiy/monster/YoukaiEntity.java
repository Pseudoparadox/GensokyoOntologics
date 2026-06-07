package com.github.fictology.gensokyoontology.common.entiy.monster;

import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;

public abstract class YoukaiEntity extends RetreatableEntity {

    public static final EntityDataAccessor<Boolean> DATA_RETREATED = SynchedEntityData.defineId(
            YoukaiEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<String> DATA_PHASE = SynchedEntityData.defineId(YoukaiEntity.class,
            EntityDataSerializers.STRING);
    public static final EntityDataAccessor<Integer> DATA_FAVORABILITY = SynchedEntityData.defineId(YoukaiEntity.class, EntityDataSerializers.INT);
    /**
     * 是否被退治
     */
    protected boolean isRetreated = false;
    /**
     * 好感度
     */
    protected int favorability = 0;

    // @OnlyIn(Dist.CLIENT)
    // private Animation animation = Animation.IDLE;
    protected boolean duringSpellCard = false;
    protected String battlePhase = "1.1";
    protected ServerBossEvent bossEvent;
    protected YoukaiEntity(EntityType<? extends TamableAnimal> type, Level worldIn) {
        super(type, worldIn);
        this.bossEvent = new ServerBossEvent(this.uuid, this.getName(), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.NOTCHED_10);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_PHASE, this.battlePhase);
    }

    @Override
    public void addAdditionalSaveData(ValueOutput compound) {
        super.addAdditionalSaveData(compound);
        compound.putString("battlePhase", this.getBattlePhase());
    }

    @Override
    public void readAdditionalSaveData(ValueInput compound) {
        super.readAdditionalSaveData(compound);
        compound.getString("battlePhase").ifPresent(this::setBattlePhase);
    }

    public String getBattlePhase() {
        return this.getEntityData().get(DATA_PHASE);
    }

    public void setBattlePhase(int mainPhase, int subPhase) {
        this.battlePhase = mainPhase + "." + subPhase;
        this.getEntityData().set(DATA_PHASE, this.battlePhase);
    }

    private void setBattlePhase(String battlePhase) {
        this.battlePhase = battlePhase;
        this.getEntityData().set(DATA_PHASE, this.battlePhase);
    }

    public void nextPhase(){
        var mainPhase = this.getMainPhase();
        var subPhase = this.getSubPhase();
        if ((subPhase + 1) > this.getMaxPhases()[mainPhase - 1].length) {
            if ((mainPhase + 1) > this.getMaxPhases().length) return;
            this.setBattlePhase(++mainPhase, + 1);
        }
        else {
            this.setBattlePhase(mainPhase, ++subPhase);
        }
    }

    public int getMainPhase(){
        var phase = this.getBattlePhase();
        return Integer.parseInt(phase.split("\\.")[0]);
    }

    public int getSubPhase() {
        var phase = this.getBattlePhase();
        return Integer.parseInt(phase.split("\\.")[1]);
    }

    public int[][] getMaxPhases(){
        return new int[0][];
    }
    public boolean isPhaseMatches(String phase){
        return this.getBattlePhase().equals(phase);
    }

    public boolean isPhaseMatches(int main, int sub){
        return this.getMainPhase() == main & this.getSubPhase() == sub;
    }

    @Override
    public void onRemoval(RemovalReason reason) {
        super.onRemoval(reason);
    }

    /**
     * 怪不得继承自YoukaiEntity的实体死不了呢（）原来是我之前在这里判断如果战胜了妖怪则将她驯服了啊（
     */
    public void onDeath(@NotNull DamageSource cause) {
        // if (!this.isRetreated) {
        //     this.setHealth(this.getMaxHealth());
        //     this.setOwnerId(cause.getTrueSource() instanceof PlayerEntity && cause.getTrueSource() == null ?
        //             cause.getTrueSource().getUniqueID() : null);
        //     if (this.getOwnerId() != null) this.setRetreated(true);
        //     return;
        // }
    }

    public int getFavorability() {
        return this.getEntityData().get(DATA_FAVORABILITY);
    }

    public void setFavorability(int favorabilityIn) {
        this.getEntityData().set(DATA_FAVORABILITY, favorabilityIn);
    }

    public boolean isRetreated() {
        return this.getEntityData().get(DATA_RETREATED);
    }

    public void setRetreated(boolean isRetreated) {
        this.getEntityData().set(DATA_RETREATED, isRetreated);
    }

    public boolean isDuringSpellCardAttack(boolean isDuringSpellCardAttack) {
        this.duringSpellCard = isDuringSpellCardAttack;
        return duringSpellCard;
    }
    // @OnlyIn(Dist.CLIENT)
    // public void setAnimation(Animation animation) {
    //     this.animation = animation;
    // }

    // @OnlyIn(Dist.CLIENT)
    // public Animation getAnimation() {
    //     return animation;
    // }

    public abstract void danmakuAttack(LivingEntity target);

    public DamageSource createDamage(ResourceKey<DamageType> damageType) {
        return new DamageSource(this.level().registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(damageType));
    }

    @Override
    public void startSeenByPlayer(@NotNull ServerPlayer serverPlayer) {
        super.startSeenByPlayer(serverPlayer);
        this.bossEvent.addPlayer(serverPlayer);
    }

    @Override
    public void stopSeenByPlayer(@NotNull ServerPlayer serverPlayer) {
        super.stopSeenByPlayer(serverPlayer);
        this.bossEvent.removePlayer(serverPlayer);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
    }
}
