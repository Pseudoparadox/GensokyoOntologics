package com.github.fictology.gensokyoontology.registry;

import com.github.fictology.gensokyoontology.util.GSKOUtil;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.platform.PolygonMode;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.neoforged.neoforge.client.stencil.StencilPerFaceTest;
import net.neoforged.neoforge.client.stencil.StencilTest;

/**
 * 新加入的Stencil特性：选区遮罩
 */
public final class PipelineRegistry {

    public static final VertexFormat GSKO_DEFAULT = VertexFormat.builder()
            .add("Position", VertexFormatElement.POSITION)
            .add("UV0", VertexFormatElement.UV0)
            .add("Normal", VertexFormatElement.NORMAL)
            .padding(1)
            .build();

    public static final RenderPipeline MASTER_SPARK = RenderPipeline.builder(RenderPipelines.MATRICES_PROJECTION_SNIPPET)
            .withCull(false)
            .withLocation(GSKOUtil.key("pipeline/master_spark"))
            .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR_NORMAL, VertexFormat.Mode.TRIANGLES)
            .withVertexShader("core/rendertype_lightning")
            .withFragmentShader("core/rendertype_lightning")
            .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT_PREMULTIPLIED_ALPHA))
            .withDepthStencilState(new DepthStencilState(CompareOp.LESS_THAN_OR_EQUAL, false))
            .withPolygonMode(PolygonMode.FILL)
            .build();

    public static final RenderPipeline DREAM_SPHERE = RenderPipeline.builder(RenderPipelines.MATRICES_PROJECTION_SNIPPET)
            .withCull(false)
            .withLocation(GSKOUtil.key("pipeline/dream_sphere"))
            .withVertexShader(GSKOUtil.key("dream_sphere"))
            .withFragmentShader(GSKOUtil.key("dream_sphere"))
            .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR_NORMAL, VertexFormat.Mode.TRIANGLES)
            .withDepthStencilState(DepthStencilState.DEFAULT)
            .withColorTargetState(new ColorTargetState(BlendFunction.LIGHTNING))
            .build();

    public static final RenderPipeline YINYANG = RenderPipeline.builder(RenderPipelines.MATRICES_PROJECTION_SNIPPET)
            .withCull(true)
            .withLocation(GSKOUtil.key("pipeline/yinyang"))
            .withVertexShader("core/position_color")
            .withFragmentShader("core/position_color")
            .withVertexFormat(DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.TRIANGLES)
            .withDepthStencilState(DepthStencilState.DEFAULT)
            .withColorTargetState(ColorTargetState.DEFAULT)
            .build();

    public static final RenderPipeline LASER = RenderPipeline.builder(RenderPipelines.MATRICES_PROJECTION_SNIPPET)
            .withCull(false)
            .withLocation(GSKOUtil.key("pipeline/laser"))
            .withVertexShader("core/position_color")
            .withFragmentShader("core/position_color")
            .withVertexFormat(DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS)
            .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
            .withDepthStencilState(new DepthStencilState(CompareOp.LESS_THAN_OR_EQUAL, false))
            .withPolygonMode(PolygonMode.FILL)
            .build();
}
