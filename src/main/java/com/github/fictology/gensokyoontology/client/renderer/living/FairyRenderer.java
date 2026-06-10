package com.github.fictology.gensokyoontology.client.renderer.living;

import com.github.fictology.gensokyoontology.client.model.FairyModel;
import com.github.fictology.gensokyoontology.client.renderer.state.FairyRenderState;
import com.github.fictology.gensokyoontology.common.entiy.monster.FairyEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;

public class FairyRenderer extends MobRenderer<FairyEntity, FairyRenderState, FairyModel> {

    public FairyRenderer(EntityRendererProvider.Context context, FairyModel model, float size) {
        super(context, model, size);
    }

    @Override
    public boolean shouldRender(FairyEntity entity, Frustum culler, double camX, double camY, double camZ) {
        return super.shouldRender(entity, culler, camX, camY, camZ);
    }

    @Override
    public void submit(FairyRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        super.submit(state, poseStack, submitNodeCollector, camera);
    }

    @Override
    public FairyRenderState createRenderState() {
        return null;
    }

    @Override
    public Identifier getTextureLocation(FairyRenderState state) {
        return null;
    }
}
