package github.thelawf.gensokyoontology.common.events;

import com.github.tartaricacid.touhoulittlemaid.capability.PowerCapability;
import com.github.tartaricacid.touhoulittlemaid.capability.PowerCapabilityProvider;
import github.thelawf.gensokyoontology.GensokyoOntology;
import github.thelawf.gensokyoontology.api.IRayTracer;
import github.thelawf.gensokyoontology.client.renderer.world.ScarletSkyRenderer;
import github.thelawf.gensokyoontology.common.block.nature.HotSpringBlock;
import github.thelawf.gensokyoontology.common.capability.entity.IdentityCapability;
import github.thelawf.gensokyoontology.common.capability.entity.GSKOPowerCapability;
import github.thelawf.gensokyoontology.common.capability.entity.SecularLifeCapability;
import github.thelawf.gensokyoontology.common.capability.world.BloodyMistCapability;
import github.thelawf.gensokyoontology.common.capability.GSKOCapabilities;
import github.thelawf.gensokyoontology.common.capability.world.ImperishableNightCapability;
import github.thelawf.gensokyoontology.common.compat.touhoulittlemaid.TouhouLittleMaidCompat;
import github.thelawf.gensokyoontology.common.entity.misc.CoasterVehicle;
import github.thelawf.gensokyoontology.common.entity.monster.FairyEntity;
import github.thelawf.gensokyoontology.common.entity.projectile.Danmaku;
import github.thelawf.gensokyoontology.common.network.GSKONetworking;
import github.thelawf.gensokyoontology.common.network.packet.CInteractCoasterPacket;
import github.thelawf.gensokyoontology.common.network.packet.PowerChangedPacket;
import github.thelawf.gensokyoontology.common.network.packet.SScarletMistPacket;
import github.thelawf.gensokyoontology.common.util.GSKODamageSource;
import github.thelawf.gensokyoontology.common.potion.HypnosisEffect;
import github.thelawf.gensokyoontology.common.potion.LovePotionEffect;
import github.thelawf.gensokyoontology.common.util.GSKOUtil;
import github.thelawf.gensokyoontology.common.util.client.GSKOMusicSelector;
import github.thelawf.gensokyoontology.common.util.danmaku.DanmakuUtil;
import github.thelawf.gensokyoontology.common.util.world.GSKOWorldUtil;
import github.thelawf.gensokyoontology.common.world.GSKODimensions;
import github.thelawf.gensokyoontology.common.world.dimension.biome.GSKOBiomes;
import github.thelawf.gensokyoontology.core.init.BlockRegistry;
import github.thelawf.gensokyoontology.core.init.EffectRegistry;
import github.thelawf.gensokyoontology.core.init.ItemRegistry;
import github.thelawf.gensokyoontology.data.GSKOPlayerData;
import github.thelawf.gensokyoontology.data.world.GSKOWorldSavedData;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.MusicTicker;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.List;
import java.util.Random;

@Mod.EventBusSubscriber(modid = "gensokyoontology")
public class GSKOEntityEvents {

    public static final MusicTicker MUSIC_TICKER = new MusicTicker(Minecraft.getInstance());
    public static int AMBIENT_SOUND_TIMER = 0;
    public static boolean CAN_START_SOUND_TIMER = true;

