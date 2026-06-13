package com.github.fictology.gensokyoontology.util.api.render;

import com.mojang.blaze3d.buffers.GpuBuffer;
import net.minecraft.client.renderer.MappableRingBuffer;

import java.nio.ByteBuffer;

public interface IBufferedMesh {
    ByteBuffer toByteBuffer();
    MappableRingBuffer toGpuBuffer(String label);
    int vertexCount();
    int getStride();
}
