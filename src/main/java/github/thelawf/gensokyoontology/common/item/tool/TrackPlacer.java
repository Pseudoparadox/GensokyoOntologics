package github.thelawf.gensokyoontology.common.item.tool;

import github.thelawf.gensokyoontology.common.entity.misc.RailEntity;
import github.thelawf.gensokyoontology.common.util.math.EulerAngle;
import github.thelawf.gensokyoontology.common.util.math.GSKOMathUtil;
import github.thelawf.gensokyoontology.core.init.EntityRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class TrackPlacer extends Item {
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
    public ActionResultType onItemUse(@NotNull ItemUseContext context) {
        PlayerEntity player = context.getPlayer();
        World world = context.getWorld();
        BlockPos pos = context.getPos();
        ItemStack stack = context.getItem();

        if (world.isRemote) return ActionResultType.FAIL;
        ServerWorld serverWorld = (ServerWorld) world;
        Entity entity = EntityRegistry.RAIL_ENTITY.get().spawn(serverWorld, stack, player, pos.up(), SpawnReason.TRIGGERED, false, false);
        if (!(entity instanceof RailEntity)) return ActionResultType.FAIL;
        RailEntity rail = (RailEntity) entity;
        // RailEntity rail = RailEntity.place(world, pos);

        if (player == null) return ActionResultType.FAIL;
        rail.setRotation(Quaternion.ONE);
        stack.shrink(1);
        return ActionResultType.CONSUME;
    }
}
