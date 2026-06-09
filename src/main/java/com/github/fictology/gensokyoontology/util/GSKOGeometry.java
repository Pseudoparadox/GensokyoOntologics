package com.github.fictology.gensokyoontology.util;

import com.mojang.blaze3d.vertex.VertexConsumer;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4i;

import java.util.ArrayList;
import java.util.List;

public final class GSKOGeometry {
    public static void renderSphere(VertexConsumer builder, Matrix4f matrix4f, int latitudeBands, int longitudeBands, float radius,
                                    Vector4i vertColor) {
        var sphere = SphereMesh.create(latitudeBands, longitudeBands, radius);

        for (int i = 0; i < sphere.indices.size(); i += 3) {
            int i1 = sphere.indices.get(i);
            int i2 = sphere.indices.get(i + 1);
            int i3 = sphere.indices.get(i + 2);

            renderTriangle(builder, matrix4f,
                    sphere.vertices.get(i1), sphere.normals.get(i1), sphere.uvs.get(i1),
                    sphere.vertices.get(i2), sphere.normals.get(i2), sphere.uvs.get(i2),
                    sphere.vertices.get(i3), sphere.normals.get(i3), sphere.uvs.get(i3),
                    vertColor);
        }
    }

    private static void renderTriangle(Matrix4f matrix, VertexConsumer vertexBuilder, float[] v1, float[] v2, float[] v3,
                                       float red, float green, float blue, float alpha) {
//        addVertex(matrix, vertexBuilder, v1, red, green, blue, alpha);
//        addVertex(matrix, vertexBuilder, v2, red, green, blue, alpha);
//        addVertex(matrix, vertexBuilder, v3, red, green, blue, alpha);
//
//        addVertex(matrix, vertexBuilder, v1, red, green, blue, alpha);
//        addVertex(matrix, vertexBuilder, v3, red, green, blue, alpha);
//        addVertex(matrix, vertexBuilder, v2, red, green, blue, alpha);
//
//        addVertex(matrix, vertexBuilder, v3, red, green, blue, alpha);
//        addVertex(matrix, vertexBuilder, v2, red, green, blue, alpha);
//        addVertex(matrix, vertexBuilder, v1, red, green, blue, alpha);
//
//        addVertex(matrix, vertexBuilder, v2, red, green, blue, alpha);
//        addVertex(matrix, vertexBuilder, v1, red, green, blue, alpha);
//        addVertex(matrix, vertexBuilder, v3, red, green, blue, alpha);
    }

    private static void renderTriangle(VertexConsumer builder, Matrix4f matrix,
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

    private static void addVertex(VertexConsumer builder, Matrix4f matrix,
                                  Vector3f pos, Vector3f normal, Vector2f uv,
                                  Vector4i color) {
        builder.addVertex(matrix, pos.x, pos.y, pos.z)
                .setColor(color.x, color.y, color.z, color.w)
                .setNormal(normal.x, normal.y, normal.z)
                .setUv(uv.x, uv.y);
    }

    public static class SphereMesh {
        final List<Vector3f> vertices;
        final List<Vector3f> normals;
        final List<Vector2f> uvs;
        final List<Integer> indices;

        SphereMesh(List<Vector3f> vertices, List<Vector3f> normals,
                   List<Vector2f> uvs, List<Integer> indices) {
            this.vertices = vertices;
            this.normals = normals;
            this.uvs = uvs;
            this.indices = indices;
        }
        public static SphereMesh create(int latitudeBands, int longitudeBands, float radius) {
            List<Vector3f> vertices = new ArrayList<>();
            List<Vector3f> normals = new ArrayList<>();
            List<Vector2f> uvs = new ArrayList<>();
            List<Integer> indices = new ArrayList<>();

            for (int latNumber = 0; latNumber <= latitudeBands; latNumber++) {
                float theta = (float) (latNumber * Math.PI / latitudeBands);
                float sinTheta = (float) Math.sin(theta);
                float cosTheta = (float) Math.cos(theta);

                for (int longNumber = 0; longNumber <= longitudeBands; longNumber++) {
                    float phi = (float) (longNumber * 2 * Math.PI / longitudeBands);
                    float sinPhi = (float) Math.sin(phi);
                    float cosPhi = (float) Math.cos(phi);

                    // 球面坐标转笛卡尔坐标
                    float x = cosPhi * sinTheta;
                    float y = cosTheta;
                    float z = sinPhi * sinTheta;

                    // 顶点位置
                    vertices.add(new Vector3f(radius * x, radius * y, radius * z));

                    // 法线（单位向量）
                    normals.add(new Vector3f(x, y, z).normalize());

                    // 墨卡托投影UV
                    float u = phi / (2 * (float) Math.PI);
                    float v = (float) latNumber / latitudeBands;
                    uvs.add(new Vector2f(u, v));
                }
            }

            // 生成索引
            for (int latNumber = 0; latNumber < latitudeBands; latNumber++) {
                for (int longNumber = 0; longNumber < longitudeBands; longNumber++) {
                    int first = (latNumber * (longitudeBands + 1)) + longNumber;
                    int second = first + longitudeBands + 1;

                    // 第一个三角形
                    indices.add(first);
                    indices.add(second);
                    indices.add(first + 1);

                    // 第二个三角形
                    indices.add(second);
                    indices.add(second + 1);
                    indices.add(first + 1);
                }
            }

            return new SphereMesh(vertices, normals, uvs, indices);
        }
    }
}
