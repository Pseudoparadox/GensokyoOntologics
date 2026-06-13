package com.github.fictology.gensokyoontology.client.renderer;

import com.github.fictology.gensokyoontology.client.RenderManager;
import com.github.fictology.gensokyoontology.client.renderer.state.RenderingQueue;
import com.github.fictology.gensokyoontology.util.api.render.IRenderingEntry;
import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.entity.Entity;

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

    @Override
    public void submit(S state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        // super.submit(state, poseStack, submitNodeCollector, camera);
        this.submitMesh(state, poseStack);
        this.submitShader(state);
    }

    protected void submitShader(S state){
        var ubo = RenderManager.getUniformBuffer(this.renderType).currentBuffer();
        try(var view = RenderSystem.getDevice().createCommandEncoder().mapBuffer(ubo, false, true)){
            this.buildUniform(Std140Builder.intoBuffer(view.data()), state);
        }
    }

    protected abstract void buildUniform(Std140Builder builder, S state);
    protected void submitMesh(S state, PoseStack stack){
        state.setModelView(stack.last().pose());
        RenderManager.setVertexBuffer(this.renderType, state.getVertexBuffer());
    }
}
