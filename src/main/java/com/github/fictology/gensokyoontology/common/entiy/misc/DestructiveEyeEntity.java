package com.github.fictology.gensokyoontology.common.entiy.misc;

import com.github.fictology.gensokyoontology.common.combat.GSKODamage;
import com.github.fictology.gensokyoontology.common.entiy.AffiliatedEntity;
import com.github.fictology.gensokyoontology.registry.EntityRegistry;
import com.github.fictology.gensokyoontology.registry.ItemRegistry;
import com.github.fictology.gensokyoontology.api.IDamageHandler;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class DestructiveEyeEntity extends AffiliatedEntity implements ItemSupplier, IDamageHandler {
    public final int MAX_LIVING_TICK = 50;

    public DestructiveEyeEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public DestructiveEyeEntity(Level level) {
        super(EntityRegistry.DESTRUCTIVE_EYE.get(), level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }

    @Override
    public void tick() {
        super.tick();
        if (this.tickCount == MAX_LIVING_TICK) {
            if (!(this.level() instanceof ServerLevel serverLevel)) return;
            this.level().getEntitiesOfClass(Player.class, this.getBoundingBox())
                    .forEach(player -> realHurt(player, this.level(), createDamage(level(), GSKODamage.EYE), 0.9f));
            this.setRemoved(RemovalReason.DISCARDED);

        }
    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource damageSource, float v) {
        return false;
    }

    public int getMaxLiving() {
        return MAX_LIVING_TICK;
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(ItemRegistry.DESTRUCTIVE_EYE.get());
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
