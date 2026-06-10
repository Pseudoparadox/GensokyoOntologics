package com.github.fictology.gensokyoontology.client.renderer.vfx;

import com.github.fictology.gensokyoontology.client.renderer.ShaderedRenderer;
import com.github.fictology.gensokyoontology.client.renderer.state.MagicSphereState;
import com.github.fictology.gensokyoontology.common.entiy.misc.DreamSphere;
import com.github.fictology.gensokyoontology.registry.PipelineRegistry;
import com.github.fictology.gensokyoontology.registry.RenderTypeRegistry;
import com.github.fictology.gensokyoontology.util.GSKOGeometry;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.buffers.Std140SizeCalculator;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MappableRingBuffer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import org.joml.Vector4i;

import java.util.OptionalInt;

public class DreamSphereRenderer extends ShaderedRenderer<DreamSphere, MagicSphereState> {

    private final MappableRingBuffer ubo = new MappableRingBuffer(
            () -> "SphereData",
            GpuBuffer.USAGE_UNIFORM | GpuBuffer.USAGE_MAP_WRITE,
            new Std140SizeCalculator()
                    .putVec4()
                    .putVec2()
                    .putVec2()
                    .putFloat().get());

    public DreamSphereRenderer(EntityRendererProvider.Context context, RenderType renderType) {
        super(context, renderType);
    }

    @Override
    public MagicSphereState createRenderState() {
        return new MagicSphereState();
    }

    @Override
    public void extractRenderState(DreamSphere entity, MagicSphereState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.latitudes = 16;
        state.longitudes = 16;
        state.size = (float) entity.getBoundingBox().getSize();
        state.mainColor = DreamSphere.INDEX_2_COLOR.get(entity.getIntSyncData(entity.getEntityData(), DreamSphere.DATA_INDEX));

    }

    @Override
    public void submit(MagicSphereState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        super.submit(state, poseStack, submitNodeCollector, camera);
    }

}
