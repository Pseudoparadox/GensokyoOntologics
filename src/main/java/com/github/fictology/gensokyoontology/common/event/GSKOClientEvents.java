package com.github.fictology.gensokyoontology.common.event;

import com.github.fictology.gensokyoontology.GensokyoOntology;
import com.github.fictology.gensokyoontology.client.renderer.NormalVectorRenderer;
import com.github.fictology.gensokyoontology.registry.EntityRegistry;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = GensokyoOntology.MODID, value = Dist.CLIENT)
public class GSKOClientEvents {

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityRegistry.DANMAKU.get(), NormalVectorRenderer::new);
    }

    /**
     * key code请查看GLFW类，key code 从 GLFW_KEY_0 开始
     * @see GLFW#GLFW_KEY_0
     */
    public static void onScarletMistRendered(ViewportEvent.ComputeFogColor event){
//        var mc = Minecraft.getInstance();
//        var player = mc.player;
//        var screen = mc.screen;
//        if (player == null) return;
//        var identity = player.getCapability(GSKOCapabilities.ENTITY_IDENTITY);
//        if (identity == null) return;
//        if (screen == null) return;
//        if (screen.keyPressed(GLFW.GLFW_KEY_L)){
//            event.setRed(0.8f);
//        }
//         if (identity.incidents.stream().anyMatch(entry ->
//                 entry == NamedEntry.SCARLET_MIST & entry.get("isTriggered") == Boolean.TRUE)) {
//             event.setRed(0.8f);
//         }
//         else {
//             event.setRed(0);
//         }

    }

    public static void onCameraTilt(ViewportEvent.ComputeCameraAngles event){
        var mc = Minecraft.getInstance();
        var camera = mc.getCameraEntity();
        // var displayEntity = new Display.BlockDisplay(EntityType.BLOCK_DISPLAY, mc.level);
        // var cameraMarker = new Marker(EntityType.MARKER, Minecraft.getInstance().level);

    }
}
