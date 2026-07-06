package github.thelawf.gensokyoontology.common.network.packet;

import github.thelawf.gensokyoontology.api.Actions;
import github.thelawf.gensokyoontology.common.entity.misc.CoasterVehicle;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

public class CInteractCoasterPacket {
    public static final String RIDING = "riding";
    public static final String STOP_RIDING = "stopRiding";
    public static final String DRIVING = "driving";

    private final String interactCommand;
    private final UUID coasterUUID;

    public static CInteractCoasterPacket fromBytes(PacketBuffer buf) {
        return new CInteractCoasterPacket(buf.readString(32767), buf.readUniqueId());
    }

    public CInteractCoasterPacket(String interactCommand, UUID coasterUUID) {
        this.interactCommand = interactCommand;
        this.coasterUUID = coasterUUID;
    }


    public void toBytes(PacketBuffer buf) {
        buf.writeString(interactCommand);
        buf.writeUniqueId(coasterUUID);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity sender = ctx.get().getSender();
            if (sender == null) return;
            COMMANDS.getOrDefault(this.interactCommand, (pkt, player, world) -> {})
                    .act(this, sender, sender.getServerWorld());
        });
        ctx.get().setPacketHandled(true);
    }


    public static final Map<String, Actions.Act3<CInteractCoasterPacket, ServerPlayerEntity, ServerWorld>> COMMANDS = new HashMap<>();
    static {
        COMMANDS.put(RIDING, (packet, serverPlayer, serverWorld) -> {
            CoasterVehicle vehicle = (CoasterVehicle) serverWorld.getEntityByUuid(packet.coasterUUID);
            if (vehicle == null) return;
            serverPlayer.startRiding(vehicle);
        });
        COMMANDS.put(STOP_RIDING, (packet, serverPlayer, serverWorld) ->
                serverPlayer.stopRiding());

        COMMANDS.put(DRIVING, (packet, serverPlayer, serverWorld) -> {
            CoasterVehicle vehicle = (CoasterVehicle) serverWorld.getEntityByUuid(packet.coasterUUID);
        });
    }
}