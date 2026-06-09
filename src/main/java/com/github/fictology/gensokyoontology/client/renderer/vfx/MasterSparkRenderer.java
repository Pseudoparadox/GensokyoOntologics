package com.github.fictology.gensokyoontology.client.renderer.vfx;

import com.github.fictology.gensokyoontology.client.renderer.ObjVFXRenderer;
import com.github.fictology.gensokyoontology.client.renderer.state.SimpleState;
import com.github.fictology.gensokyoontology.common.entiy.misc.MasterSparkEntity;
import com.github.fictology.gensokyoontology.util.GSKOUtil;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;

public class MasterSparkRenderer extends ObjVFXRenderer<MasterSparkEntity, SimpleState<MasterSparkEntity>> {
    private static final Identifier MODEL_PATH = GSKOUtil.key("models/entity/light_beam.obj");
    public MasterSparkRenderer(EntityRendererProvider.Context context, RenderType renderType) {
        super(context, renderType, MODEL_PATH);
    }

    @Override
    public SimpleState<MasterSparkEntity> createRenderState() {
        return new SimpleState<>();
    }

}
