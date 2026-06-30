package github.thelawf.gensokyoontology.common.item.touhou;

import github.thelawf.gensokyoontology.api.IHasCooldown;
import github.thelawf.gensokyoontology.api.IRayTracer;
import github.thelawf.gensokyoontology.common.entity.projectile.Danmaku;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.jetbrains.annotations.NotNull;

/**
 * 咲夜的怀表物品
 */
public class SakuyaStopWatch extends Item implements IRayTracer, IHasCooldown {
    public static int pauseTicks = 100;
    public static int totalTicks = 0;

    public SakuyaStopWatch(Properties properties) {
        super(properties);
    }

    @Override
    @NotNull
    public ActionResult<ItemStack> onItemRightClick(@NotNull World worldIn, PlayerEntity playerIn, @NotNull Hand handIn) {
        if (worldIn.isRemote) return super.onItemRightClick(worldIn, playerIn, handIn);
        if (playerIn.getCooldownTracker().hasCooldown(this)) return super.onItemRightClick(worldIn, playerIn, handIn);

        ServerWorld serverWorld = (ServerWorld) worldIn;
        serverWorld.getEntities().filter(entity -> entity instanceof Danmaku)
                .forEach(entity -> entity.setMotion(Vector3d.ZERO));

        this.setCD(playerIn, playerIn.getHeldItem(handIn), 1000);
        return super.onItemRightClick(worldIn, playerIn, handIn);
//        ItemStack stack = playerIn.getHeldItem(handIn);

//        if (stack.getTag() != null && stack.hasTag()) {
//            stack.setTag(new CompoundNBT());
//            totalTicks = 0;
//
//            if (playerIn.isCreative()) return super.onItemRightClick(worldIn, playerIn, handIn);
//            playerIn.getCooldownTracker().setCooldown(this, 6000);
//            return ActionResult.resultPass(stack);
//        }
//
//        CompoundNBT nbt = new CompoundNBT();
//        nbt.putBoolean("pause_start", true);
//        stack.setTag(nbt);
//        totalTicks = pauseTicks + playerIn.ticksExisted;
//
//        if (playerIn.isCreative())
//            return super.onItemRightClick(worldIn, playerIn, handIn);
//        if (playerIn.getHeldItemMainhand().getItem() == this &&
//                playerIn.getHeldItemOffhand().getItem() == ItemRegistry.WASHI_PAPER.get()) {
//            ItemStack offHand = playerIn.getHeldItemOffhand();
//            offHand.shrink(1);
//            ItemStack itemStack = new ItemStack(ItemRegistry.TIME_STAMP.get());
//            playerIn.setHeldItem(Hand.OFF_HAND, itemStack);
//        }
//
//        playerIn.getCooldownTracker().setCooldown(this, 6000);
    }

    @Override
    @NotNull
    public UseAction getUseAction(@NotNull ItemStack stack) {
        return UseAction.BLOCK;
    }

}
