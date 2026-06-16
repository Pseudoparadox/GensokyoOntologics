package com.github.fictology.gensokyoontology.registry;

import com.github.fictology.gensokyoontology.GensokyoOntology;
import com.github.fictology.gensokyoontology.data.*;
import com.github.fictology.gensokyoontology.util.script.*;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Objects;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public final class DataRegistry {
    public static final DeferredRegister.DataComponents DATA = DeferredRegister.createDataComponents(
            Registries.DATA_COMPONENT_TYPE, GensokyoOntology.MODID);
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS = DeferredRegister.create(
            NeoForgeRegistries.ATTACHMENT_TYPES, GensokyoOntology.MODID);

    public static final Supplier<AttachmentType<Integer>> POWER = ATTACHMENTS.register("power",
            () -> AttachmentType.builder(() -> 0).build());

    public static final Supplier<AttachmentType<CompoundTag>> NBT_DATA = ATTACHMENTS.register("nbt_data",
            () -> AttachmentType.builder(() -> new CompoundTag()).build());
    public static final Supplier<AttachmentType<Identities>> IDENTITY = ATTACHMENTS.register("identity",
            () -> AttachmentType.serializable(() -> new Identities(10000, 0, 1)).build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ByteInfo>> BYTES = register(
            "bytes", builder -> builder.persistent(ByteInfo.CODEC).networkSynchronized(ByteInfo.STREAM_CODEC));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<String>> STRING = register(
            "message", builder -> builder.persistent(Codec.STRING).networkSynchronized(StreamCodec.of(ByteBufCodecs.STRING_UTF8, Objects::toString)));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Predicate>> PREDICATE = register(
            "predicate", booleanBuilder -> booleanBuilder.persistent(Predicate.CODEC).networkSynchronized(Predicate.STREAM_CODEC));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Identifier>> LOCALE_KEY = register(
            "locale_key", builder -> builder.persistent(Identifier.CODEC).networkSynchronized(Identifier.STREAM_CODEC));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<GapInfo>> GAP_INFO = register(
            "gap_info", builder -> builder.persistent(GapInfo.CODEC).networkSynchronized(GapInfo.STREAM_CODEC));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<InscriptionInfo>> INSCRIPTION = register(
            "inscriptions", builder -> builder.persistent(InscriptionInfo.CODEC).networkSynchronized(InscriptionInfo.STREAM_CODEC));
    public static final Supplier<DataComponentType<Sorcery>> SPECIAL_MAGIC = register(
            "special_magic", builer -> builer.persistent(Sorcery.CODEC).networkSynchronized(Sorcery.STREAM_CODEC));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<PrimitiveValueInfo>> PRIMITIVE_VAR = register(
            "variable", builder -> builder.persistent(PrimitiveValueInfo.CODEC).networkSynchronized(PrimitiveValueInfo.STREAM_CODEC));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Pair<String, Vec3>>> VEC3_VAR = register(
            "vector3", builder -> builder.persistent(Codec.pair(Codec.STRING, Vec3.CODEC)).networkSynchronized(
                    ByteBufCodecs.fromCodec(Codec.pair(Codec.STRING, Vec3.CODEC))));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Pair<String, Sorcery>>> DANMAKU_VAR = register(
            "var_danmaku", builder -> builder.persistent(Codec.pair(Codec.STRING, Sorcery.CODEC)).networkSynchronized(
                    ByteBufCodecs.fromCodec(Codec.pair(Codec.STRING, Sorcery.CODEC))));

    public static final Supplier<DataComponentType<ClosureExpression>> CLOSURE = registerExp("closure",
            builder -> builder.persistent(ClosureExpression.EMPTY.type().codec())
                    .networkSynchronized(ClosureExpression.EMPTY.streamCodec()));
    public static final Supplier<DataComponentType<ConstExpression>> CONST = registerExp("const",
            builder -> builder.persistent(ConstExpression.ZERO.type().codec())
                    .networkSynchronized(ConstExpression.ZERO.streamCodec()));
    public static final Supplier<DataComponentType<ParamExpression>> PARAMS = registerExp("param",
            builder -> builder.persistent(ParamExpression.NULL.type().codec())
                    .networkSynchronized(ParamExpression.NULL.streamCodec()));
    public static final Supplier<DataComponentType<IExpressionType>> LAMBDA = registerExp("lambda",
            builder -> builder.persistent(LambdaExpression.EMPTY.type().codec())
                    .networkSynchronized(LambdaExpression.EMPTY.streamCodec()));
    public static final Supplier<DataComponentType<BinaryExpression>> BINARY_OPS = registerExp("binary_ops",
            builder -> builder.persistent(BinaryExpression.EMPTY.type().codec())
                    .networkSynchronized(BinaryExpression.EMPTY.streamCodec()));
    public static final Supplier<DataComponentType<InitExpression>> INIT = registerExp("init",
            builder -> builder.persistent(InitExpression.NULL.type().codec())
                    .networkSynchronized(InitExpression.NULL.streamCodec()));
    public static final Supplier<DataComponentType<AccessExpression>> ACCESS = registerExp("getter",
            builder -> builder.persistent(AccessExpression.NULL.type().codec())
                    .networkSynchronized(AccessExpression.NULL.streamCodec()));
    public static final Supplier<DataComponentType<ReferenceExpression>> REFERENCE = registerExp("reference",
            builder -> builder.persistent(ReferenceExpression.EMPTY.type().codec())
                    .networkSynchronized(ReferenceExpression.EMPTY.streamCodec()));

    private static <E extends IExpressionType> Supplier<DataComponentType<E>> registerExp(String name, UnaryOperator<DataComponentType.Builder<E>> builder) {
        return DATA.register(name, () -> builder.apply(DataComponentType.builder()).build());
    }

    private static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String name, UnaryOperator<DataComponentType.Builder<T>> builder) {
        return DATA.register(name, () -> builder.apply(DataComponentType.builder()).build());
    }


}
