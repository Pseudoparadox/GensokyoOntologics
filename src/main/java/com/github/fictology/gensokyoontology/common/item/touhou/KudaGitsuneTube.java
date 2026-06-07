package com.github.fictology.gensokyoontology.common.item.touhou;

import com.github.fictology.gensokyoontology.util.GSKOUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

import java.util.function.Consumer;

public class KudaGitsuneTube extends Item {
    public KudaGitsuneTube(Properties properties) {
        super(properties);
    }

    /*
    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand usedHand) {
        if (target instanceof SpectreEntity spectre) {
            stack.shrink(1);
            var spectreName = EntityRegistry.SPELL_CARD.get().getDescriptionId();
            var newItem = new ItemStack(ItemRegistry.JADE_HOE.get());

            newItem.set(DataRegistry.STRING, spectreName);
            player.getInventory().add(newItem);
            return InteractionResult.SUCCESS;
        } else if (target instanceof Ghast) {
            // target.remove();
            stack.shrink(1);
            var ghastName = EntityType.GHAST.getDescriptionId();
            var newItem = new ItemStack(ItemRegistry.JADE_BOOTS.get());

            newItem.set(DataRegistry.STRING, ghastName);
            player.getInventory().add(newItem);
            return InteractionResult.SUCCESS;
        }
        return super.interactLivingEntity(stack, player, target, usedHand);
    }

     */

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
        tooltipAdder.accept(GSKOUtil.translate("tooltip", "kuda_gitsune_tube"));
    }

}
