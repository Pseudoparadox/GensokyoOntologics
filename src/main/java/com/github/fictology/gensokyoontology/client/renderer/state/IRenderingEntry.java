package com.github.fictology.gensokyoontology.client.renderer.state;

import com.github.fictology.gensokyoontology.common.event.RenderingEvents;
import com.mojang.blaze3d.buffers.GpuBuffer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;

public interface IRenderingEntry {

    GpuBuffer getVBO(String name);
    int getVertexCount();

    default void addRenderEntry(RenderType renderType){
        RenderingEvents.getRenderingQueue(renderType).add(this);
    }
}
