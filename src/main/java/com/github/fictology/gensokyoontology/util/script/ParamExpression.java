package com.github.fictology.gensokyoontology.util.script;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.Objects;
import java.util.Stack;

public class ParamExpression extends ExpressionType<ParamExpression> {
    public final AccessibleClass classType;
    public final String paramName;
    public final IExpressionType value;
    public static ParamExpression byName(String paramName) {
        return new ParamExpression(AccessibleClass.EMPTY, paramName, null);
    }

    public static final ParamExpression NULL = new ParamExpression(AccessibleClass.EMPTY, "", null);
    public ParamExpression(AccessibleClass classType, String paramName, IExpressionType value) {
        this.classType = classType;
        this.paramName = paramName;
        this.value = value;
    }

    public static ParamExpression of(AccessibleClass className, String paramName, IExpressionType paramValue){
        return new ParamExpression(className, paramName, paramValue);
    }

    public void compile(Stack<IExpressionType> outerStack) {
        if (value instanceof InitExpression init) outerStack.push(init);
        else outerStack.push(this);
    }

    public AccessibleClass getType() {
        return this.classType;
    }
    public int getTypeOrdinal() {
        return this.classType.ordinal();
    }

    public String getName() {
        return this.paramName;
    }

    public IExpressionType getValue() {
        return this.value;
    }

    public boolean isInitValue(){
        return this.value instanceof InitExpression;
    }

    public boolean isConstValue(){
        return this.value instanceof ConstExpression;
    }
    public ConstExpression getAsConst(){
        return isConstValue()? ((ConstExpression)this.value) : ConstExpression.ZERO;
    }

    @Override
    public MapCodec<ParamExpression> type() {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.INT.fieldOf("classType").forGetter(expression -> this.getTypeOrdinal()),
                Codec.STRING.fieldOf("paramName").forGetter(paramExpression -> this.paramName),
                ExpressionType.CODEC.fieldOf("paramValue").forGetter(type -> this.value)
        ).apply(instance, ((integer, s, type) -> new ParamExpression(
                AccessibleClass.values()[integer], s, type))));
    }

    @Override
    public ExpressionType<?> getExp() {
        return this;
    }

    @Override
    public ExpressionType<ParamExpression> get() {
        return this;
    }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, ParamExpression> streamCodec() {
        return StreamCodec.composite(
                ByteBufCodecs.INT, type -> this.getTypeOrdinal(),
                ByteBufCodecs.STRING_UTF8, type -> this.getName(),
                ByteBufCodecs.fromCodec(ExpressionType.CODEC), type -> this.getValue(),
                (i, s, t) -> new ParamExpression(AccessibleClass.values()[i],s,t));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ParamExpression that = (ParamExpression) o;

        if (!Objects.equals(classType, that.classType)) return false;
        if (!Objects.equals(paramName, that.paramName)) return false;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        int result = classType != null ? classType.hashCode() : 0;
        result = 31 * result + (paramName != null ? paramName.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
