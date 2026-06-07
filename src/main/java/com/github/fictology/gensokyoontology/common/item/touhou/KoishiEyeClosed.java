package com.github.fictology.gensokyoontology.common.item.touhou;

import com.github.fictology.gensokyoontology.common.entiy.misc.Danmaku;
import com.github.fictology.gensokyoontology.util.GSKOUtil;
import com.github.fictology.gensokyoontology.util.api.IRayTraceReader;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

public class KoishiEyeClosed extends Item implements IRayTraceReader {
    public KoishiEyeClosed(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (player.getCooldowns().isOnCooldown(player.getItemInHand(hand)))
            return InteractionResult.PASS;

        var list = getRayTraceBox(player.getPosition(0), player.getLookAngle(), 50, 2);
        list.forEach(aabbs -> aabbs.forEach(aabb -> {
            var es = level.getEntitiesOfClass(Danmaku.class, aabb);
            es.forEach(e -> e.setRemoved(Entity.RemovalReason.KILLED));
        }));
        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
        tooltipAdder.accept(GSKOUtil.translate("tooltip.", ".koishi_eye_closed"));
    }

}
