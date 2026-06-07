package com.github.fictology.gensokyoontology.common.item.touhou;

import com.github.fictology.gensokyoontology.registry.DataRegistry;
import com.github.fictology.gensokyoontology.util.GSKOUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

public class OccultBall extends Item {
    public OccultBall(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        var stack = player.getItemInHand(hand);
        var predicate = stack.get(DataRegistry.PREDICATE);

        if (predicate == null) return InteractionResult.PASS;
        if (!(level instanceof ServerLevel serverLevel)) return InteractionResult.PASS;
        if (!(player instanceof ServerPlayer serverPlayer)) return InteractionResult.PASS;

        if (predicate.condition()) {

//            var gensokyo = serverLevel.getServer().getLevel(GSKODimensions.GENSOKYO);
//            TeleportHelper.teleport(serverPlayer, gensokyo, BlockPos.containing(player.getPosition(0)));
        }

        return super.use(level, player, hand);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
        tooltipAdder.accept(GSKOUtil.translate("tooltip", "occult_ball.hint"));
    }

    /*
    @Override
    public void addInformation(ItemStack stack, @Nullable Level level, List<ITextComponent> tooltip, @NotNull ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("tooltip." + GensokyoOntology.MODID + ".occult_ball.hint"));
        if (stack.getTag() != null && stack.getTag().contains("biome")) {
            String info = "tooltip." + GensokyoOntology.MODID + ".occult_ball.biome";
            tooltip.add(new TranslationTextComponent(info));
            tooltip.add(new TranslationTextComponent(stack.getTag().getString("biome")));
        }
        if (stack.getTag() != null && stack.getTag().contains("can_travel_to_gensokyo")) {
            String info = "tooltip." + GensokyoOntology.MODID + ".occult_ball.accessablity";
            // tooltip.add(new TranslationTextComponent(info));
            tooltip.add(new TranslationTextComponent(stack.getTag().getString("can_travel_to_gensokyo")));
        }
        super.addInformation(stack, level, tooltip, flagIn);
    }
    
     */

}
