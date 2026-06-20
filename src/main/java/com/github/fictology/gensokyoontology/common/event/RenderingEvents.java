package com.github.fictology.gensokyoontology.common.event;

import com.github.fictology.gensokyoontology.GensokyoOntology;
import com.github.fictology.gensokyoontology.client.RenderManager;
import com.github.fictology.gensokyoontology.client.model.FlandreScarletModel;
import com.github.fictology.gensokyoontology.client.model.RumiaModel;
import com.github.fictology.gensokyoontology.client.renderer.EmptyRenderer;
import com.github.fictology.gensokyoontology.client.renderer.NormalVectorRenderer;
import com.github.fictology.gensokyoontology.client.renderer.living.FlandreRenderer;
import com.github.fictology.gensokyoontology.client.renderer.living.RumiaRenderer;
import com.github.fictology.gensokyoontology.client.renderer.state.MagicSphereState;
import com.github.fictology.gensokyoontology.client.renderer.vfx.DreamSphereRenderer;
import com.github.fictology.gensokyoontology.client.renderer.vfx.LaserRenderer;
import com.github.fictology.gensokyoontology.client.renderer.vfx.MasterSparkRenderer;
import com.github.fictology.gensokyoontology.client.renderer.vfx.YinyangJadeRenderer;
import com.github.fictology.gensokyoontology.common.item.touhou.YinyangJadeItem;
import com.github.fictology.gensokyoontology.registry.EntityRegistry;
import com.github.fictology.gensokyoontology.registry.PipelineRegistry;
import com.github.fictology.gensokyoontology.registry.RenderTypeRegistry;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.Std140SizeCalculator;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MappableRingBuffer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.NeoForgeRenderTypes;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterRenderPipelinesEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.neoforged.neoforge.client.event.lifecycle.ClientStartedEvent;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = GensokyoOntology.MODID, value = Dist.CLIENT)
public class RenderingEvents {

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        // 投掷物实体
        event.registerEntityRenderer(EntityRegistry.DANMAKU.get(), NormalVectorRenderer::new);
        event.registerEntityRenderer(EntityRegistry.YIN_YANG_JADE.get(), ctx ->
                new YinyangJadeRenderer(ctx, RenderTypes.dragonRays(), YinyangJadeItem.MODELS));

        // 特效类实体
        event.registerEntityRenderer(EntityRegistry.DREAM_SEAL.get(), EmptyRenderer::new);
        event.registerEntityRenderer(EntityRegistry.DREAM_SPHERE.get(), ctx ->
                new DreamSphereRenderer(ctx, RenderTypes.dragonRays()));
        event.registerEntityRenderer(EntityRegistry.MASTER_SPARK_ENTITY.get(), ctx ->
                new MasterSparkRenderer(ctx, RenderTypes.dragonRays()));
        event.registerEntityRenderer(EntityRegistry.LASER_SOURCE.get(), LaserRenderer::new);

        // 生物实体
        event.registerEntityRenderer(EntityRegistry.FLANDRE_SCARLET.get(), FlandreRenderer::new);
        event.registerEntityRenderer(EntityRegistry.RUMIA.get(), RumiaRenderer::new);
    }

    // @SubscribeEvent
    public static void registerPipelines(RegisterRenderPipelinesEvent event){
//        event.registerPipeline(PipelineRegistry.DREAM_SPHERE);
//        event.registerPipeline(PipelineRegistry.MASTER_SPARK);
//        event.registerPipeline(PipelineRegistry.YINYANG);
//        event.registerPipeline(PipelineRegistry.LASER);
    }

    @SubscribeEvent // on the mod event bus only on the physical client
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(FlandreScarletModel.ID, FlandreScarletModel::createLayer);
        event.registerLayerDefinition(RumiaModel.ID, RumiaModel::createBodyLayer);
    }

    /**
     * id code请查看GLFW类，id code 从 GLFW_KEY_0 开始
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
