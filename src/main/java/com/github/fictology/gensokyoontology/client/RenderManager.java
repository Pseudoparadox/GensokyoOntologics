package com.github.fictology.gensokyoontology.client;

import com.github.fictology.gensokyoontology.client.renderer.state.RenderingQueue;
import com.github.fictology.gensokyoontology.util.api.render.IRenderingEntry;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.renderer.MappableRingBuffer;
import net.minecraft.client.renderer.rendertype.RenderType;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public final class RenderManager {

    // 不能直接将泛型通配符作为map的键或者值，但是可以作为键值包含的通配符类型
    private static final Map<RenderType, RenderingQueue> RENDERING_QUEUES = new HashMap<>();
    private static final Map<RenderType, IRenderingEntry> RENDERING_ENTRIES = new HashMap<>();
    private static final Map<RenderType, MappableRingBuffer> UBO_MAP = new HashMap<>();
    private static final Map<RenderType, MappableRingBuffer> VBO_MAP = new HashMap<>();
    private static final Map<RenderType, RenderPipeline> PIPELINE_MAP = new HashMap<>();

    public static void registerRenderingPass(RenderType renderType, RenderPipeline pipeline, IRenderingEntry entry, MappableRingBuffer uniformBuf){
        RENDERING_QUEUES.put(renderType, new RenderingQueue());
        RENDERING_ENTRIES.put(renderType, entry);
        PIPELINE_MAP.put(renderType, pipeline);
        UBO_MAP.put(renderType, uniformBuf);
        VBO_MAP.put(renderType, entry.getBufferedMesh(renderType.pipeline().getLocation().toString()));
    }

    public static IRenderingEntry getRenderEntry(RenderType renderType){
        return RENDERING_ENTRIES.get(renderType);
    }

    public static RenderingQueue getRenderingQueue(RenderType renderType){
        return RENDERING_QUEUES.get(renderType);
    }

    public static MappableRingBuffer getUniformBuffer(RenderType renderType){
        return UBO_MAP.get(renderType);
    }
    public static MappableRingBuffer getVertexBuffer(RenderType renderType){
        return VBO_MAP.get(renderType);
    }

    public static RenderPipeline getPipeline(RenderType renderType){
        return PIPELINE_MAP.get(renderType);
    }

    public static void renderOnEach(BiConsumer<RenderType, IRenderingEntry> pass){
        RENDERING_ENTRIES.forEach(pass);
    }
}
