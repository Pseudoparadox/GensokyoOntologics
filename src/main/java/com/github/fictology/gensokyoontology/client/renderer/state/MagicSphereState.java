package com.github.fictology.gensokyoontology.client.renderer.state;

import com.github.fictology.gensokyoontology.common.event.RenderingEvents;
import com.github.fictology.gensokyoontology.registry.RenderTypeRegistry;
import com.github.fictology.gensokyoontology.util.GSKOGeometry;
import com.github.fictology.gensokyoontology.util.GSKOUtil;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector2f;
import org.joml.Vector4i;

import java.util.function.Function;

public class MagicSphereState extends EntityRenderState {
    public Entity entity;
    public Vector4i mainColor;
    public Vector4i coreColor;
    public int longitudes;
    public int latitudes;
    public float size;
    public Function<Float, Float> sizeChangeFunc;


}
