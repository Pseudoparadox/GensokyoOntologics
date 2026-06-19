package com.github.fictology.gensokyoontology.client.renderer.living;

import com.github.fictology.gensokyoontology.client.model.RumiaModel;
import com.github.fictology.gensokyoontology.client.renderer.state.RumiaState;
import com.github.fictology.gensokyoontology.common.entiy.monster.RumiaEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;

public class RumiaRenderer extends MobRenderer<RumiaEntity, RumiaState, RumiaModel> {
    public RumiaRenderer(EntityRendererProvider.Context context, RumiaModel model, float shadow) {
        super(context, model, shadow);
    }

    @Override
    public Identifier getTextureLocation(RumiaState state) {
        return null;
    }

    @Override
    public RumiaState createRenderState() {
        return null;
    }
}
