package github.thelawf.gensokyoontology.common.network.packet;

import github.thelawf.gensokyoontology.common.entity.misc.RailEntity;
import github.thelawf.gensokyoontology.common.util.EnumUtil;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class CAdjustRailPacket {
    private final Quaternion selfFacing;
    private final BlockPos targetPos;
    private final int startEntityId;
    private final int exit;
    private final int enter;
    private final RailEntity.Info info;
    private final boolean autoScale;

    public CAdjustRailPacket(BlockPos targetPos, Quaternion selfFacing, int startEntityId, int exit, int enter,
                             RailEntity.Info info, boolean autoScale) {
        this.targetPos = targetPos;
        this.selfFacing = selfFacing;
        this.startEntityId = startEntityId;
        this.exit = exit;
        this.enter = enter;
        this.info = info;
        this.autoScale = autoScale;
    }

    public static CAdjustRailPacket fromBytes(PacketBuffer buf) {
        return new CAdjustRailPacket(buf.readBlockPos(),
                new Quaternion(buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat()),
                buf.readInt(), buf.readInt(), buf.readInt(), EnumUtil.readEnum(RailEntity.Info.class, buf.readInt()), buf.readBoolean());
    }
    public void toBytes(PacketBuffer buf) {
        buf.writeBlockPos(targetPos);

        buf.writeFloat(this.selfFacing.getX());
        buf.writeFloat(this.selfFacing.getY());
        buf.writeFloat(this.selfFacing.getZ());
        buf.writeFloat(this.selfFacing.getW());

        buf.writeInt(this.startEntityId);
        buf.writeInt(this.exit);
        buf.writeInt(this.enter);
        buf.writeInt(this.info.ordinal());
        buf.writeBoolean(this.autoScale);
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
        serverWorld.getBlockState(packet.targetPos);
        RailEntity rail = (RailEntity) serverWorld.getEntityByID(packet.startEntityId);

        if (rail == null) return;
        rail.setRotation(packet.selfFacing);
        rail.setInfo(packet.info.ordinal());
        rail.setScale0(packet.exit);
        rail.setScale1(packet.enter);
        rail.setAutoScale(packet.autoScale);
        serverWorld.updateEntity(rail);
        rail.getNextRail().ifPresent(serverWorld::updateEntity);
    }
}
