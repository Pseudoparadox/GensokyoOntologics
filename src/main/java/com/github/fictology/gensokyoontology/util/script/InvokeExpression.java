package com.github.fictology.gensokyoontology.util.script;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class InvokeExpression extends ExpressionType<InvokeExpression> implements IExpressionType{
    private final AccessibleClass accessibleClass;
    private final AccessibleMethod accessibleMethod;
    private final List<ParamExpression> parameters;
    private final ParamExpression varInvoker;
    public static final InvokeExpression NULL = new InvokeExpression(ParamExpression.NULL,
            AccessibleClass.EMPTY, AccessibleMethod.VOID, List.of());

    public InvokeExpression(ParamExpression varInvoker, AccessibleClass accessibleClass, AccessibleMethod accessibleMethod, List<ParamExpression> parameters) {
        this.accessibleClass = accessibleClass;
        this.accessibleMethod = accessibleMethod;
        this.parameters = parameters;
        this.varInvoker = ParamExpression.NULL;
    }

    @Override
    public ExpressionType<InvokeExpression> get() {
        return this;
    }

    public AccessibleClass getAccessibleClass() {
        return this.accessibleClass;
    }

    public AccessibleMethod getAccessibleMethod() {
        return this.accessibleMethod;
    }

    public boolean paramAllMatches(List<AccessibleClass> types){
        return this.parameters.stream().map(param -> param.classType).toList().equals(types);
    }

    public boolean isReference(ParamExpression paramExpression){
        return paramExpression.value == null;
    }

    public boolean isConstRef(ParamExpression paramExpression){
        return paramExpression.value instanceof ConstExpression;
    }

    public boolean isInitRef(ParamExpression paramExpression){
        return paramExpression.value instanceof InitExpression;
    }
    public boolean isInitRef(int index){
        return this.parameters.get(index).value instanceof InitExpression;
    }

    public InitExpression getVarInit(ParamExpression param, Map<String, IExpressionType> varMap){
        if (!(varMap.get(param.paramName) instanceof InitExpression initExp)) return InitExpression.NULL;
        return initExp;
    }

    public ConstExpression getVarConst(ParamExpression param, Map<String, IExpressionType> varMap){
        if (!(varMap.get(param.paramName) instanceof ConstExpression constExp)) return ConstExpression.ZERO;
        return constExp;
    }

    public boolean matchesType(List<AccessibleClass> paramTypes){
        return this.parameters.stream().map(ParamExpression::getType).toList().equals(paramTypes);
    }

    public int classOrdinal() {
        return this.accessibleClass.ordinal();
    }

    public int methodOrdinal() {
        return this.accessibleMethod.ordinal();
    }

    public Method getMethod() {
        return this.accessibleMethod.method;
    }

    public List<ParamExpression> getParameters() {
        return this.parameters;
    }

    public Object invoke(Object instance, Map<String, IExpressionType> varMap) throws Exception {
        var newList = new ArrayList<IExpressionType>();
        this.parameters.forEach(type -> {
            if (type instanceof ParamExpression param){
                var exp = varMap.get(param.paramName);
            }
        });
        return this.accessibleMethod.method.invoke(instance, this.parameters.toArray());
    }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, InvokeExpression> streamCodec() {
        return StreamCodec.composite(
                ByteBufCodecs.fromCodec(ParamExpression.NULL.type().codec()), t -> this.varInvoker,
                ByteBufCodecs.INT, i -> this.classOrdinal(), ByteBufCodecs.INT, i -> this.methodOrdinal(),
                ByteBufCodecs.fromCodec(Codec.list(ParamExpression.NULL.type().codec())), type -> this.getParameters(),
                (p, i1, i2, list) -> new InvokeExpression(p, AccessibleClass.values()[i1], AccessibleMethod.values()[i2], list));
    }

    @Override
    public MapCodec<InvokeExpression> type() {
        return RecordCodecBuilder.mapCodec(invokeExpressionInstance -> invokeExpressionInstance.group(
                ParamExpression.NULL.type().codec().fieldOf("var_invoker").forGetter(type -> this.varInvoker),
                Codec.INT.fieldOf("class_enum_ordinal").forGetter(type -> this.classOrdinal()),
                Codec.INT.fieldOf("method_enum_ordinal").forGetter(type -> this.methodOrdinal()),
                Codec.list(ParamExpression.NULL.type().codec()).fieldOf("parameters").forGetter(type -> this.getParameters())
        ).apply(invokeExpressionInstance, (p, i1, i2, list) -> new InvokeExpression(p,
                AccessibleClass.values()[i1], AccessibleMethod.values()[i2], list)));
    }

    @Override
    public ExpressionType<?> getExp() {
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InvokeExpression that = (InvokeExpression) o;

        if (accessibleClass != that.accessibleClass) return false;
        if (accessibleMethod != that.accessibleMethod) return false;
        if (!Objects.equals(parameters, that.parameters)) return false;
        return varInvoker.equals(that.varInvoker);
    }

    @Override
    public int hashCode() {
        int result = accessibleClass != null ? accessibleClass.hashCode() : 0;
        result = 31 * result + (accessibleMethod != null ? accessibleMethod.hashCode() : 0);
        result = 31 * result + (parameters != null ? parameters.hashCode() : 0);
        result = 31 * result + varInvoker.hashCode();
        return result;
    }
}
