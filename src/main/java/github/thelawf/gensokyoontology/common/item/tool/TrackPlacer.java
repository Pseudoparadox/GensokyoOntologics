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
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class TrackPlacer extends Item implements IRayTracer {
    public static final HashMap<Vector2f, Float> VECTOR2F_MAPPING = Util.make(new HashMap<>(), map -> {
        map.put(new Vector2f(-22.5F, 22.5F), 270F);
        map.put(new Vector2f(22.5F, 45F + 22.5F), 45F);
        map.put(new Vector2f(45F + 22.5F, 90F + 22.5F), 0F);
        map.put(new Vector2f(90F + 22.5F, 135F + 22.5F), 135F);
        map.put(new Vector2f(135F + 22.5F, 180F), 90F);
        map.put(new Vector2f(-180F, -180F + 22.5F), 90F);
        map.put(new Vector2f(-180F + 22.5F, -135F + 22.5F), 225F);
        map.put(new Vector2f(-135F + 22.5F, -90F + 22.5F), 180F);
        map.put(new Vector2f(-90F + 22.5F, -45F + 22.5F), 315F);
    });

    public TrackPlacer(Properties properties) {
        super(properties);
    }

    @NotNull
    @Override
    public ActionResult<ItemStack> onItemRightClick(@NotNull World world, @NotNull PlayerEntity player, @NotNull Hand hand) {
        if (!Screen.hasShiftDown()) return ActionResult.resultPass(player.getHeldItem(hand));
        ItemStack wrench = player.getHeldItem(hand);
        Vector3d lookVec = player.getLookVec();
        Vector3d start = player.getEyePosition(1);
        Vector3d end = player.getEyePosition(1).add(lookVec.scale(6));

        AtomicReference<ActionResult<ItemStack>> result = new AtomicReference<>();
        result.set(ActionResult.resultPass(wrench));

        this.rayTrace(world, player, start, end).ifPresent(entity -> {
            if(!(entity instanceof RailEntity)) return;
            RailEntity rail = (RailEntity) entity;
            this.setFirstRail(player, rail);
            result.set(ActionResult.resultSuccess(wrench));
        });
        return result.get();
    }

    @NotNull
    @Override
    public ActionResultType onItemUse(@NotNull ItemUseContext context) {
        PlayerEntity player = context.getPlayer();
        World world = context.getWorld();
        BlockPos pos = context.getPos();
        ItemStack stack = context.getItem();

        if (world.isRemote) return ActionResultType.FAIL;
        if (player == null) return ActionResultType.FAIL;
        ServerWorld serverWorld = (ServerWorld) world;
        Entity entity = EntityRegistry.RAIL_ENTITY.get().spawn(serverWorld, stack, player, pos.up(), SpawnReason.TRIGGERED, false, false);

        if (!(entity instanceof RailEntity)) return ActionResultType.FAIL;
        RailEntity rail = (RailEntity) entity;

        rail.setRotation(Quaternion.ONE);
        TrackInfo.tryGetInstance(player.world).ifPresent(info -> {
            info.addTracks(rail);
            info.addRailNode(rail.getUniqueID(), HermiteNodeInfo.from(rail));
        });
        stack.shrink(1);
        return this.setFirstRail(player, rail);
    }

    private ActionResultType setFirstRail(@NotNull PlayerEntity player, RailEntity rail) {
        ItemStack connector = new ItemStack(ItemRegistry.RAIL_CONNECTOR.get());
        CompoundNBT nbt = new CompoundNBT();
        nbt.putUniqueId("first", rail.getUniqueID());
        nbt.putUniqueId("uuid", rail.getUniqueID());
        nbt.putLong("prev_pos", rail.getPosition().toLong());
        connector.setTag(nbt);

        player.addItemStackToInventory(connector);
        return ActionResultType.CONSUME;
    }

    @Override
    public void addInformation(@NotNull ItemStack stack, World worldIn, @NotNull List<ITextComponent> tooltip, @NotNull ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(GSKOUtil.translateText("tooltip.", ".coaster_rail.place"));
    }
}
