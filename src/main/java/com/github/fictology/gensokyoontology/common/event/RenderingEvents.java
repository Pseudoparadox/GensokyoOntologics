package com.github.fictology.gensokyoontology.common.event;

import com.github.fictology.gensokyoontology.GensokyoOntology;
import com.github.fictology.gensokyoontology.client.RenderManager;
import com.github.fictology.gensokyoontology.client.renderer.EmptyRenderer;
import com.github.fictology.gensokyoontology.client.renderer.NormalVectorRenderer;
import com.github.fictology.gensokyoontology.client.renderer.state.DreamSphereEntry;
import com.github.fictology.gensokyoontology.client.renderer.state.RenderingQueue;
import com.github.fictology.gensokyoontology.client.renderer.vfx.DreamSphereRenderer;
import com.github.fictology.gensokyoontology.client.renderer.vfx.MasterSparkRenderer;
import com.github.fictology.gensokyoontology.registry.EntityRegistry;
import com.github.fictology.gensokyoontology.registry.PipelineRegistry;
import com.github.fictology.gensokyoontology.registry.RenderTypeRegistry;
import com.github.fictology.gensokyoontology.util.api.IUniformBuilder;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.Std140SizeCalculator;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.DynamicUniformStorage;
import net.minecraft.client.renderer.DynamicUniforms;
import net.minecraft.client.renderer.MappableRingBuffer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterRenderPipelinesEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.neoforged.neoforge.client.event.lifecycle.ClientStartedEvent;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.OptionalInt;

@EventBusSubscriber(modid = GensokyoOntology.MODID, value = Dist.CLIENT)
public class RenderingEvents {
    @SubscribeEvent
    public static void onClientStart(ClientStartedEvent event){
        var sphereBuf = new MappableRingBuffer(
                () -> "SphereData",
                GpuBuffer.USAGE_UNIFORM | GpuBuffer.USAGE_MAP_WRITE,
                new Std140SizeCalculator()
                        .putVec4()
                        .putVec2()
                        .putVec2()
                        .putFloat().get());

        var sparkBuf = new MappableRingBuffer(
                () -> "SparkData",
                GpuBuffer.USAGE_UNIFORM | GpuBuffer.USAGE_MAP_WRITE,
                new Std140SizeCalculator()
                        .putVec2()
                        .putVec2()
                        .putFloat().get());

        RenderManager.registerRenderingPass(RenderTypeRegistry.DREAM_SPHERE, PipelineRegistry.DREAM_SPHERE, sphereBuf);
        RenderManager.registerRenderingPass(RenderTypeRegistry.MASTER_SPARK, PipelineRegistry.MASTER_SPARK, sparkBuf);
    }

    @SubscribeEvent
    public static void onCustomDrawCall(RenderLevelStageEvent.AfterTranslucentParticles event){

        var renderTarget = Minecraft.getInstance().getMainRenderTarget();

        RenderManager.renderOnEach((renderType, queue) -> {
            var entries = queue.takeSnapshot();
            if (entries.isEmpty()) return;

            var entry = entries.get(0);
            var buf = RenderManager.getUniformBuffer(renderType);
            var vbo = entry.getVBO("Geo");

            try (var pass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(
                    () -> renderType.pipeline().getLocation().toString(),
                    renderTarget.getColorTextureView(), OptionalInt.empty(),
                    renderTarget.getDepthTextureView(), OptionalDouble.empty())) {

                pass.setPipeline(RenderManager.getPipeline(renderType));
                pass.setUniform(entry.uniformName(), buf.currentBuffer());
                pass.setVertexBuffer(0, vbo);
                pass.draw(0, entry.getVertexCount());
                RenderSystem.bindDefaultUniforms(pass);
            }
            queue.clear();
        });

    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityRegistry.DANMAKU.get(), NormalVectorRenderer::new);
        event.registerEntityRenderer(EntityRegistry.MASTER_SPARK_ENTITY.get(), ctx ->
                new MasterSparkRenderer(ctx, RenderTypeRegistry.MASTER_SPARK));

        event.registerEntityRenderer(EntityRegistry.DREAM_SEAL.get(), EmptyRenderer::new);
        event.registerEntityRenderer(EntityRegistry.DREAM_SPHERE.get(), ctx ->
                new DreamSphereRenderer(ctx, RenderTypeRegistry.DREAM_SPHERE));
    }

    @SubscribeEvent
    public static void registerPipelines(RegisterRenderPipelinesEvent event){
        event.registerPipeline(PipelineRegistry.MASTER_SPARK);
        event.registerPipeline(PipelineRegistry.DREAM_SPHERE);
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
