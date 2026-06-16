package com.github.fictology.gensokyoontology.common.entiy;

import com.github.fictology.gensokyoontology.api.IEntityDataAccessible;
import com.github.fictology.gensokyoontology.api.render.ITextureGetter;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public abstract class AffiliatedEntity extends Entity implements OwnableEntity, IEntityDataAccessible, ITextureGetter {

    public static final EntityDataAccessor<Optional<EntityReference<LivingEntity>>> DATA_OWNER = SynchedEntityData.defineId(
            AffiliatedEntity.class, EntityDataSerializers.OPTIONAL_LIVING_ENTITY_REFERENCE);

    public AffiliatedEntity(EntityType<?> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
    }

    public AffiliatedEntity(EntityType<?> entityTypeIn, LivingEntity owner, Level worldIn) {
        super(entityTypeIn, worldIn);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_OWNER, Optional.empty());
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        EntityReference<LivingEntity> owner = this.getOwnerReference();
        EntityReference.store(owner, output, "Owner");
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        EntityReference<LivingEntity> owner = EntityReference.readWithOldOwnerConversion(input, "Owner", this.level());
        if (owner != null) {
            try {
                this.entityData.set(DATA_OWNER, Optional.of(owner));
            } catch (Throwable _) {

            }
        } else {
            this.entityData.set(DATA_OWNER, Optional.empty());
        }
    }

    public boolean tryGetOwner(AtomicReference<LivingEntity> ref){
        if (this.getOwnerReference() == null) return false;
        if (this.getOwnerReference().getEntity(this.level(), LivingEntity.class) == null) return false;
        ref.set(this.getOwnerReference().getEntity(this.level(), LivingEntity.class));
        return true;
    }

    @Override
    public @Nullable EntityReference<LivingEntity> getOwnerReference() {
        return this.entityData.get(DATA_OWNER).orElse(null);
    }

    public void setOwner(@Nullable LivingEntity owner) {
        this.entityData.set(DATA_OWNER, Optional.ofNullable(owner).map(EntityReference::of));
    }

    public void setOwnerReference(@Nullable EntityReference<LivingEntity> owner) {
        this.entityData.set(DATA_OWNER, Optional.ofNullable(owner));
    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource damageSource, float v) {
        return false;
    }
}
