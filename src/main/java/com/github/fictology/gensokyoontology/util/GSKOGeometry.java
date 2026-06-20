package com.github.fictology.gensokyoontology.util;

import com.github.fictology.gensokyoontology.api.V3f;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.logging.LogUtils;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.util.Mth;
import org.joml.*;
import org.joml.Vector3f;

import java.lang.Math;

public final class GSKOGeometry {


    private static float[][] longitudeSphereVertices(int latitudeBands, int longitudeBands, float radius) {
        int vertexCount = (latitudeBands + 1) * (longitudeBands + 1);
        float[][] vertices = new float[vertexCount][3];

        int index = 0;
        for (int latNumber = 0; latNumber <= latitudeBands; latNumber++) {
            float theta = (float) (latNumber * Math.PI / latitudeBands);
            float sinTheta = (float) Math.sin(theta);
            float cosTheta = (float) Math.cos(theta);

            for (int longNumber = 0; longNumber <= longitudeBands; longNumber++) {
                float phi = (float) (longNumber * 2 * Math.PI / longitudeBands);
                float sinPhi = (float) Math.sin(phi);
                float cosPhi = (float) Math.cos(phi);

                float x = cosPhi * sinTheta;
                float y = cosTheta;
                float z = sinPhi * sinTheta;
                vertices[index][0] = radius * x;
                vertices[index][1] = radius * y;
                vertices[index][2] = radius * z;
                index++;
            }
        }
        return vertices;
    }

    private static int[][] longitudeSphereFaces(int latitudeBands, int longitudeBands) {
        int indexCount = latitudeBands * longitudeBands * 6;
        int[][] indices = new int[indexCount / 3][3];

        int index = 0;
        for (int latNumber = 0; latNumber < latitudeBands; latNumber++) {
            for (int longNumber = 0; longNumber < longitudeBands; longNumber++) {
                int first = (latNumber * (longitudeBands + 1)) + longNumber;
                int second = first + longitudeBands + 1;

                indices[index][0] = first;
                indices[index][1] = second;
                indices[index][2] = first + 1;
                index++;

                indices[index][0] = second;
                indices[index][1] = second + 1;
                indices[index][2] = first + 1;
                index++;
            }
        }
        return indices;
    }

    public static GpuBuffer testMesh(RenderType renderType, Matrix4f matrix){
        GpuBuffer vertexBuffer;

        int vertexCount = 6; // 每个格子 2 个三角形 × 3 顶点
        int vertexSize = renderType.format().getVertexSize();

        LogUtils.getLogger().info(String.valueOf(vertexSize));

        try (ByteBufferBuilder byteBufferBuilder =
                     ByteBufferBuilder.exactlySized(vertexCount * vertexSize)) {

            BufferBuilder builder = new BufferBuilder(byteBufferBuilder, renderType.mode(), renderType.format());
            renderTriangle(builder, new Matrix4f(),
                    V3f.of(0, 0, 0), V3f.ZN.cast(), new Vector2f(0, 0),
                    V3f.of(1, 0, 0), V3f.ZN.cast(), new Vector2f(0, 1),
                    V3f.of(1, 1, 0), V3f.ZN.cast(), new Vector2f(1, 1),
                    new Vector4i(255, 255, 255, 255));

            try (var meshData = builder.buildOrThrow()) {
                return RenderSystem.getDevice().createBuffer(() -> "Sphere Vertex Buffer",
                        GpuBuffer.USAGE_VERTEX | GpuBuffer.USAGE_MAP_WRITE,
                        meshData.vertexBuffer());
            }
        }
    }

