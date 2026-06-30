package github.thelawf.gensokyoontology.common.item;

import github.thelawf.gensokyoontology.api.IRayTracer;
import github.thelawf.gensokyoontology.common.entity.misc.CoasterVehicle;
import github.thelawf.gensokyoontology.common.entity.misc.RailEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicReference;

public class CoasterItem extends Item implements IRayTracer {
    public CoasterItem(Properties properties) {
        super(properties);
    }

    @NotNull
    @Override
    public ActionResult<ItemStack> onItemRightClick(@NotNull World world, PlayerEntity player, @NotNull Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        Vector3d lookVec = player.getLookVec();
        Vector3d start = player.getEyePosition(1);
        Vector3d end = player.getEyePosition(1).add(lookVec.scale(10));

        AtomicReference<ActionResult<ItemStack>> result = new AtomicReference<>();
        result.set(ActionResult.resultPass(stack));

        this.rayTrace(world, player, start, end).ifPresent(entity -> {
            if(!(entity instanceof RailEntity)) return;
            if (world.isRemote) return;

            ServerWorld serverWorld = (ServerWorld) world;
            RailEntity rail = (RailEntity) entity;
            CoasterVehicle coaster = new CoasterVehicle(serverWorld);

            coaster.setPosition(rail.getPosX(), rail.getPosY() + 0.5, rail.getPosZ());
            coaster.setPrevRail(rail);
            rail.getNextRail().ifPresent(coaster::setNextRail);
            serverWorld.addEntity(coaster);
            result.set(ActionResult.resultConsume(stack));
        });
        return result.get();
    }

}
