package com.github.fictology.gensokyoontology.common.item.script;

import com.github.fictology.gensokyoontology.registry.DataRegistry;
import com.github.fictology.gensokyoontology.util.GSKOUtil;
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

public abstract class ScriptBuilderItem extends Item {
    public static final String TYPE_HIGHLIGHT = "§6";    /// 金色 ///
    public static final String KEY_HIGHLIGHT = "§9";    /// 蓝色 ///
    public static final String NAME_HIGHLIGHT = "§d";    /// 粉色 ///
    public static final String VALUE_HIGHLIGHT = "§a";   /// 浅绿 ///
    public static final String STRING_HIGHLIGHT = "§b";  /// 天蓝色 ///
    public static final String EXCEPTION_HIGHLIGHT = "§c";  /// 红色 ///
    public static final String RESET_HIGHLIGHT = "§r";    /// 重置 ///

    public static final String FILED_TYPE_STR = GSKOUtil.affix("tooltip.", ".script_builder.field_type");
    public static final String FILED_NAME_STR = GSKOUtil.affix("tooltip.", ".script_builder.field_name");
    public static final String FILED_VALUE_STR = GSKOUtil.affix("tooltip.", ".script_builder.field_value");
    public static final Component FILED_TYPE_TIP = GSKOUtil.translate("tooltip.", ".script_builder.field_type");
    public static final Component FILED_NAME_TIP = GSKOUtil.translate("tooltip.", ".script_builder.field_name");
    public static final Component FILED_VALUE_TIP = GSKOUtil.translate("tooltip.", ".script_builder.field_value");

    public ScriptBuilderItem() {
        super(new Properties());
    }

    @Override
    public InteractionResult use(Level worldIn, Player playerIn, InteractionHand handIn) {
        if (!worldIn.isClientSide()) {
            ServerLevel serverWorld = (ServerLevel) worldIn;
            ServerPlayer serverPlayer = (ServerPlayer) playerIn;
            this.openScriptEditGUI(serverWorld, serverPlayer, playerIn.getItemInHand(handIn));
        }
        this.openScriptEditGUI(worldIn, playerIn, playerIn.getItemInHand(handIn));
        return super.use(worldIn, playerIn, handIn);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {

        var varInfo = stack.get(DataRegistry.PRIMITIVE_VAR);
        var vecInfo = stack.get(DataRegistry.VEC3_VAR);
        var varDanmaku = stack.get(DataRegistry.DANMAKU_VAR);
        if (varDanmaku == null) {
            return;
        }

        if (varInfo != null) {
            tooltipAdder.accept(GSKOUtil.translate(FILED_TYPE_STR, TYPE_HIGHLIGHT + varInfo.dataType()));
            tooltipAdder.accept(GSKOUtil.translate(FILED_NAME_STR, NAME_HIGHLIGHT + varInfo.name()));
            tooltipAdder.accept(GSKOUtil.translate(FILED_VALUE_STR, VALUE_HIGHLIGHT + varInfo.asFloat()));
            return;
        }

        if (vecInfo != null) {
            // tooltip.add(FILED_VALUE_TIP);
            // GSKONBTUtil.getMemberValues(nbt).forEach(s -> tooltip.add(new StringTextComponent(s)));
        }
    }

    public abstract void openScriptEditGUI(Level serverWorld, Player serverPlayer, ItemStack stack);
}
