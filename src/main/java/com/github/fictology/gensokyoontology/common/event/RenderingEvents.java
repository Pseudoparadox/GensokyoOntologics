package com.github.fictology.gensokyoontology.common.event;

import com.github.fictology.gensokyoontology.GensokyoOntology;
import com.github.fictology.gensokyoontology.client.renderer.EmptyRenderer;
import com.github.fictology.gensokyoontology.client.renderer.NormalVectorRenderer;
import com.github.fictology.gensokyoontology.client.renderer.state.MagicSphereState;
import com.github.fictology.gensokyoontology.client.renderer.state.RenderingQueue;
import com.github.fictology.gensokyoontology.client.renderer.vfx.DreamSphereRenderer;
import com.github.fictology.gensokyoontology.client.renderer.vfx.MasterSparkRenderer;
import com.github.fictology.gensokyoontology.registry.EntityRegistry;
import com.github.fictology.gensokyoontology.registry.PipelineRegistry;
import com.github.fictology.gensokyoontology.registry.RenderTypeRegistry;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.Std140SizeCalculator;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
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
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.OptionalInt;

@EventBusSubscriber(modid = GensokyoOntology.MODID, value = Dist.CLIENT)
public class RenderingEvents {

    public static final MappableRingBuffer SPHERE_BUFFER = new MappableRingBuffer(
            () -> "SphereData",
            GpuBuffer.USAGE_UNIFORM | GpuBuffer.USAGE_MAP_WRITE,
            new Std140SizeCalculator()
                    .putVec4()
                    .putVec3()
                    .putVec2()
                    .putVec2()
                    .putFloat()
                    .putFloat()
                    .putFloat().get());

    // 不能直接将泛型通配符作为map的键或者值，但是可以作为键值自身的通配符类型
    private static final Map<Identifier, RenderingQueue> RENDERING_QUEUES = new HashMap<>();
    private static final Map<Identifier, MappableRingBuffer> BUFFER_MAP = new HashMap<>();
    static {
        registerRenderingPass(PipelineRegistry.DREAM_SPHERE.getLocation(), new MagicSphereState.Queue(), SPHERE_BUFFER);
    }

    public static <Q extends RenderingQueue> void registerRenderingPass(Identifier id, Q queue, MappableRingBuffer buffer){
        RENDERING_QUEUES.put(id, queue);
        BUFFER_MAP.put(id, buffer);
    }

    public static RenderingQueue getRenderingQueue(RenderType renderType){
        return RENDERING_QUEUES.get(renderType.pipeline().getLocation());
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

    @SubscribeEvent
    public static void onCreateRenderPass(RenderLevelStageEvent event){
        if (!(event instanceof RenderLevelStageEvent.AfterTranslucentParticles)) return;
        RENDERING_QUEUES.forEach((key, value) -> {
            var entries = value.takeSnapshot();
            var renderTarget = Minecraft.getInstance().getMainRenderTarget();
            if (entries.isEmpty()) return;
            try (var pass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(
                    key::toString,
                    renderTarget.getColorTextureView(),
                    OptionalInt.empty(),
                    renderTarget.getDepthTextureView(),
                    OptionalDouble.empty())) {

                pass.setPipeline(PipelineRegistry.DREAM_SPHERE);
                RenderSystem.bindDefaultUniforms(pass);
                for (var e : entries) {
                    BUFFER_MAP.get(key).rotate();
                    pass.setUniform(e.uniformName(), SPHERE_BUFFER.currentBuffer());
                    pass.setVertexBuffer(0, e.getVBO("Sphere Geometry"));
                    pass.draw(0, e.getVertexCount());
                }
            }
        });

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
