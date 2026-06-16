package com.github.fictology.gensokyoontology.common.item.touhou;

import com.github.fictology.gensokyoontology.common.entiy.misc.MasterSparkEntity;
import com.github.fictology.gensokyoontology.registry.GSKOSoundEvents;
import com.github.fictology.gensokyoontology.registry.ItemRegistry;
import com.github.fictology.gensokyoontology.util.GSKOUtil;
import com.github.fictology.gensokyoontology.api.IRayTraceReader;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * 魔理沙的八卦炉
 */
public class MarisaHakkeiro extends Item implements IRayTraceReader, IHasCooldown {
    public MarisaHakkeiro(Properties properties) {
        super(properties);
    }


    @Override
    public void onUseTick(Level world, LivingEntity entityLiving, ItemStack stack, int remainingUseDuration) {

    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (!this.canLaunchSpark(player.getInventory())) {
            GSKOUtil.showMsg(player, "你没有足够火焰弹或者B点", true);
            return InteractionResult.FAIL;
        }
        player.startUsingItem(hand);



//        if (player.level().isClientSide()) {
//            player.level().playSound(player, BlockPos.containing(player.getPosition(0f)),
//                    GSKOSoundEvents.MASTER_SPARK.get(), SoundSource.AMBIENT, 0.8f, 1f);
//        }
        return super.use(level, player, hand);

    }

    @Override
    public boolean releaseUsing(ItemStack stack, Level world, LivingEntity entityLiving, int timeLeft) {
        if (!(entityLiving instanceof Player player)) return false;
        if (timeLeft <= getUseDuration(stack, player)) return false;

        var masterSpark = new MasterSparkEntity(player, player.level());
        masterSpark.setPos(new Vec3(player.getX(), player.getY() + player.getEyeHeight(), player.getZ()));
        masterSpark.setXRot(player.getXRot());
        masterSpark.setYRot(player.getYRot());
        player.level().addFreshEntity(masterSpark);

        if (world.isClientSide()) {
            world.playSound(player, BlockPos.containing(player.getPosition(0f)),
                    GSKOSoundEvents.MASTER_SPARK.get(), SoundSource.AMBIENT, 0.8f, 1f);
        }
        int cooldownTicks = 1800;
        if (player.isCreative()) return true;
        player.getCooldowns().addCooldown(stack, cooldownTicks);

        return true;
    }

    @SuppressWarnings("deprecation")
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull TooltipDisplay tooltip, Consumer<Component> tooltipAdder, @NotNull TooltipFlag flag) {
        // tooltipAdder.accept(GSKOUtil.translate("tooltip", "marisa_hakkeiro"));
        tooltipAdder.accept(GSKOUtil.translate("tooltip", "marisa_hakkeiro.info"));
    }

    @Override
    @NotNull
    public ItemUseAnimation getUseAnimation(@NotNull ItemStack stack) {
        return ItemUseAnimation.BOW;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 7200;
    }

    private void causeExplosion(Level worldIn, Player playerIn, Vec3 explodeStartPos) {
        for (int i = 0; i < 50; i++) {
            Vec3 explodePos = explodeStartPos.add(playerIn.getLookAngle()).scale(i);
            worldIn.explode(playerIn, explodePos.x, explodePos.y, explodePos.z,
                    1.0f, false, Level.ExplosionInteraction.NONE);
        }
    }


    public boolean canLaunchSpark(Inventory inventory) {
        boolean hasBomb = false;
        boolean has32FireCharge = false;
        ItemStack fireCharge = ItemStack.EMPTY;
        ItemStack bomb = ItemStack.EMPTY;

        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stackInSlot = inventory.getItem(i);
            if (stackInSlot.getItem().equals(ItemRegistry.BOMB_ITEM.get())) {
                hasBomb = true;
                bomb = stackInSlot;
            } else if (stackInSlot.getItem().equals(Items.FIRE_CHARGE) && stackInSlot.getCount() >= 32) {
                has32FireCharge = true;
                fireCharge = stackInSlot;
            }
        }

        if (bomb.isEmpty() || fireCharge.isEmpty() || !hasBomb || !has32FireCharge) return false;
        else {
            bomb.shrink(1);
            fireCharge.shrink(32);
            return true;
        }
    }
}
