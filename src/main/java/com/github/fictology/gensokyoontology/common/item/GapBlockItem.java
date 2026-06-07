package com.github.fictology.gensokyoontology.common.item;

import com.github.fictology.gensokyoontology.registry.DataRegistry;
import com.github.fictology.gensokyoontology.util.GSKOUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Block;

import java.util.function.Consumer;

public class GapBlockItem extends BlockItem {
    public GapBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltipDisplay, tooltipAdder, flag);
        var gapInfo = stack.get(DataRegistry.GAP_INFO);
        if (gapInfo == null) return;
        if (gapInfo.departureWorld() == null) return;
        if (gapInfo.departurePos() == null) return;

        var depaturePos = gapInfo.departurePos();
        tooltipAdder.accept(GSKOUtil.translate("tool_tip", "gap.first_pos").copy()
                .append(depaturePos.toString())
                .append(GSKOUtil.translate("tool_tip", "gap.depature_world"))
                .append(GSKOUtil.translate("dimension_name",
                        gapInfo.departureWorld().registry().getNamespace() + "." +
                                gapInfo.departureWorld().registry().getPath())));
    }
}
