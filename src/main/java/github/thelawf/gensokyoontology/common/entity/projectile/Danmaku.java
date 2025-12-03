package github.thelawf.gensokyoontology.common.entity.projectile;


import com.mojang.datafixers.util.Pair;
import github.thelawf.gensokyoontology.api.Actions;
import github.thelawf.gensokyoontology.client.GSKORenderTypes;
import github.thelawf.gensokyoontology.common.util.GSKODamageSource;
import github.thelawf.gensokyoontology.common.util.math.*;
import github.thelawf.gensokyoontology.core.init.EntityRegistry;
import github.thelawf.gensokyoontology.core.init.ItemRegistry;
import net.minecraft.block.DispenserBlock;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tileentity.DispenserTileEntity;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.Util;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;

/**
 * 抽象弹幕类，用于处理所有继承于该类的弹幕实体的那些相似的逻辑，包含如下几个方面：<br>
 * 弹幕的生命周期或存在时间：125 个游戏刻<br>
 * 弹幕的tick()方法<br>
 * 弹幕击中生物时的逻辑<br>
 * TODO: 弹幕攻击伤害的数值设定 <br>
 * （待补充……）
 */
@OnlyIn(value = Dist.CLIENT, _interface = IItemProvider.class)
public class Danmaku extends ProjectileItemEntity{

    public static final DataParameter<Float> DATA_DAMAGE = EntityDataManager.createKey(
            Danmaku.class, DataSerializers.FLOAT);
    public static final DataParameter<Integer> DATA_LIFESPAN = EntityDataManager.createKey(
            Danmaku.class, DataSerializers.VARINT);
    public static final DataParameter<CompoundNBT> DAT_EXP = EntityDataManager.createKey(
            Danmaku.class, DataSerializers.COMPOUND_NBT);

    protected float damage = 2.0f;
    // protected ClosureExpression behavior;
    private int lifespan = 125;

    // public static final EntityDataAccessor<ClosureExpression> DATA_SPELL = SynchedEntityData.defineId(
    //         Danmaku.class, GSKOSerializers.EXP_SERIALIZER.get());
    public Consumer<EntityRayTraceResult> onHit;
    public Consumer<Danmaku> onTick;

    // private Danmaku(World world, Item danmakuItem, ClosureExpression behavior) {
    //     this(EntityRegistry.DANMAKU.get(), world);
    //     this.behavior = behavior;
    //     this.setItem(new ItemStack(danmakuItem));
    // }

    public Danmaku(World world, Item danmakuItem, LivingEntity owner) {
        super(EntityRegistry.DANMAKU.get(), owner, world);
        // this.behavior = new ClosureExpression();
        this.setItem(new ItemStack(danmakuItem));
        this.setPos(owner.getPositionVec());
        this.setNoGravity(true);
        this.setLifespan(this.lifespan);
        this.setDamage(this.damage);
    }

    public Danmaku(EntityType<? extends ProjectileItemEntity> entityType, World world) {
        super(entityType, world);
        this.setItem(new ItemStack(ItemRegistry.LARGE_SHOT_RED.get()));
        this.setNoGravity(true);
        this.setLifespan(this.lifespan);
        this.setDamage(this.damage);
    }

    public Danmaku(EntityType<? extends ProjectileItemEntity> type, double x, double y, double z, World world) {
        super(type, x, y, z, world);
        this.setItem(new ItemStack(ItemRegistry.LARGE_SHOT_RED.get()));
        this.setPos(new Vector3d(x, y, z));
        this.setNoGravity(true);
        this.setLifespan(this.lifespan);
        this.setDamage(this.damage);
    }

    public static void shootTo(World level, PlayerEntity player, Danmaku danmaku, float speed){
        Vector3d angle = player.getLookVec();
        level.addEntity(danmaku);
        danmaku.shoot(angle.x, angle.y, angle.z, speed, 0f);
    }

    public static Danmaku create(World world, LivingEntity owner, ItemStack stack) {
        return Danmaku.create(world, owner, stack.getItem());
    }

