package github.thelawf.gensokyoontology.common.events;

import com.github.tartaricacid.touhoulittlemaid.capability.MaidNumCapabilityProvider;
import com.github.tartaricacid.touhoulittlemaid.capability.PowerCapabilityProvider;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.SyncCapabilityMessage;
import github.thelawf.gensokyoontology.GensokyoOntology;
import github.thelawf.gensokyoontology.common.capability.GSKOCapabilities;
import github.thelawf.gensokyoontology.common.capability.entity.*;
import github.thelawf.gensokyoontology.common.capability.world.BloodyMistProvider;
import github.thelawf.gensokyoontology.common.capability.world.EternalSummerCapProvider;
import github.thelawf.gensokyoontology.common.capability.world.IIncidentCapability;
import github.thelawf.gensokyoontology.common.capability.world.ImperishableNightProvider;
import github.thelawf.gensokyoontology.common.compat.touhoulittlemaid.TouhouLittleMaidCompat;
import github.thelawf.gensokyoontology.common.network.GSKONetworking;
import github.thelawf.gensokyoontology.common.network.packet.PowerChangedPacket;
import github.thelawf.gensokyoontology.common.network.packet.SLifeTickPacket;
import github.thelawf.gensokyoontology.common.util.GSKOUtil;
import github.thelawf.gensokyoontology.common.world.GSKODimensions;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.*;

@Mod.EventBusSubscriber(modid = GensokyoOntology.MODID)
public class GSKOCapabilityEvents {
    private static final Map<UUID, Float> lastPowerValues = new HashMap<>();
    private static ClientWorld clientWorld;
    private static ServerWorld serverWorld;

    @SubscribeEvent
    public static void onCapabilityAttachToWorld(AttachCapabilitiesEvent<World> event) {
        if (event.getObject() instanceof World) {
            List<String> biomes = new ArrayList<>();
            biomes.add("gensokyoontology:scarlet_mansion_precincts");
            biomes.add("gensokyoontology:misty_lake");

            BloodyMistProvider bloodyMist = new BloodyMistProvider(biomes, true);
            ImperishableNightProvider imperishableNight = new ImperishableNightProvider(18000, false);
            EternalSummerCapProvider eternalSummer = new EternalSummerCapProvider(true);

            event.addCapability(GSKOUtil.withRL("bloody_mist"), bloodyMist);
            event.addCapability(GSKOUtil.withRL("imperishable_night"), imperishableNight);
            event.addCapability(GSKOUtil.withRL("eternal_summer"), eternalSummer);
        }
    }

