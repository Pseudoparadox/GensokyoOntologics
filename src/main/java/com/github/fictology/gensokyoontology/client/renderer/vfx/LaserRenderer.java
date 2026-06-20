package com.github.fictology.gensokyoontology.client.renderer.vfx;

import com.github.fictology.gensokyoontology.client.renderer.ShaderedRenderer;
import com.github.fictology.gensokyoontology.client.renderer.state.SimpleState;
import com.github.fictology.gensokyoontology.common.entiy.misc.Laser;
import com.github.fictology.gensokyoontology.registry.RenderTypeRegistry;
import com.github.fictology.gensokyoontology.util.GSKOGeometry;
import com.github.fictology.gensokyoontology.util.GSKOMathUtil;
import com.github.fictology.gensokyoontology.util.GSKOUtil;
import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.Identifier;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4i;

public class LaserRenderer extends ShaderedRenderer<Laser, SimpleState<Laser>> {

    public static final Identifier LASER_BEAM_TEX = GSKOUtil.key("textures/entity/laser_beam_1.png");
    public static final RenderType LASER_BEAM = RenderTypes.entityTranslucent(LASER_BEAM_TEX);
    private static final RenderType TRIANGLE_TYPE = RenderTypes.dragonRays();
    private static final RenderType QUAD_TYPE = RenderTypes.lightning();

    public LaserRenderer(EntityRendererProvider.Context context) {
        super(context, QUAD_TYPE);
    }


    @Override
    public SimpleState<Laser> createRenderState() {
        return new SimpleState<>();
    }

    @Override
    public void extractRenderState(Laser entity, SimpleState<Laser> state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.entity = entity;
    }

    @Override
    public void submit(SimpleState<Laser> state, PoseStack matrixStackIn, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        super.submit(state, matrixStackIn, submitNodeCollector, camera);

        float length = state.entity.getRange();
        matrixStackIn.pushPose();
        GSKOMathUtil.rotateMatrixToLookVec(matrixStackIn, state.entity.getLookAngle().scale(-1));

        if (state.entity.tickCount <= state.entity.getPreparation()) {
            submitNodeCollector.submitCustomGeometry(matrixStackIn, RenderTypes.debugQuads(), (pose, buffer) -> {
                drawLaser(buffer, pose, length, 1.0f, 1.0F, 1.0F, 0.7F, 0.02f);
            });
            matrixStackIn.popPose();
            return;
        }
        matrixStackIn.scale(0.5F, 0.5F, 0.5F);
        submitNodeCollector.submitCustomGeometry(matrixStackIn, TRIANGLE_TYPE, (pose, buffer) -> {
            GSKOGeometry.buildSphereMesh(pose.pose(), buffer, 8, 8,
                    new Vector4i(255, 0, 0, 255));
        });
        matrixStackIn.scale(0.8F, 0.8F, 0.8F);
        submitNodeCollector.submitCustomGeometry(matrixStackIn, TRIANGLE_TYPE, (pose, buffer) -> {
            GSKOGeometry.buildSphereMesh(pose.pose(), buffer, 8, 8,
                    new Vector4i(255, 255, 255, 255));
        });
        matrixStackIn.scale(2.5F, 2.5F, 2.5F);

        submitNodeCollector.submitCustomGeometry(matrixStackIn, QUAD_TYPE, (pose, buffer) -> {
            drawLaser(buffer, pose, length, red(state.entity), green(state.entity), blue(state.entity), alpha(state.entity), 0.12f);
            drawLaser(buffer, pose, length, 1.0f, 1.0f, 1.0f, 1.0f, 0.08f);
        });

        matrixStackIn.popPose();
    }

    private void drawLaser(VertexConsumer builder, PoseStack.Pose pose,
                           float length, float r, float g, float b, float a,
                           float thickness) {

        // 8 个顶点
        Vector3f p0 = new Vector3f(-thickness, 0.0F, -thickness);
        Vector3f p1 = new Vector3f( thickness, 0.0F, -thickness);
        Vector3f p2 = new Vector3f( thickness, 0.0F,  thickness);
        Vector3f p3 = new Vector3f(-thickness, 0.0F,  thickness);

        Vector3f p4 = new Vector3f(-thickness, length, -thickness);
        Vector3f p5 = new Vector3f( thickness, length, -thickness);
        Vector3f p6 = new Vector3f( thickness, length,  thickness);
        Vector3f p7 = new Vector3f(-thickness, length,  thickness);

        // 每个面单独写法线（LIGHTNING 必须）
        quad(builder, pose, p0, p1, p2, p3, new Vector3f(0, 0, -1), r, g, b, a); // front
        quad(builder, pose, p5, p4, p7, p6, new Vector3f(0, 0,  1), r, g, b, a); // back
        quad(builder, pose, p4, p0, p3, p7, new Vector3f(-1, 0, 0), r, g, b, a); // left
        quad(builder, pose, p1, p5, p6, p2, new Vector3f( 1, 0, 0), r, g, b, a); // right
        quad(builder, pose, p3, p2, p6, p7, new Vector3f(0, 1, 0), r, g, b, a); // top
        quad(builder, pose, p0, p4, p5, p1, new Vector3f(0, -1, 0), r, g, b, a); // bottom
    }

    private void quad(VertexConsumer builder, PoseStack.Pose pose,
                      Vector3f v0, Vector3f v1, Vector3f v2, Vector3f v3,
                      Vector3f normal, float r, float g, float b, float a) {

        vertex(builder, pose, v0, normal, r, g, b, a);
        vertex(builder, pose, v1, normal, r, g, b, a);
        vertex(builder, pose, v2, normal, r, g, b, a);
        vertex(builder, pose, v3, normal, r, g, b, a);
    }

    private void vertex(VertexConsumer builder, PoseStack.Pose pose,
                        Vector3f pos, Vector3f normal,
                        float r, float g, float b, float a) {

        builder.addVertex(pose.pose(), pos.x(), pos.y(), pos.z())
                .setColor(r, g, b, a)
                .setNormal(pose, normal.x(), normal.y(), normal.z());
    }


    private float red(Laser entityIn) {
        return (float) entityIn.getRed() / 255;
    }

    private float green(Laser entityIn) {
        return (float) entityIn.getGreen() / 255;
    }

    private float blue(Laser entityIn) {
        return (float) entityIn.getBlue() / 255;
    }

    private float alpha(Laser entityIn) {
        return (float) entityIn.getAlpha() / 255;
    }

}
