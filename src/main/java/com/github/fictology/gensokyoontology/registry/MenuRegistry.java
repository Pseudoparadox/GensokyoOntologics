package com.github.fictology.gensokyoontology.registry;

import com.github.fictology.gensokyoontology.GensokyoOntology;
import com.github.fictology.gensokyoontology.common.container.menu.DanmakuCraftMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class MenuRegistry {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(Registries.MENU, GensokyoOntology.MODID);
    public static final Supplier<MenuType<DanmakuCraftMenu>> DANMAKU_MENU = MENU_TYPES.register("danmaku_menu",
            () -> new MenuType<>(DanmakuCraftMenu::new, FeatureFlags.VANILLA_SET));
}
