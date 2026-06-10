package com.github.fictology.gensokyoontology.client.renderer.queue;

import com.mojang.blaze3d.buffers.GpuBuffer;

public interface IRenderingEntry {

    GpuBuffer getVBO(String name);
    int getVertexCount();

}
