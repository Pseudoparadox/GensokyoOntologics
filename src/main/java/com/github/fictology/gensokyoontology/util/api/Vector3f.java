package com.github.fictology.gensokyoontology.util.api;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record Vector3f(float x, float y, float z) {

    public static final Codec<org.joml.Vector3f> CODEC = RecordCodecBuilder.create(vector3fInstance -> vector3fInstance.group(
            Codec.FLOAT.fieldOf("x").forGetter(org.joml.Vector3f::x),
            Codec.FLOAT.fieldOf("y").forGetter(org.joml.Vector3f::y),
            Codec.FLOAT.fieldOf("z").forGetter(org.joml.Vector3f::z)
    ).apply(vector3fInstance, org.joml.Vector3f::new));
    public static final Vector3f XP = new Vector3f(1, 0, 0);
    public static final Vector3f XN = new Vector3f(-1, 0, 0);
    public static final Vector3f YP = new Vector3f(0, 1, 0);
    public static final Vector3f YN = new Vector3f(0, -1, 0);
    public static final Vector3f ZP = new Vector3f(0, 0, 1);
    public static final Vector3f ZN = new Vector3f(0, 0, -1);

    public org.joml.Vector3f cast() {
        return new org.joml.Vector3f(x, y, z);
    }
}
