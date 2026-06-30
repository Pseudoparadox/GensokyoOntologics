package github.thelawf.gensokyoontology.common.network.packet;

import github.thelawf.gensokyoontology.common.entity.misc.RailEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SRenderRailPacket {
    public final int prevRailID;
    public final int nextRailID;

    public SRenderRailPacket (int prevRailID, int nextRailID) {
        this.prevRailID = prevRailID;
        this.nextRailID = nextRailID;
    }

    public static SRenderRailPacket fromBytes(PacketBuffer buf) {
        return new SRenderRailPacket(buf.readVarInt(), buf.readVarInt());
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeVarInt(this.prevRailID);
        buf.writeVarInt(this.nextRailID);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> setRenderInfo(this));
        ctx.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    public static void setRenderInfo(SRenderRailPacket packet) {
        Minecraft minecraft = Minecraft.getInstance();
        ClientWorld clientWorld = minecraft.world;
        if (clientWorld == null) return;
        if (clientWorld.getEntityByID(packet.nextRailID) == null) return;
        if (clientWorld.getEntityByID(packet.prevRailID) == null) return;

        Entity next = clientWorld.getEntityByID(packet.nextRailID);
        Entity prev = clientWorld.getEntityByID(packet.prevRailID);
        if (!(prev instanceof RailEntity)) return;
        if (!(next instanceof RailEntity)) return;

        RailEntity prevRail = (RailEntity) prev;
        RailEntity nextRail = (RailEntity) next;
        nextRail.setPrevRail(prevRail);
        prevRail.setNextRail(nextRail);
    }
}