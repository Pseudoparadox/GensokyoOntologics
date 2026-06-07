package com.github.fictology.gensokyoontology.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class IshiZakuraBlock extends Block {

    private static final VoxelShape shape;

    static {
        VoxelShape voxelShape = Block.box(4, 0, 4, 12, 6, 12);
        shape = Shapes.or(voxelShape);
    }

    public IshiZakuraBlock() {
        super(Properties.ofFullCopy(Blocks.STONE).sound(SoundType.GLASS));
    }

    @NotNull
    @Override
    public VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter worldIn, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return shape;
    }
}
