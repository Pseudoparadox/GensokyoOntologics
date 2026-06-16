package com.github.fictology.gensokyoontology.api;


import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public interface IHasCooldown {
    default int getCD(int hasteLevel, int cooldown){
        return (int) (100F / (100 + hasteLevel * 20) * cooldown);
    }

//    default int getCD(CooldownHasteEnchantment enchantment, ItemStack stack, int cooldown){
//        int level = EnchantmentHelper.getEnchantments(stack).getOrDefault(EnchantRegistry.COOLDOWN_HASTE.get(),0);
//        return this.getCD(level, cooldown);
//    }
//
//    default void setCD(PlayerEntity player, ItemStack stack, int basicCooldown) {
//        CooldownTracker cooldownTracker = player.getCooldownTracker();
//
//        int level = EnchantmentHelper.getEnchantments(stack).getOrDefault(EnchantRegistry.COOLDOWN_HASTE.get(), 0);
//        if (level == 0) cooldownTracker.setCooldown(stack.getItem(), basicCooldown);
//        if (cooldownTracker.hasCooldown(stack.getItem())) return;
//
//        cooldownTracker.setCooldown(stack.getItem(), this.getCD(level, basicCooldown));
//
//    }
}