    public static GpuBuffer createBufferedSphereMesh(RenderType renderType, int latitudeBands, int longitudeBands, float radius) {
        GpuBuffer vertexBuffer;

        int vertexCount = latitudeBands * longitudeBands * 6; // 每个格子 2 个三角形 × 3 顶点
        int vertexSize = renderType.format().getVertexSize();

        try (ByteBufferBuilder byteBufferBuilder =
                     ByteBufferBuilder.exactlySized(vertexCount * vertexSize)) {

            BufferBuilder builder = new BufferBuilder(byteBufferBuilder, renderType.mode(), renderType.format());

            for (int latNumber = 0; latNumber < latitudeBands; latNumber++) {
                float theta1 = (float) (latNumber * Math.PI / latitudeBands);
                float theta2 = (float) ((latNumber + 1) * Math.PI / latitudeBands);

                float sinTheta1 = (float) Math.sin(theta1);
                float cosTheta1 = (float) Math.cos(theta1);
                float sinTheta2 = (float) Math.sin(theta2);
                float cosTheta2 = (float) Math.cos(theta2);

                for (int longNumber = 0; longNumber < longitudeBands; longNumber++) {
                    float phi1 = (float) (longNumber * 2 * Math.PI / longitudeBands);
                    float phi2 = (float) ((longNumber + 1) * 2 * Math.PI / longitudeBands);

                    float sinPhi1 = (float) Math.sin(phi1);
                    float cosPhi1 = (float) Math.cos(phi1);
                    float sinPhi2 = (float) Math.sin(phi2);
                    float cosPhi2 = (float) Math.cos(phi2);

                    // 球面坐标转笛卡尔坐标
                    float x1 = cosPhi1 * sinTheta1;
                    float z1 = sinPhi1 * sinTheta1;

                    float x2 = cosPhi2 * sinTheta1;
                    float z2 = sinPhi2 * sinTheta1;

                    float x3 = cosPhi1 * sinTheta2;
                    float z3 = sinPhi1 * sinTheta2;

                    float x4 = cosPhi2 * sinTheta2;
                    float z4 = sinPhi2 * sinTheta2;

                    // UV 坐标
                    float u1 = (float) longNumber / longitudeBands;
                    float v1 = (float) latNumber / latitudeBands;
                    float u2 = (float) (longNumber + 1) / longitudeBands;
                    float v2 = (float) (latNumber + 1) / latitudeBands;

                    builder.addVertex(radius * x1, radius * cosTheta1, radius * z1).setUv(u1, v1).setNormal(x1, cosTheta1, z1);
                    builder.addVertex(radius * x3, radius * cosTheta2, radius * z3).setUv(u1, v2).setNormal(x3, cosTheta2, z3);
                    builder.addVertex(radius * x2, radius * cosTheta1, radius * z2).setUv(u2, v1).setNormal(x2, cosTheta1, z2);

                    builder.addVertex(radius * x3, radius * cosTheta2, radius * z3).setUv(u1, v2).setNormal(x3, cosTheta2, z3);
                    builder.addVertex(radius * x4, radius * cosTheta2, radius * z4).setUv(u2, v2).setNormal(x4, cosTheta2, z4);
                    builder.addVertex(radius * x2, radius * cosTheta1, radius * z2).setUv(u2, v1).setNormal(x2, cosTheta1, z2);
                }
            }

            try (MeshData meshData = builder.buildOrThrow()) {
                return RenderSystem.getDevice().createBuffer(() -> "Sphere Vertex Buffer",
                        GpuBuffer.USAGE_VERTEX | GpuBuffer.USAGE_MAP_WRITE,
                        meshData.vertexBuffer());
            }
        }
    }

    public static void buildSphereMesh(Matrix4f pose, VertexConsumer builder, RenderType renderType, int latitudeBands, int longitudeBands) {

        for (int latNumber = 0; latNumber < latitudeBands; latNumber++) {
            float theta1 = (float) (latNumber * Math.PI / latitudeBands);
            float theta2 = (float) ((latNumber + 1) * Math.PI / latitudeBands);

            float sinTheta1 = (float) Math.sin(theta1);
            float cosTheta1 = (float) Math.cos(theta1);
            float sinTheta2 = (float) Math.sin(theta2);
            float cosTheta2 = (float) Math.cos(theta2);

            for (int longNumber = 0; longNumber < longitudeBands; longNumber++) {
                float phi1 = (float) (longNumber * 2 * Math.PI / longitudeBands);
                float phi2 = (float) ((longNumber + 1) * 2 * Math.PI / longitudeBands);

                float sinPhi1 = (float) Math.sin(phi1);
                float cosPhi1 = (float) Math.cos(phi1);
                float sinPhi2 = (float) Math.sin(phi2);
                float cosPhi2 = (float) Math.cos(phi2);

                // 球面坐标转笛卡尔坐标
                float x1 = cosPhi1 * sinTheta1;
                float z1 = sinPhi1 * sinTheta1;

                float x2 = cosPhi2 * sinTheta1;
                float z2 = sinPhi2 * sinTheta1;

                float x3 = cosPhi1 * sinTheta2;
                float z3 = sinPhi1 * sinTheta2;

                float x4 = cosPhi2 * sinTheta2;
                float z4 = sinPhi2 * sinTheta2;

                // UV 坐标
                float u1 = (float) longNumber / longitudeBands;
                float v1 = (float) latNumber / latitudeBands;
                float u2 = (float) (longNumber + 1) / longitudeBands;
                float v2 = (float) (latNumber + 1) / latitudeBands;

                builder.addVertex(pose, x1, cosTheta1, z1).setUv(u1, v1).setNormal(x1, cosTheta1, z1).setColor(1F, 1F, 1F, 1F);
                builder.addVertex(pose, x3, cosTheta2, z3).setUv(u1, v2).setNormal(x3, cosTheta2, z3).setColor(1F, 1F, 1F, 1F);
                builder.addVertex(pose, x2, cosTheta1, z2).setUv(u2, v1).setNormal(x2, cosTheta1, z2).setColor(1F, 1F, 1F, 1F);

                builder.addVertex(pose, x3, cosTheta2, z3).setUv(u1, v2).setNormal(x3, cosTheta2, z3).setColor(1F, 1F, 1F, 1F);
                builder.addVertex(pose, x4, cosTheta2, z4).setUv(u2, v2).setNormal(x4, cosTheta2, z4).setColor(1F, 1F, 1F, 1F);
                builder.addVertex(pose, x2, cosTheta1, z2).setUv(u2, v1).setNormal(x2, cosTheta1, z2).setColor(1F, 1F, 1F, 1F);
            }
        }
    }

