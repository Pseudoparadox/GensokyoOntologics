package com.github.fictology.gensokyoontology.common.entiy.monster;

import com.github.fictology.gensokyoontology.common.entiy.ai.goal.BattlePhaseGoal;
import net.minecraft.core.registries.Registries;
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
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;

public abstract class YoukaiEntity extends RetreatableEntity implements Enemy {

//    public static final EntityDataAccessor<Boolean> DATA_RETREATED = SynchedEntityData.defineId(
//            YoukaiEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<String> DATA_PHASE = SynchedEntityData.defineId(YoukaiEntity.class,
            EntityDataSerializers.STRING);
//    public static final EntityDataAccessor<Integer> DATA_FAVORABILITY = SynchedEntityData.defineId(YoukaiEntity.class, EntityDataSerializers.INT);
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
    protected ServerBossEvent bossEvent;
    protected YoukaiEntity(EntityType<? extends TamableAnimal> type, Level worldIn) {
        super(type, worldIn);
        this.bossEvent = new ServerBossEvent(this.uuid, this.getName(), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.NOTCHED_10);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_PHASE, "1.1");
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
        this.getEntityData().set(DATA_PHASE, mainPhase + "." + subPhase);
    }

    private void setBattlePhase(String battlePhase) {
        this.getEntityData().set(DATA_PHASE, battlePhase);
    }

    /**
     * 该方法是{@link BattlePhaseGoal YoukaiBattlePhaseGoal.java}中默认调用的方法，重写该方法以自定义你想实现的切换战斗阶段的逻辑。<br>
     * 核心思路：<br>
     * 1. 判断小阶段的值是否大于当前小阶段的数量<br>
     * 2. 如果大于最大值且允许进入下一个大阶段则进入下一个大阶段<br>
     * 3. 如果小于最大值调用该实体实现的如何进入下一个小阶段的函数<br>
     */
    public void nextPhase(){
        String currentPhase = this.getBattlePhase();
        String[] parts = currentPhase.split("\\.");

        if (parts.length != 2) {
            this.setBattlePhase(1, 1);
            return;
        }

        try {
            int main = Integer.parseInt(parts[0]);
            int sub = Integer.parseInt(parts[1]);
            int maxMain = this.getMaxPhases().length;

            if (maxMain == 0) return;
            if ((sub + 1) > this.getMaxPhases()[main - 1]) {
                if ((main + 1) > maxMain) {
                    this.setBattlePhase(main, 1);
                    return;
                }
                this.setBattlePhase(++main, 1);
            }
            else this.nextSubPhase();

        } catch (NumberFormatException e) {
            this.setBattlePhase(1, 1);
        }
    }

    public abstract void nextSubPhase();

    /**
     * 如果希望BOSS的下一个小阶段是从可选阶段中随机选择一个的话，可以使用该方法覆盖{@link YoukaiEntity#nextPhase() this.nextPhase}
     */
    public void nextRandomPhase(){
        String currentPhase = this.getBattlePhase();
        String[] parts = currentPhase.split("\\.");

        if (parts.length != 2) {
            this.setBattlePhase(1, 1);
            return;
        }

        try {
            final int main = Integer.parseInt(parts[0]);
            final int currentSubPhaseCount = this.getMaxPhases()[main - 1];
            this.setBattlePhase(main, this.random.nextInt(currentSubPhaseCount) + 1);
        } catch (NumberFormatException e) {
            this.setBattlePhase(1, 1);
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

    public abstract int[] getMaxPhases();
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

//    public int getFavorability() {
//        return this.getEntityData().get(DATA_FAVORABILITY);
//    }
//
//    public void setFavorability(int favorabilityIn) {
//        this.getEntityData().set(DATA_FAVORABILITY, favorabilityIn);
//    }

//    public boolean isRetreated() {
//        return this.getEntityData().get(DATA_RETREATED);
//    }
//
//    public void setRetreated(boolean isRetreated) {
//        this.getEntityData().set(DATA_RETREATED, isRetreated);
//    }

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
