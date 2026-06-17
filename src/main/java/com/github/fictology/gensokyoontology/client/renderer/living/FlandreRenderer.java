package com.github.fictology.gensokyoontology.client.renderer.living;

import com.github.fictology.gensokyoontology.client.model.FlandreScarletModel;
import com.github.fictology.gensokyoontology.client.renderer.state.FlandreState;
import com.github.fictology.gensokyoontology.common.entiy.monster.FlandreScarletEntity;
import com.github.fictology.gensokyoontology.util.GSKOUtil;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.Identifier;

public class FlandreRenderer extends HumanoidMobRenderer<FlandreScarletEntity, FlandreState, FlandreScarletModel> {
    public static final Identifier FLANDRE_TEXTURE = GSKOUtil.key("textures/entity/flandre_scarlet.png");
    public FlandreRenderer(EntityRendererProvider.Context context) {
        super(context, new FlandreScarletModel(context.bakeLayer(FlandreScarletModel.ID)), 0.8F);
    }

    @Override
    public Identifier getTextureLocation(FlandreState state) {
        return FLANDRE_TEXTURE;
    }

    @Override
    public FlandreState createRenderState() {
        return new FlandreState();
    }

}
