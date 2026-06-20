package com.github.fictology.gensokyoontology.client.renderer.living;

import com.github.fictology.gensokyoontology.client.model.FlandreScarletModel;
import com.github.fictology.gensokyoontology.client.model.RumiaModel;
import com.github.fictology.gensokyoontology.client.renderer.state.RumiaState;
import com.github.fictology.gensokyoontology.common.entiy.monster.RumiaEntity;
import com.github.fictology.gensokyoontology.util.GSKOUtil;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;

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
}