    public static Danmaku create(World world, LivingEntity owner, Item item) {
        if (!world.isRemote){
            ServerWorld serverWorld = (ServerWorld) world;
            long count = serverWorld.getEntities().filter(entity -> entity.getType() == EntityRegistry.DANMAKU.get()).count();
            if (count >= 800) {
                Danmaku danmaku = new Danmaku(world, item, owner)
                        .pos(new Vector3d(0,0,0))
                        .disableGravity();
                danmaku.remove();
                return danmaku;
            }
        }
        return new Danmaku(world, item, owner)
                .pos(owner.getPositionVec().add(0,owner.getEyeHeight(),0))
                .rot(Rot2f.from(owner.getLookVec()))
                .disableGravity();
    }

    public static float rad(double degIn){
        return (float) Math.toRadians(degIn);
    }

    public Danmaku lifespan(int lifespan) {
        this.lifespan = lifespan;
        // this.getEntityData().set(DATA_LIFESPAN, lifespan);
        return this;
    }

    public Danmaku damage(float damage) {
        this.damage = damage;
        this.setDamage(damage);
        // this.getEntityData().set(DATA_DAMAGE, damage);
        return this;
    }

    public Danmaku owner(Entity owner) {
        this.setShooter(owner);
        return this;
    }

    public Danmaku pos(Vector3d pos) {
        this.setPos(pos);
        return this;
    }

    public void addToWorld(World wrold) {
        wrold.addEntity(this);
    }

    public Danmaku disableGravity(){
        this.setNoGravity(true);
        return this;
    }
    public Danmaku enableGravity(){
        this.setNoGravity(false);
        return this;
    }

    private void setPos(Vector3d pos) {
        this.setPosition(pos.x, pos.y, pos.z);
    }

    public Danmaku rot(Rot2f rotation) {
        this.setRotation(rotation.yaw(), rotation.pitch());
        return this;
    }

    public Danmaku boundingBox(double width, double height){
        this.setBoundingBox(AxisAlignedBB.withSizeAtOrigin(width, height, width));
        return this;
    }

    public Danmaku size(float size){
        this.setBoundingBox(AxisAlignedBB.withSizeAtOrigin(size, size, size));
        this.recalculateSize();
        return this;
    }

    public Danmaku onHit(Consumer<EntityRayTraceResult> onHit) {
        this.onHit = onHit;
        return this;
    }

    public Danmaku onTick(Consumer<Danmaku> onTick) {
        this.onTick = onTick;
        return this;
    }

    public int getLifespan() {
        return this.dataManager.get(DATA_LIFESPAN);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.world.isRemote) return;
        if (this.ticksExisted >= this.lifespan) this.remove();
        if (this.onTick != null) this.onTick.accept(this);


        // if (this.getDefaultItem() != ItemRegistry.DANMAKU_SHOT.get()) this.onBehaviorTick();
    }

    public void onBehaviorTick(){
        // var danmaku = this.getItem();
        // var closure = danmaku.get(Expressions.CLOSURE);
        // if (closure == null) return;
        // closure.compile(this.varMap).getExpressions().forEach(this::run);
    }

    // public void run(IExpressionType type){
    //     if (type instanceof InvokeExpression invoker) {
    //         if (invoker.getAccessibleClass() != AccessibleClass.THIS) GSKOUtil.error(
    //                 "Method invoker is not a Danmaku instance.");
    //         if (!Expressions.DANMAKU_METHODS.contains(invoker.getAccessibleMethod()))
    //             Expressions.NoSuchMethod(this.getClass(), invoker);
// 
    //         if (invoker.getAccessibleMethod() == AccessibleMethod.PROJECTILE_SET_MOTION)
    //             this.tryInvokeSetMotion(invoker);