    public static int CICADA_SOUND = 20 * 60 * 2;
    public static final int LILY_WHITE_DELAY = 80000;

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        event.getPlayer().getCapability(GSKOCapabilities.POWER).ifPresent(cap -> {
            GSKONetworking.sendToClientPlayer(new PowerChangedPacket(cap.getCount()), event.getPlayer());
            GSKOPowerCapability.INSTANCE = cap;
        });
    }

    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity)event.getEntity();
            player.getCapability(GSKOCapabilities.POWER).ifPresent(gskoCap -> {
                GSKONetworking.sendToClientPlayer(new PowerChangedPacket(gskoCap.getCount()), player);
                GSKOPowerCapability.INSTANCE = gskoCap; // FIXME: 在玩家第一次进入游戏时由于能力系统未创建导致渲染p点点数时闪退
            });
            // player.getCapability(GSKOCapabilities.SECULAR_LIFE).ifPresent(SecularLifeCapability::markDirty);
            player.getCapability(GSKOCapabilities.IDENTITY).ifPresent(belief -> IdentityCapability.INSTANCE = belief);
        }
    }

    @SubscribeEvent
    public static void onSyncWithTLM(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (!TouhouLittleMaidCompat.isTouhouMaidLoaded())return;

        PlayerEntity player = event.player;
        if (player.world.isRemote) return;
        if (!TouhouLittleMaidCompat.isTouhouMaidLoaded()) return;


        player.getCapability(GSKOCapabilities.POWER).ifPresent(gskoCap -> {
            GSKONetworking.sendToClientPlayer(new PowerChangedPacket(gskoCap.getCount()), player);
            player.getCapability(PowerCapabilityProvider.POWER_CAP).ifPresent(tlmCap -> {
                tlmCap.markDirty();
                handleDataSaved(player.world, tlmCap, gskoCap);
                handleDataLoaded(player.world, tlmCap, gskoCap);
            });
        });
    }

    public static void handleDataSaved(World world, PowerCapability tlmCap, GSKOPowerCapability gskoCap) {
        if (gskoCap.isDirty()) {
            GSKOWorldSavedData.getInstance(world).writePower(gskoCap.getCount());
            gskoCap.setDirty(false);
            tlmCap.markDirty();
        }
        else if (tlmCap.isDirty()) {
            GSKOWorldSavedData.getInstance(world).writePower(tlmCap.get());
            tlmCap.setDirty(false);
            gskoCap.markDirty();
        }
    }

    public static void handleDataLoaded(World world, PowerCapability tlmCap, GSKOPowerCapability gskoCap) {
        if (gskoCap.isDirty()) {
            gskoCap.setCount(GSKOWorldSavedData.getInstance(world).getPower());
            gskoCap.setDirty(false);
        }
        else if (tlmCap.isDirty()) {
            tlmCap.set(GSKOWorldSavedData.getInstance(world).getPower());
            tlmCap.setDirty(false);
        }
    }

    @SubscribeEvent
    @Deprecated
    public static void onPlayerLife(LivingEvent.LivingUpdateEvent event) {
        if (!(event.getEntityLiving() instanceof PlayerEntity)) return;
        PlayerEntity player = (PlayerEntity) event.getEntityLiving();
        if (player.getEntityWorld() instanceof ServerWorld) {
            World world = player.getEntityWorld();
            LazyOptional<SecularLifeCapability> cap = player.getCapability(GSKOCapabilities.SECULAR_LIFE);
            cap.ifPresent((capability -> {
                if (world.getGameTime() % 5 == 0) {
                    capability.addTime(1);
                }
                // if (capability.getLifetime() == 45_000L) GSKOUtil.showChatMsg(player, "You Feel yourself have no time to live",1);
                // if (capability.getLifetime() == 50_000L) player.setHealth(0);
            }));
        }
    }

    @SubscribeEvent
    public static void onHotSpringIn(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntityLiving() != null && event.getEntityLiving().isInWater()) {
            BlockState blockState = event.getEntityLiving().getBlockState();
            if (blockState.getBlockState().getBlock() instanceof HotSpringBlock &&
                    !(event.getEntityLiving() instanceof IMob)) {
                event.getEntityLiving().heal(1.2F);
            }
        }
    }

    @SubscribeEvent
    public static void onWineIn(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntityLiving() != null && event.getEntityLiving().isInWater()) {
            BlockState blockState = event.getEntityLiving().getBlockState();
            if (blockState.getBlockState().getBlock().equals(BlockRegistry.SAKE_WINE_BLOCK.get()) &&
                    event.getEntityLiving() instanceof PlayerEntity) {
                event.getEntityLiving().addPotionEffect(new EffectInstance(Effects.NAUSEA, 2 * 100));
            }
        }
    }

    @SubscribeEvent
    public static void onEnterNamelessHill(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntityLiving() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();
            if (player.getActivePotionEffects().contains(new EffectInstance(
                    EffectRegistry.HAKUREI_BLESS_EFFECT.get()))) return;

            if (GSKOWorldUtil.isEntityInBiome(player, GSKOBiomes.NAMELESS_HILL_KEY)){
                player.addPotionEffect(new EffectInstance(Effects.POISON, 2 * 20));
                player.sendStatusMessage(GSKOUtil.translateText(
                        "msg.", ".enter_danger_biome.nameless_hill"), true);
            }
        }
    }

    @SubscribeEvent
    public static void onEnterBambooForestOfLost(LivingEvent.LivingUpdateEvent event) {

        if (!(event.getEntityLiving() instanceof PlayerEntity)) return;
        if (!(event.getEntityLiving().world instanceof ServerWorld)) return;

        PlayerEntity player = (PlayerEntity) event.getEntityLiving();
        ServerWorld serverWorld = (ServerWorld) event.getEntityLiving().world;

        if (GSKOWorldUtil.isEntityInBiome(player, GSKOBiomes.BAMBOO_FOREST_LOST_KEY)){
            serverWorld.setDayTime(16000);
            // player.sendStatusMessage(GensokyoOntology.translate("msg.", ".enter_danger_biome.bamboo"), true);
        }
    }

    @SubscribeEvent
    public static void onRetreatLilywhite(TickEvent.PlayerTickEvent event) {
        if (event.player instanceof ServerPlayerEntity) {
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) event.player;
            ResourceLocation retreatLilywhite = new ResourceLocation(GensokyoOntology.MODID, "retreat_lilywhite");

            Advancement adv = GSKOUtil.getAdvancement(serverPlayer, retreatLilywhite);
            if (adv == null) return;
            AdvancementProgress progress = serverPlayer.getAdvancements().getProgress(adv);
            if (progress.isDone() && serverPlayer.ticksExisted % 15 == 0) {
                serverPlayer.addPotionEffect(new EffectInstance(Effects.HEALTH_BOOST, 2 * 20, 4));
                serverPlayer.heal(0.7f);
            }
        }
    }

    @SubscribeEvent
    public static void onLoadPlayerData(PlayerEvent.LoadFromFile event){

    }

    @SubscribeEvent
    public static void saveCustomPlayerData(PlayerEvent.SaveToFile event){
        GSKOPlayerData data = new GSKOPlayerData(event.getPlayer(), event.getPlayerDirectory());
    }

    @SubscribeEvent
    public static void onLivingEnterBiome(TickEvent.PlayerTickEvent event) {
        // GSKOWorldUtil.renderCustomSky(null);
        if (!(event.player.getEntityWorld() instanceof ServerWorld)) return;
        if (!(event.player instanceof ServerPlayerEntity)) return;

        ServerPlayerEntity player = (ServerPlayerEntity) event.player;
        ServerWorld serverWorld = (ServerWorld) event.player.world;

        if (!GSKOWorldUtil.eitherEntityInBiomes(player, BloodyMistCapability.ABNORMAL_BIOMES)) {
            GSKOWorldUtil.renderCustomSky(null);
            return;
        }
        boolean precondition = player.ticksExisted % 20 == 0 && !player.isPotionActive(EffectRegistry.HAKUREI_BLESS_EFFECT.get());
        serverWorld.getCapability(GSKOCapabilities.BLOODY_MIST).ifPresent((capability -> {

            if (!capability.isTriggered()){
                GSKONetworking.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new SScarletMistPacket(false));
                GSKOWorldUtil.renderCustomSky(null);
                return;
            }

            if (precondition) {
                player.sendStatusMessage(GSKOUtil.translateText(
                        "msg.", ".enter_danger_biome.scarlet_mansion_precincts"), true);
                player.attackEntityFrom(DamageSource.IN_WALL, 1f);
            }
            GSKONetworking.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new SScarletMistPacket(true));
            GSKOWorldUtil.renderCustomSky(new ScarletSkyRenderer());
        }));
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (event.getEntityLiving() == null) return;
        if (!(event.getEntityLiving().getEntityWorld() instanceof ServerWorld)) return;

        fairyDropDanmaku(event);
        dropLunarDanmaku(event.getEntityLiving(), (ServerWorld) event.getEntityLiving().world);

        if (event.getEntityLiving() instanceof PlayerEntity) {
            GSKOPowerCapability.INSTANCE.add(-1);
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();
            World world = player.getEntityWorld();
            PlayerInventory inventory = player.inventory;

            for (int i = 0; i < inventory.getSizeInventory(); i++) {
                if (inventory.getStackInSlot(i).getItem() == ItemRegistry.EXTEND_ITEM.get()) {
                    player.setHealth(20F);
                    world.setEntityState(player, (byte) 35);
                    inventory.getStackInSlot(i).shrink(1);
                    event.setCanceled(true);
                    break;
                }
            }
        }
    }

    @SubscribeEvent
    public static void playBGMToPlayer(TickEvent.PlayerTickEvent event) {
       if (MUSIC_TICKER.isBackgroundMusicPlaying(GSKOMusicSelector.MUSIC_BAMBOO_PARTRIDGE)){
           MUSIC_TICKER.selectRandomBackgroundMusic(GSKOMusicSelector.MUSIC_BAMBOO_PARTRIDGE);
       }
    }

    @SubscribeEvent
    public static void onPlayerInteractWithCoaster(PlayerInteractEvent.RightClickEmpty event){
        PlayerEntity player = event.getPlayer();
        IRayTracer.rayCast(event.getPlayer().world, event.getPlayer(), 10, Vector3d.ZERO).forEach(entity -> {
                if (!(entity instanceof CoasterVehicle)) return;
                CoasterVehicle coaster = (CoasterVehicle) entity;
                player.startRiding(coaster);
                GSKONetworking.CHANNEL.sendToServer(new CInteractCoasterPacket(
                        CInteractCoasterPacket.RIDING, coaster.getUniqueID()));
            });
    }

    @SubscribeEvent
    public static void onPlayerStopRidingCoaster(TickEvent.PlayerTickEvent event){
        PlayerEntity player = event.player;
        if (!(player.getLowestRidingEntity() instanceof CoasterVehicle)) return;
        if (Screen.hasShiftDown()) {
            player.stopRiding();
            GSKONetworking.CHANNEL.sendToServer(new CInteractCoasterPacket(
                    CInteractCoasterPacket.STOP_RIDING, player.getUniqueID()));
        }
    }

    private static void fairyDropDanmaku(LivingDeathEvent event) {
        if (event.getEntityLiving() instanceof FairyEntity) {
            FairyEntity fairy = (FairyEntity) event.getEntityLiving();
            Random random = new Random();
            if (event.getSource() == GSKODamageSource.DANMAKU) {
                if (random.nextInt(100) < 8) {
                    // fairy.entityDropItem(new ItemStack(random.nextInt() % 2 == 0 ? ItemRegistry.LIFE_FRAGMENT.get() : ItemRegistry.BOMB_FRAGMENT.get()));
                    fairy.entityDropItem(new ItemStack(ItemRegistry.LIFE_FRAGMENT.get()));
                }
                for (int i = 0; i < random.nextInt(3); i++) {
                    List<Item> danmakuItems = DanmakuUtil.getAllDanmakuItem();
                    fairy.entityDropItem(danmakuItems.get(random.nextInt(danmakuItems.size())));
                }
            }
        }
    }

    private static void dropLunarDanmaku(LivingEntity living, ServerWorld serverWorld) {
        if (GSKOWorldUtil.isEntityInDimension(living, GSKODimensions.GENSOKYO)) {
            LazyOptional<ImperishableNightCapability> capability = serverWorld.getCapability(GSKOCapabilities.IMPERISHABLE_NIGHT);
            capability.ifPresent(cap -> {
                Random random = new Random();
                if (random.nextInt(100) < 23) {
                    living.entityDropItem(new ItemStack(ItemRegistry.FAKE_LUNAR_ITEM.get()));
                }
            });
        }
    }

    private static void performHypnosis(LivingEvent.LivingUpdateEvent event, HypnosisEffect effect) {

        if (event.getEntityLiving() == null) return;

        if (event.getEntityLiving() instanceof ServerPlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();
            if (player.isPotionActive(effect)) {
                ServerWorld serverWorld = (ServerWorld) player.getEntityWorld();
                serverWorld.setDayTime(13000);
                player.setPose(Pose.SLEEPING);
                player.startSleeping(player.getPosition());
            }
        } else if (event.getEntityLiving() instanceof VillagerEntity) {
            VillagerEntity villager = (VillagerEntity) event.getEntityLiving();
            if (villager.isPotionActive(effect)) {
                ServerWorld serverWorld = (ServerWorld) villager.getEntityWorld();
                serverWorld.setDayTime(13000);
                villager.setPose(Pose.SLEEPING);
                villager.startSleeping(villager.getPosition());
            }
        }
    }

    @SubscribeEvent
    public static void resetDanmakuSize(EntityEvent.Size event) {
        if (!(event.getEntity() instanceof Danmaku)) return;
        Danmaku danmaku = (Danmaku) event.getEntity();
        if (danmaku.getItem().getItem() != ItemRegistry.FAKE_LUNAR_ITEM.get()) return;
        GSKOUtil.log("??? " + danmaku.getItem().getItem());
        event.setNewSize(new EntitySize(5,5, true), true);
    }

    private static void performLovePotion(LivingEvent.LivingUpdateEvent event, LovePotionEffect effect) {

    }
}
