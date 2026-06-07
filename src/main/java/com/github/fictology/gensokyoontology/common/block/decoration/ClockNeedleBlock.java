package com.github.fictology.gensokyoontology.common.block.decoration;
/*
import github.thelawf.gensokyoontology.common.util.block.ClockHandDirection;
import github.thelawf.gensokyoontology.core.init.ItemRegistry;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ClockNeedleBlock extends Block {

    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    public static final EnumProperty<ClockHandDirection> CLOCK_HAND = EnumProperty.create("clock", ClockHandDirection.class);

    public ClockNeedleBlock() {
        super(Properties.from(Blocks.IRON_BLOCK));
    }

    @Override
    @NotNull
    @SuppressWarnings("deprecation")
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {

        return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        super.onBlockHarvested(worldIn, pos, state, player);
        spawnDrops(state, worldIn, pos, null, player, new ItemStack(ItemRegistry.CROOKED_CLOCK_NEEDLE.codec()));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return super.getStateForPlacement(context);
    }

    public void switchHandRotState() {
        if (CLOCK_HAND.getAllowedValues().contains(ClockHandDirection.CLOCK_12)) {
            this.setDefaultState(this.getDefaultState().with(CLOCK_HAND, ClockHandDirection.CLOCK_1));
        }
    }
}

 */
