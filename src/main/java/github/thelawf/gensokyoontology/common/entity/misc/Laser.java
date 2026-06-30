package github.thelawf.gensokyoontology.common.entity.misc;

import github.thelawf.gensokyoontology.api.IRayTracer;
import github.thelawf.gensokyoontology.common.entity.AffiliatedEntity;
import github.thelawf.gensokyoontology.common.util.GSKODamageSource;
import github.thelawf.gensokyoontology.common.util.danmaku.SpellBehavior;
import github.thelawf.gensokyoontology.common.util.math.Rot2f;
import github.thelawf.gensokyoontology.core.init.EntityRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Laser extends AffiliatedEntity implements IRayTracer {
    private int lifespan = 100;
    private int preparation = 30;
    private float range = 128;
    private int argb = 0xFFFF0000;
    private final List<SpellBehavior> behaviors = new ArrayList<>();
    public static final DataParameter<Integer> DATA_COLOR = EntityDataManager.createKey(Laser.class, DataSerializers.VARINT);
    public static final DataParameter<Integer> DATA_LIFESPAN = EntityDataManager.createKey(Laser.class, DataSerializers.VARINT);
    public static final DataParameter<Integer> DATA_PREPARATION = EntityDataManager.createKey(Laser.class, DataSerializers.VARINT);
    public static final DataParameter<Float> DATA_RANGE = EntityDataManager.createKey(Laser.class, DataSerializers.FLOAT);
    public Laser(EntityType<?> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
        this.ignoreFrustumCheck = true;
        this.setARGB(0x88FF0000);
        this.init(100, 30, 128F);
    }

    public Laser(World worldIn, Entity owner) {
        this(EntityRegistry.LASER_SOURCE_ENTITY.get(), worldIn);
        this.ignoreFrustumCheck = true;
        this.setARGB(0x88FF0000);
        this.init(100, 30, 128F);
        this.setOwnerId(owner.getUniqueID());
    }

    public static Laser create(World worldIn, Entity owner) {
        Laser laser = new Laser(worldIn, owner);
        laser.setPosition(owner.getPosX(), owner.getPosY(), owner.getPosZ());
        worldIn.addEntity(laser);
        return laser;
    }

    public Laser pos(Vector3d pos) {
        this.setPosition(pos.x, pos.y, pos.z);
        return this;
    }

    public Laser rot(Rot2f rotation){
        this.setRotation(rotation.yaw(), rotation.pitch());
        return this;
    }

    public Laser color(int color) {
        this.setARGB(color);
        return this;
    }

    public Laser lifespan(int lifespan) {
        this.setLifespan(lifespan);
        return this;
    }

    public Laser range(float range) {
        this.setRange(range);
        return this;
    }

    public Laser preparation(int preparation) {
        this.setPreparation(preparation);
        return this;
    }

    public Laser owner(Entity owner) {
        this.setOwnerId(owner.getUniqueID());
        return this;
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(DATA_LIFESPAN, this.lifespan);
        this.dataManager.register(DATA_PREPARATION, this.preparation);
        this.dataManager.register(DATA_RANGE, this.range);
        this.dataManager.register(DATA_COLOR, this.argb);
    }

    @Override
    protected void readAdditional(@NotNull CompoundNBT compound) {
        super.readAdditional(compound);
        if (compound.contains("lifespan")) {
            this.lifespan = compound.getInt("lifespan");
            this.setLifespan(compound.getInt("lifespan"));
        }
        if (compound.contains("preparation")) {
            this.preparation = compound.getInt("preparation");
            this.setPreparation(compound.getInt("preparation"));
        }
        if (compound.contains("argb")) {
            this.argb = compound.getInt("argb");
            this.setARGB(compound.getInt("argb"));
        }
        if (compound.contains("range")) {
            this.range = compound.getFloat("range");
            this.setRange(compound.getFloat("range"));
        }
    }

    @Override
    protected void writeAdditional(@NotNull CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putInt("lifespan", this.lifespan);
        compound.putInt("preparation", this.preparation);
        compound.putInt("argb", this.argb);
        compound.putFloat("range", this.range);
    }

    @Override
    public void tick() {
        super.tick();
        if (getRemainingLife() <= 0) this.remove();
        if (!shouldEmit()) return;

        behaviors.stream().filter(behavior -> ticksExisted == behavior.keyTick).forEach(
                behavior -> {
                    this.setLocationAndAngles(behavior.pos.x, behavior.pos.y, behavior.pos.z, behavior.rotation.x, behavior.rotation.y);
                    this.setMotion(behavior.motion.x, behavior.motion.y, behavior.motion.z);
                });

        Vector3d start = this.getPositionVec();
        Vector3d end = this.getLookVec().scale(this.range).add(start);
        Predicate<Entity> canAttack = entity -> this.getOwnerID().isPresent() && entity.getUniqueID() != this.getOwnerID().get();

        if (this.ticksExisted % 2 == 0 && rayTrace(this.world, this, start, end).isPresent()) {
            rayTrace(this.world, this, start, end).ifPresent(entity -> {
                if (canAttack.test(entity)) entity.attackEntityFrom(GSKODamageSource.LASER, 3);
            });
        }
    }

    public void init(int lifespan, int preparation, float range) {
        this.setLifespan(lifespan);
        this.setPreparation(preparation);
        this.setRange(range);
    }

    public int getLifespan() {
        return this.dataManager.get(DATA_LIFESPAN) == 0 ? this.lifespan : this.dataManager.get(DATA_LIFESPAN);
    }

    public int getRemainingLife() {
        return getLifespan() - this.ticksExisted;
    }

    public void setLifespan(int lifespan) {
        this.lifespan = lifespan;
        this.dataManager.set(DATA_LIFESPAN, lifespan);
    }

    public int getPreparation() {
        return this.dataManager.get(DATA_PREPARATION) == 0 ? this.preparation : this.dataManager.get(DATA_PREPARATION);
    }

    public boolean shouldEmit() {
        return this.ticksExisted >= this.getPreparation() && this.ticksExisted < this.getLifespan();
    }

    public void setPreparation(int preparation) {
        this.preparation = preparation;
        this.dataManager.set(DATA_PREPARATION, preparation);
    }

    public float getRange() {
        return this.dataManager.get(DATA_RANGE) == 0 ? this.range : this.dataManager.get(DATA_RANGE);
    }

    public void setRange(float range) {
        this.range = range;
        this.dataManager.set(DATA_RANGE, range);
    }

    public void setARGB(int argb) {
        this.argb = argb;
        this.dataManager.set(DATA_COLOR, argb);
    }

    public int getARGB() {
        return this.dataManager.get(DATA_COLOR);
    }

    public int getAlpha() {
        return (this.getARGB() >> 24) & 0xFF;
    }

    public int getRed() {
        return (this.getARGB() >> 16) & 0xFF;
    }

    public int getGreen() {
        return (this.getARGB() >> 8) & 0xFF;
    }

    public int getBlue() {
        return this.getARGB() & 0xFF;
    }

}
