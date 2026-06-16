package com.github.fictology.gensokyoontology.client;

import com.github.fictology.gensokyoontology.client.renderer.state.RenderingQueue;
import com.github.fictology.gensokyoontology.api.render.IRenderingEntry;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.renderer.MappableRingBuffer;
import net.minecraft.client.renderer.rendertype.RenderType;
import org.joml.Matrix4f;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public final class RenderManager {

    // 不能直接将泛型通配符作为map的键或者值，但是可以作为键值包含的通配符类型
    private static final Map<RenderType, RenderingQueue> RENDERING_QUEUES = new HashMap<>();
    private static final Map<RenderType, IRenderingEntry> RENDERING_ENTRIES = new HashMap<>();
    private static final Map<RenderType, MappableRingBuffer> UBO_MAP = new HashMap<>();
    private static final Map<RenderType, Matrix4f> VIEW_MAP = new HashMap<>();
    private static final Map<RenderType, RenderPipeline> PIPELINE_MAP = new HashMap<>();

    public static void registerRenderingPass(RenderType renderType, RenderPipeline pipeline, IRenderingEntry entry, MappableRingBuffer uniformBuf){
        RENDERING_QUEUES.put(renderType, new RenderingQueue());
        RENDERING_ENTRIES.put(renderType, entry);
        PIPELINE_MAP.put(renderType, pipeline);
        UBO_MAP.put(renderType, uniformBuf);
        VIEW_MAP.put(renderType, new Matrix4f());
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
    public static void setVertexBuffer(RenderType renderType, GpuBuffer buffer){
        RENDERING_ENTRIES.get(renderType).setVertexBuffer(buffer);
    }
    public static GpuBuffer getVertexBuffer(RenderType renderType){
        return RENDERING_ENTRIES.get(renderType).getVertexBuffer();
    }

    public static void setModelView(RenderType renderType, Matrix4f matrix4f){
        VIEW_MAP.get(renderType).set(matrix4f);
    }
    public static Matrix4f getModelView(RenderType renderType){
        return VIEW_MAP.get(renderType);
    }

    public static RenderPipeline getPipeline(RenderType renderType){
        return PIPELINE_MAP.get(renderType);
    }

    public static void renderOnEach(BiConsumer<RenderType, IRenderingEntry> pass){
        RENDERING_ENTRIES.forEach(pass);
    }
}
