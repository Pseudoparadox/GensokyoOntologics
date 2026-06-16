package com.github.fictology.gensokyoontology.api.render;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.vertex.MeshData;
import org.joml.Matrix4f;

import java.util.concurrent.atomic.AtomicReference;

public interface IRenderingEntry {

    int getVertexCount();
    String uniformName();
    void setVertexBuffer(GpuBuffer buffer);
    GpuBuffer getVertexBuffer();
    void setModelView(Matrix4f matrix4f);
    default MeshData getMesh(){
        return null;
    }
    boolean tryGetModelView(AtomicReference<Matrix4f> ref);
    void clear();
}
