package github.thelawf.gensokyoontology.common.item.touhou;

import github.thelawf.gensokyoontology.api.IHasCooldown;
import github.thelawf.gensokyoontology.api.IRayTracer;
import github.thelawf.gensokyoontology.common.entity.misc.Laser;
import github.thelawf.gensokyoontology.common.item.MultiModeItem;
import github.thelawf.gensokyoontology.common.util.GSKOUtil;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public class KoishiEyeOpen extends MultiModeItem implements IRayTracer, IHasCooldown {
    private int totalCount = 0;
    public KoishiEyeOpen(Properties properties) {
        super(properties);
    }

    @Override
    @NotNull
    public ActionResult<ItemStack> onItemRightClick(@NotNull World worldIn, @NotNull PlayerEntity playerIn, @NotNull Hand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        if (playerIn.getCooldownTracker().hasCooldown(this))
            return ActionResult.resultPass(stack);

        if (stack.getTag() == null || !stack.getTag().contains("total_count")) {
            CompoundNBT nbt = new CompoundNBT();
            nbt.putInt("total_count",  playerIn.ticksExisted + this.getUseDuration(stack));
            stack.setTag(nbt);
        }

        Laser laserSource = new Laser(worldIn, playerIn);
        laserSource.init(200, 40, 85);
        laserSource.setARGB(0x8800FF00);
        // lasers(worldIn, playerIn);

        laserSource.setLocationAndAngles(playerIn.getPosX(), playerIn.getPosY() + playerIn.getEyeHeight() * 0.5,
                playerIn.getPosZ(), playerIn.rotationYaw, playerIn.rotationPitch);
        worldIn.addEntity(laserSource);

        playerIn.setActiveHand(handIn);
        if (stack.getTag() != null && playerIn.ticksExisted < stack.getTag().getInt("total_count")) {
            if (playerIn.ticksExisted < stack.getTag().getInt("total_count")) {
                playerIn.stopActiveHand();
                applyRayDamage(worldIn, playerIn, stack, 15);
            }
            else onItemUseFinish(stack, worldIn, playerIn);
        }

        this.setCD(playerIn, stack, 300);
        if (playerIn.isCreative()) return super.onItemRightClick(worldIn, playerIn, handIn);
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    /**
     * 这里实现的是妖怪测谎仪的激光，需要注意的是这里第一行的谓词需要取非再传入IRayTraceReader.getEntityWithinSphere()
     */
    public void applyRayDamage(World worldIn, LivingEntity user, ItemStack stack, int radius) {
        Predicate<LivingEntity> predicate = living -> living instanceof PlayerEntity;

        if (predicate.test(user)) {
            PlayerEntity player = (PlayerEntity) user;
            if (player.getCooldownTracker().hasCooldown(this)) return;
            AxisAlignedBB box = createCubeBox(player.getPositionVec(), radius);

            Vector3d start = player.getPositionVec();
            Vector3d vec = new Vector3d(player.getLookVec().x, 0, player.getLookVec().z);
            Vector3d vector3d = start.add(vec.scale(10));

            // TODO: 妖怪测谎仪数值设计
            for (int i = 0; i < 8; i++) {
                double angle = Math.PI * 2 / this.getUseDuration(stack) * totalCount;
                Vector3d end = vector3d.rotateYaw((float) Math.PI * 2 / 8 * i).rotateYaw((float) angle);
                getEntityWithinSphere(worldIn, LivingEntity.class, predicate.negate(), box, 12F).stream()
                        .filter(living -> isIntersecting(start, end, living.getBoundingBox().offset(0, -1.5, 0)))
                        .forEach(living -> living.attackEntityFrom(DamageSource.causePlayerDamage(player), 8F));
                if (angle >= Math.PI * 2) totalCount = 0;
            }
        }
    }

    @Override
    public void onPlayerStoppedUsing(@NotNull ItemStack stack, @NotNull World worldIn, @NotNull LivingEntity entityLiving, int timeLeft) {
        super.onPlayerStoppedUsing(stack, worldIn, entityLiving, timeLeft);
        stack.setTag(new CompoundNBT());
    }

    @Override
    public @NotNull ItemStack onItemUseFinish(@NotNull ItemStack stack, @NotNull World worldIn, @NotNull LivingEntity entityLiving) {
        stack.setTag(new CompoundNBT());
        return super.onItemUseFinish(stack, worldIn, entityLiving);
    }

    @Override
    public void addInformation(@NotNull ItemStack stack, @Nullable World worldIn, @NotNull List<ITextComponent> tooltip, @NotNull ITooltipFlag flagIn) {
        tooltip.add(GSKOUtil.translateText("tooltip.", ".koishi_eye_open"));
        if (Screen.hasShiftDown()) {
            tooltip.add(GSKOUtil.translateText("tooltip.", ".koishi_eye_open.comment"));
        }
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    @NotNull
    public UseAction getUseAction(@NotNull ItemStack stack) {
        return UseAction.BOW;
    }

    public static Mode getMode(CompoundNBT nbt) {
        return Mode.values()[nbt.getInt("mode")];
    }
    public enum Mode{
        SINGLE_LASER,
        YOUKAI_LIE_DETECTOR;
    }
}
