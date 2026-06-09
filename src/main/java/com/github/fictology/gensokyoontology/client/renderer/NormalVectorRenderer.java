package com.github.fictology.gensokyoontology.client.renderer;

import com.github.fictology.gensokyoontology.client.renderer.state.SimpleState;
import com.github.fictology.gensokyoontology.common.entiy.misc.Danmaku;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;

public class NormalVectorRenderer extends EntityRenderer<Danmaku, SimpleState<Danmaku>> implements SubmitNodeCollector.CustomGeometryRenderer {

    public NormalVectorRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public SimpleState<Danmaku> createRenderState() {
        return new SimpleState<>();
    }

    @Override
    public void extractRenderState(Danmaku entity, SimpleState<Danmaku> state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.entity = entity;
    }

    @Override
    public void submit(SimpleState<Danmaku> state, PoseStack poseStack, SubmitNodeCollector node, CameraRenderState camera) {

        poseStack.pushPose();
        poseStack.translate(0, 1F, 0);

        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(state.partialTick, state.entity.yRotO, state.entity.getYRot()) - 90f));
        poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(state.partialTick, state.entity.xRotO, state.entity.getXRot()) - 90f));
        poseStack.mulPose(Axis.YP.rotationDegrees(90.f));

        poseStack.scale(state.entity.getScale(), state.entity.getScale(), state.entity.getScale());
        node.submitCustomGeometry(poseStack, RenderTypes.entityTranslucentEmissive(state.entity.getTexture()), this);
        poseStack.popPose();
    }

    @Override
    public void render(PoseStack.Pose pose, VertexConsumer buffer) {
        this.renderSingleSprite(pose, buffer);
    }
    public void renderSingleSprite(PoseStack.Pose pose, VertexConsumer buffer) {
        var matrix = pose.pose();
        var uMin = 0;   // 纹理左边界
        var uMax = 1;  // 纹理右边界
        var vMin = 0;
        float vMax = 1;
        this.vertex(matrix, buffer, -0.5f, -0.5f, 0, uMin, vMax);
        this.vertex(matrix, buffer, -0.5f,  0.5f, 0, uMin, vMin);
        this.vertex(matrix, buffer,  0.5f,  0.5f, 0, uMax, vMin);
        this.vertex(matrix, buffer,  0.5f, -0.5f, 0, uMax, vMax);

        this.vertex(matrix, buffer,  0.5f, -0.5f, 0, uMax, vMax);
        this.vertex(matrix, buffer,  0.5f,  0.5f, 0, uMax, vMin);
        this.vertex(matrix, buffer, -0.5f,  0.5f, 0, uMin, vMin);
        this.vertex(matrix, buffer, -0.5f, -0.5f, 0, uMin, vMax);
    }

    public void vertex(Matrix4f matrix, VertexConsumer vertex, float x, float y, float z, float u, float v) {
        vertex.addVertex(matrix, x, y, z)
                .setColor(255, 255, 255, 255) // 白色，无颜色变化
                .setUv(u, v)
                .setUv2(1, 1)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setNormal(0, 1, 0); // 法向量（不重要，因为是2D）
    }
}
