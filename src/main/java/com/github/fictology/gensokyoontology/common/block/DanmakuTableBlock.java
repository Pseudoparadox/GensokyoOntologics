package com.github.fictology.gensokyoontology.common.block;


import com.github.fictology.gensokyoontology.common.container.menu.DanmakuCraftMenu;
import com.github.fictology.gensokyoontology.util.GSKOUtil;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class DanmakuTableBlock extends Block {
    public static final MapCodec<DanmakuTableBlock> CODEC = simpleCodec(DanmakuTableBlock::new);
    public static Component TITLE = GSKOUtil.translate("container", "danmaku_craft.title");

    public DanmakuTableBlock(Properties p) {
        super(p);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        } else {
            player.openMenu(state.getMenuProvider(level, pos));
            return InteractionResult.CONSUME;
        }
    }

    @Override
    protected @Nullable MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        return new SimpleMenuProvider((id, inventory, player) ->
                new DanmakuCraftMenu(id, inventory, ContainerLevelAccess.create(level, pos)), TITLE);
    }

}
