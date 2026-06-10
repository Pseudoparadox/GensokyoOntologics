package com.github.fictology.gensokyoontology.registry;

import com.github.fictology.gensokyoontology.util.GSKOUtil;
import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.textures.TextureFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;

public final class PipelineRegistry {
    public static final VertexFormat GSKO_DEFAULT = VertexFormat.builder()
            .add("Position", VertexFormatElement.POSITION)
            .add("UV", VertexFormatElement.UV0)
            .add("Normal", VertexFormatElement.NORMAL)
            .build();

    public static final RenderPipeline MASTER_SPARK = RenderPipeline.builder()
            .withLocation(GSKOUtil.key("pipeline/master_spark"))
            .withVertexShader(GSKOUtil.key("master_spark"))
            .withFragmentShader(GSKOUtil.key("master_spark"))
            .withVertexFormat(GSKO_DEFAULT, VertexFormat.Mode.TRIANGLES)
                .build();

    public static final RenderPipeline DREAM_SPHERE = RenderPipeline.builder()
            .withLocation(GSKOUtil.key("pipeline/dream_sphere"))
            .withVertexShader(GSKOUtil.key("dream_sphere"))
            .withFragmentShader(GSKOUtil.key("dream_sphere"))
            .withVertexFormat(GSKO_DEFAULT, VertexFormat.Mode.TRIANGLES)
            .withUniform("SphereData", UniformType.UNIFORM_BUFFER)
            .build();
}
