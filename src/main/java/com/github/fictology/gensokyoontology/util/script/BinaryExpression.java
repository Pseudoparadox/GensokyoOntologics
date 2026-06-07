package com.github.fictology.gensokyoontology.util.script;

import com.github.fictology.gensokyoontology.data.ByteInfo;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;

import java.util.Objects;

public class BinaryExpression extends ExpressionType<BinaryExpression> implements IExpressionType {
    private static final CastHelper<BinaryExpression, ExpressionType<?>> CAST_HELPER = (superObj -> (BinaryExpression) superObj.getExp());
    public final ExpressionType<? extends IExpressionType> left;
    public final ExpressionType<? extends IExpressionType> right;
    public final BinaryOps operator;

    public static final BinaryExpression EMPTY = new BinaryExpression(null, null, BinaryOps.NONE.ordinal());


    public BinaryExpression(IExpressionType left, IExpressionType right, int operator) {
        this.left = (ExpressionType<? extends IExpressionType>) left;
        this.right = (ExpressionType<? extends IExpressionType>) right;
        this.operator = BinaryOps.values()[operator];
    }

    public static BinaryExpression of(IExpressionType left, IExpressionType right, int operator) {
        return new BinaryExpression(left, right, operator);
    }

    public Number getMathResult() {
        if (!(left instanceof ConstExpression leftConst)) return Float.NaN;
        if (!(right instanceof ConstExpression rightConst)) return Float.NaN;
        return 0;
    }

    public ByteInfo getResultRaw(){
        if (left instanceof ConstExpression leftVal){
            return flatRight(leftVal.value);
        }
        if (left instanceof ParamExpression leftParam){
            return flatRightIfLeftIsParam(leftParam);

        }
        if (left instanceof BinaryExpression leftBin){
            return flatRight(leftBin.getResultRaw());
        }
        return ByteInfo.of(0);
    }

    public ByteInfo flatRight(ByteInfo leftVal){
        if (right instanceof ConstExpression rightVal){
            return getResultIfBothConst(leftVal, rightVal.value);
        }
        if (right instanceof ParamExpression rightParam){
            if (rightParam.getValue() instanceof BinaryExpression bin){
                return getResultIfBothConst(leftVal, bin.getResultRaw());
            }
            if (rightParam.getValue() instanceof ConstExpression cons){
                return getResultIfBothConst(leftVal, cons.value);
            }
        }
        if (right instanceof BinaryExpression bin){
            return bin.getResultRaw();
        }
        return ByteInfo.of(0);
    }

    public ByteInfo flatRightIfLeftIsParam(ParamExpression leftParam){
        if (leftParam.getValue() instanceof BinaryExpression leftBin){
            if (right instanceof ConstExpression rightVal){
                return getResultIfBothConst(leftBin.getResultRaw(), rightVal.value);
            }
            if (right instanceof ParamExpression rightParam){
                if (rightParam.getValue() instanceof BinaryExpression bin){
                    return getResultIfBothConst(leftBin.getResultRaw(), bin.getResultRaw());
                }
                if (rightParam.getValue() instanceof ConstExpression cons){
                    return getResultIfBothConst(leftBin.getResultRaw(), cons.value);
                }
            }
            if (right instanceof BinaryExpression bin){
                return bin.getResultRaw();
            }
        }
        return ByteInfo.of(0);
    }

    private ByteInfo getResultIfBothConst(ByteInfo left, ByteInfo right){
        return switch (this.operator) {
            case ADD -> ByteInfo.of(left.asFloat() + right.asFloat());
            case SUB -> ByteInfo.of(left.asFloat() - right.asFloat());
            case MUL -> ByteInfo.of(left.asFloat() * right.asFloat());
            case DIV -> ByteInfo.of(left.asFloat() / right.asFloat());
            case ASSIGN -> throw new IllegalCallerException("Left Expression is not a variable.");
            default -> throw new IllegalStateException("Operator is not defined.");
        };
    }

    @Override
    public MapCodec<BinaryExpression> type() {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
                ExpressionType.CODEC.fieldOf("left").forGetter(type -> this.left),
                ExpressionType.CODEC.fieldOf("right").forGetter(type -> this.right),
                StringRepresentable.EnumCodec.INT.fieldOf("operator").forGetter(type -> this.operator.ordinal())
        ).apply(instance, BinaryExpression::new));
    }

    @Override
    public ExpressionType<?> getExp() {
        return this.get();
    }

    @Override
    public ExpressionType<BinaryExpression> get() {
        return this;
    }

    public int getOperator() {
        return this.operator.ordinal();
    }

    public ExpressionType<? extends IExpressionType> getLeft() {
        return this.left;
    }

    public ExpressionType<? extends IExpressionType> getRight() {
        return this.left;
    }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, BinaryExpression> streamCodec() {
        return StreamCodec.composite(
                ByteBufCodecs.fromCodec(ExpressionType.CODEC), type -> this.getLeft(),
                ByteBufCodecs.fromCodec(ExpressionType.CODEC), type -> this.getRight(),
                ByteBufCodecs.fromCodec(StringRepresentable.EnumCodec.INT), type -> this.getOperator(),
                BinaryExpression::new);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BinaryExpression that = (BinaryExpression) o;

        if (!Objects.equals(left, that.left)) return false;
        if (!Objects.equals(right, that.right)) return false;
        return operator == that.operator;
    }

    @Override
    public int hashCode() {
        int result = left != null ? left.hashCode() : 0;
        result = 31 * result + (right != null ? right.hashCode() : 0);
        result = 31 * result + (operator != null ? operator.hashCode() : 0);
        return result;
    }

    public enum BinaryOps {
        ADD,
        SUB,
        MUL,
        DIV,
        ASSIGN,
        NONE
    }
}

