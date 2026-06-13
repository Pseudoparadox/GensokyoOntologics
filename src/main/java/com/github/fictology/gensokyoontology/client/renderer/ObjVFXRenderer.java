package com.github.fictology.gensokyoontology.client.renderer;

import com.github.fictology.gensokyoontology.client.model.ObjMesh;
import com.github.fictology.gensokyoontology.util.GSKOUtil;
import com.github.fictology.gensokyoontology.util.api.render.IRenderingEntry;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;

import java.util.HashMap;

public abstract class ObjVFXRenderer<E extends Entity, S extends EntityRenderState & IRenderingEntry> extends ShaderedRenderer<E, S> {
    protected final HashMap<Identifier, ObjMesh> modelMap = new HashMap<>();
    protected final RandomSource randomSource;
    public static final Identifier TRANSPARENT = GSKOUtil.key("textures/transparent.png");

    public ObjVFXRenderer(EntityRendererProvider.Context context, RenderType renderType, Identifier... objPaths) {
        super(context, renderType);
        for (Identifier objPath : objPaths) {
            this.modelMap.put(objPath, ObjMesh.load(objPath));
        }

        this.randomSource = RandomSource.create();
        this.randomSource.setSeed(10230);
    }

    @Override
    public void submit(S state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        super.submit(state, poseStack, submitNodeCollector, camera);
        this.modelMap.values().forEach(mesh ->
                submitNodeCollector.submitCustomGeometry(poseStack, this.renderType, mesh));
    }

}
