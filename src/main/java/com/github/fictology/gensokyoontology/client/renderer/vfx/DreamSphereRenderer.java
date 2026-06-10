package com.github.fictology.gensokyoontology.client.renderer.vfx;

import com.github.fictology.gensokyoontology.client.renderer.ShaderedRenderer;
import com.github.fictology.gensokyoontology.client.renderer.state.DreamSphereQueue;
import com.github.fictology.gensokyoontology.client.renderer.state.MagicSphereState;
import com.github.fictology.gensokyoontology.common.entiy.misc.DreamSphere;
import com.github.fictology.gensokyoontology.common.event.RenderingEvents;
import com.github.fictology.gensokyoontology.registry.RenderTypeRegistry;
import com.github.fictology.gensokyoontology.util.GSKOGeometry;
import com.github.fictology.gensokyoontology.util.GSKOUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector2f;

public class DreamSphereRenderer extends ShaderedRenderer<DreamSphere, MagicSphereState> {

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
        state.entity = entity;
        state.latitudes = 16;
        state.longitudes = 16;
        state.size = (float) entity.getBoundingBox().getSize();
        state.mainColor = DreamSphere.INDEX_2_COLOR.get(entity.getIntSyncData(entity.getEntityData(), DreamSphere.DATA_INDEX));

    }

    @Override
    public void submit(MagicSphereState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        super.submit(state, poseStack, submitNodeCollector, camera);

        var entry = new DreamSphereQueue.Entry();
        var mesh = GSKOGeometry.sphereMesh(18, 18, 2);

        entry.sphereColor = GSKOUtil.wrapColor(state.mainColor);
        entry.worldPos = new Vec3(state.x, state.y, state.z);
        entry.offset.x = state.partialTick + state.entity.tickCount;
        entry.tilling = new Vector2f(1F, 1F);

        entry.buffer = mesh.toByteBuffer();
        entry.vertexCount = mesh.vertexCount();

        var queue = RenderingEvents.getRenderingQueue(RenderTypeRegistry.DREAM_SPHERE);
        queue.add(entry);
    }

}
