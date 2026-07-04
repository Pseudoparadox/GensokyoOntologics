package github.thelawf.gensokyoontology.common.network.packet;

import com.github.tartaricacid.touhoulittlemaid.capability.PowerCapabilityProvider;
import github.thelawf.gensokyoontology.common.capability.GSKOCapabilities;
import github.thelawf.gensokyoontology.common.capability.entity.GSKOPowerCapability;
import github.thelawf.gensokyoontology.common.compat.touhoulittlemaid.TouhouLittleMaidCompat;
import github.thelawf.gensokyoontology.data.world.GSKOWorldSavedData;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * 使用的命名规范：由服务端发给客户端的数据包类名叫做 SXxxXxx.java，由客户端发给服务端的数据包类名叫做 CXxxXxx.java
 */
public class PowerChangedPacket {
    private float count;
    public PowerChangedPacket(float count) {
        this.count = count;
    }

    public static PowerChangedPacket fromBytes(PacketBuffer buf) {
        return new PowerChangedPacket(buf.readFloat());
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeFloat(this.count);
    }

    public static void handle(PowerChangedPacket packet, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide().isClient()) {
            ctx.get().enqueueWork(() -> {
                Minecraft mc = Minecraft.getInstance();
                if (mc.player != null) {
                    mc.player.getCapability(GSKOCapabilities.POWER).ifPresent(gskoCap -> {
                        gskoCap.setCount(packet.count);
                    });
                }
                ServerPlayerEntity player = ctx.get().getSender();
                if (player != null) sendToServer(player, packet);
            });
        }
        ctx.get().setPacketHandled(true);
    }

    private static void sendToServer(ServerPlayerEntity serverPlayer, PowerChangedPacket packet) {
        GSKOWorldSavedData.getInstance(serverPlayer.world).writePower(packet.count);
    }

    private static void sendToClient(PowerChangedPacket packet) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) {
            mc.player.getCapability(GSKOCapabilities.POWER).ifPresent(gskoCap -> {
                gskoCap.setCount(packet.count);
            });
            if (TouhouLittleMaidCompat.isTouhouMaidLoaded()){
                mc.player.getCapability(PowerCapabilityProvider.POWER_CAP).ifPresent(power ->{
                    power.set(packet.count);
                });
            }
        }

    }

    public float getCount() {
        return this.count;
    }

    @Override
    public String toString() {
        return "Power Packet Has: " + this.count;
    }
}