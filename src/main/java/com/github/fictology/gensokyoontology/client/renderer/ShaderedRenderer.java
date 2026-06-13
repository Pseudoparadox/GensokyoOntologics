package com.github.fictology.gensokyoontology.client.renderer;

import com.github.fictology.gensokyoontology.client.RenderManager;
import com.github.fictology.gensokyoontology.client.renderer.state.RenderingQueue;
import com.github.fictology.gensokyoontology.util.api.render.IRenderingEntry;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.world.entity.Entity;

import java.nio.ByteBuffer;

public abstract class ShaderedRenderer<E extends Entity, S extends EntityRenderState & IRenderingEntry> extends EntityRenderer<E, S>
        implements SubmitNodeCollector.CustomGeometryRenderer {

    protected final RenderingQueue queue;
    protected final RenderType renderType;
    protected ShaderedRenderer(EntityRendererProvider.Context context, RenderType renderType) {
        super(context);
        this.renderType = renderType;
        this.queue = RenderManager.getRenderingQueue(this.renderType);

    }

    @Override
    public void render(PoseStack.Pose pose, VertexConsumer vertexConsumer) {

    }

    protected void submitShader(S state){
        var ubo = RenderManager.getUniformBuffer(this.renderType).currentBuffer();
        try(var view = RenderSystem.getDevice().createCommandEncoder().mapBuffer(ubo, false, true)){
            this.buildUniform(Std140Builder.intoBuffer(view.data()), state);
            this.buildVertex(view, state.getBufferedMesh(this.renderType.pipeline().getLocation().toString()).toByteBuffer());
        }
    }

    protected abstract void buildUniform(Std140Builder builder, S state);
    protected void buildVertex(GpuBuffer.MappedView view, ByteBuffer vertexBuffer){
        view.data().put(vertexBuffer);
    }
}
