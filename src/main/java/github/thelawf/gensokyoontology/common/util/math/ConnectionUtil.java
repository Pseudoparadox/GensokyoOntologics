package github.thelawf.gensokyoontology.common.util.math;


import com.mojang.datafixers.util.Pair;
import github.thelawf.gensokyoontology.api.IRayTracer;
import github.thelawf.gensokyoontology.common.entity.misc.RailEntity;
import github.thelawf.gensokyoontology.common.item.tool.RailWrench;
import github.thelawf.gensokyoontology.data.HermiteNodeInfo;
import github.thelawf.gensokyoontology.data.TrackInfo;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public final class ConnectionUtil implements IRayTracer {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final ConnectionUtil INSTANCE = new ConnectionUtil();
    public List<BlockPos> getRailNodeFromSaves(World world){
        if (world.isRemote()) return new ArrayList<>();
        List<BlockPos> list = new ArrayList<>();
        ServerWorld serverWorld = (ServerWorld)world;
        TrackInfo info = TrackInfo.tryGetInstance(serverWorld).get();

        if (info == null) return list;
        info.tracks().forEach((key, value) -> {
            BlockPos firstPos = serverWorld.getEntityByUuid(key).getPosition();
            list.add(firstPos);
            list.addAll(value.toList().stream().map(HermiteNodeInfo::getStartPos).collect(Collectors.toList()));
        });

        return list;
    }

    public void handleWrenchClickRailFromSaves(PlayerEntity player, World world, ItemStack wrench){
        Vector3d lookVec = player.getLookVec();
        Vector3d start = player.getEyePosition(1);
        Vector3d end = player.getEyePosition(1).add(lookVec.scale(10));

        AtomicReference<ActionResult<ItemStack>> result = new AtomicReference<>();
        result.set(ActionResult.resultPass(wrench));

        this.getRailNodeFromSaves(world).forEach(blockPos -> {
            AxisAlignedBB aabb = new AxisAlignedBB(blockPos);
            if (!checkRayHitBox(aabb, start, end)) return;
            this.rayTrace(world, player, start, end).ifPresent(entity -> {
                if (!(entity instanceof RailEntity)) return;
                RailEntity rail = (RailEntity) entity;
                ((RailWrench) wrench.getItem()).onClickFirstRail(player, rail);
                result.set(ActionResult.resultSuccess(wrench));
            });
        });
    }
}
