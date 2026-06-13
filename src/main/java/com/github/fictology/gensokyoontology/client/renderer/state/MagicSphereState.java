package com.github.fictology.gensokyoontology.client.renderer.state;

import com.github.fictology.gensokyoontology.util.GSKOGeometry;
import com.github.fictology.gensokyoontology.util.api.render.IBufferedMesh;
import com.github.fictology.gensokyoontology.util.api.render.IRenderingEntry;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.Entity;
import org.joml.Vector2f;
import org.joml.Vector4i;

public class MagicSphereState extends EntityRenderState implements IRenderingEntry {
    public Entity entity;
    public Vector4i mainColor;
    public Vector4i coreColor;
    public Vector2f offset;
    public Vector2f tilling;
    public float cellDensity;
    private final int vertCount;
    private final GSKOGeometry.SphereMesh mesh;

    public MagicSphereState(GSKOGeometry.SphereMesh mesh){

        this.mesh = mesh;
        this.vertCount = mesh.vertexCount();
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
    public IBufferedMesh getBufferedMesh() {
        return this.mesh;
    }

    @Override
    public void clear() {

    }


}
