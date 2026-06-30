package github.thelawf.gensokyoontology.common.item.touhou;

import github.thelawf.gensokyoontology.api.IHasCooldown;
import github.thelawf.gensokyoontology.api.IRayTracer;
import github.thelawf.gensokyoontology.common.util.GSKOUtil;
import github.thelawf.gensokyoontology.common.util.math.GSKOMathUtil;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public class AyaFans extends Item implements IRayTracer, IHasCooldown {
    public AyaFans(Properties properties) {
        super(properties);
    }

    @Override
    @NotNull
    public ActionResult<ItemStack> onItemRightClick(@NotNull World worldIn, PlayerEntity playerIn, @NotNull Hand handIn) {
        if (playerIn.getCooldownTracker().hasCooldown(this))
            return ActionResult.resultPass(playerIn.getHeldItem(handIn));

        AxisAlignedBB aabb = new AxisAlignedBB(playerIn.getPositionVec().subtract(new Vector3d(5, 10, 5)),
                playerIn.getPositionVec().add(new Vector3d(5, 10, 5)));

        Vector3d lookVec = playerIn.getLookVec();
        Predicate<LivingEntity> predicate = living -> !(living instanceof PlayerEntity);
        getEntityWithinSphere(worldIn, LivingEntity.class, predicate, aabb, 12F).forEach(living -> {
            living.applyKnockback(3.0f, -lookVec.x, -lookVec.z);
        });

        AxisAlignedBB box = new AxisAlignedBB(playerIn.getPositionVec().subtract(new Vector3d(12, 12, 12)),
                playerIn.getPositionVec().add(new Vector3d(12, 12, 12)));

        getEntityWithinSphere(worldIn, ProjectileEntity.class, box, 12).forEach(projectile ->
                applyProjectileKnockback(projectile, 3.0f, -lookVec.x, -lookVec.z));

        if (worldIn.isRemote) {
            for (int i = 0; i < GSKOMathUtil.randomRange(30, 60); i++) {
                worldIn.addParticle(ParticleTypes.CLOUD, false, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(),
                        lookVec.x + random.nextDouble(), lookVec.y + random.nextDouble(), lookVec.z + random.nextDouble());
            }
        }

        this.setCD(playerIn, playerIn.getHeldItem(handIn), 160);
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    private void applyProjectileKnockback(ProjectileEntity projectile, float strength, double ratioX, double ratioZ) {
        if (!(strength <= 0.0F)) {
            Vector3d vector3d = projectile.getMotion();
            Vector3d vector3d1 = (new Vector3d(ratioX, 0.0D, ratioZ)).normalize().scale(strength);
            projectile.setMotion(vector3d.x / 2.0D - vector3d1.x, projectile.isOnGround() ? Math.min(0.4D, vector3d.y / 2.0D + (double) strength) : vector3d.y, vector3d.z / 2.0D - vector3d1.z);
        }
    }

    @Override
    public void addInformation(@NotNull ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, @NotNull ITooltipFlag flagIn) {
        tooltip.add(GSKOUtil.translateText("tooltip.", ".aya_fans"));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    @NotNull
    public UseAction getUseAction(@NotNull ItemStack stack) {
        return UseAction.BLOCK;
    }
}
