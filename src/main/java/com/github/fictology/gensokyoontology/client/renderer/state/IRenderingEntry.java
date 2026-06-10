package com.github.fictology.gensokyoontology.client.renderer.state;

import com.mojang.blaze3d.buffers.GpuBuffer;

public interface IRenderingEntry {

    GpuBuffer getVBO(String label);
    int getVertexCount();
    String uniformName();
}
