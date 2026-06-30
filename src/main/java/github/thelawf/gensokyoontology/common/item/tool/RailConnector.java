package github.thelawf.gensokyoontology.common.item.tool;

import github.thelawf.gensokyoontology.api.IRayTracer;
import github.thelawf.gensokyoontology.common.entity.misc.RailEntity;
import github.thelawf.gensokyoontology.common.util.GSKOUtil;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class RailConnector extends Item implements IRayTracer {
    public RailConnector(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack connector = player.getHeldItem(hand);
        Vector3d lookVec = player.getLookVec();
        Vector3d start = player.getEyePosition(1);
        Vector3d end = player.getEyePosition(1).add(lookVec.scale(10));

        AtomicReference<ActionResult<ItemStack>> result = new AtomicReference<>();
        result.set(ActionResult.resultPass(connector));

        this.rayTrace(world, player, start, end).ifPresent(entity -> {
            if(!(entity instanceof RailEntity)) return;
            RailEntity rail = (RailEntity) entity;
            RailWrench.onClickNextRail(world, player, rail, connector);
            result.set(ActionResult.resultConsume(connector));
        });
        return result.get();
    }

    @Override
    public void addInformation(@NotNull ItemStack stack, World worldIn, @NotNull List<ITextComponent> tooltip, @NotNull ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        if (stack.getTag() == null) return;
        if (worldIn == null) return;
        if (worldIn.isRemote) return;
        if (!stack.getTag().contains("uuid")) {
            ServerWorld serverWorld = (ServerWorld) worldIn;
            UUID id = stack.getTag().getUniqueId("uuid");
            RailWrench.getStartRail(serverWorld, id).ifPresent(rail -> tooltip.add(
                    GSKOUtil.translateText("tooltip.", ".coaster_rail.start_pos").appendSibling(
                            GSKOUtil.stringText("§a(" +
                                    rail.getPosX() + ", " +
                                    rail.getPosY() + ", " +
                                    rail.getPosZ() + ")"))));
        }
    }
}
