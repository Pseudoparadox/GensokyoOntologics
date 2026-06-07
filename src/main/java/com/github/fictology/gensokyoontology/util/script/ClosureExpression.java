package com.github.fictology.gensokyoontology.util.script;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ClosureExpression extends ExpressionType<ClosureExpression> implements IExpressionType {
    public final List<IExpressionType> expressions;

    public static final ClosureExpression EMPTY = new ClosureExpression();
    public Stack<IExpressionType> stack = new Stack<>();
    public ClosureExpression(){
        this.expressions = new ArrayList<>();
    }

    public ClosureExpression(List<IExpressionType> expressions) {
       this.expressions = expressions;
    }

    public List<IExpressionType> getExpressions() {
        return this.expressions;
    }

    public ClosureExpression add(IExpressionType expression) {
        this.expressions.add(expression);
        return this;
    }

    public ClosureExpression compile(Map<String, IExpressionType> varMap) {
        for (var exp : expressions) {
            if (exp instanceof ParamExpression param) varMap.put(param.getName(), param.getValue());
        }
        return this;
    }

    public void run(ServerLevel serverLevel, @Nullable Entity entity,
                    @Nullable BlockPos pos, @Nullable ItemStack stack){

    }

    @Override
    public MapCodec<ClosureExpression> type() {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.list(ExpressionType.CODEC).fieldOf("expression").forGetter(type -> this.expressions)
        ).apply(instance, ClosureExpression::new));
    }


    @Override
    public ExpressionType<ClosureExpression> get() {
        return this;
    }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, ClosureExpression> streamCodec() {
        return StreamCodec.composite(
                ByteBufCodecs.fromCodec(Codec.list(ExpressionType.CODEC)), type -> this.getExpressions(),
                ClosureExpression::new);
    }


    @Override
    public ClosureExpression getExp() {
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClosureExpression that = (ClosureExpression) o;

        return Objects.equals(expressions, that.expressions);
    }

    @Override
    public int hashCode() {
        return expressions != null ? expressions.hashCode() : 0;
    }

    public CompoundTag toNbt() {
        return (CompoundTag) this.type().codec().encode(this, NbtOps.INSTANCE, NbtOps.INSTANCE.empty()).getOrThrow();
    }
}
