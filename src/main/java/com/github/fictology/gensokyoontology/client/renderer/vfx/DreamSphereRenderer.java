package com.github.fictology.gensokyoontology.client.renderer.vfx;

import com.github.fictology.gensokyoontology.client.renderer.state.MagicSphereState;
import com.github.fictology.gensokyoontology.common.entiy.misc.DreamSphere;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class DreamSphereRenderer extends EntityRenderer<DreamSphere, MagicSphereState> {
    protected DreamSphereRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public MagicSphereState createRenderState() {
        return new MagicSphereState();
    }
}
