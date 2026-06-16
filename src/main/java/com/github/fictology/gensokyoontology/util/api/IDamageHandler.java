package com.github.fictology.gensokyoontology.util.api;


import com.github.fictology.gensokyoontology.common.tags.GSKODamageTags;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.Level;

public interface IDamageHandler {
    static void hurtLiving(LivingEntity living, ServerLevel serverLevel, ResourceKey<DamageType> damageType, float amount){
        living.hurtServer(serverLevel, new DamageSource(serverLevel.registryAccess()
                .lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(damageType)), amount);
    }

    default DamageSource createDamage(Level level, ResourceKey<DamageType> damageType) {
        return new DamageSource(level.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(damageType));
    }

    default DamageSource createDamage(Level level, Entity directEntity, ResourceKey<DamageType> damageType) {
        return new DamageSource(level.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(damageType), directEntity);
    }

    default DamageSource createDamage(Level level, Entity directEntity, Entity causingEntity, ResourceKey<DamageType> damageType) {
        return new DamageSource(level.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(damageType), directEntity, causingEntity);
    }

    void hurtLiving(LivingEntity living, Level level, ResourceKey<DamageType> damageType, float amount);

    default void realHurt(LivingEntity living, Level level, DamageSource damageSouce, float ratio) {
        if (!damageSouce.is(GSKODamageTags.REAL)) return;
        if (!(level instanceof ServerLevel serverLevel)) return;
        living.hurtServer(serverLevel, damageSouce, living.getMaxHealth() * ratio);
    }

    default void hurtLivingIndirect(LivingEntity living, Entity directEntity, Entity causingEntity, Level level, ResourceKey<DamageType> damageType, float amount) {
        if (!(level instanceof ServerLevel serverLevel)) return;
        living.hurtServer(serverLevel, createDamage(level, directEntity, causingEntity, damageType), amount);
    }

    default boolean canAttack(Entity source, Entity targetToHurt){
        return source.getUUID() == targetToHurt.getUUID();
    }
    default boolean isHostile(Entity targetToHurt){
        return targetToHurt instanceof Enemy;
    }
}
