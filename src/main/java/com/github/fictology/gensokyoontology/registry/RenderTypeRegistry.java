package com.github.fictology.gensokyoontology.registry;

import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;

public final class RenderTypeRegistry {
    public static final RenderType MASTER_SPARK = RenderType.create("master_spark", RenderSetup.builder(
            PipelineRegistry.MASTER_SPARK).createRenderSetup());
    public static final RenderType DREAM_SPHERE = RenderType.create("dream_sphere", RenderSetup.builder(
            PipelineRegistry.DREAM_SPHERE).createRenderSetup());
}
