package com.github.fictology.gensokyoontology.client.renderer.state;

import com.github.fictology.gensokyoontology.api.render.IResourceGetter;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.Entity;

public class SimpleState<E extends Entity & IResourceGetter> extends EntityRenderState {
    public E entity;
}
