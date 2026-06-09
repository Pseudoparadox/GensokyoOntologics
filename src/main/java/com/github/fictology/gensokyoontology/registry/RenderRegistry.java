package com.github.fictology.gensokyoontology.registry;

import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;

public final class RenderRegistry {
    public static final RenderType MASTER_SPARK = RenderType.create("master_spark", RenderSetup.builder(
            PipelineRegistry.OBJ).createRenderSetup());
}
