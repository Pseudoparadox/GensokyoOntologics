package com.github.fictology.gensokyoontology.util.api.render;

import net.minecraft.client.renderer.MappableRingBuffer;

public interface IRenderingEntry {

    int getVertexCount();
    String uniformName();
    IBufferedMesh getBufferedMesh(String label);

    void clear();
}
