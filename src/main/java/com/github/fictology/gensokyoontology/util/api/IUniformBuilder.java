package com.github.fictology.gensokyoontology.util.api;

import net.minecraft.client.renderer.DynamicUniformStorage;

public interface IUniformBuilder<E extends IRenderingEntry> extends DynamicUniformStorage.DynamicUniform {
    E getEntry();
}
