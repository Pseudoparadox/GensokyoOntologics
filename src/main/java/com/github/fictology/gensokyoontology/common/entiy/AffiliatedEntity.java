package com.github.fictology.gensokyoontology.common.entiy;

import com.github.fictology.gensokyoontology.util.api.IEntityDataAccessible;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.Optional;
import java.util.UUID;

public abstract class AffiliatedEntity extends Entity implements IEntityDataAccessible {

    public static final EntityDataAccessor<Optional<EntityReference<LivingEntity>>> DATA_OWNER = SynchedEntityData.defineId(
            AffiliatedEntity.class, EntityDataSerializers.OPTIONAL_LIVING_ENTITY_REFERENCE);
    private LivingEntity owner;
    private UUID ownerUUID;

    public AffiliatedEntity(EntityType<?> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
    }

    public AffiliatedEntity(EntityType<?> entityTypeIn, LivingEntity owner, Level worldIn) {
        super(entityTypeIn, worldIn);
        this.owner = owner;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_OWNER, Optional.empty());
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput compound) {
        this.getOwner().ifPresent(entityreference -> entityreference.store(compound, "Owner"));
    }

    protected boolean ownedBy(LivingEntity entity) {
        return entity.getUUID().equals(this.ownerUUID);
    }

    public Optional<EntityReference<LivingEntity>> getOwner() {
        return this.entityData.get(DATA_OWNER);
    }

    public void setOwner(LivingEntity owner) {
        this.owner = owner;
        this.getEntityData().set(DATA_OWNER, Optional.of(EntityReference.of(owner)));
    }

    @Override
    protected void readAdditionalSaveData(ValueInput compound) {
        EntityReference<LivingEntity> entityreference = EntityReference.readWithOldOwnerConversion(compound, "Owner", this.level());
        if (entityreference != null) {
            this.entityData.set(DATA_OWNER, Optional.of(entityreference));
        } else {
            this.entityData.set(DATA_OWNER, Optional.empty());
        }
    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource damageSource, float v) {
        return false;
    }
}
