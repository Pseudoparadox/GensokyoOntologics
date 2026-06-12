package com.github.fictology.gensokyoontology.client;

import com.github.fictology.gensokyoontology.client.renderer.state.RenderingQueue;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.renderer.MappableRingBuffer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public final class RenderManager {

    // 不能直接将泛型通配符作为map的键或者值，但是可以作为键值包含的通配符类型
    private static final Map<RenderType, RenderingQueue> RENDERING_QUEUES = new HashMap<>();
    private static final Map<RenderType, MappableRingBuffer> BUFFER_MAP = new HashMap<>();
    private static final Map<RenderType, RenderPipeline> PIPELINE_MAP = new HashMap<>();

    public static void registerRenderingPass(RenderType renderType, RenderPipeline pipeline, MappableRingBuffer buffer){
        RENDERING_QUEUES.put(renderType, new RenderingQueue());
        PIPELINE_MAP.put(renderType, pipeline);
        BUFFER_MAP.put(renderType, buffer);
    }

    public static RenderingQueue getRenderingQueue(RenderType renderType){
        return RENDERING_QUEUES.get(renderType);
    }

    public static MappableRingBuffer getUniformBuffer(RenderType renderType){
        return BUFFER_MAP.get(renderType);
    }

    public static RenderPipeline getPipeline(RenderType renderType){
        return PIPELINE_MAP.get(renderType);
    }

    public static void renderOnEach(BiConsumer<RenderType, RenderingQueue> pass){
        RENDERING_QUEUES.forEach(pass);
    }
}
