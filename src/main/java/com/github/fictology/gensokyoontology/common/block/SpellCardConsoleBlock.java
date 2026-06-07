//package com.github.fictology.gensokyoontology.common.block;
//
//
//import net.minecraft.core.BlockPos;
//import net.minecraft.world.InteractionResult;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraft.world.phys.BlockHitResult;
//
///**
// * 符卡控制台
// */
//public class SpellCardConsoleBlock extends Block {
//    public SpellCardConsoleBlock(Properties p) {
//        super(p);
//    }
//
//    @Override
//    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
//        if (!level.isClientSide()) {
//            var tileEntity = level.getBlockEntity(pos);
//            if (tileEntity instanceof SpellConsoleTile console) {
//                player.openMenu(console);
//            } else {
//                throw new IllegalStateException("Missing Container Provider");
//            }
//        }
//        return InteractionResult.SUCCESS;
//    }
//
//}
