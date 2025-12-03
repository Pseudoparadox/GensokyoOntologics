package github.thelawf.gensokyoontology.data.expression;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundNBT;

import java.util.Objects;

//public class ConstExpression extends ExpressionType<ConstExpression> implements IExpressionType {
//    public final ByteInfo value;
//    public static final Codec<ConstExpression> CODEC = RecordCodecBuilder.create(expressionInstance -> expressionInstance.group(
//            ByteInfo.CODEC.fieldOf("value").forGetter(ConstExpression::getRawValue)
//    ).apply(expressionInstance, ConstExpression::new));
//
//    public static final ConstExpression ZERO = new ConstExpression(ByteInfo.of(0));
//    public ConstExpression(ByteInfo value) {
//        this.value = value;
//    }
//
//    public static ConstExpression of(float value) {
//        CompoundNBT tag = new CompoundNBT();
//        tag.putFloat("float_type", value);
//        return new ConstExpression(ByteInfo.of(value));
//    }
//
//    public static ConstExpression of(int value) {
//        CompoundNBT tag = new CompoundNBT();
//        tag.putInt("int_type", value);
//        return new ConstExpression(ByteInfo.of(value));
//    }
//
//    public static ConstExpression of(String value) {
//        CompoundNBT tag = new CompoundNBT();
//        tag.putString("string_type", value);
//        return new ConstExpression(ByteInfo.of(value));
//    }
//
//    public ByteInfo getRawValue() {
//        return this.value;
//    }
//
//    public double getDouble(){
//        return this.value.asDouble();
//    }
//
//    @Override
//    public MapCodec<ConstExpression> type() {
//        return RecordCodecBuilder.mapCodec(expressionInstance -> expressionInstance.group(
//                ByteInfo.CODEC.fieldOf("value").forGetter(type -> this.getRawValue())
//        ).apply(expressionInstance, ConstExpression::new));
//    }
//
//
//    @Override
//    public ExpressionType<ConstExpression> get() {
//        return this;
//    }
//
//    @Override
//    public ConstExpression getExp() {
//        return this;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        ConstExpression that = (ConstExpression) o;
//
//        return Objects.equals(value, that.value);
//    }
//
//    @Override
//    public int hashCode() {
//        return value != null ? value.hashCode() : 0;
//    }
//
//}
