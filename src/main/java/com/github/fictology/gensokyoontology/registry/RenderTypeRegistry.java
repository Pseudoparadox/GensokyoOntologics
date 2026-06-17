package com.github.fictology.gensokyoontology.registry;

import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.LayeringTransform;
import net.minecraft.client.renderer.rendertype.OutputTarget;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;

public final class RenderTypeRegistry {
    public static final RenderType MASTER_SPARK = RenderType.create("master_spark",
            RenderSetup.builder(PipelineRegistry.MASTER_SPARK)
                    .setOutputTarget(OutputTarget.MAIN_TARGET).createRenderSetup());

    public static final RenderType YINYANG = RenderType.create("yinyang",
            RenderSetup.builder(PipelineRegistry.YINYANG)
                    .setOutputTarget(OutputTarget.MAIN_TARGET).createRenderSetup());

    public static final RenderType DREAM_SPHERE = RenderType.create("dream_sphere",
            RenderSetup.builder(PipelineRegistry.DREAM_SPHERE)
                    .setOutputTarget(OutputTarget.MAIN_TARGET).createRenderSetup());

}
