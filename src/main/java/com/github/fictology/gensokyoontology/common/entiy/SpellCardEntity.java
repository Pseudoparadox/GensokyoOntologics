package com.github.fictology.gensokyoontology.common.entiy;

import com.github.fictology.gensokyoontology.util.api.V3f;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;

// TODO: 按照符卡的强度决定其登场和获取顺序
public class SpellCardEntity extends Entity implements ItemSupplier {

    public static final EntityDataAccessor<Integer> DATA_LIFESPAN = SynchedEntityData.defineId(
            SpellCardEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Optional<EntityReference<LivingEntity>>> DATA_OWNER_UUID = SynchedEntityData.defineId(
            SpellCardEntity.class, EntityDataSerializers.OPTIONAL_LIVING_ENTITY_REFERENCE);
    protected int lifeSpan = 500;
    protected UUID owner;
    /**
     * 初始化设置弹幕的射击方位为X轴正方向，即游戏中的东方
     */
    protected Vec3 shootAngle = new Vec3(V3f.XP.cast());
    private int ownerId;
    private BiConsumer<Level, LivingEntity> simpleBehavior;
    private Item spellItem;

    public SpellCardEntity(EntityType<SpellCardEntity> entityType, Level worldIn, Item spellItem, BiConsumer<Level, LivingEntity> simpleBehavior) {
        this(entityType, worldIn);
        // this.setPosition(living.getPosX(), living.getPosY(), living.getPosZ());
        this.simpleBehavior = simpleBehavior;
        this.spellItem = spellItem;
    }

    public SpellCardEntity(EntityType<? extends Entity> entityType, Level worldIn) {
        super(entityType, worldIn);
    }

    public void init(LivingEntity owner, BiConsumer<Level, LivingEntity> spellBehavior) {
        this.simpleBehavior = spellBehavior;
        this.setOldPosAndRot(owner.getPosition(0), owner.yRotO, owner.xRotO);
        this.setOwner(owner);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_LIFESPAN, this.lifeSpan);
        builder.define(DATA_OWNER_UUID, Optional.empty());
    }

    @Override
    protected void readAdditionalSaveData(ValueInput compound) {
        compound.getInt("Lifespan").ifPresent(this::setLifespan);
        this.setOwner(compound.read("Owner", UUIDUtil.CODEC).orElse(null));
    }

    private void setLifespan(int lifespan) {
        this.lifeSpan = lifespan;
    }

    protected void addAdditionalSaveData(ValueOutput compound) {
        compound.putInt("Lifespan", this.lifeSpan);
        compound.storeNullable("Owner", UUIDUtil.CODEC, this.owner);
    }

    @Nullable
    public Entity getOwner() {
        if (this.level() instanceof ServerLevel serverLevel) {
            return serverLevel.getEntity(this.owner);
        }
        return null;
    }

    public void setOwner(@Nullable Entity entityIn) {
        if (entityIn != null) {
            this.owner = entityIn.getUUID();
        }
    }

    public void setOwner(UUID uuid) {
        if (uuid != null) {
            this.owner = uuid;
        }
    }

    public int getLifeSpan() {
        return lifeSpan;
    }


    /**
     * 使用tick()方法让弹幕在每个游戏刻执行不同的操作，将实体类中的 tickExisted 参数作为变换函数 increment 增加值的迭代单位，
     * 因为这个参数的存在，实体类的tick方法比单纯继承了 ITickable 接口的类更加便捷好用。
     */
    @Override
    public void tick() {
        super.tick();
        if (tickCount >= this.lifeSpan) {
            this.setRemoved(RemovalReason.DISCARDED);
        }
        // onTick(world, getOwner(), ticksExisted);
        simpleBehavior.accept(this.level(), (LivingEntity) this.getOwner());
    }

    @Override
    public void baseTick() {
        super.baseTick();
        // onTick(world, this.getOwner(), ticksExisted);
    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource damageSource, float v) {
        return false;
    }

    public void onTick(Level world, Entity entity, int ticksIn) {
    }

    public void onScriptTick(Level world, Entity owner, int ticksIn) {

    }

    @Override
    public ItemStack getItem() {
        return null;
    }
}

