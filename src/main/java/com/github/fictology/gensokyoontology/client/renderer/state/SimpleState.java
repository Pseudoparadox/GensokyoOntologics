package com.github.fictology.gensokyoontology.client.renderer.state;

import com.github.fictology.gensokyoontology.util.api.render.IBufferedMesh;
import com.github.fictology.gensokyoontology.util.api.render.IRenderingEntry;
import com.github.fictology.gensokyoontology.util.api.render.IResourceGetter;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.vertex.MeshData;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

import java.util.concurrent.atomic.AtomicReference;

public class SimpleState<E extends Entity & IResourceGetter> extends EntityRenderState {
    public E entity;
}
