package com.github.fictology.gensokyoontology.common.entiy.misc;


import com.github.fictology.gensokyoontology.common.combat.DanmakuUtil;
import com.github.fictology.gensokyoontology.common.combat.GSKODamage;
import com.github.fictology.gensokyoontology.common.entiy.AffiliatedEntity;
import com.github.fictology.gensokyoontology.registry.EntityRegistry;
import com.github.fictology.gensokyoontology.util.api.IDamageHandler;
import com.github.fictology.gensokyoontology.util.api.IRayTraceReader;
import com.github.fictology.gensokyoontology.util.api.IResourceGetter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.Predicate;

public class MasterSparkEntity extends AffiliatedEntity implements IRayTraceReader, IDamageHandler, IResourceGetter {
    public static final float DISTANCE = 50F;

    public MasterSparkEntity(EntityType<?> entityTypeIn, Level levelIn) {
        super(entityTypeIn, levelIn);
    }

    public MasterSparkEntity(Entity owner, Level levelIn) {
        super(EntityRegistry.MASTER_SPARK_ENTITY.get(), levelIn);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }

    @Override
    public void tick() {
        super.tick();
        if (tickCount >= 120) this.setRemoved(RemovalReason.DISCARDED);
        if (tickCount < 40) return;
        if (level().isClientSide()) return;
        var serverLevel = (ServerLevel) this.level();
        var entities = rayTrace(serverLevel, this, DISTANCE, new Vec3(0, 0, 0));
        var startList = DanmakuUtil.spheroidPos(1.5, 10);

        startList.forEach(vector3d -> entities.addAll(rayTrace(serverLevel, this, DISTANCE, vector3d)));
        Predicate<Entity> canAttack = entity -> this.getOwner().isPresent();

        entities.stream().filter(canAttack).forEach(entity -> this.hurtLiving((LivingEntity) entity, serverLevel, GSKODamage.LASER, 10F));
    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource damageSource, float v) {
        return false;
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
