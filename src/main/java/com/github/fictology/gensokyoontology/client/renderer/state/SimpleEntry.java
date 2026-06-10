package com.github.fictology.gensokyoontology.client.renderer.state;

import com.mojang.blaze3d.buffers.GpuBuffer;

public abstract class SimpleEntry implements IRenderingEntry{
    @Override
    public GpuBuffer getVBO(String label) {
        return null;
    }

    @Override
    public int getVertexCount() {
        return 0;
    }

}
