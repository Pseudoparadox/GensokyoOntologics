package com.github.fictology.gensokyoontology.registry;

import com.github.fictology.gensokyoontology.GensokyoOntology;
import com.github.fictology.gensokyoontology.util.script.ClosureExpression;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class GSKOSerializers {
    public static final EntityDataSerializer<ClosureExpression> CLOSURE = EntityDataSerializer.forValueType(ClosureExpression.EMPTY.streamCodec());
    public static final DeferredRegister<EntityDataSerializer<?>> SERIALIZERS = DeferredRegister.create(
            NeoForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, GensokyoOntology.MODID);
    public static final Supplier<EntityDataSerializer<ClosureExpression>> EXP_SERIALIZER = SERIALIZERS.register(
            "exp_serializer", () -> CLOSURE);
}
