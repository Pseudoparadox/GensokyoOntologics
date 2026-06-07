package com.github.fictology.gensokyoontology.data;


import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import static com.github.fictology.gensokyoontology.util.script.DataType.FLOAT;
import static com.github.fictology.gensokyoontology.util.script.DataType.INT;

public record PrimitiveValueInfo(String dataType, String name, String value) {
    public static final String intRegex = "^[+-]?\\d+$";
    public static final String floatRegex = "^[+-]?\\d+[\\.]?\\d+$";
    public static final Codec<PrimitiveValueInfo> CODEC = RecordCodecBuilder.create(variableInfoInstance -> variableInfoInstance.group(
            Codec.STRING.fieldOf("dataType").forGetter(PrimitiveValueInfo::dataType),
            Codec.STRING.fieldOf("name").forGetter(PrimitiveValueInfo::name),
            Codec.STRING.fieldOf("value").forGetter(PrimitiveValueInfo::value)
    ).apply(variableInfoInstance, PrimitiveValueInfo::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, PrimitiveValueInfo> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, PrimitiveValueInfo::dataType,
            ByteBufCodecs.STRING_UTF8, PrimitiveValueInfo::name,
            ByteBufCodecs.STRING_UTF8, PrimitiveValueInfo::value,
            PrimitiveValueInfo::new);

    public boolean isPrimitiveType() {
        return dataType.equals(INT) || dataType.equals(FLOAT);
    }

    public boolean isNull() {
        return value.equals("");
    }

    private boolean isValueLegal() {
        if (!isPrimitiveType()) {
            LogUtils.getLogger().error("Variable: (type={}, name={}, value={}) is not a primitive type.", dataType, name, value);
            return false;
        }
        if (isNull()) {
            LogUtils.getLogger().error("Variable: (type={}, name={}, value={}) is null", dataType, name, value);
            return false;
        }
        return true;
    }

    public int asInt() {
        if (!isValueLegal()) return 0;
        if (!intRegex.matches(value)) {
            LogUtils.getLogger().error("Variable: (type={}, name={}, value={}) is not an integer", dataType, name, value);
            return 0;
        }
        return Integer.parseInt(value);
    }

    public float asFloat() {
        if (!isValueLegal()) return 0;
        if (!floatRegex.matches(value)) {
            LogUtils.getLogger().error("Variable: (type={}, name={}, value={}) is not an integer", dataType, name, value);
            return 0;
        }
        return Float.parseFloat(value);
    }
}
