package com.github.fictology.gensokyoontology.common.entiy.misc;

import com.github.fictology.gensokyoontology.common.combat.GSKODamage;
import com.github.fictology.gensokyoontology.common.entiy.monster.FairyEntity;
import com.github.fictology.gensokyoontology.common.entiy.monster.YoukaiEntity;
import com.github.fictology.gensokyoontology.registry.EntityRegistry;
import com.github.fictology.gensokyoontology.util.api.IDamageHandler;
import com.github.fictology.gensokyoontology.util.api.IRayTraceReader;
import com.github.fictology.gensokyoontology.util.script.ClosureExpression;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class DreamSealEntity extends Danmaku implements IRayTraceReader, IDamageHandler {
    private String colorId;
    private Entity trackingTarget;

    public DreamSealEntity(Level worldIn, LivingEntity shooter, String colorId) {
        super(EntityRegistry.DREAM_SEAL.get(), worldIn);
        this.lifespan(300).owner(shooter);// .setBehavior(new ClosureExpression());

        this.setColor(colorId);
        this.setOwner(shooter);
        this.setTarget(findTarget(worldIn, shooter.getPosition(0), createCubeBox(shooter.getPosition(0), 20)));
    }

    public DreamSealEntity(EntityType<? extends Danmaku> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
    }

    @Nullable
    public static LivingEntity findTarget(Level world, Vec3 center, AABB box) {
        var entities = world.getEntitiesOfClass(LivingEntity.class, box, e -> !(e instanceof Player));
        var optional = entities.stream().map(living -> living.getPosition(0).distanceTo(center)).min(Double::compareTo);
        var atom = new AtomicReference<LivingEntity>();
        optional.flatMap(distance -> entities.stream().filter(living -> living.getPosition(0).distanceTo(center) == distance)
                .findFirst()).ifPresent(atom::set);
        return atom.get();
    }

    private void setColor(String colorId) {
    }

    // /tp -19 70.0 -164
    @Override
    public void tick() {

        if (tickCount % 3 == 0) return;
        if (this.getOwner() != null && this.getTarget() != null) {
            Vec3 aimedVec = this.getAimedVec((LivingEntity) this.getOwner(), this.getTarget());
            float offset = 0.3f / this.getTarget().getEyeHeight();
            this.shoot(aimedVec.x, aimedVec.y - offset, aimedVec.z, 1.6f, 0f);
        }
//        if (this.onGround()){
//            ProjectileDeflection.REVERSE.deflect(this, this.getOwner(), this.random);
//        }
        super.tick();
    }

    private Entity getTarget() {
        return this.trackingTarget;
    }

    private void setTarget(LivingEntity target) {
        this.trackingTarget = target;
    }

    private boolean isPlayer() {
        AtomicBoolean flag = new AtomicBoolean();
        if (this.getOwner() == null) return false;
        return this.getOwner() instanceof Player;
    }

    @Override
    protected void onHitEntity(@NotNull EntityHitResult result) {
        super.onHitEntity(result);
        if (result.getEntity() instanceof Danmaku danmaku) {
            danmaku.setRemoved(RemovalReason.KILLED);
        }
        if (result.getEntity() instanceof YoukaiEntity youkai) {
            float value = youkai.getMaxHealth() < 400 ? youkai.getMaxHealth() * 0.1f : youkai.getMaxHealth() * 0.075f;
            hurtLiving(youkai, level(), GSKODamage.HAKUREI_POWER, value);
        }
        if (result.getEntity() instanceof FairyEntity fairy) {
            hurtLiving(fairy, level(), GSKODamage.HAKUREI_POWER, 999999f);
        }
    }

    @Override
    public @NotNull ItemStack getItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public void hurtLiving(LivingEntity hurtLiving, Level level, ResourceKey<DamageType> damageType, float amount) {
        if (level instanceof ServerLevel serverLevel)
            hurtLiving.hurtServer(serverLevel, createDamage(level, damageType), amount);
    }
}
