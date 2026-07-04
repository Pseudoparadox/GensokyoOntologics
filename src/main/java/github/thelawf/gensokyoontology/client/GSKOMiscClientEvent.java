package github.thelawf.gensokyoontology.client;

import com.github.tartaricacid.touhoulittlemaid.mclib.math.functions.limit.Min;
import com.mojang.blaze3d.systems.RenderSystem;
import github.thelawf.gensokyoontology.GensokyoOntology;
import github.thelawf.gensokyoontology.client.gui.GensokyoLoadingScreen;
import github.thelawf.gensokyoontology.client.gui.screen.skill.GoheiModeSelectScreen;
import github.thelawf.gensokyoontology.client.gui.screen.skill.KoishiEyeSwitchScreen;
import github.thelawf.gensokyoontology.client.model.KoishiHatModel;
import github.thelawf.gensokyoontology.client.renderer.entity.misc.CoasterRenderer;
import github.thelawf.gensokyoontology.client.renderer.world.ScarletSkyRenderer;
import github.thelawf.gensokyoontology.client.settings.GSKOKeyboardManager;
import github.thelawf.gensokyoontology.common.capability.GSKOCapabilities;
import github.thelawf.gensokyoontology.common.capability.entity.GSKOPowerCapability;
import github.thelawf.gensokyoontology.common.container.script.OneSlotContainer;
import github.thelawf.gensokyoontology.common.entity.misc.CoasterVehicle;
import github.thelawf.gensokyoontology.common.item.touhou.HakureiGohei;
import github.thelawf.gensokyoontology.common.item.touhou.KoishiEyeOpen;
import github.thelawf.gensokyoontology.common.network.GSKONetworking;
import github.thelawf.gensokyoontology.common.network.packet.CInteractCoasterPacket;
import github.thelawf.gensokyoontology.common.util.GSKOUtil;
import github.thelawf.gensokyoontology.common.util.world.GSKOWorldUtil;
import github.thelawf.gensokyoontology.common.world.GSKODimensions;
import github.thelawf.gensokyoontology.core.init.ItemRegistry;
import net.java.games.input.Keyboard;
import net.minecraft.client.GameSettings;
import net.minecraft.client.KeyboardListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.client.gui.screen.DownloadTerrainScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = GensokyoOntology.MODID, value = Dist.CLIENT)
public class GSKOMiscClientEvent {
    private static int TIMER = 0;
    private final Minecraft mc = Minecraft.getInstance();
    public static final ITextComponent GOHEI_TITLE = GSKOUtil.translateText("gui.", ".hakurei_gohei.title");


    @SubscribeEvent
    public static void onTerrainGUIOpen(GuiOpenEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (event.getGui() instanceof DownloadTerrainScreen && mc.player != null) {
            if (mc.player.getEntityWorld().getDimensionKey() == GSKODimensions.GENSOKYO) {
                GensokyoLoadingScreen guiLoading = new GensokyoLoadingScreen(NarratorChatListener.EMPTY);
                event.setGui(guiLoading);
            }
        }
    }

