package github.thelawf.gensokyoontology.common.entity;

import github.thelawf.gensokyoontology.core.init.EntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class CoasterVehicleEntity extends AbstractMinecartEntity {
    public CoasterVehicleEntity(EntityType<?> entityType, World worldIn) {
        super(entityType, worldIn);
    }

    @Override
    protected void moveAlongTrack(BlockPos pos, BlockState state) {
        super.moveAlongTrack(pos, state);
    }

    @Override
    @NotNull
    public Type getMinecartType() {
        return Type.RIDEABLE;
    }
}

