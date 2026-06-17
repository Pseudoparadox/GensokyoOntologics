package com.github.fictology.gensokyoontology.common.entiy.misc;

import com.github.fictology.gensokyoontology.GensokyoOntology;
import com.github.fictology.gensokyoontology.api.IDamageHandler;
import com.github.fictology.gensokyoontology.api.render.IModelGetter;
import com.github.fictology.gensokyoontology.api.render.ITextureGetter;
import com.github.fictology.gensokyoontology.common.item.touhou.YinyangJadeItem;
import com.github.fictology.gensokyoontology.registry.EntityRegistry;
import com.github.fictology.gensokyoontology.registry.ItemRegistry;
import com.github.fictology.gensokyoontology.util.GSKOUtil;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;

public class YinyangJade extends ThrowableItemProjectile implements ItemSupplier, IDamageHandler, IModelGetter {

    // ===== 弹跳配置 =====
    private static final double BOUNCE_DAMPING = 0.7;      // 弹跳衰减系数 (0-1)
    private static final double MIN_BOUNCE_SPEED = 0.1;   // 最小弹跳速度，低于此值停止弹跳
    private static final int MAX_BOUNCES = 10;            // 最大弹跳次数
    private static final double ENTITY_BOUNCE_MULTIPLIER = 0.8; // 实体碰撞弹跳强度

    private int bounceCount = 0;
    private boolean isBouncing = false;
    private Vec3 lastVelocity = Vec3.ZERO;

    public YinyangJade(EntityType<? extends ThrowableItemProjectile> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
    }

    public YinyangJade(ServerLevel serverLevel, LivingEntity owner, ItemStack itemStack) {
        super(EntityRegistry.YIN_YANG_JADE.get(), owner, serverLevel, itemStack);
    }

    @Override
    protected @NonNull Item getDefaultItem() {
        return ItemRegistry.YINYANG_JADE_BLACK.get();
    }

    @Override
    public @NonNull ItemStack getItem() {
        return super.getItem();
    }

    @Override
    public void hurtLiving(LivingEntity living, Level level, ResourceKey<DamageType> damageType, float amount) {
        // 击中实体时造成伤害并触发弹跳
        if (!level.isClientSide()) {
            living.hurt(level.damageSources().thrown(this, this.getOwner()), amount);
            bounceOffEntity(living);
        }
    }

    @Override
    public Identifier modelPath() {
        var registryName = BuiltInRegistries.ITEM.getKey(this.getItem().getItem());
        var fileName = registryName.toString().replace(GensokyoOntology.MODID + ":", "");
        return GSKOUtil.key("models/entity/" + fileName + ".obj");
    }