// 
    //         if (invoker.getAccessibleMethod() == AccessibleMethod.PROJECTILE_SHOOT)
    //             this.tryInvokeShoot(invoker);
    //     }
    // }

    // public void tryInvokeShoot(InvokeExpression invoker){
    //     var list = invoker.getParameters();
    //     this.checkParamInMap(list, invoker);
    // }

    /*
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

     */
    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(DATA_DAMAGE, this.damage);
        this.dataManager.register(DATA_LIFESPAN, this.lifespan);
        // this.behavior = new ClosureExpression();
        // builder.define(DATA_SPELL, this.behavior);
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        // var registryops = this.registryAccess().createSerializationContext(NbtOps.INSTANCE);
        this.setDamage(compound.getFloat("damage"));
        this.setLifespan(compound.getInt("lifespan"));
        // this.setBehavior((ClosureExpression) ExpressionType.CODEC.parse(NbtOps.INSTANCE, compound.get("behavior")).getOrThrow());
    }

    @Override
    public void writeAdditional(@NotNull CompoundNBT compound) {
        super.writeAdditional(compound);
        // var registryops = this.registryAccess().createSerializationContext(NbtOps.INSTANCE);
        compound.putFloat("damage", this.getDamage());
        compound.putInt("lifespan", this.getLifespan());

        // if (this.getBehavior() == null) return;
        // compound.put("behavior", this.getBehavior().toNbt());
    }

    public void setDamage(float damage) {
        this.dataManager.set(DATA_DAMAGE, damage);
        this.damage = damage;
    }

    public void setLifespan(int lifespan) {
        this.dataManager.set(DATA_LIFESPAN, this.lifespan);
        this.lifespan = lifespan;
    }

    public float getDamage() {
        return this.dataManager.get(DATA_DAMAGE);
    }


    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    protected void onEntityHit(EntityRayTraceResult result) {
        if (this.onHit != null) this.onHit.accept(result);
        this.onLunarCollide(result);
        if (this.getShooter() == null) return;
        if (this.getShooter() instanceof MobEntity || this.getShooter() instanceof IAngerable)
            this.onMobShoot(result);

        if (this.getShooter() instanceof PlayerEntity)
            this.onPlayerShoot(result);
    }

    private void onMobShoot(EntityRayTraceResult result){
        if (this.getShooter() == null) return;
        if (!(result.getEntity() instanceof LivingEntity)) return;
        if (result.getEntity() instanceof MobEntity) return;

        LivingEntity target = (LivingEntity) result.getEntity();
        if (target.getUniqueID() == this.getUniqueID()) return;

        target.attackEntityFrom(GSKODamageSource.DANMAKU, this.getDamage());
        this.remove();
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    private void onPlayerShoot(EntityRayTraceResult result){

        if (this.getShooter() == null) return;
        if (!(result.getEntity() instanceof LivingEntity)) return;

        LivingEntity target = (LivingEntity) result.getEntity();
        if (target.getUniqueID() == this.getUniqueID()) return;

        target.attackEntityFrom(GSKODamageSource.DANMAKU, this.getDamage());
        this.remove();
    }

    private void onLunarCollide(EntityRayTraceResult result){
        if (!(this instanceof FakeLunarEntity)) return;
        if (!(result.getEntity() instanceof Danmaku)) return;

        Danmaku danmaku = (Danmaku) result.getEntity();
        danmaku.remove();
    }

    @Override
    public @NotNull Item getDefaultItem() {
        return ItemRegistry.LARGE_SHOT_RED.get();
    }

    public void shoot(Vector3d shootVec, float speed){
        if (this.getItem().getItem() == Items.AIR) return;

        this.shoot(shootVec.x, shootVec.y, shootVec.z, speed, 0);
        this.world.addEntity(this);
    }

    public void shootTo(LivingEntity target, float speed){
        float offset = (float) (0.3f / target.getYOffset());
        Vector3d shootVec = new Vector3d(target.getPosX() - this.getPosX(), target.getPosY() - this.getPosY() - offset, target.getPosZ() - this.getPosZ())
                .normalize();
        this.shoot(shootVec, speed);
    }


    /**
     * 这个哈希表中存放的是那些应该被进行法向渲染的投掷物弹幕。使用普通精灵渲染的弹幕物品不在此列内。布尔值表示是否将该弹幕贴图朝下的方向作为其法向。
     */
    public static final Map<Item,Pair<Boolean, Float>> NORMAL_DANMAKU = Util.make(() -> {
        Map<Item, Pair<Boolean, Float>> map = new HashMap<>();
        map.put(null, Pair.of(false, 0F));
        map.put(Items.AIR, Pair.of(false, 0F));

        map.put(ItemRegistry.SCALE_SHOT.get(), Pair.of(false, 0.5F));
        map.put(ItemRegistry.SCALE_SHOT_RED.get(), Pair.of(false, 0.5F));
        map.put(ItemRegistry.SCALE_SHOT_YELLOW.get(), Pair.of(false, 0.5F));
        map.put(ItemRegistry.SCALE_SHOT_GREEN.get(), Pair.of(false, 0.5F));
        map.put(ItemRegistry.SCALE_SHOT_BLUE.get(), Pair.of(false, 0.5F));
        map.put(ItemRegistry.SCALE_SHOT_PURPLE.get(), Pair.of(false, 0.5F));

        map.put(ItemRegistry.TALISMAN_SHOT.get(), Pair.of(false, 1F));
        map.put(ItemRegistry.TALISMAN_SHOT_RED.get(), Pair.of(false, 1F));
        map.put(ItemRegistry.TALISMAN_SHOT_GREEN.get(), Pair.of(false, 1F));
        map.put(ItemRegistry.TALISMAN_SHOT_BLUE.get(), Pair.of(false, 1F));
        map.put(ItemRegistry.TALISMAN_SHOT_PURPLE.get(), Pair.of(false, 1F));

        map.put(ItemRegistry.RICE_SHOT.get(), Pair.of(false, 0.5F));
        map.put(ItemRegistry.RICE_SHOT_RED.get(), Pair.of(false, 0.5F));
        map.put(ItemRegistry.RICE_SHOT_BLUE.get(), Pair.of(false, 0.5F));
        map.put(ItemRegistry.RICE_SHOT_PURPLE.get(), Pair.of(false, 0.5F));

        map.put(ItemRegistry.HEART_SHOT.get(), Pair.of(true, 2.0F));
        map.put(ItemRegistry.HEART_SHOT_RED.get(), Pair.of(true, 2.0F));
        map.put(ItemRegistry.HEART_SHOT_BLUE.get(), Pair.of(true, 2.0F));
        map.put(ItemRegistry.HEART_SHOT_PINK.get(), Pair.of(true, 2.0F));

        map.put(ItemRegistry.CRYSTAL_AQUA.get(), Pair.of(false, 0.4F));
        map.put(ItemRegistry.CRYSTAL_BLUE.get(), Pair.of(false, 0.4F));
        map.put(ItemRegistry.KNIFE_BLUE.get(), Pair.of(false, 0.6F));
        map.put(ItemRegistry.KNIFE_RED.get(), Pair.of(false, 0.6F));
        map.put(ItemRegistry.KNIFE_GREEN.get(), Pair.of(false, 0.6F));

        return map;
    });

    /**
     * 这个哈希表中存放的是普通渲染的弹幕，通过浮点数哈希值来确定弹幕应该被渲染的大小
     */
    public static final Map<Item,Float> DANMAKU_SIZES = Util.make(() -> {
        Map<Item, Float> map = new HashMap<>();
        map.put(null, 0.5F);
        map.put(Items.AIR, 0.5F);

        map.put(ItemRegistry.LARGE_SHOT.get(), 2F);
        map.put(ItemRegistry.LARGE_SHOT_RED.get(), 2F);
        map.put(ItemRegistry.LARGE_SHOT_ORANGE.get(), 2F);
        map.put(ItemRegistry.LARGE_SHOT_YELLOW.get(), 2F);
        map.put(ItemRegistry.LARGE_SHOT_GREEN.get(), 2F);
        map.put(ItemRegistry.LARGE_SHOT_AQUA.get(), 2F);
        map.put(ItemRegistry.LARGE_SHOT_BLUE.get(), 2F);
        map.put(ItemRegistry.LARGE_SHOT_PURPLE.get(), 2F);
        map.put(ItemRegistry.LARGE_SHOT_MAGENTA.get(), 2F);

        map.put(ItemRegistry.SMALL_SHOT.get(), 0.4F);
        map.put(ItemRegistry.SMALL_SHOT_RED.get(), 0.4F);
        map.put(ItemRegistry.SMALL_SHOT_ORANGE.get(), 0.4F);
        map.put(ItemRegistry.SMALL_SHOT_YELLOW.get(), 0.4F);
        map.put(ItemRegistry.SMALL_SHOT_GREEN.get(), 0.4F);
        map.put(ItemRegistry.SMALL_SHOT_AQUA.get(), 0.4F);
        map.put(ItemRegistry.SMALL_SHOT_BLUE.get(), 0.4F);
        map.put(ItemRegistry.SMALL_SHOT_PURPLE.get(), 0.4F);
        map.put(ItemRegistry.SMALL_SHOT_MAGENTA.get(), 0.4F);

        map.put(ItemRegistry.LARGE_STAR_SHOT.get(), 3F);
        map.put(ItemRegistry.LARGE_STAR_SHOT_RED.get(), 3F);
        map.put(ItemRegistry.LARGE_STAR_SHOT_YELLOW.get(), 3F);
        map.put(ItemRegistry.LARGE_STAR_SHOT_GREEN.get(), 3F);
        map.put(ItemRegistry.LARGE_STAR_SHOT_AQUA.get(), 3F);
        map.put(ItemRegistry.LARGE_STAR_SHOT_BLUE.get(), 3F);
        map.put(ItemRegistry.LARGE_STAR_SHOT_PURPLE.get(), 3F);

        map.put(ItemRegistry.SMALL_STAR_SHOT.get(), 0.4F);
        map.put(ItemRegistry.SMALL_STAR_SHOT_RED.get(), 0.4F);
        map.put(ItemRegistry.SMALL_STAR_SHOT_YELLOW.get(), 0.4F);
        map.put(ItemRegistry.SMALL_STAR_SHOT_GREEN.get(), 0.4F);
        map.put(ItemRegistry.SMALL_STAR_SHOT_AQUA.get(), 0.4F);
        map.put(ItemRegistry.SMALL_STAR_SHOT_BLUE.get(), 0.4F);
        map.put(ItemRegistry.SMALL_STAR_SHOT_PURPLE.get(), 0.4F);

        map.put(ItemRegistry.FAKE_LUNAR_ITEM.get(), 4F);

        return map;
    });

    public static final Map<Item, Actions.EntityRender<Danmaku>> SPECIAL_RENDERER = Util.make(() -> {
        Map<Item, Actions.EntityRender<Danmaku>> map = new HashMap<>();

        map.put(null, (entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn) -> {});
        map.put(Items.AIR, (entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn) -> {});

        map.put(ItemRegistry.DESTRUCTIVE_EYE.get(), (entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn) -> {
            matrixStackIn.push();
            Matrix4f matrix4f = GSKOMathUtil.transform(matrixStackIn,
                    V3f.of(0, 0.5f, 0),
                    Rot3f.identity(),
                    V3f.one(),
                    V3f.zero());

            GeometryUtil.renderSphere(bufferIn.getBuffer(GSKORenderTypes.MULTI_FACE_SOLID),
                    matrix4f, 12, 12, 0.5F, 0F, 0F, 0F, 1F);
            matrixStackIn.pop();
        });

        map.put(ItemRegistry.DREAM_SEAL_ITEM.get(), (entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn) -> {
            matrixStackIn.push();
            matrixStackIn.translate(0, 0.5f, 0f);
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180.0F));
            int rand = new Random().nextInt(3);
            int color = 0;
            switch (rand){
                case 0:
                default:
                    color = 0xFF0000;
                    break;
                case 1:
                    color = 0x00FF00;
                    break;
                case 2:
                    color = 0x0000FF;
                    break;
            }
            float r = (color >> 16) & 0xFF;
            float g = (color >> 8) & 0xFF;
            float b = color & 0xFF;

            matrixStackIn.scale(4f, 4f, 4f);
            matrixStackIn.translate(-0.5f, -0.5f, 0f);
            GeometryUtil.renderSphere(bufferIn.getBuffer(RenderType.getLightning()), matrixStackIn.getLast().getMatrix(),
                    10, 10, 0.3f, r, g, b, 0.5f);
            GeometryUtil.renderSphere(bufferIn.getBuffer(RenderType.getLightning()), matrixStackIn.getLast().getMatrix(),
                    10, 10, 0.2f, 1f, 1f, 1f, 1f);
            matrixStackIn.pop();
        });
        return map;
    });
}