    public static void buildSphereMesh(Matrix4f pose, VertexConsumer builder, int latitudeBands, int longitudeBands,
                                       Vector4i color) {

        for (int latNumber = 0; latNumber < latitudeBands; latNumber++) {
            float theta1 = (float) (latNumber * Math.PI / latitudeBands);
            float theta2 = (float) ((latNumber + 1) * Math.PI / latitudeBands);

            float sinTheta1 = (float) Math.sin(theta1);
            float cosTheta1 = (float) Math.cos(theta1);
            float sinTheta2 = (float) Math.sin(theta2);
            float cosTheta2 = (float) Math.cos(theta2);

            for (int longNumber = 0; longNumber < longitudeBands; longNumber++) {
                float phi1 = (float) (longNumber * 2 * Math.PI / longitudeBands);
                float phi2 = (float) ((longNumber + 1) * 2 * Math.PI / longitudeBands);

                float sinPhi1 = (float) Math.sin(phi1);
                float cosPhi1 = (float) Math.cos(phi1);
                float sinPhi2 = (float) Math.sin(phi2);
                float cosPhi2 = (float) Math.cos(phi2);

                // 球面坐标转笛卡尔坐标
                float x1 = cosPhi1 * sinTheta1;
                float z1 = sinPhi1 * sinTheta1;

                float x2 = cosPhi2 * sinTheta1;
                float z2 = sinPhi2 * sinTheta1;

                float x3 = cosPhi1 * sinTheta2;
                float z3 = sinPhi1 * sinTheta2;

                float x4 = cosPhi2 * sinTheta2;
                float z4 = sinPhi2 * sinTheta2;

                // UV 坐标
                float u1 = (float) longNumber / longitudeBands;
                float v1 = (float) latNumber / latitudeBands;
                float u2 = (float) (longNumber + 1) / longitudeBands;
                float v2 = (float) (latNumber + 1) / latitudeBands;

                builder.addVertex(pose, x1, cosTheta1, z1).setUv(u1, v1).setNormal(x1, cosTheta1, z1).setColor(color.x, color.y, color.z, color.w);
                builder.addVertex(pose, x3, cosTheta2, z3).setUv(u1, v2).setNormal(x3, cosTheta2, z3).setColor(color.x, color.y, color.z, color.w);
                builder.addVertex(pose, x2, cosTheta1, z2).setUv(u2, v1).setNormal(x2, cosTheta1, z2).setColor(color.x, color.y, color.z, color.w);
                builder.addVertex(pose, x3, cosTheta2, z3).setUv(u1, v2).setNormal(x3, cosTheta2, z3).setColor(color.x, color.y, color.z, color.w);
                builder.addVertex(pose, x4, cosTheta2, z4).setUv(u2, v2).setNormal(x4, cosTheta2, z4).setColor(color.x, color.y, color.z, color.w);
                builder.addVertex(pose, x2, cosTheta1, z2).setUv(u2, v1).setNormal(x2, cosTheta1, z2).setColor(color.x, color.y, color.z, color.w);
            }

        }
    }

