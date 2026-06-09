package com.github.fictology.gensokyoontology.client.renderer;

import com.github.fictology.gensokyoontology.util.api.IResourceGetter;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.world.entity.Entity;

import java.util.function.Supplier;

public abstract class ShaderedRenderer<E extends Entity, S extends EntityRenderState> extends EntityRenderer<E, S>
        implements SubmitNodeCollector.CustomGeometryRenderer {

    protected final RenderType renderType;
    protected ShaderedRenderer(EntityRendererProvider.Context context,RenderType renderType) {
        super(context);
        this.renderType = renderType;
    }

}
