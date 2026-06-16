package com.github.fictology.gensokyoontology.client.renderer.state;

import com.github.fictology.gensokyoontology.common.entiy.misc.Danmaku;
import com.github.fictology.gensokyoontology.registry.ItemRegistry;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.util.Util;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.HashMap;
import java.util.Map;

public class DanmakuNormalState extends EntityRenderState {
    public Danmaku danmaku;
    public float size = 1F;
    public boolean hasNormal;
    public boolean fliped;
    public boolean customMesh;
    
    public DanmakuNormalState size(float size){
        this.size = size;
        return this;
    }
    public DanmakuNormalState normal(){
        this.hasNormal = true;
        return this;
    }
    public DanmakuNormalState flip(){
        this.fliped = true;
        return this;
    }

    private static final Map<Item, DanmakuNormalState> STATE_MAP = Util.make(() -> {
        var map = new HashMap<Item, DanmakuNormalState>();
        map.put(null, new DanmakuNormalState());
        map.put(Items.AIR, new DanmakuNormalState());

        map.put(ItemRegistry.LARGE_SHOT.get(), new DanmakuNormalState().size(2.0f));
        map.put(ItemRegistry.LARGE_SHOT_RED.get(), new DanmakuNormalState().size(2.0f));
        map.put(ItemRegistry.LARGE_SHOT_ORANGE.get(), new DanmakuNormalState().size(2.0f));
        map.put(ItemRegistry.LARGE_SHOT_YELLOW.get(), new DanmakuNormalState().size(2.0f));
        map.put(ItemRegistry.LARGE_SHOT_GREEN.get(), new DanmakuNormalState().size(2.0f));
        map.put(ItemRegistry.LARGE_SHOT_AQUA.get(), new DanmakuNormalState().size(2.0f));
        map.put(ItemRegistry.LARGE_SHOT_BLUE.get(), new DanmakuNormalState().size(2.0f));
        map.put(ItemRegistry.LARGE_SHOT_PURPLE.get(), new DanmakuNormalState().size(2.0f));
        map.put(ItemRegistry.LARGE_SHOT_MAGENTA.get(), new DanmakuNormalState().size(2.0f));

        map.put(ItemRegistry.SMALL_SHOT.get(), new DanmakuNormalState().size(0.4f));
        map.put(ItemRegistry.SMALL_SHOT_RED.get(), new DanmakuNormalState().size(0.4f));
        map.put(ItemRegistry.SMALL_SHOT_ORANGE.get(), new DanmakuNormalState().size(0.4f));
        map.put(ItemRegistry.SMALL_SHOT_YELLOW.get(), new DanmakuNormalState().size(0.4f));
        map.put(ItemRegistry.SMALL_SHOT_GREEN.get(), new DanmakuNormalState().size(0.4f));
        map.put(ItemRegistry.SMALL_SHOT_AQUA.get(), new DanmakuNormalState().size(0.4f));
        map.put(ItemRegistry.SMALL_SHOT_BLUE.get(), new DanmakuNormalState().size(0.4f));
        map.put(ItemRegistry.SMALL_SHOT_PURPLE.get(), new DanmakuNormalState().size(0.4f));
        map.put(ItemRegistry.SMALL_SHOT_MAGENTA.get(), new DanmakuNormalState().size(0.4f));

        map.put(ItemRegistry.CIRCLE_SHOT_BLUE.get(), new DanmakuNormalState().size(0.4F));
        map.put(ItemRegistry.CIRCLE_SHOT_MAGENTA.get(), new DanmakuNormalState().size(0.4F));

        map.put(ItemRegistry.LARGE_STAR_SHOT.get(), new DanmakuNormalState().size(3f));
        map.put(ItemRegistry.LARGE_STAR_SHOT_RED.get(), new DanmakuNormalState().size(3f));
        map.put(ItemRegistry.LARGE_STAR_SHOT_YELLOW.get(), new DanmakuNormalState().size(3f));
        map.put(ItemRegistry.LARGE_STAR_SHOT_GREEN.get(), new DanmakuNormalState().size(3f));
        map.put(ItemRegistry.LARGE_STAR_SHOT_AQUA.get(), new DanmakuNormalState().size(3f));
        map.put(ItemRegistry.LARGE_STAR_SHOT_BLUE.get(), new DanmakuNormalState().size(3f));
        map.put(ItemRegistry.LARGE_STAR_SHOT_PURPLE.get(), new DanmakuNormalState().size(3f));

        map.put(ItemRegistry.SMALL_STAR_SHOT.get(), new DanmakuNormalState().size(0.4f));
        map.put(ItemRegistry.SMALL_STAR_SHOT_RED.get(), new DanmakuNormalState().size(0.4f));
        map.put(ItemRegistry.SMALL_STAR_SHOT_YELLOW.get(), new DanmakuNormalState().size(0.4f));
        map.put(ItemRegistry.SMALL_STAR_SHOT_GREEN.get(), new DanmakuNormalState().size(0.4f));
        map.put(ItemRegistry.SMALL_STAR_SHOT_AQUA.get(), new DanmakuNormalState().size(0.4f));
        map.put(ItemRegistry.SMALL_STAR_SHOT_BLUE.get(), new DanmakuNormalState().size(0.4f));
        map.put(ItemRegistry.SMALL_STAR_SHOT_PURPLE.get(), new DanmakuNormalState().size(0.4f));

        map.put(ItemRegistry.FAKE_LUNAR_ITEM.get(), new DanmakuNormalState().size(4f));

        map.put(ItemRegistry.SCALE_SHOT.get(), new DanmakuNormalState().size(0.5F).normal());
        map.put(ItemRegistry.SCALE_SHOT_RED.get(), new DanmakuNormalState().size(0.5F).normal());
        map.put(ItemRegistry.SCALE_SHOT_YELLOW.get(), new DanmakuNormalState().size(0.5F).normal());
        map.put(ItemRegistry.SCALE_SHOT_GREEN.get(), new DanmakuNormalState().size(0.5F).normal());
        map.put(ItemRegistry.SCALE_SHOT_BLUE.get(), new DanmakuNormalState().size(0.5F).normal());
        map.put(ItemRegistry.SCALE_SHOT_PURPLE.get(), new DanmakuNormalState().size(0.5F).normal());

        map.put(ItemRegistry.TALISMAN_SHOT.get(), new DanmakuNormalState().size(1.0F).normal());
        map.put(ItemRegistry.TALISMAN_SHOT_RED.get(), new DanmakuNormalState().size(1.0F).normal());
        map.put(ItemRegistry.TALISMAN_SHOT_GREEN.get(), new DanmakuNormalState().size(1.0F).normal());
        map.put(ItemRegistry.TALISMAN_SHOT_BLUE.get(), new DanmakuNormalState().size(1.0F).normal());
        map.put(ItemRegistry.TALISMAN_SHOT_PURPLE.get(), new DanmakuNormalState().size(1.0F).normal());
        map.put(ItemRegistry.TALISMAN_SHOT_AQUA.get(), new DanmakuNormalState().size(1.0F).normal());

        map.put(ItemRegistry.RICE_SHOT.get(), new DanmakuNormalState().size(0.5F).normal());
        map.put(ItemRegistry.RICE_SHOT_RED.get(), new DanmakuNormalState().size(0.5F).normal());
        map.put(ItemRegistry.RICE_SHOT_BLUE.get(), new DanmakuNormalState().size(0.5F).normal());
        map.put(ItemRegistry.RICE_SHOT_PURPLE.get(), new DanmakuNormalState().size(0.5F).normal());

        map.put(ItemRegistry.HEART_SHOT.get(), new DanmakuNormalState().size(2.0F).normal().flip());
        map.put(ItemRegistry.HEART_SHOT_RED.get(), new DanmakuNormalState().size(2.0F).normal().flip());
        map.put(ItemRegistry.HEART_SHOT_BLUE.get(), new DanmakuNormalState().size(2.0F).normal().flip());
        map.put(ItemRegistry.HEART_SHOT_PINK.get(), new DanmakuNormalState().size(2.0F).normal().flip());
        map.put(ItemRegistry.HEART_SHOT_AQUA.get(), new DanmakuNormalState().size(2.0F).normal().flip());

        return map;
    });

    public static DanmakuNormalState getStateForItem(Item item){
        return STATE_MAP.get(item) == null ? new DanmakuNormalState() : STATE_MAP.get(item);
    }
}
