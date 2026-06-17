package com.github.fictology.gensokyoontology.common.item.touhou;

import com.github.fictology.gensokyoontology.common.entiy.misc.Laser;
import com.github.fictology.gensokyoontology.util.GSKOUtil;
import com.github.fictology.gensokyoontology.api.IRayTraceReader;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.function.Consumer;

public class KoishiEyeOpen extends Item implements IRayTraceReader {
    private int totalCount = 0;

    public KoishiEyeOpen(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player playerIn, InteractionHand handIn) {
        ItemStack stack = playerIn.getItemInHand(handIn);
        if (playerIn.getCooldowns().isOnCooldown(stack)) return InteractionResult.PASS;

        var laserSource = new Laser(level, playerIn);
        laserSource.init(200, 40, 85);
        laserSource.setRGBA(0x88FF0000);
        // lasers(level, playerIn);

        laserSource.setOldPosAndRot(new Vec3(playerIn.getX(), playerIn.getY() + playerIn.getEyeHeight() * 0.5,
                playerIn.getZ()), playerIn.yHeadRot, playerIn.xRotO);
        level.addFreshEntity(laserSource);

        return super.use(level, playerIn, handIn);
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 50;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
        tooltipAdder.accept(GSKOUtil.translate("tooltip", "koishi_eye_open"));
    }

}
