package github.thelawf.gensokyoontology.common.network.packet;

import github.thelawf.gensokyoontology.common.capability.GSKOCapabilities;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;

import java.util.function.Supplier;

public class SLifeTickPacket {

    private long lifetime;
    public SLifeTickPacket(long lifetime) {
        this.lifetime = lifetime;
    }

    public static SLifeTickPacket fromBytes(PacketBuffer buf) {
        return new SLifeTickPacket(buf.readLong());
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeLong(this.lifetime);
    }

    public static void handle(SLifeTickPacket packet, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide().isClient()) {
            ctx.get().enqueueWork(() -> update(packet));
        }
        ctx.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void update(SLifeTickPacket packet) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.world != null && mc.player != null) {
            mc.player.getCapability(GSKOCapabilities.SECULAR_LIFE).ifPresent(cap -> {
                cap.setLifetime(packet.getLifetime());
                // LogManager.getLogger().info("[Server] Player Life: {}",cap.getLifetime());
            });
        }
    }

    public long getLifetime() {
        return this.lifetime;
    }
}