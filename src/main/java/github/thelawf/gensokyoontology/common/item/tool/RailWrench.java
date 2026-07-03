package github.thelawf.gensokyoontology.common.item.tool;

import github.thelawf.gensokyoontology.api.IRayTracer;
import github.thelawf.gensokyoontology.client.gui.screen.RailDashboardScreen;
import github.thelawf.gensokyoontology.common.entity.misc.RailEntity;
import github.thelawf.gensokyoontology.common.network.GSKONetworking;
import github.thelawf.gensokyoontology.common.network.packet.S2CRenderRailPacket;
import github.thelawf.gensokyoontology.common.tileentity.RailTileEntity;
import github.thelawf.gensokyoontology.common.util.GSKOUtil;
import github.thelawf.gensokyoontology.core.init.BlockRegistry;
import github.thelawf.gensokyoontology.core.init.ItemRegistry;
import github.thelawf.gensokyoontology.core.init.TileEntityRegistry;
import github.thelawf.gensokyoontology.data.HermiteNodeInfo;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class RailWrench extends Item implements IRayTracer {
    public RailWrench(Properties properties) {
        super(properties);
    }

    @NotNull
    @Override
    public ActionResult<ItemStack> onItemRightClick(@NotNull World world, PlayerEntity player, @NotNull Hand hand) {
        ItemStack wrench = player.getHeldItem(hand);
        Vector3d lookVec = player.getLookVec();
        Vector3d start = player.getEyePosition(1);
        Vector3d end = player.getEyePosition(1).add(lookVec.scale(10));

        AtomicReference<ActionResult<ItemStack>> result = new AtomicReference<>();
        result.set(ActionResult.resultPass(wrench));

        this.rayTrace(world, player, start, end).ifPresent(entity -> {
            if(!(entity instanceof RailEntity)) return;
            RailEntity rail = (RailEntity) entity;
            this.onClickFirstRail(player, rail);
            result.set(ActionResult.resultSuccess(wrench));
        });
        return result.get();
    }

    private void onClickFirstRail(@NotNull PlayerEntity player, RailEntity startRail) {
        if (player.world.isRemote) {
            HermiteNodeInfo node = HermiteNodeInfo.of(startRail.getInfo(), startRail.getPosition(),
                            BlockPos.fromLong(0), startRail.getRotation(), Quaternion.ONE)
                    .setAutoSmooth(startRail.isAutoScale())
                    .setFlipNormal(startRail.isFlipNormal())
                    .setPrevScale(startRail.getScale0())
                    .setNextScale(startRail.getScale1());
            new RailDashboardScreen(startRail.getPosition(), node, startRail.getEntityId()).open();
        }
    }
}
