package com.github.fictology.gensokyoontology.client.renderer.vfx;

import com.github.fictology.gensokyoontology.client.RenderManager;
import com.github.fictology.gensokyoontology.client.renderer.ShaderedRenderer;
import com.github.fictology.gensokyoontology.client.renderer.state.MagicSphereState;
import com.github.fictology.gensokyoontology.common.entiy.misc.DreamSphere;
import com.github.fictology.gensokyoontology.util.GSKOGeometry;
import com.github.fictology.gensokyoontology.util.GSKOUtil;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import org.joml.Vector2f;

import java.nio.ByteBuffer;

public class DreamSphereRenderer extends ShaderedRenderer<DreamSphere, MagicSphereState> {

    public DreamSphereRenderer(EntityRendererProvider.Context context, RenderType renderType) {
        super(context, renderType);
    }

    @Override
    public MagicSphereState createRenderState() {
        return new MagicSphereState(GSKOGeometry.sphereMesh(18, 18, 2F));
    }

    @Override
    public void extractRenderState(DreamSphere entity, MagicSphereState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.entity = entity;
        state.offset = new Vector2f(state.partialTick + state.entity.tickCount, 0);
        state.tilling = new Vector2f(1F, 1F);
        state.cellDensity = 2f;
        state.mainColor = DreamSphere.INDEX_2_COLOR.get(entity.getIntSyncData(entity.getEntityData(), DreamSphere.DATA_INDEX));
    }

    @Override
    public void submit(MagicSphereState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        super.submit(state, poseStack, submitNodeCollector, camera);
        this.submitShader(state);
    }

    @Override
    protected void buildUniform(Std140Builder builder, MagicSphereState state) {
        builder.putVec4(GSKOUtil.wrapColor(state.mainColor))
                .putVec2(state.offset)
                .putVec2(state.tilling)
                .putFloat(state.cellDensity);
    }
}
