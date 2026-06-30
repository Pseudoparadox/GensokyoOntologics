package github.thelawf.gensokyoontology.api;

import net.minecraft.item.ItemStack;

import java.util.function.Predicate;

public interface INBTRunnable {
    default void runIf(Predicate<ItemStack> predicate, ItemStack stack, Runnable runnable) {
        if (predicate.test(stack)) {
            runnable.run();
        }
    }
}
