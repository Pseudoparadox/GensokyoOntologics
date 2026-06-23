package com.github.fictology.gensokyoontology.common.entiy.misc;


import com.github.fictology.gensokyoontology.GensokyoOntology;
import com.github.fictology.gensokyoontology.client.renderer.state.DanmakuNormalState;
import com.github.fictology.gensokyoontology.common.combat.GSKODamage;
import com.github.fictology.gensokyoontology.common.item.DanmakuItem;
import com.github.fictology.gensokyoontology.data.DanmakuColor;
import com.github.fictology.gensokyoontology.registry.DataRegistry;
import com.github.fictology.gensokyoontology.registry.EntityRegistry;
import com.github.fictology.gensokyoontology.registry.Expressions;
import com.github.fictology.gensokyoontology.registry.ItemRegistry;
import com.github.fictology.gensokyoontology.util.GSKOMathUtil;
import com.github.fictology.gensokyoontology.util.GSKOUtil;
import com.github.fictology.gensokyoontology.api.IDamageHandler;
import com.github.fictology.gensokyoontology.api.render.ITextureGetter;
import com.github.fictology.gensokyoontology.util.script.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 抽象弹幕类，用于处理所有继承于该类的弹幕实体的那些相似的逻辑，包含如下几个方面：<br>
 * 弹幕的生命周期或存在时间：125 个游戏刻<br>
 * 弹幕的tick()方法<br>
 * 弹幕击中生物时的逻辑<br>
 * （待补充……）
 */
