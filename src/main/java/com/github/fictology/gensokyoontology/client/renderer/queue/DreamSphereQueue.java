package com.github.fictology.gensokyoontology.client.renderer.queue;

import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/** 一个非常轻量的、渲染线程内的收集容器。 */
public class DreamSphereQueue extends RenderingQueue<DreamSphereQueue.Entry> {

    // ── 每帧一条记录 ──────────────────────────────────────────
    public static class Entry extends RenderingEntry {
        public Vec3 worldPos;
        public float radius;
        public float lifeT;
        public float r, g, b;       // mainColor (0..1)
        public int latitudes = 16;
        public int longitudes = 16;
        // 模型视图 / proj 你需要在 mini-pass 里从别处拿
        // 这里只存 world-space 的定位数据
    }
}