    @SubscribeEvent
    public static void onRenderTick(TickEvent.RenderTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            Minecraft minecraft = Minecraft.getInstance();

            // only fire if we're in the twilight forest
            if (minecraft.world != null && GSKODimensions.GENSOKYO.getRegistryName().equals(
                    minecraft.world.getDimensionKey().getLocation())) {
                if (minecraft.ingameGUI != null) {
                    minecraft.ingameGUI.prevVignetteBrightness = 0.0F;
                }
            }
        }
    }

    @SubscribeEvent
    public static void onRenderArmorLayer(RenderPlayerEvent.Post event) {
        KoishiHatModel model = new KoishiHatModel(1f);
        model.render(event.getMatrixStack(), event.getBuffers().getBuffer(RenderType.getArmorEntityGlint()),
                event.getLight(), event.getLight(), 1.f, 1.f, 1.f, 1.f);
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        TIMER++;

        Minecraft mc = Minecraft.getInstance();
        float partial = mc.getRenderPartialTicks();
        // DimensionRenderInfo info = DimensionRenderInfo.field_239208_a_.get(new ResourceLocation(GensokyoOntology.MODID, "render"));
    }


    // @SubscribeEvent
    @Deprecated
    public static void onSkyRendering(RenderWorldLastEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.world != null && mc.world.getServer() != null) {
            ServerWorld serverWorld = mc.world.getServer().getWorld(GSKODimensions.GENSOKYO);
            if (serverWorld != null) {
                serverWorld.getCapability(GSKOCapabilities.BLOODY_MIST).ifPresent(cap -> {
                    if (cap.isTriggered()) GSKOWorldUtil.renderCustomSky(new ScarletSkyRenderer());
                });
            }
        }
    }

    @SubscribeEvent
    public static void onBloodyMistRender(EntityViewRenderEvent.FogColors event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.world == null || mc.world.getServer() == null) return;
        ServerWorld serverWorld = mc.world.getServer().getWorld(GSKODimensions.GENSOKYO);

        if (serverWorld == null) return;
        serverWorld.getCapability(GSKOCapabilities.BLOODY_MIST).ifPresent(cap -> {
            if (!cap.isTriggered()) return;
            event.setRed(0.89F);
            event.setBlue(0.24F);
            event.setGreen(0.24F);
        });
    }


    /**
     * Copy from Touhou Little Maid: {@link com.github.tartaricacid.touhoulittlemaid.client.event.ShowPowerEvent ShowPowerEvent}
     *
     */
    @SubscribeEvent
    public static void onRenderPowerDisplay(RenderGameOverlayEvent event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) {
            ClientPlayerEntity player = Minecraft.getInstance().player;
            if (player == null) {
                return;
            }

            ItemStack stack = player.getHeldItemMainhand();
            if (stack.getItem() != ItemRegistry.HAKUREI_GOHEI.get()) {
                return;
            }

            ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
            FontRenderer fontRenderer = Minecraft.getInstance().fontRenderer;
            final ItemStack POWER_ITEM = ItemRegistry.POWER_ITEM.get().getDefaultInstance();

            itemRenderer.renderItemIntoGUI(POWER_ITEM, 5, 5);

            // FIXME: 在玩家第一次进入游戏时由于能力系统未创建导致渲染p点点数时闪退
            player.getCapability(GSKOCapabilities.POWER).ifPresent(cap -> {
                fontRenderer.drawString(event.getMatrixStack(), String.format("%s×%.2f", TextFormatting.BOLD,
                        GSKOPowerCapability.INSTANCE.getCount()), 20.0F, 10.0F, 16777215);
            });
            RenderSystem.enableBlend();
        }
    }


    /**
     * 在 {@link OneSlotContainer OneSlotContainer} 中取消了 E 键关闭容器的事件以便让玩家可以输入字母 E。<br>
     * Canceled the close screen event so that the player can input letter E in One Slot Container Screen<br>
     * <br>
     * 在 {@link github.thelawf.gensokyoontology.client.gui.screen.skill.ModeSwitchScreen MultiSelectScreen} 中判断玩家是否输入了技能切换热键 alt加向左箭头 <br>
     * Check whether player inputs skill-switching hot keys
     * @see org.lwjgl.glfw.GLFW
     * @see InputMappings
     */
    @SubscribeEvent
    public static void onGuiKeyDown(GuiScreenEvent.KeyboardKeyPressedEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        PlayerEntity player = minecraft.player;

        if (player != null && player.openContainer instanceof OneSlotContainer) {
            event.setCanceled(event.getKeyCode() == 69);
        }
    }

    @SubscribeEvent
    public static void onGuiKeyReleased(GuiScreenEvent.KeyboardKeyReleasedEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        PlayerEntity player = minecraft.player;

        if (player != null && minecraft.currentScreen instanceof GoheiModeSelectScreen) {
            minecraft.currentScreen.closeScreen();
        }
    }

    @SubscribeEvent
    public static void onSwitchHakureiGohei(InputEvent.KeyInputEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        PlayerEntity player = minecraft.player;

        // 检测如果alt被按下则打开御币的能力选择界面
        if (player == null || player.getHeldItemMainhand().getItem() != ItemRegistry.HAKUREI_GOHEI.get()) return;
        ItemStack stack = player.getHeldItemMainhand();
        if (!GSKOKeyboardManager.KEY_SWITCH_MODE.isKeyDown() || stack.getTag() == null) return;

        minecraft.displayGuiScreen(new GoheiModeSelectScreen(GOHEI_TITLE, HakureiGohei.getMode(stack.getTag())));
    }

    @SubscribeEvent
    public static void onSwitchKoishiEye(InputEvent.KeyInputEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        PlayerEntity player = minecraft.player;

        if (player != null && player.getHeldItemMainhand().getItem() == ItemRegistry.KOISHI_EYE_OPEN.get()) {
            ItemStack stack = player.getHeldItemMainhand();
            if (GSKOKeyboardManager.KEY_SWITCH_MODE.isKeyDown() && stack.getTag() != null) {
                minecraft.displayGuiScreen(new KoishiEyeSwitchScreen(KoishiEyeSwitchScreen.TITLE,
                        KoishiEyeOpen.getMode(stack.getTag())));
            }
        }
    }

    @SubscribeEvent
    public static void onHakureiGoheiModeScroll(GuiScreenEvent.MouseScrollEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        PlayerEntity player = minecraft.player;
        // alt+鼠标滚轮切换模式
        if (player == null || player.getHeldItem(Hand.MAIN_HAND).getItem() != ItemRegistry.HAKUREI_GOHEI.get()) return;
        if (!(minecraft.currentScreen instanceof GoheiModeSelectScreen)) return;

        GoheiModeSelectScreen screen = (GoheiModeSelectScreen) minecraft.currentScreen;
        if (player.world.isRemote) screen.switchMode((int) -event.getScrollDelta());
    }

    @SubscribeEvent
    public static void onCameraRotate(EntityViewRenderEvent.CameraSetup event) {
        Minecraft minecraft = Minecraft.getInstance();
        ClientPlayerEntity player = minecraft.player;
        if (player == null) return;
        if (player.getHeldItemMainhand().getItem() == ItemRegistry.ROT_ROLL_CLOCKWISE.get()) {
            float angleDeg = player.ticksExisted % 360F;
            event.setRoll(event.getRoll() + angleDeg);
        }
    }

    /**
     * 通过 Minecraft 实例获取游戏绑定热键设置
     * @see net.minecraft.client.GameSettings GameSettings: Minecraft 原版中设置的绑定热键
     */
    @SubscribeEvent
    public static void onPlayerDriveCoaster(InputEvent.KeyInputEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        PlayerEntity player = minecraft.player;
        if (player == null) return;
        Entity entity = player.getRidingEntity();

        if (!(entity instanceof CoasterVehicle)) return;
        CoasterVehicle vehicle = (CoasterVehicle) entity;

        if (vehicle.shouldMove()) return;
        if (minecraft.gameSettings.keyBindJump.isKeyDown()) {
            GSKONetworking.CHANNEL.sendToServer(new CInteractCoasterPacket(
                    CInteractCoasterPacket.DRIVING, vehicle.getUniqueID()));
        }
    }
}
