package com.github.fictology.gensokyoontology.util;

import com.github.fictology.gensokyoontology.registry.ItemRegistry;
import net.minecraft.util.Util;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class StoneGambleHelper {

    public static final Map<Float, Item> GSKO_10_PROB = Util.make(() -> {
        var map = new HashMap<Float, Item>();
        map.put(0.9f / 100, ItemRegistry.JADE_LEVEL_SSS.get());
        map.put(2.f / 100, ItemRegistry.JADE_LEVEL_SS.get());
        map.put(13.1f / 100, ItemRegistry.JADE_LEVEL_S.get());
        map.put(38f / 100, ItemRegistry.JADE_LEVEL_A.get());
        map.put((100f - 0.9f - 2f - 13.1f - 38f) / 100f, ItemRegistry.JADE_LEVEL_B.get());
        return map;
    });
    public static final Map<Float, Item> GSKO_1_PROB = Util.make(() -> {
        var map = new HashMap<Float, Item>();
        map.put(0.4f / 100, ItemRegistry.JADE_LEVEL_SSS.get());
        map.put(0.9f / 100, ItemRegistry.JADE_LEVEL_SS.get());
        map.put(3f / 100, ItemRegistry.JADE_LEVEL_S.get());
        map.put(22f / 100, ItemRegistry.JADE_LEVEL_A.get());
        map.put((100f - 0.4f - 0.9f - 3f - 22f) / 100f, ItemRegistry.JADE_LEVEL_B.get());
        return map;
    });
    static final int ONE = 10000;

    /**
     * 玉石矿在不同的世界会存在着不同的掉落概率
     */
    public static ItemStack rollItemToDrop(Level worldIn, int sss, int ss, int s, int a) {
        int probability = new Random().nextInt(ONE);
        if (probability <= sss) {
            return new ItemStack(ItemRegistry.JADE_LEVEL_SSS.get());
        } else if (probability > sss + 1 && probability <= ss) {
            return new ItemStack(ItemRegistry.JADE_LEVEL_SS.get());
        } else if (probability > ss + 1 && probability <= s) {
            return new ItemStack(ItemRegistry.JADE_LEVEL_S.get());
        } else if (probability > s + 1 && probability <= a) {
            return new ItemStack(ItemRegistry.JADE_LEVEL_A.get());
        } else {
            return new ItemStack(ItemRegistry.JADE_LEVEL_B.get());
        }
    }

    public static ItemStack rollItemToDrop(Map<Float, Item> map) {
        int probability = new Random().nextInt(ONE);
        AtomicInteger cache = new AtomicInteger();
        AtomicReference<ItemStack> stack = new AtomicReference<>();
        stack.set(ItemStack.EMPTY);
        map.forEach((f, item) -> {
            if (probability <= f) {
                stack.set(new ItemStack(item));
                cache.addAndGet((int) (ONE * f));
            }
        });
        return stack.get();
    }


    // @Override
    // public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
    //     super.onBlockHarvested(worldIn, pos, state, player);
    //     spawnDropByWeight(worldIn, pos, state, player);
    // }
}
