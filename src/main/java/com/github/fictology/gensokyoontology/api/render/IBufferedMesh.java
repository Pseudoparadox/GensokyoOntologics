package com.github.fictology.gensokyoontology.api.render;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.vertex.BufferBuilder;
import net.minecraft.client.renderer.MappableRingBuffer;
import net.minecraft.client.renderer.rendertype.RenderType;

import java.nio.ByteBuffer;

public interface IBufferedMesh {
    ByteBuffer meshByteBuffer();
    ByteBuffer indexByteBuffer();
    BufferBuilder create(RenderType renderType);
    GpuBuffer vertexBuffer(BufferBuilder builder);
    MappableRingBuffer indexBuffer(String label);
    int vertexCount();
    int indexCount();
    int getStride();
}
