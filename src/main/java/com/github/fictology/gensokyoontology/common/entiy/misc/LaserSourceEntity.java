package com.github.fictology.gensokyoontology.common.entiy.misc;


import com.github.fictology.gensokyoontology.common.combat.GSKODamage;
import com.github.fictology.gensokyoontology.common.entiy.AffiliatedEntity;
import com.github.fictology.gensokyoontology.registry.EntityRegistry;
import com.github.fictology.gensokyoontology.util.api.IDamageHandler;
import com.github.fictology.gensokyoontology.util.api.IRayTraceReader;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

public class LaserSourceEntity extends AffiliatedEntity implements IRayTraceReader, IDamageHandler {
    public static final EntityDataAccessor<Integer> DATA_COLOR = SynchedEntityData.defineId(LaserSourceEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> DATA_LIFESPAN = SynchedEntityData.defineId(LaserSourceEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> DATA_PREPARATION = SynchedEntityData.defineId(LaserSourceEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Float> DATA_RANGE = SynchedEntityData.defineId(LaserSourceEntity.class, EntityDataSerializers.FLOAT);
    public int rgba = 0xFF0000FF;
    private int lifespan = 100;
    private int preparation = 30;
    private float range = 128;

    public LaserSourceEntity(EntityType<?> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
        this.setRGBA(0xFF0000AA);
        this.init(100, 30, 128F);
    }

    public LaserSourceEntity(Level worldIn, Entity owner) {
        super(EntityRegistry.LASER_SOURCE.get(), worldIn);
        this.setOwner((LivingEntity) owner);
        this.setRGBA(0xFF0000AA);
        this.init(100, 30, 128F);
    }


    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_LIFESPAN, this.lifespan);
        builder.define(DATA_PREPARATION, this.preparation);
        builder.define(DATA_RANGE, this.range);
        builder.define(DATA_COLOR, this.rgba);
    }


    @Override
    protected void readAdditionalSaveData(ValueInput tag) {
        super.readAdditionalSaveData(tag);
        tag.getInt("lifespan").ifPresent(this::setLifespan);
        tag.getInt("preparation").ifPresent(this::setPreparation);
        tag.getInt("rgba").ifPresent(this::setRGBA);

        this.setRange(tag.getFloatOr("range", 10F));
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("lifespan", this.lifespan);
        tag.putInt("preparation", this.preparation);
        tag.putInt("argb", this.rgba);
        tag.putFloat("range", this.range);
    }

    @Override
    public void tick() {
        super.tick();
        if (getRemainingLife() <= 0) this.setRemoved(RemovalReason.DISCARDED);
        if (!shouldEmit()) return;

        Vec3 start = this.getPosition(0);
        Vec3 end = this.getLookAngle().scale(this.range).add(start);
        var ref = new AtomicReference<EntityReference<LivingEntity>>();

        if (!this.tryGetOwner(ref)) return;
        if (this.tickCount % 2 == 0 && rayTrace(this.level(), this, start, end).isPresent()) {
            rayTrace(this.level(), this, start, end).ifPresent(entity -> {
                // 这里检测 ref 里存储的实体所有者是否和射线检测的实体是同一个实体
                if (!ref.get().matches((LivingEntity) entity))
                    hurtLiving((LivingEntity) entity, level(), GSKODamage.LASER, 5f);
            });
        }

    }


    public void init(int lifespan, int preparation, float range) {
        this.setLifespan(lifespan);
        this.setPreparation(preparation);
        this.setRange(range);
    }

    public int getLifespan() {
        return this.getEntityData().get(DATA_LIFESPAN) == 0 ? this.lifespan : this.getEntityData().get(DATA_LIFESPAN);
    }

    public void setLifespan(int lifespan) {
        this.lifespan = lifespan;
        this.getEntityData().set(DATA_LIFESPAN, lifespan);
    }

    public int getRemainingLife() {
        return getLifespan() - this.tickCount;
    }

    public int getPreparation() {
        return this.getEntityData().get(DATA_PREPARATION) == 0 ? this.preparation : this.getEntityData().get(DATA_PREPARATION);
    }

    public void setPreparation(int preparation) {
        this.preparation = preparation;
        this.getEntityData().set(DATA_PREPARATION, preparation);
    }

    public boolean shouldEmit() {
        return this.getEntityData().get(DATA_PREPARATION) >= this.getPreparation() && this.tickCount < this.getLifespan();
    }

    public float getRange() {
        return this.getEntityData().get(DATA_RANGE) == 0 ? this.range : this.getEntityData().get(DATA_RANGE);
    }

    public void setRange(float range) {
        this.range = range;
        this.getEntityData().set(DATA_RANGE, range);
    }

    public int getRGBA() {
        return this.getEntityData().get(DATA_COLOR);
    }

    public void setRGBA(int rgba) {
        this.rgba = rgba;
        this.getEntityData().set(DATA_COLOR, rgba);
    }

    public int getRed() {
        return (this.getRGBA() >> 24) & 0xFF;
    }

    public int getGreen() {
        return (this.getRGBA() >> 16) & 0xFF;
    }

    public int getBlue() {
        return (this.getRed() >> 8) & 0xFF;
    }

    public int getAlpha() {
        return this.getRGBA() & 0xFF;
    }

    @Override
    public void hurtLiving(LivingEntity hurtLiving, Level level, ResourceKey<DamageType> damageType, float amount) {
        if (level instanceof ServerLevel serverLevel)
            hurtLiving.hurtServer(serverLevel, createDamage(level, damageType), amount);
    }

    @Override
    public Identifier getTexture() {
        return null;
    }
}
