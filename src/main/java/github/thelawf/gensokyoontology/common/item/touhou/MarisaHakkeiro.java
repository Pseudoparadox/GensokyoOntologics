package github.thelawf.gensokyoontology.common.item.touhou;

import github.thelawf.gensokyoontology.GensokyoOntology;
import github.thelawf.gensokyoontology.api.IHasCooldown;
import github.thelawf.gensokyoontology.api.IRayTracer;
import github.thelawf.gensokyoontology.common.entity.misc.MasterSparkEntity;
import github.thelawf.gensokyoontology.common.util.GSKOUtil;
import github.thelawf.gensokyoontology.core.GSKOSoundEvents;
import github.thelawf.gensokyoontology.core.init.ItemRegistry;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShootableItem;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

/**
 * 魔理沙的八卦炉
 */
public class MarisaHakkeiro extends ShootableItem implements IRayTracer, IHasCooldown {
    public MarisaHakkeiro(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull Predicate<ItemStack> getInventoryAmmoPredicate() {
        return stack -> true;
    }

    @Override
    public int func_230305_d_() {
        return 15;
    }

    /**
     * @param timeLeft 该参数会从 {@link MarisaHakkeiro#getUseDuration(ItemStack)} 的返回值开始每 1 tick 减 1
     */
    @Override
    public void onPlayerStoppedUsing(@NotNull ItemStack stack, @NotNull World worldIn, @NotNull LivingEntity living, int timeLeft) {
        super.onPlayerStoppedUsing(stack, worldIn, living, timeLeft);
    }

    @Override
    public @NotNull ActionResult<ItemStack> onItemRightClick(@NotNull World worldIn, PlayerEntity playerIn, @NotNull Hand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        playerIn.setActiveHand(handIn);

        if (playerIn.getCooldownTracker().hasCooldown(this)) return ActionResult.resultPass(stack);
        return ActionResult.resultPass(stack);
    }

    /**
     * @param count 这个参数会随着玩家使用时间从 0 开始每 1 tick 加 1
     */
    @Override
    public void onUsingTick(ItemStack stack, LivingEntity living, int count) {
        super.onUsingTick(stack, living, count);
        if (!(living instanceof PlayerEntity)) return;
        PlayerEntity playerIn = (PlayerEntity) living;
        World world = playerIn.world;

        if (!this.canLaunchSpark(stack, playerIn, count)) return;

        Vector3d sparkPos = playerIn.getPositionVec().add(playerIn.getLookVec().scale(2));
        MasterSparkEntity masterSpark = new MasterSparkEntity(playerIn, world);
        masterSpark.setLocationAndAngles(sparkPos.x, sparkPos.y, sparkPos.z, playerIn.rotationYaw, playerIn.rotationPitch);
        world.addEntity(masterSpark);

        if (world.isRemote) {
            world.playSound(playerIn, playerIn.getPosition(), GSKOSoundEvents.MASTER_SPARK.get(), SoundCategory.AMBIENT, 0.8f, 1f);
        }
        // Vector3d explodeStartPos = playerIn.getEyePosition(1.0F).add(playerIn.getLookVec().scale(8));
        // this.causeExplosion(worldIn, playerIn, explodeStartPos);
        this.setCD(playerIn, stack, 1800);
    }

    @Override
    public void addInformation(@NotNull ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, @NotNull ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("tooltip." + GensokyoOntology.MODID + ".marisa_hakkeiro.info"));
//        tooltip.add(new TranslationTextComponent("tooltip." + GensokyoOntology.MODID + ".marisa_hakkeiro"));
//        if (Screen.hasShiftDown()) {
//            tooltip.add(new TranslationTextComponent("tooltip." + GensokyoOntology.MODID + ".marisa_hakkeiro.info"));
//         }
    }

    @Override
    @NotNull
    public UseAction getUseAction(@NotNull ItemStack stack) {
        return UseAction.BOW;
    }


    @Override
    public int getUseDuration(@NotNull ItemStack stack) {
        return 72000;
    }

    public int getChargeDuration() {
        return 60;
    }

    private void causeExplosion(World worldIn, PlayerEntity playerIn, Vector3d explodeStartPos) {
        for (int i = 0; i < 50; i++) {
            Vector3d explodePos = explodeStartPos.add(playerIn.getLookVec().scale(i));
            worldIn.createExplosion(playerIn, explodePos.getX(), explodePos.getY(), explodePos.getZ(),
                    1.0f, false, Explosion.Mode.NONE);
        }
    }

    public boolean canLaunchSpark(ItemStack stack, PlayerEntity player, int tickLeft) {
        if (tickLeft != this.getUseDuration(stack) - this.getChargeDuration()) return false;

        boolean hasBomb = false;
        boolean has32FireCharge = false;
        ItemStack fireCharge = ItemStack.EMPTY;
        ItemStack bomb = ItemStack.EMPTY;
        PlayerInventory inventory = player.inventory;

        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            ItemStack stackInSlot = inventory.getStackInSlot(i);
            if (stackInSlot.getItem().equals(ItemRegistry.BOMB_ITEM.get())) {
                hasBomb = true;
                bomb = stackInSlot;
            } else if (stackInSlot.getItem().equals(Items.FIRE_CHARGE) && stackInSlot.getCount() >= 32) {
                has32FireCharge = true;
                fireCharge = stackInSlot;
            }
        }
        if (bomb.isEmpty() || !hasBomb) {
            GSKOUtil.showChatMsg(player, GSKOUtil.translateText("error.",".hakkeiro.no_enough_bomb"), 1);
            return false;
        }
        if (fireCharge.isEmpty() || !has32FireCharge) {
            GSKOUtil.showChatMsg(player, GSKOUtil.translateText("error.", ".hakkeiro.no_enough_fire_charge"), 1);
            return false;
        }

        bomb.shrink(1);
        fireCharge.shrink(32);
        return true;
    }
}
