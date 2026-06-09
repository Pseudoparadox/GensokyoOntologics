package com.github.fictology.gensokyoontology.client.renderer.vfx;

import com.github.fictology.gensokyoontology.client.renderer.ShaderedRenderer;
import com.github.fictology.gensokyoontology.client.renderer.state.MagicSphereState;
import com.github.fictology.gensokyoontology.common.entiy.misc.DreamSphere;
import com.github.fictology.gensokyoontology.registry.RenderTypeRegistry;
import com.github.fictology.gensokyoontology.util.GSKOGeometry;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import org.joml.Vector4i;

public class DreamSphereRenderer extends ShaderedRenderer<DreamSphere, MagicSphereState> {

    protected DreamSphereRenderer(EntityRendererProvider.Context context, RenderType renderType) {
        super(context, renderType);
    }

    @Override
    public MagicSphereState createRenderState() {
        return new MagicSphereState();
    }

    @Override
    public void extractRenderState(DreamSphere entity, MagicSphereState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.latitudes = 16;
        state.longitudes = 16;
        state.size = (float) entity.getBoundingBox().getSize();
    }

    @Override
    public void submit(MagicSphereState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        super.submit(state, poseStack, submitNodeCollector, camera);
        submitNodeCollector.submitCustomGeometry(poseStack, RenderTypeRegistry.MASTER_SPARK, (pose, vert) ->
                this.renderMeshByState(pose, vert, state));
    }

    public void renderMeshByState(PoseStack.Pose pose, VertexConsumer vertexConsumer, MagicSphereState state) {
        GSKOGeometry.renderSphere(vertexConsumer, pose.pose(), state.latitudes, state.longitudes, state.size,
                new Vector4i(1,1,1,1));
    }

    @Override
    public void render(PoseStack.Pose pose, VertexConsumer vertexConsumer) {

    }
}
