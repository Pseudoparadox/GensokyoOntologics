package github.thelawf.gensokyoontology.common.entity.misc;

import github.thelawf.gensokyoontology.api.IRayTracer;
import github.thelawf.gensokyoontology.common.entity.AffiliatedEntity;
import github.thelawf.gensokyoontology.common.util.GSKODamageSource;
import github.thelawf.gensokyoontology.common.util.danmaku.DanmakuUtil;
import github.thelawf.gensokyoontology.core.init.EntityRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.List;
import java.util.function.Predicate;

public class MasterSparkEntity extends AffiliatedEntity implements IRayTracer {
    public static final float DISTANCE = 50F;
    public MasterSparkEntity(EntityType<?> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
    }

    public MasterSparkEntity(Entity owner, World worldIn) {
        super(EntityRegistry.MASTER_SPARK_ENTITY.get(), owner, worldIn);

    }

    @Override
    public void tick() {
        super.tick();
        if (ticksExisted >= 120) this.remove();
        if (ticksExisted < 40) return;
        if (world.isRemote) return;
        ServerWorld serverWorld = (ServerWorld) world;
        List<Entity> entities = rayTrace(serverWorld, this, DISTANCE, new Vector3d(0,0,0));
        List<Vector3d> startList = DanmakuUtil.ellipticPos(1.5, 10);
        startList.addAll(DanmakuUtil.ellipticPos(2.5, 10));

        startList.forEach(vector3d -> entities.addAll(rayTrace(serverWorld, this, DISTANCE, vector3d)));
        Predicate<Entity> canAttack = entity -> this.getOwnerID().isPresent() && entity.getUniqueID() != this.getOwnerID().get();

        entities.stream().filter(canAttack).forEach(entity -> entity.attackEntityFrom(GSKODamageSource.LASER, 10F));

    }
}
