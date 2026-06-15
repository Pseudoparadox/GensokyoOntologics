package com.github.fictology.gensokyoontology.client.renderer.vfx;

import com.github.fictology.gensokyoontology.client.renderer.ObjVFXRenderer;
import com.github.fictology.gensokyoontology.client.renderer.state.SimpleState;
import com.github.fictology.gensokyoontology.common.entiy.misc.MasterSparkEntity;
import com.github.fictology.gensokyoontology.util.GSKOMathUtil;
import com.github.fictology.gensokyoontology.util.GSKOUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;
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
    public void submit(SimpleState<MasterSparkEntity> state, PoseStack poseStack, SubmitNodeCollector submitor, CameraRenderState camera) {
        poseStack.pushPose();

        var hue = GSKOMathUtil.wavyPeriod(state.partialTick, 0F, 1F);
        var color = Color.getHSBColor(hue, 1.0F, 1.0F);

        submitor.submitCustomGeometry(poseStack, this.renderType, (pose, vert) -> this.modelMap.get(MODEL_PATH)
                .render(pose, vert, new Vector4i(color.getRed(), color.getGreen(), color.getBlue(), 100)));
        poseStack.scale(0.8F, 0.8F, 0.76F);
        submitor.submitCustomGeometry(poseStack, this.renderType, (pose, vert) -> this.modelMap.get(MODEL_PATH)
                .render(pose, vert, new Vector4i(255, 255, 255, 255)));
        poseStack.popPose();
    }


    public void renderObjModel(PoseStack.Pose pose, VertexConsumer vertexConsumer, Vector4i color) {

    }

}
