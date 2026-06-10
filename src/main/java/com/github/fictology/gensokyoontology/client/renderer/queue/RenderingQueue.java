package com.github.fictology.gensokyoontology.client.renderer.queue;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public abstract class RenderingQueue {
    // 我们用两个缓冲：building（本帧 submit 在填）和 snapshot（AfterLevel 在消费）
    private final List<IRenderingEntry> building = new ArrayList<>();
    private @Nullable List<IRenderingEntry> snapshot = null;
    /** 由 EntityRenderer.submit() 调用（仍在渲染线程，但算"submission phase"） */
    public void add(IRenderingEntry e) {
        building.add(e);
    }

    /** 由 AfterLevel 回调调用：swap + 取走本帧快照 */
    public List<IRenderingEntry> takeSnapshot() {
        snapshot = new ArrayList<>(building);
        building.clear();
        return snapshot;
    }

    public void clear() {
        building.clear();
        snapshot = null;
    }

    public boolean isEmpty() {
        return building.isEmpty();
    }

}
