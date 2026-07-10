package github.thelawf.gensokyoontology.api;

import github.thelawf.gensokyoontology.api.util.Maybe;
import net.minecraft.nbt.*;
import net.minecraftforge.common.util.NonNullFunction;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

public class Functions {
    public static final NonNullFunction<INBT, Maybe<Integer>> NBT_2_INT = inbt -> {
        Maybe<Integer> maybe = Maybe.empty();
        if (inbt.getType() == IntNBT.TYPE){
            IntNBT intNBT = (IntNBT) inbt;
            maybe.set(intNBT.getInt());
        }
        return maybe;
    };

    public static final NonNullFunction<INBT, Maybe<Float>> NBT_2_FLOAT = inbt -> {
        Maybe<Float> maybe = Maybe.empty();
        if (inbt.getType() == FloatNBT.TYPE){
            FloatNBT floatNBT = (FloatNBT) inbt;
            maybe.set(floatNBT.getFloat());
        }
        return maybe;
    };

    public static final NonNullFunction<INBT, Maybe<String>> NBT_2_STRING = inbt -> {
        Maybe<String> maybe = Maybe.empty();
        if (inbt.getType() == StringNBT.TYPE){
            StringNBT stringNBT = (StringNBT) inbt;
            maybe.set(stringNBT.getString());
        }
        return maybe;
    };


    @FunctionalInterface
    public interface Func3<P1, P2, P3, R> {
        R apply(P1 p1, P2 p2, P3 p3);
    }
}
