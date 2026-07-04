package github.thelawf.gensokyoontology.common.network;

import github.thelawf.gensokyoontology.common.network.packet.*;
import github.thelawf.gensokyoontology.common.util.GSKOUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class GSKONetworking {

    public static final String VERSION = "1.0";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            GSKOUtil.withRL("network"),
            () -> VERSION, VERSION::equals, VERSION::equals);

    private static int ID = 0;

    private static int next() {
        return ID++;
    }

    public static void register() {

        CHANNEL.messageBuilder(SScarletMistPacket.class, next()).encoder(SScarletMistPacket::toBytes).decoder(SScarletMistPacket::new).consumer(SScarletMistPacket::handle).add();
        // CHANNEL.messageBuilder(ImperishableNightPacket.class, next()).encoder(ImperishableNightPacket::toBytes).decoder(ImperishableNightPacket::new).consumer(ImperishableNightPacket::handle).add();
        // CHANNEL.messageBuilder(PowerChangedPacket.class, next()).encoder(PowerChangedPacket::toBytes).decoder(PowerChangedPacket::fromBytes).consumer(PowerChangedPacket::handle).add();
        // CHANNEL.messageBuilder(SLifeTickPacket.class, next()).encoder(SLifeTickPacket::toBytes).decoder(SLifeTickPacket::fromBytes).consumer(SLifeTickPacket::handle).add();

        CHANNEL.messageBuilder(CMergeScriptPacket.class, next()).encoder(CMergeScriptPacket::toBytes).decoder(CMergeScriptPacket::fromBytes).consumer(CMergeScriptPacket::handle).add();
        CHANNEL.messageBuilder(CAddScriptPacket.class, next()).encoder(CAddScriptPacket::toBytes).decoder(CAddScriptPacket::fromBytes).consumer(CAddScriptPacket::handle).add();
        CHANNEL.messageBuilder(CInvokeFunctionPacket.class, next()).encoder(CInvokeFunctionPacket::toBytes).decoder(CInvokeFunctionPacket::fromBytes).consumer(CInvokeFunctionPacket::handle).add();
        CHANNEL.messageBuilder(CSwitchModePacket.class, next()).encoder(CSwitchModePacket::toBytes).decoder(CSwitchModePacket::fromBytes).consumer(CSwitchModePacket::handle).add();

        CHANNEL.messageBuilder(CInteractCoasterPacket.class, next()).encoder(CInteractCoasterPacket::toBytes).decoder(CInteractCoasterPacket::fromBytes).consumer(CInteractCoasterPacket::handle).add();
        CHANNEL.messageBuilder(CAdjustRailPacket.class, next()).encoder(CAdjustRailPacket::toBytes).decoder(CAdjustRailPacket::fromBytes).consumer(CAdjustRailPacket::handle).add();

        CHANNEL.messageBuilder(SRotateCameraPacket.class, next()).encoder(SRotateCameraPacket::toBytes).decoder(SRotateCameraPacket::fromBytes).consumer(SRotateCameraPacket::handle).add();
        CHANNEL.messageBuilder(SDanmakuTilePacket.class, next()).encoder(SDanmakuTilePacket::toBytes).decoder(SDanmakuTilePacket::fromBytes).consumer(SDanmakuTilePacket::handle).add();
        CHANNEL.messageBuilder(SJigsawPatternRenderPacket.class, next()).encoder(SJigsawPatternRenderPacket::toBytes).decoder(SJigsawPatternRenderPacket::fromBytes).consumer(SJigsawPatternRenderPacket::handle).add();
        CHANNEL.messageBuilder(S2CRenderRailPacket.class, next()).encoder(S2CRenderRailPacket::toBytes).decoder(S2CRenderRailPacket::fromBytes).consumer(S2CRenderRailPacket::handle).add();

        CHANNEL.messageBuilder(SInteractCoasterPacket.class, next()).encoder(SInteractCoasterPacket::toBytes).decoder(SInteractCoasterPacket::fromBytes).consumer(SInteractCoasterPacket::handle).add();

    }

    public static void sendToClientPlayer(Object message, PlayerEntity player) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), message);
    }

}
