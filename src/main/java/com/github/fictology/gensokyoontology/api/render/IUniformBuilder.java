package com.github.fictology.gensokyoontology.api.render;

import net.minecraft.client.renderer.DynamicUniformStorage;

public interface IUniformBuilder<E extends IRenderingEntry> extends DynamicUniformStorage.DynamicUniform {
    E getEntry();
}
