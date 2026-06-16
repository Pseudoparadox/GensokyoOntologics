package com.github.fictology.gensokyoontology.client.renderer.vfx;

import com.github.fictology.gensokyoontology.client.renderer.ObjVFXRenderer;
import com.github.fictology.gensokyoontology.client.renderer.state.SimpleState;
import com.github.fictology.gensokyoontology.common.entiy.misc.MasterSparkEntity;
import com.github.fictology.gensokyoontology.util.GSKOMathUtil;
import com.github.fictology.gensokyoontology.util.GSKOUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import org.joml.Vector4i;

import java.awt.*;

public class MasterSparkRenderer extends ObjVFXRenderer<MasterSparkEntity, SimpleState<MasterSparkEntity>> {
    private static final Identifier MODEL_PATH = GSKOUtil.key("models/entity/beam.obj");
    public MasterSparkRenderer(EntityRendererProvider.Context context, RenderType renderType) {
        super(context, renderType, MODEL_PATH);
    }

    @Override
    public SimpleState<MasterSparkEntity> createRenderState() {
        return new SimpleState<>();
    }

    @Override
    public void extractRenderState(MasterSparkEntity entity, SimpleState<MasterSparkEntity> state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.entity = entity;
        state.xRot = entity.getXRot();
        state.yRot = entity.getYRot();
        state.prevXRot = entity.xRotO;
        state.prevYRot = entity.yRotO;
        state.partialTick = partialTicks;
    }

    @Override
    public void submit(SimpleState<MasterSparkEntity> state, PoseStack poseStack, SubmitNodeCollector submitor, CameraRenderState camera) {
        poseStack.pushPose();
        var hue = 3 * GSKOMathUtil.triangularPeriod(state.entity.tickCount, 0, 360) / 360f;
        var color = Color.getHSBColor(hue, 1.0F, 1.0F);

        float lerpedYRot = Mth.rotLerp(state.partialTick, state.prevXRot, state.yRot);
        float lerpedXRot = Mth.rotLerp(state.partialTick, state.prevXRot, state.xRot);

        poseStack.mulPose(Axis.YP.rotationDegrees(lerpedYRot));
        poseStack.mulPose(Axis.XP.rotationDegrees(lerpedXRot));

        poseStack.translate(0, 20, 0);
        poseStack.scale(2, 5, 2);

        submitor.submitCustomGeometry(poseStack, this.renderType, (pose, vert) -> this.modelMap.get(MODEL_PATH)
                .render(pose, vert, new Vector4i(color.getRed(), color.getGreen(), color.getBlue(), 255)));

        poseStack.pushPose();
        poseStack.scale(0.5F, 1, 0.5F);
        submitor.submitCustomGeometry(poseStack, this.renderType, (pose, vert) -> this.modelMap.get(MODEL_PATH)
                .render(pose, vert, new Vector4i(255, 255, 255, 255)));

        poseStack.popPose();
        poseStack.popPose();
    }
}
