package com.github.fictology.gensokyoontology.registry;

import com.github.fictology.gensokyoontology.util.GSKOUtil;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import net.neoforged.neoforge.client.stencil.StencilPerFaceTest;
import net.neoforged.neoforge.client.stencil.StencilTest;

public final class PipelineRegistry {

    public static final VertexFormat GSKO_DEFAULT = VertexFormat.builder()
            .add("Position", VertexFormatElement.POSITION)
            .add("UV0", VertexFormatElement.UV0)
            .add("Normal", VertexFormatElement.NORMAL)
            .padding(1)
            .build();

    public static final RenderPipeline MASTER_SPARK = RenderPipeline.builder()
            .withLocation(GSKOUtil.key("pipeline/master_spark"))
            .withVertexShader(GSKOUtil.key("master_spark"))
            .withFragmentShader(GSKOUtil.key("master_spark"))
            .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR_NORMAL, VertexFormat.Mode.TRIANGLES)
                .build();

    public static final RenderPipeline DREAM_SPHERE = RenderPipeline.builder()
            .withLocation(GSKOUtil.key("pipeline/dream_sphere"))
            .withVertexShader(GSKOUtil.key("dream_sphere"))
            .withFragmentShader(GSKOUtil.key("dream_sphere"))
            .withVertexFormat(GSKO_DEFAULT, VertexFormat.Mode.TRIANGLES)
            .withUniform("SphereData", UniformType.UNIFORM_BUFFER)
            .withDepthStencilState(new DepthStencilState(CompareOp.LESS_THAN_OR_EQUAL, false)) // 或 NO_DEPTH_TEST 如果你要纯overlay

            .withCull(false)         // 球体从里面也要看
            .withColorTargetState(ColorTargetState.DEFAULT)
            .build();
}
