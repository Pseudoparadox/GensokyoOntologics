package com.github.fictology.gensokyoontology.util.api.render;

public interface IRenderingEntry {

    int getVertexCount();
    String uniformName();
    IBufferedMesh getBufferedMesh();

    void clear();
}
