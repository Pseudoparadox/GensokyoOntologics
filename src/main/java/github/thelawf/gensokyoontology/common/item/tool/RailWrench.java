package github.thelawf.gensokyoontology.common.item.tool;

import github.thelawf.gensokyoontology.api.IRayTracer;
import github.thelawf.gensokyoontology.client.gui.screen.RailDashboardScreen;
import github.thelawf.gensokyoontology.client.gui.screen.RailHandlerScreen;
import github.thelawf.gensokyoontology.common.entity.misc.RailEntity;
import github.thelawf.gensokyoontology.common.network.GSKONetworking;
import github.thelawf.gensokyoontology.common.network.packet.S2CRenderRailPacket;
import github.thelawf.gensokyoontology.common.tileentity.RailTileEntity;
import github.thelawf.gensokyoontology.common.util.GSKOUtil;
import github.thelawf.gensokyoontology.core.init.BlockRegistry;
import github.thelawf.gensokyoontology.core.init.ItemRegistry;
import github.thelawf.gensokyoontology.core.init.TileEntityRegistry;
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
            this.onClickFirstRail(player, rail, wrench);
            result.set(ActionResult.resultConsume(wrench));
        });
        return result.get();
    }

    @Override
    public @NotNull ActionResultType onItemUse(@NotNull ItemUseContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getPos();
        PlayerEntity player = context.getPlayer();
        BlockState blockState = world.getBlockState(pos);
        ItemStack wrench = context.getItem();

        if (player == null) return ActionResultType.FAIL;
        if (blockState.getBlock() != BlockRegistry.COASTER_RAIL.get()) return super.onItemUse(context);
        Optional<RailTileEntity> optional = GSKOUtil.getTileByType(world, pos, TileEntityRegistry.RAIL_TILE_ENTITY.get());

        if (!optional.isPresent()) return super.onItemUse(context);
        RailTileEntity railTile = optional.get();

        return this.onClickFirstRailBlock(pos, player, wrench);
    }

    private void onClickFirstRail(@NotNull PlayerEntity player, RailEntity startRail, ItemStack wrench) {
        if (Screen.hasShiftDown() & player.world.isRemote) {
            new RailDashboardScreen(startRail.getPosition(), startRail.getRotation(), startRail.getEntityId()).open();
            return;
        }
        ItemStack connector = new ItemStack(ItemRegistry.RAIL_CONNECTOR.get());
        CompoundNBT nbt = new CompoundNBT();
        nbt.putUniqueId("uuid", startRail.getUniqueID());
        connector.setTag(nbt);

        player.addItemStackToInventory(connector);
    }

    private ActionResultType onClickFirstRailBlock(BlockPos startPos, @NotNull PlayerEntity player,
                                              ItemStack wrench) {
        ItemStack connector = new ItemStack(ItemRegistry.RAIL_CONNECTOR.get());
        CompoundNBT nbt = new CompoundNBT();
        nbt.putLong("startPos", startPos.toLong());
        connector.setTag(nbt);

        wrench.shrink(1);
        player.addItemStackToInventory(connector);
        return ActionResultType.SUCCESS;
    }

    public static ActionResultType onClickNextRailBlock(ItemUseContext targetCtx) {
        World world = targetCtx.getWorld();
        PlayerEntity player = targetCtx.getPlayer();
        BlockState blockState = world.getBlockState(targetCtx.getPos());
        ItemStack connector = targetCtx.getItem();

        if (player == null) return ActionResultType.FAIL;
        if (blockState.getBlock() != BlockRegistry.COASTER_RAIL.get()) return ActionResultType.PASS;
        if (connector.getTag() == null) return ActionResultType.PASS;

        BlockPos startPos = BlockPos.fromLong(connector.getTag().getLong("startPos"));
        Optional<RailTileEntity> optional = GSKOUtil.getTileByType(world, startPos, TileEntityRegistry.RAIL_TILE_ENTITY.get());
        if (!optional.isPresent()) return ActionResultType.PASS;
        RailTileEntity startRail = optional.get();

        startRail.setTargetPos(targetCtx.getPos());
        startRail.setShouldRender(false);

        ItemStack railItem = new ItemStack(ItemRegistry.RAIL_WRENCH.get());
        player.addItemStackToInventory(railItem);
        return ActionResultType.SUCCESS;
    }

    public static void onClickNextRail(World world, @NotNull PlayerEntity player,
                                       RailEntity targetRail , ItemStack connector) {
        if (connector.getItem() != ItemRegistry.RAIL_CONNECTOR.get()) return;
        if (connector.getTag() == null) return;
        if (world.isRemote) return;
        ServerWorld serverWorld = (ServerWorld) world;
        getStartRail(serverWorld, connector.getTag().getUniqueId("uuid")).ifPresent(entity -> {
            if (!(entity instanceof RailEntity)) return;
            RailEntity startRail = (RailEntity) entity;
            startRail.setNextId(targetRail.getUniqueID());
            startRail.setNextPos(targetRail.getPosition());
            connector.shrink(1);
            player.addItemStackToInventory(new ItemStack(ItemRegistry.RAIL_WRENCH.get()));

            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
            GSKONetworking.sendToClientPlayer(new S2CRenderRailPacket(startRail.getEntityId(),
                            targetRail.getEntityId()), serverPlayer);
        });

    }

    public static Optional<Entity> getStartRail(ServerWorld world, UUID uuid) {
        return Optional.ofNullable(world.getEntityByUuid(uuid));
    }

    public static ActionResultType onRemoveConnection(BlockPos pos, PlayerEntity player, ItemStack connector) {
        return ActionResultType.SUCCESS;
    }
}
