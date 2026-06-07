package com.github.fictology.gensokyoontology.client.model.state;

import net.minecraft.client.renderer.entity.state.HumanoidRenderState;

public class FairyRenderState extends HumanoidRenderState {
    public final FairyType fairyType;

    public FairyRenderState(FairyType fairyType) {
        this.fairyType = fairyType;
    }

    public enum FairyType{
        RED,
        GREEN,
        BLUE,
        SUNFLOWER
    }
}
