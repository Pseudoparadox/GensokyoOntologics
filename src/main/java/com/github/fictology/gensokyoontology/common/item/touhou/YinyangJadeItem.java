package com.github.fictology.gensokyoontology.common.item.touhou;

import com.github.fictology.gensokyoontology.util.GSKOUtil;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

public class YinyangJadeItem extends Item {
    public YinyangJadeItem(Properties properties) {
        super(properties);
    }

    public static final Identifier[] MODELS = new Identifier[]{
            GSKOUtil.key("models/entity/yinyang_jade_black.obj"),
            GSKOUtil.key("models/entity/yinyang_jade_red.obj"),
            GSKOUtil.key("models/entity/yinyang_jade_yellow.obj"),
            GSKOUtil.key("models/entity/yinyang_jade_green.obj"),
            GSKOUtil.key("models/entity/yinyang_jade_aqua.obj"),
            GSKOUtil.key("models/entity/yinyang_jade_blue.obj"),
            GSKOUtil.key("models/entity/yinyang_jade_purple.obj"),
    };
}
