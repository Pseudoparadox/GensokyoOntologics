package com.github.fictology.gensokyoontology.util.script;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LambdaExpression extends ExpressionType<LambdaExpression> {
    private final CastHelper<LambdaExpression, ExpressionType<?>> CAST = (superObj -> (LambdaExpression) superObj);
    public final IExpressionType body;
    public final List<IExpressionType> parameters;

    public static final LambdaExpression EMPTY = new LambdaExpression(null, new ArrayList<>());
    public LambdaExpression(IExpressionType body, List<IExpressionType> parameters) {
        this.body = body;
        this.parameters = parameters;
    }
    public static LambdaExpression of(IExpressionType body, List<IExpressionType> parameters) {
        return new LambdaExpression(body, parameters);
    }

    @Override
    public ExpressionType<LambdaExpression> get() {
        return this;
    }

    public Object invoke(Object instance, AccessibleMethod method) throws Exception {
        return method.method.invoke(instance, this.parameters.toArray());
    }

    public IExpressionType getBody() {
        return this.body;
    }

    public List<IExpressionType> getParameters() {
        return this.parameters;
    }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, IExpressionType> streamCodec() {
        return StreamCodec.composite(
                ByteBufCodecs.fromCodec(ExpressionType.CODEC), type -> this.getBody(),
                ByteBufCodecs.fromCodec(Codec.list(ExpressionType.CODEC)), type -> this.getParameters(),
                LambdaExpression::new);
    }

    @Override
    public MapCodec<IExpressionType> type() {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
                ExpressionType.CODEC.fieldOf("body").forGetter(type -> this.body),
                Codec.list(ExpressionType.CODEC).fieldOf("parameters").forGetter(type -> this.parameters)
        ).apply(instance, LambdaExpression::new));
    }

    @Override
    public ExpressionType<?> getExp() {
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LambdaExpression that = (LambdaExpression) o;

        if (!CAST.equals(that.CAST)) return false;
        if (!Objects.equals(body, that.body)) return false;
        return Objects.equals(parameters, that.parameters);
    }

    @Override
    public int hashCode() {
        int result = CAST.hashCode();
        result = 31 * result + (body != null ? body.hashCode() : 0);
        result = 31 * result + (parameters != null ? parameters.hashCode() : 0);
        return result;
    }
}
