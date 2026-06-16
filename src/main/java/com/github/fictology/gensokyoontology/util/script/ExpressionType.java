package com.github.fictology.gensokyoontology.util.script;

import com.github.fictology.gensokyoontology.registry.Expressions;
import com.github.fictology.gensokyoontology.api.Tree;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Util;
import net.neoforged.fml.util.ObfuscationReflectionHelper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public abstract class ExpressionType<E extends IExpressionType> implements IExpressionType {

    public static final Codec<IExpressionType> CODEC = Expressions.REGISTRY.byNameCodec().dispatch(
            "expression", IExpressionType::getExp, IExpressionType::type);

    public static final StreamCodec<RegistryFriendlyByteBuf, IExpressionType> STREAM_CODEC = ByteBufCodecs.registry(
            Expressions.KEY).dispatch(IExpressionType::getExp, type -> type.getExp().streamCodec());

    private ExpressionType<? extends IExpressionType> expressionType;
    private E e;
    public static final Map<String, ?> METHODS = Util.make(() -> {
        var map = new HashMap<String, Constructor<?>>();
        // map.put(AccessibleMethod.DANMAKU_INIT.methodName, findConstructor(Danmaku.class, Level.class, Item.class, ClosureExpression.class));
        // map.put(AccessibleMethod.VEC3_INIT.methodName, findConstructor(Vec3.class, double.class, double.class, double.class));
        return map;
    });


    public Tree<ExpressionType<E>> expTree = new Tree<>();

    private static Constructor<?> findConstructor(Class<?> c, Class<?>... params) {
        return ObfuscationReflectionHelper.findConstructor(c, params);
    }

    private static Method findMethod(Class<?> c, String name, Class<?>... params) {
        return ObfuscationReflectionHelper.findMethod(c, name, params);
    }

    public static IExpressionType Const(float f) {
        return ConstExpression.of(f);
    }


    public abstract ExpressionType<E> get();

    public abstract StreamCodec<? super RegistryFriendlyByteBuf, ? extends IExpressionType> streamCodec();

    public JsonElement toJson() {
        var dataResult = CODEC.encodeStart(JsonOps.INSTANCE, this);
        return dataResult.result().orElseThrow();
    }

    public String toJsonString() {
        return GsonHelper.toStableString(toJson());
    }
}
