package com.github.fictology.gensokyoontology.common.event;

import com.github.fictology.gensokyoontology.registry.EntityRegistry;
import com.github.fictology.gensokyoontology.registry.Expressions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;

@EventBusSubscriber
public class GSKOCommonEvents {
    @SubscribeEvent
    public static void onCustomRegistry(NewRegistryEvent event) {
        event.register(Expressions.REGISTRY);
    }

    @SubscribeEvent
    public static void onAddAttributes(EntityAttributeCreationEvent event){
        event.put(EntityRegistry.FAIRY_ENTITY.get(),
                LivingEntity.createLivingAttributes()
                        .add(Attributes.MOVEMENT_SPEED)
                        .add(Attributes.MAX_HEALTH, 20)
                        .add(Attributes.ATTACK_DAMAGE, 2)
                        .build());

        event.put(EntityRegistry.LILY_WHITE.get(),
                LivingEntity.createLivingAttributes()
                        .add(Attributes.MOVEMENT_SPEED)
                        .add(Attributes.MAX_HEALTH, 100)
                        .add(Attributes.ATTACK_DAMAGE, 3)
                        .build());

        event.put(EntityRegistry.HAKUREI_REIMU.get(),
                LivingEntity.createLivingAttributes()
                        .add(Attributes.MOVEMENT_SPEED)
                        .add(Attributes.ARMOR, 10)
                        .add(Attributes.MAX_HEALTH, 200)
                        .add(Attributes.ATTACK_DAMAGE, 10)
                        .build());

        event.put(EntityRegistry.FLANDRE_SCARLET.get(),
                LivingEntity.createLivingAttributes()
                        .add(Attributes.MOVEMENT_SPEED, 0.8)
                        .add(Attributes.ARMOR, 10)
                        .add(Attributes.ARMOR_TOUGHNESS, 20)
                        .add(Attributes.MAX_HEALTH, 495)
                        .add(Attributes.ATTACK_DAMAGE, 10)
                        .build());

        event.put(EntityRegistry.REMILIA_SCARLET.get(),
                LivingEntity.createLivingAttributes()
                        .add(Attributes.MOVEMENT_SPEED, 0.8)
                        .add(Attributes.ARMOR, 15)
                        .add(Attributes.MAX_HEALTH, 500)
                        .add(Attributes.ATTACK_DAMAGE, 10)
                        .build());
    }

    public static void redirectAttribute(RangedAttribute attribute, double min, double max){
        // attribute.getMaxValue(); // use AccessTransformer to make the inner field not private and not final
    }

}
