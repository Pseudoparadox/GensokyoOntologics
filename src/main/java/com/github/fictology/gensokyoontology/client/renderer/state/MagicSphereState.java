package com.github.fictology.gensokyoontology.client.renderer.state;

import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.Entity;
import org.joml.Vector2f;
import org.joml.Vector4i;

import java.util.function.Function;

public class MagicSphereState extends EntityRenderState {
    public Entity entity;
    public Vector4i mainColor;
    public Vector4i coreColor;
    public Vector2f offset;
    public Vector2f tilling;
    public float cellDensity;
    public static class Queue extends RenderingQueue{

    }
}
