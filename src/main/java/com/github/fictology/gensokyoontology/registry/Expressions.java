package com.github.fictology.gensokyoontology.registry;

import com.github.fictology.gensokyoontology.GensokyoOntology;
import com.github.fictology.gensokyoontology.data.ByteInfo;
import com.github.fictology.gensokyoontology.util.GSKOUtil;
import com.github.fictology.gensokyoontology.util.script.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

public final class Expressions {
    public static final ResourceKey<Registry<IExpressionType>> KEY = ResourceKey.createRegistryKey(GSKOUtil.key("expression"));
    public static final Registry<IExpressionType> REGISTRY = new RegistryBuilder<>(KEY)
            .sync(true).defaultKey(GSKOUtil.key("empty")).maxId(256).create();
    public static final DeferredRegister<IExpressionType> EXPRESSIONS = DeferredRegister.create(REGISTRY,
            GensokyoOntology.MODID);
    public static final DeferredHolder<IExpressionType, ClosureExpression> CLOSURE_EXP = DeferredHolder.create(KEY, GSKOUtil.key("closure"));
           // EXPRESSIONS.register("closure", () -> new ClosureExpression(new ArrayList<>()));
    public static final Supplier<BinaryExpression> BINARY_EXP = EXPRESSIONS.register("binary", () -> BinaryExpression.EMPTY);
    public static final Supplier<LambdaExpression> LAMBDA_EXP = EXPRESSIONS.register("lambda", () -> LambdaExpression.EMPTY);
    public static final Supplier<ParamExpression> PARAM_EXP = EXPRESSIONS.register("param", () -> ParamExpression.NULL);
    public static final Supplier<ConstExpression> CONST_EXP = EXPRESSIONS.register("const", () -> ConstExpression.ZERO);
    public static final Supplier<InitExpression> INIT_EXP = EXPRESSIONS.register("init", () -> InitExpression.NULL);
    public static final Supplier<AccessExpression> ACCESS_EXP = EXPRESSIONS.register("getter", () -> AccessExpression.NULL);
    public static final Supplier<InvokeExpression> INVOKE_EXP = EXPRESSIONS.register("invoke", () -> InvokeExpression.NULL);
    public static final Supplier<ReferenceExpression> REF_EXP = EXPRESSIONS.register("ref", () -> ReferenceExpression.EMPTY);

    public static final List<AccessibleMethod> DANMAKU_METHODS = List.of(
            AccessibleMethod.ENTITY_TICK,
            AccessibleMethod.PROJECTILE_SHOOT,
            AccessibleMethod.PROJECTILE_SET_MOTION);

    public static ExpressionType<ConstExpression> Const(int i) {
        return new ConstExpression(ByteInfo.of(i));
    }

    public static ExpressionType<ConstExpression> Const(String s) {
        return new ConstExpression(ByteInfo.of(s));
    }


    public static BinaryExpression Assign(ExpressionType<?> left, ExpressionType<?> right) {
        return new BinaryExpression(left, right, BinaryExpression.BinaryOps.ASSIGN.ordinal());
    }

    public static BinaryExpression Subtract(ExpressionType<?> left, ExpressionType<?> right) {
        return new BinaryExpression(left, right, BinaryExpression.BinaryOps.SUB.ordinal());
    }

    public static BinaryExpression Mul(ExpressionType<?> left, ExpressionType<?> right) {
        return new BinaryExpression(left, right, BinaryExpression.BinaryOps.MUL.ordinal());
    }

    public static BinaryExpression Div(ExpressionType<?> left, ExpressionType<?> right) {
        return new BinaryExpression(left, right, BinaryExpression.BinaryOps.DIV.ordinal());
    }
    public static BinaryExpression Add(ExpressionType<?> left, ExpressionType<?> right) {
        return new BinaryExpression(left, right, BinaryExpression.BinaryOps.ADD.ordinal());
    }

    public static ParamExpression Var(AccessibleClass type, String name, IExpressionType value) {
        return new ParamExpression(type, name, value);
    }

    public static ClosureExpression Closure(){
        return new ClosureExpression();
    }

    public static LambdaExpression Lambda(IExpressionType body, IExpressionType... parameters) {
        return new LambdaExpression(body, Arrays.stream(parameters).toList());
    }
    public static InitExpression Init(AccessibleInit init, List<IExpressionType> params){
        return new InitExpression(init, params);
    }
    public static AccessExpression Access(AccessibleClass accessibleClass){
        return new AccessExpression(accessibleClass);
    }
    public static InvokeExpression Invoke(ParamExpression varInvoker, AccessibleClass accessibleClass, AccessibleMethod accessibleMethod, List<ParamExpression> parameters){
        return new InvokeExpression(varInvoker, accessibleClass, accessibleMethod, parameters);
    }
    public static ReferenceExpression Ref(ParamExpression variableRef, @Nullable UUID uuid, @Nullable BlockPos pos, Level level){
        return new ReferenceExpression(variableRef, Optional.ofNullable(uuid), Optional.ofNullable(pos), level.dimension());
    }


    public static void UnexpectedExpression(IExpressionType type){
        GSKOUtil.error("The expression type is unexpected here");
    }

    public static void NoSuchMethod(Class<?> clazz, InvokeExpression invoker){
        GSKOUtil.error(clazz.getName() + " class has no such method: " + invoker.getAccessibleMethod().methodName());
    }

    public static void ParametersNotMatch(InvokeExpression invoker){
        GSKOUtil.error("Parameter types do not match method: " + invoker.getAccessibleMethod().methodName());
    }

    public static void NotInMap(){
        GSKOUtil.error("Parameter types do not in variable map. Please check whether the expressions is complied or not.");
    }

}
