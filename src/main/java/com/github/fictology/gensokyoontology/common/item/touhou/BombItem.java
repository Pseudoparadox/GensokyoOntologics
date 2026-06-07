package com.github.fictology.gensokyoontology.common.item.touhou;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public class BombItem extends Item {
    private boolean flag;

    public BombItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        return super.use(level, player, hand);
    }
    /*
    @Override
    @NotNull
    public ActionResult<ItemStack> onItemRightClick(@NotNull World worldIn, @NotNull PlayerEntity playerIn, @NotNull Hand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);

        if (stack.hasTag() && stack.getTag() != null && stack.getTag().contains("is_triggered")){
            playerIn.sendMessage(new StringTextComponent("has_tag"), playerIn.getUniqueID());
            setCapabilityTriggered(worldIn, playerIn, false);
            if (!worldIn.isRemote) {
                ServerWorld serverWorld = (ServerWorld) worldIn;
                serverWorld.getServer().getWorlds().forEach(sw -> sw.setDayTime(15000));
            }
            stack.setTag(new CompoundNBT());
            return super.onItemRightClick(worldIn, playerIn, handIn);
        }

        CompoundNBT nbt = new CompoundNBT();
        nbt.putBoolean("isTriggered", this.flag);
        playerIn.sendMessage(new StringTextComponent("no_tag"), playerIn.getUniqueID());
        setCapabilityTriggered(worldIn, playerIn, true);
        if (!worldIn.isRemote) {
            ServerWorld serverWorld = (ServerWorld) worldIn;
            serverWorld.getServer().getWorlds().forEach(sw -> sw.setDayTime(15000));
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    private void setCapabilityTriggered(World worldIn, PlayerEntity playerIn, boolean triggered) {
        //if (worldIn.isRemote) ImperishableNightPacket.sendToServer(worldIn, GSKONetworking.IMPERISHABLE_NIGHT, triggered);
        if (!worldIn.isRemote) {
            ServerWorld serverWorld = (ServerWorld) worldIn;
            serverWorld.getCapability(GSKOCapabilities.IMPERISHABLE_NIGHT).ifPresent(cap -> {
                cap.setTriggered(triggered);
                // playerIn.sendMessage(new StringTextComponent(String.valueOf(cap.isTriggered())), playerIn.getUniqueID());
                serverWorld.getGameRules().codec(GameRules.DO_DAYLIGHT_CYCLE).set(cap.isTriggered(), serverWorld.getServer());
            });
            //ImperishableNightPacket.sendToPlayer(serverWorld, (ServerPlayerEntity) playerIn, GSKONetworking.IMPERISHABLE_NIGHT, triggered);
        }

    }

    */
}
