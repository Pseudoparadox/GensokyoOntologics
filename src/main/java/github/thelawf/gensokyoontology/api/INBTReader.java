package github.thelawf.gensokyoontology.api;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.util.math.BlockPos;

import java.util.Optional;

public interface INBTReader {

    default Optional<CompoundNBT> getOrEmptyTag(ItemStack stack) {
        return stack.getTag() == null ? Optional.empty() : Optional.of(stack.getTag());
    }

    default CompoundNBT getOrCreateTag(ItemStack stack) {
        return stack.getTag() == null ? new CompoundNBT() : stack.getTag();
    }

    default boolean containsKey(ItemStack stack, String key) {
        return getOrEmptyTag(stack).isPresent() && getOrEmptyTag(stack).get().contains(key);
    }

    default int getNBTInt(CompoundNBT nbt, String key) {
        return nbt.getInt(key);
    }

    default int getIntOrThrow(ItemStack stack, String key) {
        if (stack.getTag() == null) {
            throw new NullPointerException("Item stack has no key like: " + key);
        }
        return stack.getTag().getInt(key);
    }

    default String getNBTString(ItemStack stack, String key) {
        return containsKey(stack, key) && getOrEmptyTag(stack).isPresent() ?
                getOrEmptyTag(stack).get().getString(key) :
                "NULL";
    }

    default boolean getNBTBoolean(ItemStack stack, String key) {
        return containsKey(stack, key) && getOrEmptyTag(stack).isPresent() &&
                getOrEmptyTag(stack).get().getBoolean(key);
    }

    default BlockPos getNBTBlockPos(ItemStack stack, String key) {
        return containsKey(stack, key) && getOrEmptyTag(stack).isPresent() ?
                BlockPos.fromLong(getOrEmptyTag(stack).get().getLong(key)) :
                BlockPos.ZERO;
    }

    default ListNBT getNBTList(ItemStack stack, String key, Type type) {
        ListNBT list = new ListNBT();
        if (!stack.hasTag()) return list;
        if (stack.getTag() == null) return list;
        CompoundNBT nbt = stack.getTag();
        return nbt.getList(key, type.id);
    }

    default <T extends INBT> T cast(INBT nbt, Type type) {
        return type.cast(nbt);
    }

    enum Type {
        END(0, EndNBT.class),
        BYTE(1, ByteNBT.class),
        SHORT(2, ShortNBT.class),
        INT(3, IntNBT.class),
        LONG(4, LongNBT.class),
        FLOAT(5, FloatNBT.class),
        DOUBLE(6, DoubleNBT.class),
        BYTE_ARRAY(7, ByteArrayNBT.class),
        STRING(8, StringNBT.class),
        LIST(9, ListNBT.class),
        COMPOUND(10, CompoundNBT.class),
        INT_ARRAY(11, IntArrayNBT.class),;

        public int id;
        public Class<? extends INBT> clazz;

        Type(int id, Class<? extends INBT> clazz) {
            this.id = id;
            this.clazz = clazz;
        }

        @SuppressWarnings("unchecked")
        public <T extends INBT> T cast(Object o) {
            return (T) this.clazz.cast(o);
        }
    }
}
