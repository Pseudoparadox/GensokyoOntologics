package com.github.fictology.gensokyoontology.common.item;

import net.minecraft.world.item.Item;

//TODO: 把名称改为 “赛钱”
public class CoinItem extends Item {
    public float value;

    public CoinItem(float value) {
        super(new Properties());
        this.value = value;
    }

    public float getValue() {
        return value;
    }

}
