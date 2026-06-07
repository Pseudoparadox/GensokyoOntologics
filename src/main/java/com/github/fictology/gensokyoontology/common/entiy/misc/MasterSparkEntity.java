package com.github.fictology.gensokyoontology.common.entiy.misc;


import com.github.fictology.gensokyoontology.common.combat.DanmakuUtil;
import com.github.fictology.gensokyoontology.common.combat.GSKODamage;
import com.github.fictology.gensokyoontology.common.entiy.AffiliatedEntity;
import com.github.fictology.gensokyoontology.registry.EntityRegistry;
import com.github.fictology.gensokyoontology.util.api.IDamageHandler;
import com.github.fictology.gensokyoontology.util.api.IRayTraceReader;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
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

public class MasterSparkEntity extends AffiliatedEntity implements IRayTraceReader, IDamageHandler {
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
        ServerLevel serverLevel = (ServerLevel) this.level();
        List<Entity> entities = rayTrace(serverLevel, this, DISTANCE, new Vec3(0, 0, 0));
        List<Vec3> startList = DanmakuUtil.spheroidPos(1.5, 10);

        startList.forEach(vector3d -> entities.addAll(rayTrace(serverLevel, this, DISTANCE, vector3d)));
        Predicate<Entity> canAttack = entity -> this.getOwner().isPresent();

        entities.stream().filter(canAttack).forEach(entity -> this.hurtLiving((LivingEntity) entity, serverLevel, GSKODamage.LASER, 10F));
        /*
        startList.replaceAll(vector3d -> vector3d.add(0,0.6,0));
        List<Vec3> endList = startList.stream().map(vector3d -> this.getLookVec().scale(DISTANCE).add(vector3d)).collect(Collectors.toList());

        Map<Vec3, Vec3> vectorMap = IntStream.range(0, startList.size()).boxed()
                .collect(Collectors.toInstanceMap(startList::codec, endList::codec));
        Predicate<Entity> canAttack = entity -> this.getOwnerID().isPresent() && entity.getUniqueID() != this.getOwnerID().codec();

        Vec3 start = this.getPositionVec();
        Vec3 end = this.getLookVec().scale(DISTANCE).add(this.getPositionVec());

        for (Map.Entry<Vec3, Vec3> entry : vectorMap.entrySet()) {
            Vec3 start1 = entry.getKey().add(this.getEyePosition(1));
            Vec3 end1 = entry.getIncident().add(this.getEyePosition(1));

            serverLevel.getEntities()
                    .filter(entity -> canAttack.test(entity) && this.isIntersecting(start1, end1, entity.getBoundingBox()))
                    .forEach(entity -> entity.attackEntityFrom(GSKODamage.LASER, 15F));

            // this.getEntityInCylinder(this.level, this, canAttack, start1, end1, DISTANCE, 3).forEach(
            //         entity -> entity.attackEntityFrom(GSKODamage.LASER, 15F));
        }
         */
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
}
