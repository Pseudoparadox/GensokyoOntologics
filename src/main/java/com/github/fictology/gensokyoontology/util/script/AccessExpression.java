package com.github.fictology.gensokyoontology.util.script;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.lang.reflect.Field;
import java.util.Optional;

public class AccessExpression extends ExpressionType<AccessExpression> implements IExpressionType{

    private final AccessibleClass accessibleClass;

    public static final AccessExpression NULL = AccessExpression.of(AccessibleClass.EMPTY);
    public AccessExpression(AccessibleClass accessibleClass) {
        this.accessibleClass = accessibleClass;
    }

    public static AccessExpression of(AccessibleClass accessibleClass){
        return new AccessExpression(accessibleClass);
    }

    @Override
    public ExpressionType<AccessExpression> get() {
        return this;
    }

    public Optional<Field> getField(String fieldName){
        return this.accessibleClass.fields.stream().filter(field -> field.getName().equals(fieldName)).findFirst();
    }

    public int classOrdinal() {
        return this.accessibleClass.ordinal();
    }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, AccessExpression> streamCodec() {
        return StreamCodec.composite(ByteBufCodecs.INT, type -> this.classOrdinal(), integer -> new AccessExpression(AccessibleClass.values()[integer]));
    }

    @Override
    public MapCodec<AccessExpression> type() {
        return RecordCodecBuilder.mapCodec(iExpressionTypeInstance -> iExpressionTypeInstance.group(
                Codec.INT.fieldOf("class_enum_ordinal").forGetter(type -> this.accessibleClass.ordinal())
        ).apply(iExpressionTypeInstance, integer -> new AccessExpression(AccessibleClass.values()[integer])));
    }

    @Override
    public ExpressionType<?> getExp() {
        return this;
    }
}
