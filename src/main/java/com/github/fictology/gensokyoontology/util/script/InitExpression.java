package com.github.fictology.gensokyoontology.util.script;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class InitExpression extends ExpressionType<InitExpression> implements IExpressionType{
    public final AccessibleInit init;
    public final List<IExpressionType> parameters;

    public static final InitExpression NULL = new InitExpression(AccessibleInit.NULL, new ArrayList<>());

    public InitExpression(AccessibleInit init, List<IExpressionType> parameters) {
        this.init = init;
        this.parameters = parameters;
    }

    public static InitExpression of(AccessibleInit initClass, List<IExpressionType> parameters) {
        return new InitExpression(initClass, parameters);
    }

    public Object getReturnValue(Object... parameterInstances){
        return new Object();
    }

    public Object newInstance(AccessibleInit init) throws Exception {
        return init.constructor.newInstance(this.parameters.toArray());
    }

    @Override
    public ExpressionType<InitExpression> get() {
        return this;
    }

    public List<IExpressionType> getParameters() {
        return this.parameters;
    }

    public boolean allMatches(Object... paramToMatch){
        return Arrays.equals(this.parameters.toArray(), paramToMatch);
    }

    public void addParameter(ParamExpression type) {
        this.parameters.add(type);
    }

    public int getInit() {
        return this.init.ordinal();
    }

    public AccessibleInit getClassInit(){
        return this.init;
    }

    public boolean matchesInit(AccessibleInit accessibleInit){
        return this.init == accessibleInit;
    }

    public boolean isInitValue(int indexOfParams){
        return this.parameters.get(indexOfParams) instanceof InitExpression;
    }

    public boolean isConstValue(int indexOfParams){
        return this.parameters.get(indexOfParams) instanceof ConstExpression;
    }
    public boolean isParamValue(int indexOfParams){
        return this.parameters.get(indexOfParams) instanceof ParamExpression;
    }

    public boolean isAllConst(){
        return this.parameters.stream().allMatch(type -> this.isConstValue(this.parameters.indexOf(type)));
    }

    public IExpressionType get(int indexOfParams){
        return this.parameters.get(indexOfParams);
    }

    public ConstExpression getAsConst(int indexOfParams){
        return this.isConstValue(indexOfParams) ? ((ConstExpression)this.parameters.get(indexOfParams)) : ConstExpression.ZERO;
    }
    public InitExpression getAsInit(int indexOfParams){
        return this.isInitValue(indexOfParams) ? ((InitExpression)this.parameters.get(indexOfParams)) : InitExpression.NULL;
    }
    public ParamExpression getAsParam(int indexOfParams){
        return this.isParamValue(indexOfParams) ? ((ParamExpression)this.parameters.get(indexOfParams)) : ParamExpression.NULL;
    }
    public List<ConstExpression> getAsConstList() {
        return this.isAllConst() ? this.parameters.stream().map(type -> (ConstExpression)type).toList() : new ArrayList<>();
    }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, InitExpression> streamCodec() {
        return StreamCodec.composite(
                ByteBufCodecs.INT, type -> this.getInit(),
                ByteBufCodecs.fromCodec(Codec.list(ExpressionType.CODEC)), type -> this.getParameters(),
                (i, l) -> new InitExpression(AccessibleInit.values()[i], l));
    }

    @Override
    public MapCodec<InitExpression> type() {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.INT.fieldOf("constructor").forGetter(expression -> this.getInit()),
                Codec.list(ExpressionType.CODEC).fieldOf("parameters").forGetter(type -> this.parameters)
        ).apply(instance, (integer, list) -> new InitExpression(AccessibleInit.values()[integer], list)));
    }

    /**
     * <code>
     *     Expressions.Closure().add(Expression.Var(Type.Int, "intConst",
     *     Expression.Const(1))).add(Expression.Invoke(Object, Method,
     *     parameters)
     * </code>
     */
    public Object newInstance(Object... objects){
        try {
            return this.init.constructor.newInstance(objects);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ExpressionType<?> getExp() {
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InitExpression that = (InitExpression) o;

        return Objects.equals(parameters, that.parameters);
    }

    @Override
    public int hashCode() {
        return parameters != null ? parameters.hashCode() : 0;
    }
}
