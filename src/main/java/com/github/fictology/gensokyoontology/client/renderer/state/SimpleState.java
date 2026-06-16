package com.github.fictology.gensokyoontology.client.renderer.state;

import com.github.fictology.gensokyoontology.api.render.ITextureGetter;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.Entity;

public class SimpleState<E extends Entity> extends EntityRenderState {
    public E entity;
    public float prevYRot;
    public float prevXRot;
    public float yRot;
    public float xRot;
}
