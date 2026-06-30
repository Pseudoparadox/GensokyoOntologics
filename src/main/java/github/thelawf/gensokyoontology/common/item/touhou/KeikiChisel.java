package github.thelawf.gensokyoontology.common.item.touhou;

import github.thelawf.gensokyoontology.api.IRayTracer;
import github.thelawf.gensokyoontology.common.block.decoration.ClayAdobeBlock;
import github.thelawf.gensokyoontology.common.tileentity.AdobeTileEntity;
import github.thelawf.gensokyoontology.core.init.BlockRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.server.ServerWorld;
import org.jetbrains.annotations.NotNull;

public class KeikiChisel extends Item implements IRayTracer {
    public KeikiChisel(Properties properties) {
        super(properties);
    }

    @NotNull
    @Override
    public ActionResultType onItemUse(@NotNull ItemUseContext context) {
        onAdobeCarved(context);
        return super.onItemUse(context);
    }

    public void onAdobeCarved(@NotNull ItemUseContext context) {
        if (context.getWorld().getBlockState(context.getPos()).getBlock() == BlockRegistry.CLAY_ADOBE_BLOCK.get() &&
                context.getWorld() instanceof ServerWorld && context.getPlayer() != null) {
            ServerWorld serverWorld = (ServerWorld) context.getWorld();
            BlockPos pos = context.getPos();
            ClayAdobeBlock adobe = (ClayAdobeBlock) serverWorld.getBlockState(pos).getBlock();
            AdobeTileEntity tileEntity = adobe.getTileEntity(serverWorld, pos);
            tileEntity.add(getIntersectedCurvature(context.getPlayer(), serverWorld, pos, tileEntity));
        }

    }

    public void onTombClicked(@NotNull ItemUseContext context) {

    }

    public Vector3i getIntersectedCurvature(PlayerEntity player, ServerWorld serverWorld, BlockPos pos, AdobeTileEntity tileEntity) {
        Vector3d start = player.getPositionVec();
        Vector3d end = getLookEnd(start, player.getLookVec(), player.getEyeHeight(), 5);
        AxisAlignedBB aabb = new AxisAlignedBB(pos);
        Vector3d intersected = getIntersectedPos(start, end, aabb);
        ChunkPos chunkPos = serverWorld.getChunk(pos).getPos();
        return new Vector3i(((int) intersected.x * 16) >> chunkPos.x, intersected.y * 16 - pos.getY(), ((int) intersected.z * 16) >> chunkPos.z);
    }
}
