package github.thelawf.gensokyoontology.common.network.packet;

import github.thelawf.gensokyoontology.common.entity.misc.RailEntity;
import github.thelawf.gensokyoontology.common.util.EnumUtil;
import github.thelawf.gensokyoontology.common.util.GSKOUtil;
import github.thelawf.gensokyoontology.data.HermiteNodeInfo;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class CAdjustRailPacket {
    private final HermiteNodeInfo node;
    private final int entityId;

    public CAdjustRailPacket(HermiteNodeInfo node, int entityId) {
        this.node = node;
        this.entityId = entityId;
    }

    public static CAdjustRailPacket fromBytes(PacketBuffer buf) {
        return new CAdjustRailPacket(HermiteNodeInfo.EMPTY.read(buf), buf.readInt());
    }
    public void toBytes(PacketBuffer buf) {
        buf.writeCompoundTag(this.node.serializeNBT());
        buf.writeInt(this.entityId);
    }

    public static void handle(CAdjustRailPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity serverPlayer = ctx.get().getSender();
            if (serverPlayer != null) {
                ServerWorld serverWorld = serverPlayer.getServerWorld();
                changeAndSaveTileData(packet, serverWorld);
            }
        });
        ctx.get().setPacketHandled(true);
    }

    private static void changeAndSaveTileData(CAdjustRailPacket packet, ServerWorld serverWorld){
        serverWorld.getBlockState(packet.node.getStartPos());
        RailEntity rail = (RailEntity) serverWorld.getEntityByID(packet.entityId);

        if (rail == null) return;
        rail.setRotation(packet.node.rotation0());
        rail.setInfo(packet.node.getRailType().ordinal());
        rail.setScale0(packet.node.scale0());
        rail.setScale1(packet.node.scale1());
        rail.setAutoScale(packet.node.shouldAutoSmooth());
        rail.setFlipNormal(packet.node.shouldFlipNormal());
        serverWorld.updateEntity(rail);
        rail.getNextRail().ifPresent(serverWorld::updateEntity);
    }
}
