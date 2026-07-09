package github.thelawf.gensokyoontology.common.item.tool;

import github.thelawf.gensokyoontology.api.IRayTracer;
import github.thelawf.gensokyoontology.api.util.Maybe;
import github.thelawf.gensokyoontology.common.entity.misc.RailEntity;
import github.thelawf.gensokyoontology.common.network.GSKONetworking;
import github.thelawf.gensokyoontology.common.network.packet.S2CRenderRailPacket;
import github.thelawf.gensokyoontology.common.util.GSKOUtil;
import github.thelawf.gensokyoontology.core.init.EntityRegistry;
import github.thelawf.gensokyoontology.core.init.ItemRegistry;
import github.thelawf.gensokyoontology.data.HermiteNodeInfo;
import github.thelawf.gensokyoontology.data.TrackInfo;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SpawnReason;
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
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class RailConnector extends Item implements IRayTracer {
    public RailConnector(Properties properties) {
        super(properties);
    }

    @NotNull
    @Override
    public ActionResult<ItemStack> onItemRightClick(@NotNull World world, @NotNull PlayerEntity player, @NotNull Hand hand) {
        if (!Screen.hasShiftDown()) return ActionResult.resultPass(player.getHeldItem(hand));
        ItemStack connector = player.getHeldItem(hand);
        Vector3d lookVec = player.getLookVec();
        Vector3d start = player.getEyePosition(1);
        Vector3d end = player.getEyePosition(1).add(lookVec.scale(6));

        AtomicReference<ActionResult<ItemStack>> result = new AtomicReference<>();
        result.set(ActionResult.resultPass(connector));

        this.rayTrace(world, player, start, end).ifPresent(entity -> {
            if(!(entity instanceof RailEntity)) return;
            RailEntity rail = (RailEntity) entity;
            this.connect(player, world, connector, rail);
            connector.shrink(1);
            result.set(ActionResult.resultConsume(connector));
        });
        return result.get();
    }

    /**
     * @apiNote {@link ActionResult#func_233538_a_(Object, boolean)}: updateActionResult(T, boolean)
     */
    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        PlayerEntity player = context.getPlayer();
        World world = context.getWorld();
        BlockPos pos = context.getPos();
        ItemStack stack = context.getItem();

        if (player == null) return ActionResultType.PASS;
        if (world.isRemote) return ActionResultType.PASS;
        ServerWorld serverWorld = (ServerWorld) world;
        Entity entity = EntityRegistry.RAIL_ENTITY.get().spawn(serverWorld, stack, player, pos.up(), SpawnReason.TRIGGERED, false, false);

        if (!(entity instanceof RailEntity)) return ActionResultType.PASS;
        RailEntity rail = (RailEntity) entity;
        this.onPlaceNextRail(world, player, rail, stack);
        return ActionResult.func_233538_a_(stack, false).getType();
    }

    @Override
    public void addInformation(@NotNull ItemStack stack, World worldIn, @NotNull List<ITextComponent> tooltip, @NotNull ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        if (stack.getTag() == null) return;
        if (worldIn == null) return;
        if (stack.getTag().contains("prev_pos")) {
            BlockPos pos = BlockPos.fromLong(stack.getTag().getLong("prev_pos"));
            tooltip.add(GSKOUtil.translateText("tooltip.", ".coaster_rail.place_connect"));
            tooltip.add(GSKOUtil.translateText("tooltip.", ".coaster_rail.connect"));
            tooltip.add(GSKOUtil.translateText("tooltip.", ".coaster_rail.start_pos").appendSibling(
                    GSKOUtil.stringText("§a(" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + ")")));
        }
    }

    private void onPlaceNextRail(World world, @NotNull PlayerEntity player,
                                RailEntity targetRail , ItemStack connector) {
        if (connector.getItem() != ItemRegistry.RAIL_CONNECTOR.get()) return;
        if (connector.getTag() == null) return;
        if (world.isRemote) return;
        ServerWorld serverWorld = (ServerWorld) world;
        this.getStartRail(serverWorld, connector.getTag().getUniqueId("uuid")).ifPresent(entity -> {
            if (!(entity instanceof RailEntity)) return;
            RailEntity startRail = (RailEntity) entity;
            startRail.setNextId(targetRail.getUniqueID());
            startRail.setNextPos(targetRail.getPosition());
            connector.shrink(1);
            this.setNextStartRail(player, targetRail);

            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
            TrackInfo.tryGetInstance(serverWorld)
                    .ifPresent(info -> this.tryGetFirstRail(serverWorld, connector)
                            .ifPresent(first -> info.addRailNode(startRail.getUniqueID(), HermiteNodeInfo.from(first))));
            GSKONetworking.sendToClientPlayer(new S2CRenderRailPacket(startRail.getEntityId(),
                    targetRail.getEntityId()), serverPlayer);
        });
    }

    private Maybe<RailEntity> tryGetFirstRail(ServerWorld serverWorld, ItemStack connector){
        return Maybe.ofNullable(connector.getTag())
                .map(nbt -> nbt.getUniqueId("first"))
                .map(serverWorld::getEntityByUuid)
                .map(entity -> (RailEntity)entity);
    }


    private Optional<Entity> getStartRail(ServerWorld world, UUID uuid) {
        return Optional.ofNullable(world.getEntityByUuid(uuid));
    }

    private void setNextStartRail(@NotNull PlayerEntity player, RailEntity rail) {
        ItemStack connector = new ItemStack(ItemRegistry.RAIL_CONNECTOR.get());
        CompoundNBT nbt = new CompoundNBT();
        nbt.putUniqueId("uuid", rail.getUniqueID());
        connector.setTag(nbt);

        player.addItemStackToInventory(connector);
    }

    private void connect(@NotNull PlayerEntity player, World world, ItemStack connector, RailEntity nextRail){
        if (connector.getItem() != ItemRegistry.RAIL_CONNECTOR.get()) return;
        if (connector.getTag() == null) return;
        if (world.isRemote) return;
        ServerWorld serverWorld = (ServerWorld) world;
        this.getStartRail(serverWorld, connector.getTag().getUniqueId("uuid")).ifPresent(entity -> {
            if (!(entity instanceof RailEntity)) return;
            RailEntity startRail = (RailEntity) entity;
            startRail.setNextId(nextRail.getUniqueID());
            startRail.setNextPos(nextRail.getPosition());

            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
            GSKONetworking.sendToClientPlayer(new S2CRenderRailPacket(startRail.getEntityId(),
                    nextRail.getEntityId()), serverPlayer);
        });
    }
}
