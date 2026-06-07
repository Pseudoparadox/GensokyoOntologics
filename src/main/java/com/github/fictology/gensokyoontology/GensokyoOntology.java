package com.github.fictology.gensokyoontology;

import com.github.fictology.gensokyoontology.common.entiy.misc.Danmaku;
import com.github.fictology.gensokyoontology.registry.*;
import net.minecraft.world.item.*;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(GensokyoOntology.MODID)
public class GensokyoOntology {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "gensokyoontology";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    public static final Supplier<CreativeModeTab> ITEM_TAB = CREATIVE_TABS.register("item_tab", () -> CreativeModeTab.builder().title(Component.translatable("itemGroup.gensokyoontology_combat")).withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> ItemRegistry.AYA_FANS.get().getDefaultInstance()).displayItems((parameters, output) -> {
                output.acceptAll(ItemRegistry.ITEMS.getEntries().stream().map(holder -> new ItemStack(holder.get())).toList());
                // output.accept(ItemRegistry.DANMAKU_SHOT.get());
                // output.accept(ItemRegistry.LARGE_SHOT.get());
                // output.accept(ItemRegistry.LARGE_SHOT_RED.get());
                // output.accept(ItemRegistry.LARGE_SHOT_ORANGE.get());
                // output.accept(ItemRegistry.LARGE_SHOT_YELLOW.get());
                // output.accept(ItemRegistry.LARGE_SHOT_GREEN.get());
                // output.accept(ItemRegistry.LARGE_SHOT_AQUA.get());
            }).build());

    public GensokyoOntology(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        // modEventBus.addListener(this::registerExp);

        BlockRegistry.BLOCKS.register(modEventBus);
        ItemRegistry.ITEMS.register(modEventBus);
        EntityRegistry.ENTITIES.register(modEventBus);

        DataRegistry.ATTACHMENTS.register(modEventBus);
        DataRegistry.DATA.register(modEventBus);
        Expressions.EXPRESSIONS.register(modEventBus);
        CREATIVE_TABS.register(modEventBus);
        // modEventBus.addListener(this::onItemRegister);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.LOG_DIRT_BLOCK.getAsBoolean()) {
            LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));
        }

        LOGGER.info("{}{}", Config.MAGIC_NUMBER_INTRODUCTION.get(), Config.MAGIC_NUMBER.getAsInt());

        Config.ITEM_STRINGS.get().forEach((item) -> LOGGER.info("ITEM >> {}", item));
    }


    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }
}
