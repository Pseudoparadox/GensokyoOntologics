package com.github.fictology.gensokyoontology.client.renderer.state;

import com.github.fictology.gensokyoontology.registry.RenderTypeRegistry;
import com.github.fictology.gensokyoontology.util.api.IRenderingEntry;
import com.github.fictology.gensokyoontology.util.api.IUniformBuilder;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.DynamicUniformStorage;
import net.minecraft.client.renderer.MappableRingBuffer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.nio.ByteBuffer;

/**
 * ── 每帧一条记录 ──────────────────────────────────────────
 * 模型视图 / proj 你需要在 mini-pass 里从别处拿
 * 这里只存 world-space 的定位数据
 */
public class DreamSphereEntry implements IRenderingEntry {
    public Vec3 worldPos;
    public Vector4f sphereColor;
    public Vector2f offset;
    public Vector2f tilling;
    public float cellDensity;
    private GpuBuffer sharedVbo;
    public ByteBuffer meshBuffer;
    public ByteBuffer uniformBuffer;
    public int vertexCount;

    @Override
    public GpuBuffer getVBO(String label) {
        if (this.sharedVbo == null) {
            // 2. 创建或重用 VBO
            this.sharedVbo = RenderSystem.getDevice().createBuffer(
                    () -> label + "_" + System.identityHashCode(this),
                    GpuBuffer.USAGE_VERTEX | GpuBuffer.USAGE_MAP_WRITE,
                    this.meshBuffer.remaining()
            );
            // 3. 上传数据
            try (var view = RenderSystem.getDevice()
                    .createCommandEncoder()
                    .mapBuffer(this.sharedVbo, false, true)) {
                view.data().put(this.meshBuffer);
            }
        }
        return sharedVbo;
    }


    @Override
    public int getVertexCount() {
        return this.vertexCount;
    }

    @Override
    public String uniformName() {
        return "SphereData";
    }

    @Override
    public RenderType getRenderType() {
        return RenderTypeRegistry.DREAM_SPHERE;
    }

    @Override
    public ByteBuffer getMeshBuffer() {
        return this.meshBuffer;
    }

    // 清理资源（在实体移除/卸载时调用）
    public void clear() {
        if (sharedVbo != null) {
            sharedVbo.close();
            sharedVbo = null;
        }
    }
}
