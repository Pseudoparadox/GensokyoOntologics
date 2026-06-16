package com.github.fictology.gensokyoontology.api;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.joml.Vector3f;

public record V3f(float x, float y, float z) {

    public static final Codec<org.joml.Vector3f> CODEC = RecordCodecBuilder.create(vector3fInstance -> vector3fInstance.group(
            Codec.FLOAT.fieldOf("x").forGetter(org.joml.Vector3f::x),
            Codec.FLOAT.fieldOf("y").forGetter(org.joml.Vector3f::y),
            Codec.FLOAT.fieldOf("z").forGetter(org.joml.Vector3f::z)
    ).apply(vector3fInstance, org.joml.Vector3f::new));
    public static final V3f XP = new V3f(1, 0, 0);
    public static final V3f XN = new V3f(-1, 0, 0);
    public static final V3f YP = new V3f(0, 1, 0);
    public static final V3f YN = new V3f(0, -1, 0);
    public static final V3f ZP = new V3f(0, 0, 1);
    public static final V3f ZN = new V3f(0, 0, -1);

    public static Vector3f of(float x, float y, float z){
        return new Vector3f(x, y, z);
    }

    public Vector3f cast() {
        return new Vector3f(x, y, z);
    }
}
