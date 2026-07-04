package github.thelawf.gensokyoontology.common.block.nature;

import github.thelawf.gensokyoontology.common.capability.GSKOCapabilities;
import github.thelawf.gensokyoontology.data.world.GSKOWorldSavedData;
import github.thelawf.gensokyoontology.data.world.GensokyoSeason;
import net.minecraft.block.*;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class SakuraLeaves extends LeavesBlock {
    public static final BooleanProperty BLOOMED = BooleanProperty.create("bloomed");
    public SakuraLeaves() {
        super(Properties.from(Blocks.ACACIA_LEAVES).tickRandomly().sound(SoundType.PLANT));
        //this.setDefaultState(this.getDefaultState().create(BLOOMED, true));
    }

    @Override
    public void randomTick(BlockState state, @NotNull ServerWorld worldIn, @NotNull BlockPos pos, @NotNull Random random) {
        if (!state.get(PERSISTENT) && state.get(DISTANCE) == 7) {
            spawnDrops(state, worldIn, pos);
            worldIn.removeBlock(pos, false);
        }
        GSKOWorldSavedData gskoWorldData = GSKOWorldSavedData.getInstance(worldIn);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(BLOOMED);
    }
}
