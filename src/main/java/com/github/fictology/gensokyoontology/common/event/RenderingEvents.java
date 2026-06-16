package com.github.fictology.gensokyoontology.common.event;

import com.github.fictology.gensokyoontology.GensokyoOntology;
import com.github.fictology.gensokyoontology.client.RenderManager;
import com.github.fictology.gensokyoontology.client.renderer.EmptyRenderer;
import com.github.fictology.gensokyoontology.client.renderer.NormalVectorRenderer;
import com.github.fictology.gensokyoontology.client.renderer.state.MagicSphereState;
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
import net.minecraft.client.resources.model.ModelDiscovery;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterRenderPipelinesEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.neoforged.neoforge.client.event.lifecycle.ClientStartedEvent;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

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
                () -> "SphereData",
                GpuBuffer.USAGE_UNIFORM | GpuBuffer.USAGE_MAP_WRITE,
                new Std140SizeCalculator()
                        .putVec2()
                        .putVec2().get());

        var state = new MagicSphereState();
        // state.buildMesh(RenderTypeRegistry.DREAM_SPHERE, 18, 18);

        RenderManager.registerRenderingPass(RenderTypeRegistry.DREAM_SPHERE, PipelineRegistry.DREAM_SPHERE,
                state, sphereBuf);
    }

    @SubscribeEvent
    public static void onCustomDrawCall(RenderLevelStageEvent.AfterTranslucentParticles event){
        var poseStack = event.getPoseStack();
        var renderTarget = Minecraft.getInstance().getMainRenderTarget();
        var projMatrix = RenderSystem.getProjectionMatrixBuffer();

//        RenderManager.renderOnEach((renderType, entry) -> {
//
//            var ubo = RenderManager.getUniformBuffer(renderType);
//            var vbo = RenderManager.getVertexBuffer(renderType);
//            var pipeline = RenderManager.getPipeline(renderType);
//
//            Matrix4fStack matrixStack = RenderSystem.getModelViewStack();
//            matrixStack.pushMatrix();
//            RenderManager.setModelView(renderType, matrixStack);
//
//            var transforms = RenderSystem.getDynamicUniforms().writeTransform(
//                    poseStack.last().pose(),              // <-- 实体 PoseStack pose
//                    new Vector4f(0f,0f,0f,1f),              // ColorModulator
//                    new Vector3f(),
//                    new Matrix4f());
//
//            ubo.rotate();
//            if (renderTarget.getColorTextureView() != null) {
//                try (var pass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(
//                        () -> renderType.pipeline().getLocation().toString(),
//                        renderTarget.getColorTextureView(), OptionalInt.empty(),
//                        renderTarget.getDepthTextureView(), OptionalDouble.empty())) {
//
//                    pass.setPipeline(pipeline);
//                    pass.setUniform("DynamicTransforms", transforms);
//                    RenderSystem.bindDefaultUniforms(pass);
//                    pass.setUniform(entry.uniformName(), ubo.currentBuffer());
//                    pass.setVertexBuffer(0, vbo);
//                    pass.draw(0, entry.getVertexCount());
//                }
//            }
//            matrixStack.popMatrix();
//        });

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
