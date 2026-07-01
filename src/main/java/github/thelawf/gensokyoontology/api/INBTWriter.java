package github.thelawf.gensokyoontology.api;

import github.thelawf.gensokyoontology.common.nbt.GSKONBTUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public interface INBTWriter extends INBTReader {
    default <T extends INBT, S extends INBTSynchornizable<T, S>> void writeList(CompoundNBT nbtToWirteIn, String key, List<S> list, GSKONBTUtil.NBTType nbtType){
        ListNBT listNBT = new ListNBT();
        list.forEach(s -> listNBT.add(nbtType.typeByte, s.serializeNBT()));
        nbtToWirteIn.put(key, listNBT);
    }


    default void writeBoolean(ItemStack stack, String key, Boolean value) {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putBoolean(key, value);
        stack.setTag(nbt);
    }

    default void writeString(ItemStack stack, String key, String value) {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putString(key, value);
        stack.setTag(nbt);
    }


    default void writeBlockPos(ItemStack stack, String key, BlockPos pos) {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putLong(key, pos.toLong());
        stack.setTag(nbt);
    }

    default void mergeBoolean(ItemStack stack, String key, Boolean value) {
        CompoundNBT nbt = getOrCreateTag(stack);
        CompoundNBT newNBT = new CompoundNBT();
        newNBT.putBoolean(key, value);
        nbt.merge(newNBT);
        stack.setTag(nbt);
    }

    default void mergeString(ItemStack stack, String key, String value) {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putString(key, value);
        stack.setTag(nbt);
    }

    default void mergeBlockPos(ItemStack stack, String key, BlockPos pos) {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putLong(key, pos.toLong());
        stack.setTag(nbt);

    }


    default CompoundNBT withBlockPos(CompoundNBT nbt, String xKey, String yKey, String zKey, BlockPos pos) {
        nbt.putInt(xKey, pos.getX());
        nbt.putInt(yKey, pos.getY());
        nbt.putInt(zKey, pos.getZ());
        return nbt;
    }

    default void mergeBlockPos(ItemStack stack, String xKey, String yKey, String zKey, BlockPos pos) {
        CompoundNBT nbt = getOrCreateTag(stack);
        CompoundNBT newNBT = new CompoundNBT();
        newNBT.putInt(xKey, pos.getX());
        newNBT.putInt(yKey, pos.getY());
        newNBT.putInt(zKey, pos.getZ());
        nbt.merge(newNBT);
        stack.setTag(nbt);
    }

    default void writeStringIf(Predicate<ItemStack> predicate, ItemStack stack, String key, String value) {
        CompoundNBT nbt = new CompoundNBT();
        if (predicate.test(stack)) {
            nbt.putString(key, value);
            stack.setTag(nbt);
        }
    }

    default void writeBooleanIf(Predicate<ItemStack> predicate, ItemStack stack, String key, boolean value) {
        CompoundNBT nbt = new CompoundNBT();
        if (predicate.test(stack)) {
            nbt.putBoolean(key, value);
            stack.setTag(nbt);
        }
    }

    default void writeBlockPosIf(Predicate<ItemStack> predicate, ItemStack stack, String key, BlockPos pos) {
        CompoundNBT nbt = new CompoundNBT();
        if (predicate.test(stack)) {
            nbt.putLong(key, pos.toLong());
            stack.setTag(nbt);
        }
    }

    default void mergeStringIf(Predicate<ItemStack> predicate, ItemStack stack, String key, String value) {
        CompoundNBT nbt = new CompoundNBT();
        if (predicate.test(stack)) {
            nbt.putString(key, value);
            stack.setTag(nbt);
        }
    }

    default void mergeBooleanIf(Predicate<ItemStack> predicate, ItemStack stack, String key, boolean value) {
        CompoundNBT nbt = new CompoundNBT();
        if (predicate.test(stack)) {
            nbt.putBoolean(key, value);
            stack.setTag(nbt);
        }
    }

    default void mergeBlockPosIf(Predicate<ItemStack> predicate, ItemStack stack, String key, BlockPos pos) {
        CompoundNBT nbt = new CompoundNBT();
        if (predicate.test(stack)) {
            nbt.putLong(key, pos.toLong());
            stack.setTag(nbt);
        }
    }
}
