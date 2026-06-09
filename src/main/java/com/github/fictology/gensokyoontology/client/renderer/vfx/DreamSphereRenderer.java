package com.github.fictology.gensokyoontology.client.renderer.vfx;

import com.github.fictology.gensokyoontology.client.renderer.ShaderedRenderer;
import com.github.fictology.gensokyoontology.client.renderer.state.MagicSphereState;
import com.github.fictology.gensokyoontology.common.entiy.misc.DreamSphere;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderType;

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
    }
}
