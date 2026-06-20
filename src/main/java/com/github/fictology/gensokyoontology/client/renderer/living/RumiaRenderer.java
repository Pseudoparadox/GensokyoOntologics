package com.github.fictology.gensokyoontology.client.renderer.living;

import com.github.fictology.gensokyoontology.client.model.FlandreScarletModel;
import com.github.fictology.gensokyoontology.client.model.RumiaModel;
import com.github.fictology.gensokyoontology.client.renderer.state.RumiaState;
import com.github.fictology.gensokyoontology.common.entiy.monster.RumiaEntity;
import com.github.fictology.gensokyoontology.util.GSKOGeometry;
import com.github.fictology.gensokyoontology.util.GSKOUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.LightLayer;
import org.joml.Vector4f;
import org.joml.Vector4i;

public class RumiaRenderer extends MobRenderer<RumiaEntity, RumiaState, RumiaModel> {
    public static final Identifier TEXTURE = GSKOUtil.key("textures/entity/rumia_texture.png");
    public RumiaRenderer(EntityRendererProvider.Context context) {
        super(context, new RumiaModel(context.bakeLayer(RumiaModel.ID)), 0.8F);
    }

    @Override
    public Identifier getTextureLocation(RumiaState state) {
        return TEXTURE;
    }

    @Override
    public RumiaState createRenderState() {
        return new RumiaState();
    }

    @Override
    public void extractRenderState(RumiaEntity entity, RumiaState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.rumia = entity;
        state.light = entity.level().getRawBrightness(entity.blockPosition(), 0);
    }

    @Override
    public void submit(RumiaState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        super.submit(state, poseStack, submitNodeCollector, camera);
        submitNodeCollector.submitCustomGeometry(poseStack, RenderTypes.dragonRays(), ((pose, buffer) -> {
            GSKOGeometry.buildSphereMesh(pose.pose(), buffer, RenderTypes.dragonRays(), 12, 12,
                    new Vector4i(0,0,0,255));
        }));
    }
}
