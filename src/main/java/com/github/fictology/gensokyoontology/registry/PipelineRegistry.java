package com.github.fictology.gensokyoontology.registry;

import com.github.fictology.gensokyoontology.util.GSKOUtil;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.textures.TextureFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;

public final class PipelineRegistry {
    public static final VertexFormat OBJ_VERTEX_FORMAT = VertexFormat.builder()
            .add("Position", VertexFormatElement.POSITION)
            .add("Normal", VertexFormatElement.NORMAL)
            .add("UV", VertexFormatElement.UV0)
            .build();

    public static final RenderPipeline OBJ = RenderPipeline.builder()
            .withLocation(GSKOUtil.key("pipeline/obj_pipeline"))
            .withVertexShader(GSKOUtil.key("master_spark"))
            .withFragmentShader(GSKOUtil.key("master_spark"))
            .withVertexFormat(OBJ_VERTEX_FORMAT, VertexFormat.Mode.QUADS)
                .withSampler("Sampler0")
                .withUniform("ModelViewProj",UniformType.UNIFORM_BUFFER, TextureFormat.RGBA8)
                .withUniform("NormalMatrix", UniformType.UNIFORM_BUFFER, TextureFormat.RGBA8)
                .build();
}
