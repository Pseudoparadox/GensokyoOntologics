package com.github.fictology.gensokyoontology.common.event;

import com.github.fictology.gensokyoontology.registry.Expressions;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.NewRegistryEvent;

@EventBusSubscriber
public class GSKOCommonEvents {
    @SubscribeEvent
    public static void onCustomRegistry(NewRegistryEvent event) {
        event.register(Expressions.REGISTRY);
    }
}
