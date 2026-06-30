package github.thelawf.gensokyoontology.common.item.touhou;

import github.thelawf.gensokyoontology.api.INBTRunnable;
import github.thelawf.gensokyoontology.api.INBTWriter;
import github.thelawf.gensokyoontology.core.init.BlockRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GapItem extends BlockItem implements INBTWriter, INBTRunnable {
    public GapItem(Properties properties) {
        super(BlockRegistry.GAP_BLOCK.get(), properties);
    }

    @Override
    protected boolean onBlockPlaced(@NotNull BlockPos pos, @NotNull World worldIn, @Nullable PlayerEntity player, ItemStack stack, BlockState state) {

        if (player == null) return super.onBlockPlaced(pos, worldIn, player, stack, state);

        if (player.getHeldItemMainhand().getItem() == this) {

            if (stack.hasTag()) {
                CompoundNBT nbt = stack.getTag();
                // setBlockTileSecond(player, worldIn, pos, nbt);
                stack.setTag(new CompoundNBT());
                stack.shrink(1);
                return super.onBlockPlaced(pos, worldIn, player, stack, state);

            }
        }

        // setBlockTileFirst(worldIn, player, pos);
        return super.onBlockPlaced(pos, worldIn, player, stack, state);
    }


    @Override
    public void addInformation(@NotNull ItemStack stack, @Nullable World worldIn, @NotNull List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        if (stack.getTag() != null && stack.getTag().contains("first_pos")) {
            CompoundNBT nbt = stack.getTag();
            if (nbt.contains("first_pos")) {
                BlockPos pos = BlockPos.fromLong(nbt.getLong("first_pos"));
                tooltip.add(new StringTextComponent("第一处隙间设置为: " + pos.getCoordinatesAsString()));
            }
            if (nbt.contains("departure_world")) {
                tooltip.add(new TranslationTextComponent(nbt.getString("departure_world")));
            }
            if (nbt.contains("is_first_placement")) {
                tooltip.add(new StringTextComponent("是否是第一次点击：" + nbt.getBoolean("is_first_placement")));
            }
        }
    }
}
