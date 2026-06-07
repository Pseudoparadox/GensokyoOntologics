package com.github.fictology.gensokyoontology.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class ByteInfo {

    public static ByteInfo of(int intData) {
        return new ByteInfo(asByteArray(intData));
    }

    public static ByteInfo of(String strData) {
        return new ByteInfo(strData.getBytes(StandardCharsets.UTF_8));
    }

    public static ByteInfo of(float floatData) {
        return new ByteInfo(asByteArray(floatData));
    }

    public static byte[] asByteArray(int intData) {
        var buf = ByteBuffer.allocate(4);
        buf.order(ByteOrder.LITTLE_ENDIAN);
        buf.putInt(intData);
        return buf.array();
    }

    public static byte[] asByteArray(float floatData) {
        var buf = ByteBuffer.allocate(4);
        buf.order(ByteOrder.LITTLE_ENDIAN);
        buf.putFloat(floatData);
        return buf.array();
    }

    public static final StreamCodec<RegistryFriendlyByteBuf, ByteInfo> STREAM_CODEC = new StreamCodec<>() {

        @Override
        public void encode(RegistryFriendlyByteBuf buffer, ByteInfo info) {
            buffer.writeBytes(info.value);
        }

        @Override
        public ByteInfo decode(RegistryFriendlyByteBuf buffer) {
            return new ByteInfo(new byte[]{buffer.readByte()});
        }
    };
    public static final Codec<ByteInfo> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.BYTE_BUFFER.fieldOf("byte_info").forGetter(byteInfo -> ByteBuffer.wrap(byteInfo.value))
    ).apply(instance, byteBuffer -> new ByteInfo(byteBuffer.array())));
    private byte[] value;

    public ByteInfo(byte[] value) {
        this.value = value;
    }


    public int asInt() {
        return this.value[3] & 0xFF |
                (this.value[2] & 0xFF) << 8 |
                (this.value[1] & 0xFF) << 16 |
                (this.value[0] & 0xFF) << 24;
    }

    public float asFloat() {
        int accum = 0;
        accum = accum | (this.value[0] & 0xff);
        accum = accum | (this.value[1] & 0xff) << 8;
        accum = accum | (this.value[2] & 0xff) << 16;
        accum = accum | (this.value[3] & 0xff) << 24;
        return Float.intBitsToFloat(accum);
    }


    public long asLong() {
        var buf = ByteBuffer.wrap(this.value);
        return buf.flip().getLong();
    }

    public double asDouble() {
        long l = 0;
        for (int i = 0; i < 8; i++) {
            l |= ((long) (this.value[i] & 0xff)) << (8 * i);
        }
        return Double.longBitsToDouble(l);
    }

    public String asString() {
        return new String(value, StandardCharsets.UTF_8);
    }

    public String asString(Charset charset) {
        return new String(value, charset);
    }

    public void setValue(int i){
        this.value = asByteArray(i);
    }

    public void setValue(float f){
        this.value = asByteArray(f);
    }

    public void increment(){
        var i = this.asInt();
        this.setValue(++i);
    }

    public void add(int i) {
        this.setValue(this.asInt() + i);
    }

    public void sub(int i) {
        this.setValue(this.asInt() - i);
    }

    public void mul(int i) {
        this.setValue(this.asInt() * i);
    }

    public void add(float f) {
        this.setValue(this.asFloat() + f);
    }

    public void sub(float f) {
        this.setValue(this.asFloat() - f);
    }

    public void mul(float f) {
        this.setValue(this.asFloat() * f);
    }

    public void div(float f){
        this.setValue(this.asFloat() / f);
    }

    public ByteInfo add(ByteInfo b){
        return ByteInfo.of(b.asLong() + this.asLong());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ByteInfo byteInfo = (ByteInfo) o;
        return Arrays.equals(value, byteInfo.value);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(value);
    }
}