public class Danmaku extends ThrowableItemProjectile implements ItemSupplier, IDamageHandler, ITextureGetter {
    public static final EntityDataAccessor<Float> DATA_DAMAGE = SynchedEntityData.defineId(
            Danmaku.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Integer> DATA_LIFESPAN = SynchedEntityData.defineId(
            Danmaku.class, EntityDataSerializers.INT);
    protected float damage = 2.0f;
    // protected ClosureExpression behavior;
    private int lifespan = 125;

    private boolean hasBeenShot = false;

    // public static final EntityDataAccessor<ClosureExpression> DATA_SPELL = SynchedEntityData.defineId(
    //         Danmaku.class, GSKOSerializers.EXP_SERIALIZER.get());
    private String danmakuColor = DanmakuColor.NONE;
    public Map<String, IExpressionType> varMap = new HashMap<>();

    public Danmaku(Level world, Item danmakuItem) {
        this(EntityRegistry.DANMAKU.get(), world);
        // this.behavior = behavior;
        this.setItem(new ItemStack(danmakuItem));
        this.setNoGravity(true);
        this.noPhysics = true;
    }

    public Danmaku(ServerLevel world, Item danmakuItem, LivingEntity owner) {
        super(EntityRegistry.DANMAKU.get(), owner, world, new ItemStack(danmakuItem));
        // this.behavior = new ClosureExpression();
        this.setItem(new ItemStack(danmakuItem));
        this.setNoGravity(true);
        this.noPhysics = true;
    }

    public Danmaku(EntityType<? extends ThrowableItemProjectile> entityType, Level level) {
        super(entityType, level);
        this.setItem(ItemStack.EMPTY);
        this.setNoGravity(true);
        this.noPhysics = true;
    }

    public Danmaku(ServerLevel serverLevel, LivingEntity owner, ItemStack itemStack) {
        super(EntityRegistry.DANMAKU.get(), owner, serverLevel, itemStack);
        this.setNoGravity(true);
        this.noPhysics = true;
        // this.behavior = new ClosureExpression();
    }

    public static void shootTo(Level level, Player player, Danmaku danmaku, float speed){
        var angle = player.getLookAngle();
        level.addFreshEntity(danmaku);
        danmaku.shoot(angle.x, angle.y, angle.z, speed, 0f);
    }

    public static Danmaku create(ServerLevel serverLevel, LivingEntity owner, ItemStack stack) {
        return new Danmaku(serverLevel, stack.getItem(), owner);// .setBehavior(ClosureExpression.EMPTY);
    }

    public static Danmaku create(ServerLevel level, LivingEntity owner, ItemStack stack, ClosureExpression expression) {
        return new Danmaku(level, stack.getItem(), owner);// .setBehavior(expression);
    }

    public static Danmaku create(Level level, Item item, LivingEntity living) {
        if (!(level instanceof ServerLevel serverLevel)) return null;
        return new Danmaku(serverLevel, item, living);
    }

    public Danmaku lifespan(int lifespan) {
        this.lifespan = lifespan;
        // this.getEntityData().set(DATA_LIFESPAN, lifespan);
        return this;
    }

    public Danmaku damage(float damage) {
        this.damage = damage;
        // this.getEntityData().set(DATA_DAMAGE, damage);
        return this;
    }

//    public Danmaku setBehavior(ClosureExpression expression) {
//        // this.getEntityData().set(DATA_SPELL, this.behavior);
//        // this.behavior = expression;
//        return this;
//    }


    @Override
    public EntityDimensions getDimensions(Pose pose) {
        var dim = super.getDimensions(pose);
        dim.scale(DanmakuNormalState.getStateForItem(this.getItem().getItem()).size);
        return dim;
    }

    public Danmaku owner(Entity owner) {
        this.setOwner(owner);
        return this;
    }

    public Danmaku from(Entity living) {
        this.pos(living.getPosition(0f)).rot(living.getRotationVector()).setOwner(living);
        return this;
    }

    public Danmaku pos(Vec3 pos) {
        this.setPos(pos);
        return this;
    }

    public Danmaku rot(Vec2 rot) {
        this.setRot(rot.y, rot.x);
        return this;
    }

    public void init(int lifespan, float damage, String color, Entity owner) {
        this.lifespan(lifespan).damage(damage).owner(owner);
    }

    public void initExp(int lifespan, float damage, String color, Entity owner, ClosureExpression behavior) {
        // this.lifespan(lifespan).damage(damage).owner(owner).setBehavior(behavior);
    }

    public int getLifespan() {
        return this.getEntityData().get(DATA_LIFESPAN);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.tickCount >= this.lifespan) this.remove(RemovalReason.DISCARDED);
        if (this.getKnownSpeed().length() <= 1e-4 && this.tickCount > 1) this.remove(RemovalReason.DISCARDED);
//        if (this.getDefaultItem() != ItemRegistry.DANMAKU_SHOT.get()) this.onBehaviorTick();
    }

    public void onBehaviorTick(){
        var danmaku = this.getItem();
        var closure = danmaku.get(DataRegistry.CLOSURE);
        if (closure == null) return;
        closure.compile(this.varMap).getExpressions().forEach(this::run);
    }

    public void run(IExpressionType type){
        if (type instanceof InvokeExpression invoker) {
            if (invoker.getAccessibleClass() != AccessibleClass.THIS) GSKOUtil.error(
                    "Method invoker is not a Danmaku instance.");
            if (!Expressions.DANMAKU_METHODS.contains(invoker.getAccessibleMethod()))
                Expressions.NoSuchMethod(this.getClass(), invoker);

            if (invoker.getAccessibleMethod() == AccessibleMethod.PROJECTILE_SET_MOTION)
                this.tryInvokeSetMotion(invoker);

            if (invoker.getAccessibleMethod() == AccessibleMethod.PROJECTILE_SHOOT)
                this.tryInvokeShoot(invoker);
        }
    }

    public void tryInvokeShoot(InvokeExpression invoker){
        var list = invoker.getParameters();
        this.checkParamInMap(list, invoker);

    }

