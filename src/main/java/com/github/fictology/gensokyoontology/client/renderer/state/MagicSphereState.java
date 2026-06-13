package com.github.fictology.gensokyoontology.client.renderer.state;

import com.github.fictology.gensokyoontology.util.GSKOGeometry;
import com.github.fictology.gensokyoontology.util.api.render.IRenderingEntry;
import com.mojang.blaze3d.buffers.GpuBuffer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.world.entity.Entity;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4i;

import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicReference;

public class MagicSphereState extends EntityRenderState implements IRenderingEntry {
    public Entity entity;
    public Vector4i mainColor;
    public Vector4i coreColor;
    public Vector2f offset;
    public Vector2f tilling;
    public float cellDensity;
    private int latitudeBands;
    private int longitudeBands;
    private int radius;
    public RenderType renderType;
    private int vertCount;
    private GpuBuffer vertexBuffer;
    public @Nullable Matrix4f modelView = null;
    public MagicSphereState(){
    }

    public void buildMesh(RenderType renderType, int latitudeBands, int longitudeBands){
        this.vertexBuffer = GSKOGeometry.buildSphereMesh(renderType, new Matrix4f(), latitudeBands, longitudeBands, 1F);
        this.vertCount = latitudeBands * longitudeBands * 6; // 每个格子 6 个顶点
    }

    @Override
    public int getVertexCount() {
        return this.vertCount;
    }

    @Override
    public String uniformName() {
        return "SphereData";
    }

    @Override
    public GpuBuffer getVertexBuffer() {
        if (this.vertexBuffer == null) throw new NullPointerException("VertexBuffer not set yet, Please check.");
        return this.vertexBuffer;
    }

    @Override
    public void setModelView(Matrix4f matrix4f) {

    }

    @Override
    public boolean tryGetModelView(AtomicReference<Matrix4f> ref) {
        if (this.modelView == null) return false;
        ref.set(this.modelView);
        return true;
    }

    public void setVertexBuffer(GpuBuffer buffer){
        this.vertexBuffer = buffer;
    }

    @Override
    public void clear() {

    }


}
