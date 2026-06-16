package com.github.fictology.gensokyoontology.client.renderer.vfx;

import com.github.fictology.gensokyoontology.client.renderer.ObjVFXRenderer;
import com.github.fictology.gensokyoontology.client.renderer.state.SimpleState;
import com.github.fictology.gensokyoontology.common.entiy.misc.YinyangJade;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;

public class YinyangJadeRenderer extends ObjVFXRenderer<YinyangJade, SimpleState<YinyangJade>> {

    public YinyangJadeRenderer(EntityRendererProvider.Context context, RenderType renderType, Identifier... objPaths) {
        super(context, renderType, objPaths);
    }

    @Override
    public SimpleState<YinyangJade> createRenderState() {
        return new SimpleState<>();
    }

    @Override
    public void extractRenderState(YinyangJade entity, SimpleState<YinyangJade> state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.entity = entity;
    }

    @Override
    public void submit(SimpleState<YinyangJade> state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        super.submit(state, poseStack, submitNodeCollector, camera);
        submitNodeCollector.submitCustomGeometry(poseStack, this.renderType, ((pose, buffer) -> {
            this.modelMap.get(state.entity.modelPath()).render(pose, buffer);
        }));
    }
}
