package com.github.fictology.gensokyoontology.client.renderer;

import com.github.fictology.gensokyoontology.client.model.ObjMesh;
import com.github.fictology.gensokyoontology.util.GSKOUtil;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import org.jspecify.annotations.NonNull;

import java.util.HashMap;

public abstract class ObjVFXRenderer<E extends Entity, S extends EntityRenderState> extends EntityRenderer<E, S> {
    protected final HashMap<Identifier, ObjMesh> modelMap = new HashMap<>();
    protected final RandomSource randomSource;
    protected final RenderType renderType;
    public static final Identifier TRANSPARENT = GSKOUtil.key("textures/transparent.png");

    public ObjVFXRenderer(EntityRendererProvider.Context context, RenderType renderType, Identifier... objPaths) {
        super(context);
        this.renderType = renderType;
        for (Identifier objPath : objPaths) {
            this.modelMap.put(objPath, ObjMesh.load(objPath));
        }

        this.randomSource = RandomSource.create();
        this.randomSource.setSeed(10230);
    }

    @Override
    protected boolean affectedByCulling(@NonNull E entity) {
        return true;
    }
}
