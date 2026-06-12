package com.github.fictology.gensokyoontology.util.api;

import com.github.fictology.gensokyoontology.common.event.RenderingEvents;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.rendertype.RenderType;

import java.nio.ByteBuffer;

public interface IRenderingEntry {

    GpuBuffer getVBO(String label);

    int getVertexCount();
    String uniformName();
    RenderType getRenderType();
    ByteBuffer getMeshBuffer();

}
