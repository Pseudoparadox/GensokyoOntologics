package com.github.fictology.gensokyoontology.common.item.touhou;

import com.github.fictology.gensokyoontology.util.GSKOUtil;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

public class YinyangJadeItem extends Item {
    public YinyangJadeItem(Properties properties) {
        super(properties);
    }

    public static final Identifier[] MODELS = new Identifier[]{
            GSKOUtil.key("models/entity/jade_black.obj"),
            GSKOUtil.key("models/entity/jade_red.obj"),
            GSKOUtil.key("models/entity/jade_yellow.obj"),
            GSKOUtil.key("models/entity/jade_green.obj"),
            GSKOUtil.key("models/entity/jade_aqua.obj"),
            GSKOUtil.key("models/entity/jade_blue.obj"),
            GSKOUtil.key("models/entity/jade_purple.obj"),
    };
}