    // ===== 弹跳核心逻辑 =====

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);

        if (this.level().isClientSide()) return;

        // 记录碰撞前的速度
        lastVelocity = this.getDeltaMovement();

        // 检查是否可以继续弹跳
        if (bounceCount >= MAX_BOUNCES || lastVelocity.length() < MIN_BOUNCE_SPEED) {
            this.discard(); // 超过最大弹跳次数或速度太小，销毁实体
            return;
        }

        // 计算弹跳
        bounceOffBlock(result.getLocation(), result.getDirection());
        bounceCount++;

        // 播放弹跳音效
        playBounceSound();

        // 生成粒子效果
        spawnBounceParticles(result.getLocation());
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);

        if (this.level().isClientSide()) return;

        Entity hitEntity = result.getEntity();

        // 如果是玩家或生物，触发伤害逻辑
        if (hitEntity instanceof LivingEntity living) {
            hurtLiving(living, this.level(),
                    this.level().damageSources().thrown(this, this.getOwner()).typeHolder().unwrapKey().orElse(null),
                    4.0f); // 基础伤害值
        }

        // 实体碰撞弹跳（不同于方块碰撞）
        bounceOffEntity(hitEntity);

        // 播放碰撞音效
        this.playSound(SoundEvents.STONE_HIT, 0.5f, 1.2f);
    }

    /**
     * 方块碰撞弹跳
     */
    private void bounceOffBlock(Vec3 hitPos, Direction hitFace) {
        Vec3 velocity = this.getDeltaMovement();

        // 根据碰撞面法向量计算反射
        Vec3 normal = Vec3.atLowerCornerOf(hitFace.getUnitVec3i());
        double dot = velocity.dot(normal);

        // 反射公式: v' = v - 2*(v·n)*n
        Vec3 reflected = velocity.subtract(normal.scale(2 * dot));

        // 应用阻尼衰减
        Vec3 damped = reflected.scale(BOUNCE_DAMPING);

        // 轻微随机化弹跳方向，避免无限循环弹跳
        damped = damped.add(
                (this.random.nextDouble() - 0.5) * 0.1,
                (this.random.nextDouble() - 0.5) * 0.05,
                (this.random.nextDouble() - 0.5) * 0.1
        );

        // 设置新速度
        this.setDeltaMovement(damped);

        // 确保不会卡在方块里
        this.setPos(hitPos.add(normal.scale(0.1)));

        isBouncing = true;
    }

    /**
     * 实体碰撞弹跳
     */
    private void bounceOffEntity(Entity hitEntity) {
        Vec3 velocity = this.getDeltaMovement();
        Vec3 entityVelocity = hitEntity.getDeltaMovement();

        // 计算从实体到阴阳玉的方向
        Vec3 toJade = this.position().subtract(hitEntity.position()).normalize();

        // 结合实体速度和自身速度计算弹跳
        Vec3 combinedVelocity = velocity.scale(0.7).add(entityVelocity.scale(0.3));

        // 沿法线方向反射
        double dot = combinedVelocity.dot(toJade);
        Vec3 reflected = combinedVelocity.subtract(toJade.scale(2 * dot));

        // 应用实体碰撞特有的衰减
        Vec3 damped = reflected.scale(ENTITY_BOUNCE_MULTIPLIER);

        // 添加垂直分量，避免水平滑动
        damped = damped.add(0, Math.abs(damped.y) * 0.3, 0);

        this.setDeltaMovement(damped);

        // 稍微推开一点距离
        this.setPos(this.position().add(toJade.scale(0.5)));

        bounceCount++;
        isBouncing = true;
    }

    /**
     * 播放弹跳音效
     */
    private void playBounceSound() {
        float pitch = 1.0f + (random.nextFloat() - 0.5f) * 0.3f;
        float volume = 0.5f + (float)(lastVelocity.length() * 0.1);

        // 根据弹跳次数调整音效
        var sound = bounceCount % 3 == 0 ? SoundEvents.NOTE_BLOCK_PLING.value() : SoundEvents.STONE_HIT;
        this.playSound(sound, volume, pitch);
    }

    /**
     * 生成弹跳粒子
     */
    private void spawnBounceParticles(Vec3 pos) {
        if (this.level().isClientSide()) return;

        ServerLevel serverLevel = (ServerLevel) this.level();

        // 根据阴阳玉类型选择粒子颜色
        var particle = this.getItem().is(ItemRegistry.YINYANG_JADE_BLACK.get()) ?
                ParticleTypes.SMOKE : ParticleTypes.CLOUD;

        // 生成粒子环
        for (int i = 0; i < 8; i++) {
            double angle = i * Math.PI / 4;
            double offsetX = Math.cos(angle) * 0.3;
            double offsetZ = Math.sin(angle) * 0.3;

            serverLevel.sendParticles(
                    particle,
                    pos.x + offsetX, pos.y, pos.z + offsetZ,
                    1, 0, 0.05, 0, 0.02
            );
        }
    }

    // ===== 生命周期管理 =====

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide()) return;

        // 检查是否需要销毁（速度过小或飞行时间过长）
        Vec3 velocity = this.getDeltaMovement();
        if (velocity.length() < MIN_BOUNCE_SPEED || this.tickCount > 200) {
            this.discard();
            return;
        }

        // 弹跳状态重置
        if (isBouncing) {
            isBouncing = false;
        }

        // 重力影响（轻微）
        if (this.tickCount > 10) {
            this.setDeltaMovement(velocity.add(0, -0.02, 0));
        }

        // 旋转效果（可选）
        this.setYRot(this.getYRot() + 15.0f);
        this.setXRot(this.getXRot() + 5.0f);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        // 可以添加同步数据，如弹跳次数等
    }

    // ===== 辅助方法 =====

    /**
     * 获取当前弹跳次数
     */
    public int getBounceCount() {
        return bounceCount;
    }

    /**
     * 重置弹跳计数
     */
    public void resetBounces() {
        bounceCount = 0;
    }

    /**
     * 设置弹跳阻尼系数
     */
    public void setBounceDamping(double damping) {
        // 可以在外部调整弹跳特性
    }
}
