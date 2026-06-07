package com.github.fictology.gensokyoontology.common.block.decoration;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.VoxelShape;

public class AdobeTileBlock extends Block {
    public static final VoxelShape SHAPE = box(0, 0, 0, 1, 1, 1);

    public AdobeTileBlock(Properties p) {
        super(p);
    }

}
