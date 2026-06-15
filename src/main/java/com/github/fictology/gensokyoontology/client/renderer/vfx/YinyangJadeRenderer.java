package com.github.fictology.gensokyoontology.client.renderer.vfx;

import com.github.fictology.gensokyoontology.client.renderer.ObjVFXRenderer;
import com.github.fictology.gensokyoontology.client.renderer.state.SimpleState;
import com.github.fictology.gensokyoontology.common.entiy.misc.YinyangJade;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;

public class YinyangJadeRenderer extends ObjVFXRenderer<YinyangJade, SimpleState<YinyangJade>> {
    public YinyangJadeRenderer(EntityRendererProvider.Context context, RenderType renderType, Identifier... objPaths) {
        super(context, renderType, objPaths);
    }

    @Override
    public SimpleState<YinyangJade> createRenderState() {
        return new SimpleState<>();
    }
}
