package github.thelawf.gensokyoontology.common.entity.misc;

import github.thelawf.gensokyoontology.api.IRayTracer;
import github.thelawf.gensokyoontology.common.capability.GSKOCapabilities;
import github.thelawf.gensokyoontology.common.entity.AffiliatedEntity;
import github.thelawf.gensokyoontology.common.util.GSKODamageSource;
import github.thelawf.gensokyoontology.core.GSKOSoundEvents;
import github.thelawf.gensokyoontology.core.init.EntityRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.IPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

public class LunarFall extends AffiliatedEntity implements IRayTracer {
    public static final int PREPARATION = 60;
    public static final int MAX_TICK = 200;
    public static final float MIN_RADIUS = 3.F;
    public static final float MAX_RADIUS = 100.F;
    public static final int FALL_START = PREPARATION - 20;

    public LunarFall(EntityType<?> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
        this.ignoreFrustumCheck = true;
    }

    public LunarFall(Entity owner, World worldIn) {
        super(EntityRegistry.LUNAR_FALL.get(), owner, worldIn);
        this.ignoreFrustumCheck = true;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.ticksExisted > MAX_TICK) {
            this.remove();
            return;
        }

        if(this.ticksExisted == 0 && this.world.isRemote) {
            this.world.playSound(this.getPosX(), this.getPosY(), this.getPosZ(), GSKOSoundEvents.MUSIC_GENSOKYO.get(),
                    SoundCategory.VOICE, 1.0F, 1.0F, false);
        }

        if (this.ticksExisted == PREPARATION) {
            this.world.createExplosion(this, this.getPosX(), this.getPosY(), this.getPosZ(), 3, Explosion.Mode.NONE);
            this.world.getEntitiesWithinAABB(LivingEntity.class, this.getBoundingBox()).stream().filter(
                    this::isEntityInRange).forEach(entity -> {
                if (this.getOwner() == null) return;
                this.getOwner().getCapability(GSKOCapabilities.POWER).ifPresent(power ->
                        entity.attackEntityFrom(DamageSource.MAGIC, power.getCount() * 6));
            });

            if (this.ticksExisted > PREPARATION) {
                double amount = (this.ticksExisted - PREPARATION);
                this.world.getEntitiesWithinAABB(LivingEntity.class, this.getBoundingBox().expand(amount, 0.0D, amount))
                        .stream().filter(this::isEntityInRange).forEach(entity -> {
                            if (this.getOwner() == null) return;
                            GSKODamageSource.causeImpact(this.getOwner(), entity, 0.3F);
                        });

            }
        }
    }

    @Override
    public @NotNull IPacket<?> createSpawnPacket() {
        return  NetworkHooks.getEntitySpawningPacket(this);
    }

    public boolean isEntityInRange(LivingEntity living) {
        double amount = (this.ticksExisted - PREPARATION);
        AxisAlignedBB aabb = this.getBoundingBox().expand(amount, 0.0D, amount);
        double maxRadius = aabb.maxX - aabb.minX;
        double minRadius = maxRadius - 1;
        double distance = living.getPositionVec().distanceTo(aabb.getCenter());
        return distance >= minRadius && distance <= maxRadius;
    }
}
