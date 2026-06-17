package com.github.fictology.gensokyoontology.client.renderer.vfx;

import com.github.fictology.gensokyoontology.client.renderer.state.SimpleState;
import com.github.fictology.gensokyoontology.common.entiy.misc.Laser;
import com.github.fictology.gensokyoontology.util.GSKOMathUtil;
import com.github.fictology.gensokyoontology.util.GSKOUtil;
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

public class LaserEntityRenderer extends EntityRenderer<Laser, SimpleState<Laser>> {

    public static final Identifier LASER_BEAM_TEX = GSKOUtil.key("textures/entity/laser_beam_1.png");
    public static final RenderType LASER_BEAM = RenderTypes.entityTranslucent(LASER_BEAM_TEX);

    protected LaserEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
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

        // TODO: 实现激光源头的贴图渲染
        float length = state.entity.getRange();
        matrixStackIn.pushPose();
        GSKOMathUtil.rotateMatrixToLookVec(matrixStackIn, state.entity.getLookAngle().scale(-1));

        if (state.entity.tickCount <= state.entity.getPreparation()) {
            submitNodeCollector.submitCustomGeometry(matrixStackIn, LASER_BEAM, (pose, buffer) -> {
                drawLaser(buffer, matrixStackIn, length, 1.0f, 1.0F, 1.0F, 0.7F, 0.02f);
            });
            matrixStackIn.popPose();
            return;
        }

        submitNodeCollector.submitCustomGeometry(matrixStackIn, LASER_BEAM, (pose, buffer) -> {
            drawLaser(buffer, matrixStackIn, length, red(state.entity), green(state.entity), blue(state.entity), alpha(state.entity), 0.08f);
            drawLaser(buffer, matrixStackIn, length, 1.0f, 1.0f, 1.0f, 1.0f, 0.03f);
        });

        matrixStackIn.popPose();
    }



    private static void drawSprite(VertexConsumer builder, Matrix4f matrix4f, TextureAtlasSprite sprite) {
        builder.addVertex(matrix4f, 0,0,1).setColor(255, 255, 255, 255).setUv(sprite.getU0(), sprite.getV0()).setUv1(0, 240);
        builder.addVertex(matrix4f, 1,0,1).setColor(255, 255, 255, 255).setUv(sprite.getU1(), sprite.getV0()).setUv1(0, 240);
        builder.addVertex(matrix4f, 1,1,1).setColor(255, 255, 255, 255).setUv(sprite.getU1(), sprite.getV1()).setUv1(0, 240);
        builder.addVertex(matrix4f, 0,1,1).setColor(255, 255, 255, 255).setUv(sprite.getU0(), sprite.getV1()).setUv1(0, 240);
    }

    private void drawLaser(VertexConsumer vertexBuilder, PoseStack matrixStack, float length, float red, float green, float blue, float alpha, float thickness) {

        // Front face
        vertex(matrixStack, vertexBuilder, -thickness, 0.0F, -thickness, red, green, blue, alpha);
        vertex(matrixStack, vertexBuilder, thickness, 0.0F,  -thickness, red, green, blue, alpha);
        vertex(matrixStack, vertexBuilder, thickness, 0.0F, thickness, red, green, blue, alpha);
        vertex(matrixStack, vertexBuilder, -thickness, 0.0F, thickness, red, green, blue, alpha);

        // Back face
        vertex(matrixStack, vertexBuilder, -thickness, length, thickness, red, green, blue, alpha);
        vertex(matrixStack, vertexBuilder, thickness, length, -thickness, red, green, blue, alpha);
        vertex(matrixStack, vertexBuilder, thickness, length, thickness, red, green, blue, alpha);
        vertex(matrixStack, vertexBuilder, -thickness, length, thickness, red, green, blue, alpha);


        // Left face
        vertex(matrixStack, vertexBuilder, -thickness, 0.0F, -thickness, red, green, blue, alpha);
        vertex(matrixStack, vertexBuilder, -thickness, length, -thickness, red, green, blue, alpha);
        vertex(matrixStack, vertexBuilder, -thickness, length, thickness, red, green, blue, alpha);
        vertex(matrixStack, vertexBuilder, -thickness, 0.0F, thickness, red, green, blue, alpha);

        // Right face
        vertex(matrixStack, vertexBuilder, thickness, 0.0F, -thickness, red, green, blue, alpha);
        vertex(matrixStack, vertexBuilder, thickness, length, -thickness, red, green, blue, alpha);
        vertex(matrixStack, vertexBuilder, thickness, length, thickness, red, green, blue, alpha);
        vertex(matrixStack, vertexBuilder, thickness, 0.0F, thickness, red, green, blue, alpha);

        // Top face
        vertex(matrixStack, vertexBuilder, -thickness, 0.0F, thickness, red, green, blue, alpha);
        vertex(matrixStack, vertexBuilder, thickness, 0.0F, thickness, red, green, blue, alpha);
        vertex(matrixStack, vertexBuilder, thickness, length, thickness, red, green, blue, alpha);
        vertex(matrixStack, vertexBuilder, -thickness, length, thickness, red, green, blue, alpha);

        // Bottom face
        vertex(matrixStack, vertexBuilder, -thickness, 0.0F, -thickness, red, green, blue, alpha);
        vertex(matrixStack, vertexBuilder, thickness, 0.0F, -thickness, red, green, blue, alpha);
        vertex(matrixStack, vertexBuilder, thickness, length, -thickness, red, green, blue, alpha);
        vertex(matrixStack, vertexBuilder, -thickness, length, -thickness, red, green, blue, alpha);

    }

    private void vertex(PoseStack matrixStack, VertexConsumer vertexBuilder, float x, float y, float z, float red, float green, float blue, float alpha) {
        vertexBuilder.addVertex(matrixStack.last().pose(), x, y, z)
                .setColor(red, green, blue, alpha);
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
