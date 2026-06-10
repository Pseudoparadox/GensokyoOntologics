package com.github.fictology.gensokyoontology.client.renderer.state;

import com.github.fictology.gensokyoontology.common.event.RenderingEvents;
import com.github.fictology.gensokyoontology.registry.RenderTypeRegistry;
import com.github.fictology.gensokyoontology.util.GSKOGeometry;
import com.github.fictology.gensokyoontology.util.GSKOUtil;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.nio.ByteBuffer;

public class DreamSphereQueue extends RenderingQueue{
    /** ── 每帧一条记录 ──────────────────────────────────────────
     * 模型视图 / proj 你需要在 mini-pass 里从别处拿
     * 这里只存 world-space 的定位数据
     */
    public static class Entry implements IRenderingEntry {
        public Vec3 worldPos;
        public Vector4f sphereColor;
        public Vector2f offset;
        public Vector2f tilling;
        public float cellDensity;
        private GpuBuffer cachedVbo;
        public ByteBuffer buffer;
        public int vertexCount;
        private boolean dirty;

        @Override
        public GpuBuffer getVBO(String name) {
            if (this.cachedVbo == null || dirty) {
                // 2. 创建或重用 VBO
                if (this.cachedVbo == null) {
                    this.cachedVbo = RenderSystem.getDevice().createBuffer(
                            () -> name + "_" + System.identityHashCode(this),
                            GpuBuffer.USAGE_VERTEX | GpuBuffer.USAGE_MAP_WRITE,
                            this.buffer.remaining()
                    );
                }
                // 3. 上传数据
                try (var view = RenderSystem.getDevice()
                        .createCommandEncoder()
                        .mapBuffer(this.cachedVbo, false, true)) {
                    view.data().put(this.buffer);
                }
                this.dirty = false;
            }
            return this.cachedVbo;
        }

        @Override
        public int getVertexCount() {
            return this.vertexCount;
        }

        // 清理资源（在实体移除/卸载时调用）
        public void clear() {
            if (cachedVbo != null) {
                cachedVbo.close();
                cachedVbo = null;
            }
        }
    }
}
