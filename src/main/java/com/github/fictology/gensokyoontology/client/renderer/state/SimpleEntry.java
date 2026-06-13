package com.github.fictology.gensokyoontology.client.renderer.state;

import com.github.fictology.gensokyoontology.util.api.render.IRenderingEntry;
import com.mojang.blaze3d.buffers.GpuBuffer;
import net.minecraft.client.renderer.MappableRingBuffer;

public abstract class SimpleEntry implements IRenderingEntry {

    @Override
    public int getVertexCount() {
        return 0;
    }

}