    public void tryInvokeSetMotion(InvokeExpression invoker){
        var list = invoker.getParameters();
        this.checkParamInMap(list, invoker);
        if (invoker.isInitRef(0)){
            var init = invoker.getVarInit(list.getFirst(), this.varMap);
            if (!init.matchesInit(AccessibleInit.VEC3_INIT)) Expressions.UnexpectedExpression(invoker);
            if (!init.isAllConst())Expressions.ParametersNotMatch(invoker);

            var initParams = init.getAsConstList();
            var vec3 = new Vec3(
                    initParams.get(0).getDouble(),
                    initParams.get(1).getDouble(),
                    initParams.get(2).getDouble());
            this.setDeltaMovement(vec3);

        }
    }

    private void checkParamInMap(List<ParamExpression> list, InvokeExpression invoker){
        var paramInMap = list.stream().allMatch(param -> this.varMap.containsKey(param.paramName));
        if (!paramInMap) Expressions.NotInMap();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_DAMAGE, this.damage);
        builder.define(DATA_LIFESPAN, this.lifespan);
        // this.behavior = new ClosureExpression();
        // builder.define(DATA_SPELL, this.behavior);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        var registryops = this.registryAccess().createSerializationContext(NbtOps.INSTANCE);
        this.setDamage(input.getFloatOr("damage", 2F));
        input.getInt("lifespan").ifPresent(this::setLifespan);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putFloat("damage", this.getDamage());
        output.putInt("lifespan", this.getLifespan());
    }


    public void setDamage(float damage) {
        this.getEntityData().set(DATA_DAMAGE, damage);
    }

    public void setLifespan(int lifespan) {
        this.getEntityData().set(DATA_DAMAGE, damage);
    }

    public float getDamage() {
        return this.getEntityData().get(DATA_DAMAGE);
    }

//    public ClosureExpression getBehavior() {
//        return this.behavior;
//    }

    @Override
    public void setOwner(@Nullable Entity entityIn) {
        super.setOwner(entityIn);
    }

    @Override
    public boolean canBeCollidedWith(@org.jspecify.annotations.Nullable Entity other) {
        return true;
    }

    @Override
    protected void onHitEntity(@NotNull EntityHitResult result) {
        if (!(result.getEntity() instanceof LivingEntity living)) return;
        if (!(living.level() instanceof ServerLevel serverLevel)) return;

        if (this.getOwner() instanceof Monster || this.getOwner() instanceof Enemy) {

            if (this.getType() != EntityRegistry.FAKE_LUNAR.get())
                this.hurtLiving(living, serverLevel, DamageTypes.MAGIC, 4f);

            this.setRemoved(RemovalReason.DISCARDED);
            return;
        }

        if (result.getEntity() instanceof Danmaku danmaku) {
            if (danmaku.getType() != EntityRegistry.FAKE_LUNAR.get()) return;
            this.setRemoved(RemovalReason.KILLED);
            return;
        }

        if (!(living instanceof Player)) {
            this.hurtLiving(living, serverLevel, DamageTypes.MAGIC, this.getDamage());
            this.setRemoved(RemovalReason.KILLED);
        }
    }

    @Override
    public @NotNull Item getDefaultItem() {
        return ItemRegistry.DANMAKU_SHOT.get();
    }

    @Override
    public void hurtLiving(LivingEntity hurtLiving, Level level, ResourceKey<DamageType> damageType, float amount) {
        if (level instanceof ServerLevel serverLevel)
            hurtLiving.hurtServer(serverLevel, createDamage(level, damageType), amount);
    }

    @Override
    public Identifier getTexture() {
        var registryName = BuiltInRegistries.ITEM.getKey(this.getItem().getItem());
        var fileName = registryName.toString().replace(GensokyoOntology.MODID + ":", "");
        return GSKOUtil.key("textures/item/" + fileName + ".png");
    }

    public static void shoot(LivingEntity owner, DanmakuItem danmakuItem, Vec3 shootVec, float velocity) {
        if (!(owner.level() instanceof ServerLevel serverLevel)) return;
        Projectile.spawnProjectileUsingShoot(Danmaku::create, serverLevel, danmakuItem.getDefaultInstance(), owner,
                shootVec.x, shootVec.y, shootVec.z, velocity, 0F);
    }
}
