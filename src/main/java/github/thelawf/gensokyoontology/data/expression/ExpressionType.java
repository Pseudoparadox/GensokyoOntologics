package github.thelawf.gensokyoontology.data.expression;

import github.thelawf.gensokyoontology.api.Tree;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/*
public abstract class ExpressionType<E extends IExpressionType> extends ForgeRegistryEntry<IExpressionType> implements IExpressionType, IForgeRegistryEntry<IExpressionType> {

    // public static final Codec<IExpressionType> CODEC = Expressions.EXP_KEY.REGISTRY.byNameCodec().dispatch(
    //         "expression", IExpressionType::getExp, IExpressionType::type);
//
    // public static final RegistryKeyCodec<RegistryFriendlyByteBuf, IExpressionType> STREAM_CODEC = ByteBufCodecs.registry(
    //         Expressions.KEY).dispatch(IExpressionType::getExp, type -> type.getExp().streamCodec());

    private ExpressionType<? extends IExpressionType> expressionType;
    private E expression;
    // public static final Map<String, ?> METHODS = Util.make(() -> {
    //     // var map = new HashMap<String, Constructor<?>>();
    //     // map.put(AccessibleMethod.DANMAKU_INIT.methodName, findConstructor(Danmaku.class, Level.class, Item.class, ClosureExpression.class));
    //     // map.put(AccessibleMethod.VEC3_INIT.methodName, findConstructor(Vec3.class, double.class, double.class, double.class));
    //     // return map;
    // });


    public Tree<ExpressionType<E>> expTree = new Tree<>();

    private static Constructor<?> findConstructor(Class<?> c, Class<?>... params) {
        return ObfuscationReflectionHelper.findConstructor(c, params);
    }

    private static Method findMethod(Class<?> c, String name, Class<?>... params) {
        return ObfuscationReflectionHelper.findMethod(c, name, params);
    }

    public abstract ExpressionType<E> get();


    // public JsonElement toJson() {
    //     DataResult<JsonElement> dataResult = CODEC.encodeStart(JsonOps.INSTANCE, this);
    //     return dataResult.result().get();
    // }

}

 */
