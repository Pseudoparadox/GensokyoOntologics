package github.thelawf.gensokyoontology.common.network.packet;

import github.thelawf.gensokyoontology.api.Actions;
import github.thelawf.gensokyoontology.common.entity.misc.CoasterVehicle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

public class SInteractCoasterPacket {

    public static final String RIDING = "riding";
    public static final String STOP_RIDING = "stopRiding";
    public static final String DRIVING = "driving";

    private final String interactCommand;
    private final int coasterID;

    public static SInteractCoasterPacket fromBytes(PacketBuffer buf) {
        return new SInteractCoasterPacket(buf.readString(32767), buf.readVarInt());
    }

    public SInteractCoasterPacket(String interactCommand, int coasterID) {
        this.interactCommand = interactCommand;
        this.coasterID = coasterID;
    }


    public void toBytes(PacketBuffer buf) {
        buf.writeString(this.interactCommand);
        buf.writeVarInt(this.coasterID);
    }

    @OnlyIn(Dist.CLIENT)
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientPlayerEntity clientPlayer = Minecraft.getInstance().player;
            if (clientPlayer == null) return;
            COMMANDS.getOrDefault(this.interactCommand, (pkt, player, world) -> {})
                    .act(this, clientPlayer, Minecraft.getInstance().world);
        });
        ctx.get().setPacketHandled(true);
    }


    public static final Map<String, Actions.Act3<SInteractCoasterPacket, ClientPlayerEntity, ClientWorld>> COMMANDS = new HashMap<>();
    static {
        COMMANDS.put(RIDING, (packet, player, world) -> {});
        COMMANDS.put(STOP_RIDING, (packet, player, world) ->
        {
            CoasterVehicle vehicle = (CoasterVehicle) world.getEntityByID(packet.coasterID);
            if (vehicle == null) return;
            player.stopRiding();
        });

        COMMANDS.put(DRIVING, (packet, player, world) -> {
            CoasterVehicle vehicle = (CoasterVehicle) world.getEntityByID(packet.coasterID);
            if (vehicle == null) return;
        });
    }
}