    private static void buildCylinder(Matrix4f matrix, VertexConsumer builder, Vector3f start, Vector3f end, float radius, float height, int segments, Vector4i color) {
        var direction = new Vector3f(end).sub(new Vector3f(start)).normalize();

        // 计算一个垂直于方向的法线
        Vector3f perpendicular = new Vector3f(0, 1, 0);
        if (Math.abs(direction.dot(perpendicular)) > 0.99) {
            perpendicular = new Vector3f(1, 0, 0);
        }
        Vector3f normal = perpendicular.cross(direction).normalize();
        Vector3f binormal = direction.cross(normal).normalize();
        for (int i = 0; i < segments; i++) {
            double theta0 = (2.0 * Mth.PI / segments) * i;
            double theta1 = (2.0 * Mth.PI / segments) * (i + 1);

            // 计算当前和下一个圆周上的点
            Vector3f p0 = normal.mul(Mth.cos(theta0) * radius)
                    .add(binormal.mul(Mth.sin(theta0) * radius))
                    .add(new Vector3f(start));

            Vector3f p1 = normal.mul(Mth.cos(theta1) * radius)
                    .add(binormal.mul(Mth.sin(theta1) * radius))
                    .add(new Vector3f(start));

            Vector3f p2 = normal.mul(Mth.cos(theta1) * radius)
                    .add(binormal.mul(Mth.sin(theta1) * radius))
                    .add(new Vector3f(end));

            Vector3f p3 = normal.mul(Mth.cos(theta0) * radius)
                    .add(binormal.mul(Mth.sin(theta0) * radius))
                    .add(new Vector3f(end));

            renderQuad(builder, matrix, p0, p1, p2, p3, color.x, color.y, color.z, color.w);
            renderQuad(builder, matrix, p3, p2, p1, p0, color.x, color.y, color.z, color.w);
        }
    }

    private static void buildCylinder(Matrix4f matrix, VertexConsumer vertexBuilder, float radius, float height,
                                            int segments, Vector4i color) {
        for (int i = 0; i < segments; i++) {
            double angle1 = 2 * Math.PI * i / segments;
            double angle2 = 2 * Math.PI * (i + 1) / segments;

            float x1 = (float) Math.cos(angle1) * radius;
            float z1 = (float) Math.sin(angle1) * radius;
            float x2 = (float) Math.cos(angle2) * radius;
            float z2 = (float) Math.sin(angle2) * radius;

            // 计算法线（侧面法线指向外部）
            float normalX = (x1 + x2) / 2 / radius;
            float normalZ = (z1 + z2) / 2 / radius;

            vertexBuilder.addVertex(matrix, x1, 0,      z1).setColor(color.x, color.y, color.z, color.w).setNormal(normalX, 0.0f, normalZ);
            vertexBuilder.addVertex(matrix, x2, 0,      z2).setColor(color.x, color.y, color.z, color.w).setNormal(normalX, 0.0f, normalZ);
            vertexBuilder.addVertex(matrix, x2, height, z2).setColor(color.x, color.y, color.z, color.w).setNormal(normalX, 0.0f, normalZ);
            vertexBuilder.addVertex(matrix, x1, height, z1).setColor(color.x, color.y, color.z, color.w).setNormal(normalX, 0.0f, normalZ);
        }
    }


    public static void renderTriangle(BufferBuilder builder, Matrix4f matrix,
                                      Vector3f v1, Vector3f n1, Vector2f uv1,
                                      Vector3f v2, Vector3f n2, Vector2f uv2,
                                      Vector3f v3, Vector3f n3, Vector2f uv3,
                                      Vector4i color) {
        addVertex(builder, matrix, v1, n1, uv1, color);
        addVertex(builder, matrix, v2, n2, uv2, color);
        addVertex(builder, matrix, v3, n3, uv3, color);

        addVertex(builder, matrix, v3, n3, uv3, color);
        addVertex(builder, matrix, v2, n2, uv2, color);
        addVertex(builder, matrix, v1, n1, uv1, color);
    }

    private static void renderQuad(VertexConsumer builder, Matrix4f matrix, Vector3f p0, Vector3f p1, Vector3f p2, Vector3f p3,
                                   int red, int green, int blue, int alpha) {
        // 设置顶点，按逆时针顺序渲染四边面
        builder.addVertex(matrix, p0.x, p0.y, p0.z).setColor(red, green, blue, alpha);
        builder.addVertex(matrix, p1.x, p1.y, p1.z).setColor(red, green, blue, alpha);
        builder.addVertex(matrix, p2.x, p2.y, p2.z).setColor(red, green, blue, alpha);
        builder.addVertex(matrix, p3.x, p3.y, p3.z).setColor(red, green, blue, alpha);
    }

    private static void addVertex(BufferBuilder builder, Matrix4f matrix,
                                  Vector3f pos, Vector3f normal, Vector2f uv,
                                  Vector4i color) {
        builder.addVertex(matrix, pos.x, pos.y, pos.z)
                .setUv(uv.x, uv.y)
                .setNormal(normal.x, normal.y, normal.z);
    }

}
