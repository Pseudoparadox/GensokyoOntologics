package github.thelawf.gensokyoontology.common.network.packet;

import github.thelawf.gensokyoontology.common.entity.misc.RailEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class C2SRenderRailPacket {
    public final UUID prevRailID;
    public final UUID nextRailID;

    public C2SRenderRailPacket(UUID prevRailID, UUID nextRailID) {
        this.prevRailID = prevRailID;
        this.nextRailID = nextRailID;
    }

    public static S2CRenderRailPacket fromBytes(PacketBuffer buf) {
        return new S2CRenderRailPacket(buf.readVarInt(), buf.readVarInt());
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeUniqueId(this.prevRailID);
        buf.writeUniqueId(this.nextRailID);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity serverPlayer = ctx.get().getSender();
            if (serverPlayer == null) return;
            setRenderInfo(serverPlayer, this);
        });
        ctx.get().setPacketHandled(true);
    }

    public static void setRenderInfo(ServerPlayerEntity serverPlayer, C2SRenderRailPacket packet) {
        World world = serverPlayer.world;
        if (world instanceof ServerWorld){
            ServerWorld serverWorld = (ServerWorld) world;
            Entity nextEntity = serverWorld.getEntityByUuid(packet.nextRailID);
            Entity prevEntity = serverWorld.getEntityByUuid(packet.prevRailID);
            if (nextEntity instanceof RailEntity && prevEntity instanceof RailEntity){
                RailEntity nextRail = (RailEntity) nextEntity;
                RailEntity prevRail = (RailEntity) prevEntity;
                nextRail.setPrevId(packet.nextRailID);
                prevRail.setNextId(packet.nextRailID);
            }
        }
    }
}
