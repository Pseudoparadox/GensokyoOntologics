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

public class SimpleState<E extends Entity & IResourceGetter> extends EntityRenderState implements IRenderingEntry {
    public E entity;
    public Vec3 deltaOffset(){
        var end = this.entity.oldPosition().add(this.entity.getDeltaMovement());
        var x = Mth.lerp(this.partialTick, this.entity.xo, end.x);
        var y = Mth.lerp(this.partialTick, this.entity.yo, end.y);
        var z = Mth.lerp(this.partialTick, this.entity.zo, end.z);
        return new Vec3(x, y, z).subtract(this.entity.oldPosition());
    }

    @Override
    public int getVertexCount() {
        return 0;
    }


    @Override
    public String uniformName() {
        return null;
    }

    @Override
    public void setVertexBuffer(GpuBuffer buffer) {

    }

    @Override
    public GpuBuffer getVertexBuffer() {
        return null;
    }

    @Override
    public void setModelView(Matrix4f matrix4f) {

    }

    @Override
    public MeshData getMesh() {
        return null;
    }

    @Override
    public boolean tryGetModelView(AtomicReference<Matrix4f> ref) {
        return false;
    }

    @Override
    public void clear() {

    }
}
