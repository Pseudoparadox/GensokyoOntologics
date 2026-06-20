package com.github.fictology.gensokyoontology.api;


import com.github.fictology.gensokyoontology.data.CooldownHaste;
import com.github.fictology.gensokyoontology.registry.EnchantRegistry;
import com.github.fictology.gensokyoontology.util.GSKOGeometry;
import com.github.fictology.gensokyoontology.util.GSKOUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public interface IHasCooldown {
    default int getCD(int hasteLevel, int cooldown){
        return (int) (100F / (100 + hasteLevel * 20) * cooldown);
    }

    default int getCD(ItemStack stack, int cooldown){
        var cd = GSKOUtil.<Integer>atomic();
        EnchantmentHelper.runIterationOnItem(stack, (_, level) -> {
            cd.set(level);
        });
        return cd.get() == null ? cooldown : this.getCD(cd.get(), cooldown);
    }

    default void setCD(Player player, ItemStack stack, int basicCooldown) {
        var cooldownTracker = player.getCooldowns();
        var cd = GSKOUtil.<Integer>atomic();
        cooldownTracker.addCooldown(stack, this.getCD(stack, basicCooldown));
    }
}
