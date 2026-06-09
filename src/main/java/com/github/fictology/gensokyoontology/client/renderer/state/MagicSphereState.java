package com.github.fictology.gensokyoontology.client.renderer.state;

import net.minecraft.client.renderer.entity.state.EntityRenderState;
import org.joml.Vector4f;
import org.joml.Vector4i;

import java.util.function.Function;

public class MagicSphereState extends EntityRenderState {
    public Vector4i mainColor;
    public Vector4i coreColor;
    public int longitudes;
    public int latitudes;
    public float size;
    public Function<Float, Float> sizeChangeFunc;
}
