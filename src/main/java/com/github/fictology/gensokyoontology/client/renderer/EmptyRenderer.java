package com.github.fictology.gensokyoontology.client.renderer;

import com.github.fictology.gensokyoontology.client.renderer.state.SimpleState;
import com.github.fictology.gensokyoontology.util.api.IResourceGetter;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;

public class EmptyRenderer<E extends Entity & IResourceGetter> extends EntityRenderer<E, SimpleState<E>> {
    public EmptyRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public SimpleState<E> createRenderState() {
        return new SimpleState<>();
    }
}