    @SubscribeEvent
    public static void onCapabilityAttachToEntity(AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();
        if (entity instanceof PlayerEntity) {

            GSKOPowerProvider power = new GSKOPowerProvider(0f);
            IdentityCapabilityProvider identity = new IdentityCapabilityProvider();
            SecularLifetimeProvider lifetime = new SecularLifetimeProvider(0L);

            event.addCapability(GSKOUtil.withRL("power"), power);
            event.addCapability(GSKOUtil.withRL("identities"), identity);
            event.addCapability(GSKOUtil.withRL("secular_lifetime"), lifetime);
        }
    }


    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        updateBelief(event);
        updatePower(event);
        updateLife(event);
    }

    @SubscribeEvent
    public static void onWorldTickDuringIncident(WorldEvent.Load event) {
        if (event.getWorld() instanceof ServerWorld) {
            ServerWorld serverWorld = ((ServerWorld) event.getWorld()).getServer().getWorld(GSKODimensions.GENSOKYO);

            if (serverWorld != null) {
                updateCapability(serverWorld, GSKOCapabilities.BLOODY_MIST);
                updateCapability(serverWorld, GSKOCapabilities.IMPERISHABLE_NIGHT);
            }
        }
    }

    /**
     * 该方法只有在检测到玩家在车万女仆模组中更改了他自己的Power点数之后才会起作用，作用是将车万女仆的Power点数同步至本模组的Power点数。
     * 订阅tick事件以进行数据包的发送操作，需要获取逻辑端和tick事件阶段。
     * @param event 玩家tick事件
     * @apiNote This method will make effects only when it detects a player change his power counts in Touhou Little Maid mod.
     * The effect of3D this method is to sync the power counts from Touhou Little Maid to this Mod.
     *
     */
    @SubscribeEvent
    public static void onCapabilitySync(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (event.player.world.isRemote) return; // 只在服务端执行
        PlayerEntity player = event.player;
        UUID playerId = player.getUniqueID();

        // 获取当前能力值
        float currentPower = player.getCapability(GSKOCapabilities.POWER)
                .map(GSKOPowerCapability::getCount)
                .orElse(0.0f);

        // 检查是否变化
        Float lastPower = lastPowerValues.get(playerId);
        if (lastPower == null || Math.abs(currentPower - lastPower) > 0.001f) {
            // 同步到车万女仆系统
            // TouhouLittleMaidCompat.syncPower(player);
            // 更新记录
            lastPowerValues.put(playerId, currentPower);

        }
    }

    public static void trySyncCapabilities(World world) {

        if (world.isRemote) clientWorld = (ClientWorld) world;
        if (!world.isRemote) serverWorld = (ServerWorld) world;
        if (clientWorld == null) return;
        if (serverWorld == null) return;

        GSKOUtil.syncWorldCapability(clientWorld, serverWorld, GSKOCapabilities.BLOODY_MIST);
    }

    public static void trySyncCapabilities(PlayerEntity player) {
        player.getCapability(GSKOCapabilities.SECULAR_LIFE).ifPresent(cap -> {
            // GSKOUtil.showChatMsg(player, cap.isDirty(), 20);
            cap.addTime(1L);
            // GSKONetworking.sendToClientPlayer(new SLifeTickPacket(cap.getLifetime()), player);
        });

    }

    public static void trySyncPower(PlayerEntity player) {
        player.getCapability(GSKOCapabilities.POWER).ifPresent(cap -> {
            GSKONetworking.sendToClientPlayer(new PowerChangedPacket(cap.getCount()), player);
            cap.markDirty();
        });
    }

    public static void trySyncPowerToTLM(PlayerEntity player) {
        player.getCapability(GSKOCapabilities.POWER).ifPresent(gskoCap ->
                player.getCapability(PowerCapabilityProvider.POWER_CAP).ifPresent(tlmCap ->
                        player.getCapability(MaidNumCapabilityProvider.MAID_NUM_CAP).ifPresent(maidNumCap ->
                        {
                            tlmCap.set(gskoCap.getCount());
                            NetworkHandler.sendToClientPlayer(new SyncCapabilityMessage(gskoCap.getCount(), maidNumCap.get()), player);
                            tlmCap.setDirty(true);
                        })));
    }
    public static void trySyncPowerFromTLM(PlayerEntity player) {
        player.getCapability(GSKOCapabilities.POWER).ifPresent(gskoCap ->
                player.getCapability(PowerCapabilityProvider.POWER_CAP).ifPresent(tlmCap ->
                {
                    gskoCap.setCount(tlmCap.get());
                    GSKONetworking.sendToClientPlayer(new PowerChangedPacket(tlmCap.get()), player);
                    tlmCap.setDirty(true);
                }));
    }

    @SubscribeEvent
    public static void onPacketSendToClient(TickEvent.PlayerTickEvent event) {
        PlayerEntity player = event.player;
        if (event.side == LogicalSide.CLIENT && event.phase == TickEvent.Phase.END) {
            player.getCapability(GSKOCapabilities.POWER).ifPresent(cap -> {
                GSKONetworking.CHANNEL.sendToServer(new GSKOPowerCapability(cap.getCount()));
            });
            player.getCapability(GSKOCapabilities.POWER).ifPresent(cap -> {
                GSKONetworking.sendToClientPlayer(new PowerChangedPacket(cap.getCount()), player);
            });
        }
    }

    private static void updatePower(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            LazyOptional<GSKOPowerCapability> oldCapability = event.getOriginal().getCapability(GSKOCapabilities.POWER);
            LazyOptional<GSKOPowerCapability> newCapability = event.getPlayer().getCapability(GSKOCapabilities.POWER);

            newCapability.ifPresent(capNew -> oldCapability.ifPresent(capOld -> {
                capNew.setCount(capOld.getCount());

            }));
        }
    }

    private static void updateLife(PlayerEvent.Clone event) {

        PlayerEntity playerOld = event.getOriginal();
        PlayerEntity playerNew = event.getPlayer();

        LazyOptional<SecularLifeCapability> oldCapability = playerOld.getCapability(GSKOCapabilities.SECULAR_LIFE);
        LazyOptional<SecularLifeCapability> newCapability = playerNew.getCapability(GSKOCapabilities.SECULAR_LIFE);
        if (oldCapability.isPresent() && newCapability.isPresent()) {
            newCapability.ifPresent(capNew -> oldCapability.ifPresent(capOld -> {
                if (event.isWasDeath()) {
                    capNew.setLifetime(0L);
                }
                else {
                    capNew.setLifetime(capOld.getLifetime());
                }
            }));
        }
    }

    private static void updateBelief(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            LazyOptional<IdentityCapability> oldCapability = event.getOriginal().getCapability(GSKOCapabilities.IDENTITY);
            LazyOptional<IdentityCapability> newCapability = event.getPlayer().getCapability(GSKOCapabilities.IDENTITY);

            newCapability.ifPresent(capNew -> oldCapability.ifPresent(capOld -> {
                capNew.deserializeNBT(IdentityCapability.INSTANCE.serializeNBT());
            }));
        }
    }

    private static <C extends IIncidentCapability> void updateCapability(ServerWorld serverWorld, Capability<C> capability) {
        LazyOptional<C> oldCapability = serverWorld.getCapability(capability);
        LazyOptional<C> newCapability = serverWorld.getCapability(capability);
        if (oldCapability.isPresent() && newCapability.isPresent()) {
            newCapability.ifPresent(capNew -> oldCapability.ifPresent(capOld -> capNew.deserializeNBT(capOld.serializeNBT())));
        }
    }

    private static void updateCapability(ServerPlayerEntity serverPlayer) {
        GSKONetworking.CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new PowerChangedPacket(3));
    }
}
