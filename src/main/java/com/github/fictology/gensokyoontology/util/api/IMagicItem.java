package com.github.fictology.gensokyoontology.util.api;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface IMagicItem {
    default void setCooldownTicks(Player player, ItemStack stack, int cooldownTicks){
        player.getCooldowns().addCooldown(stack, cooldownTicks);
    }
}
