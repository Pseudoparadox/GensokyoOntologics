package github.thelawf.gensokyoontology.common.util.math;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import github.thelawf.gensokyoontology.api.Color4i;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.math.vector.*;

public class GeometryUtil {
    public static final double PHI = (1 + Math.sqrt(5)) / 2;
    public static final double PHI_RECIPROCAL = 1 / PHI;

    /**
     * 创建一个二十面体的三角面顶点组
     * @param builder 顶点构建器
     * @param matrix4f 位置渲染矩阵
     * @param a 二十面体的周长
     */
    private static void regularIcosahedron(IVertexBuilder builder, Matrix4f matrix4f, float a,
                                      float red, float green, float blue, float alpha) {
        // final double scale = (a * Math.sqrt(2)) / (Math.sqrt(5 + Math.sqrt(5)));
        final double scale = 1;
        final double phiS = scale * PHI;
        final double phiSR = scale * PHI_RECIPROCAL;
        triangularFace(builder, matrix4f, red, green, blue, alpha, of(scale, scale, scale), of(scale, scale, -scale), of(0, phiS, phiSR));
        triangularFace(builder, matrix4f, red, green, blue, alpha, of(scale, scale, scale), of(0, phiS, phiSR), of(-scale, scale, scale));
        triangularFace(builder, matrix4f, red, green, blue, alpha, of(scale, scale, scale), of(-scale, scale, scale), of(phiS, phiSR, 0));
        triangularFace(builder, matrix4f, red, green, blue, alpha, of(scale, scale, scale), of(phiS, phiSR, 0), of(phiSR, 0, phiS));
        triangularFace(builder, matrix4f, red, green, blue, alpha, of(scale, scale, scale), of(phiSR, 0, phiS), of(scale, scale, -scale));

        triangularFace(builder, matrix4f, red, green, blue, alpha, of(scale, scale, -scale), of(phiSR, 0, -phiS), of(0, phiS, -phiSR));
        triangularFace(builder, matrix4f, red, green, blue, alpha, of(scale, scale, -scale), of(0, phiS, -phiSR), of(0, phiS, phiSR));
        triangularFace(builder, matrix4f, red, green, blue, alpha, of(scale, -scale, scale), of(0, -phiS, phiSR), of(-scale, -scale, scale));
        triangularFace(builder, matrix4f, red, green, blue, alpha, of(scale, -scale, scale), of(-scale, -scale, scale), of(-phiSR, 0, phiS));
        triangularFace(builder, matrix4f, red, green, blue, alpha, of(scale, -scale, scale), of(-phiSR, 0, phiS), of(phiS, -phiSR, 0));

        triangularFace(builder, matrix4f, red, green, blue, alpha, of(scale, -scale, scale), of(phiS, -phiSR, 0), of(phiSR, 0, phiS));
        triangularFace(builder, matrix4f, red, green, blue, alpha, of(scale, -scale, scale), of(phiSR, 0, phiS), of(0, -phiS, phiSR));
        triangularFace(builder, matrix4f, red, green, blue, alpha, of(scale, -scale, -scale), of(-scale, scale, -scale), of(0, -phiS, -phiSR));
        triangularFace(builder, matrix4f, red, green, blue, alpha, of(scale, -scale, -scale), of(0, -phiS, -phiSR), of(-phiSR, 0, -phiS));
        triangularFace(builder, matrix4f, red, green, blue, alpha, of(scale, -scale, -scale), of(-phiSR, 0, -phiS), of(-scale, -scale, -scale));

        triangularFace(builder, matrix4f, red, green, blue, alpha, of(scale, -scale, -scale), of(-scale, -scale, -scale), of(phiSR, 0, -phiS));
        triangularFace(builder, matrix4f, red, green, blue, alpha, of(scale, -scale, -scale), of(phiSR, 0, -phiS), of(-scale, scale, -scale));
        triangularFace(builder, matrix4f, red, green, blue, alpha, of(-scale, scale, scale), of(-phiS, phiSR, 0), of(-phiSR, 0, phiS));
        triangularFace(builder, matrix4f, red, green, blue, alpha, of(-scale, scale, scale), of(-phiSR, 0, phiS), of(-scale, -scale, scale));
        triangularFace(builder, matrix4f, red, green, blue, alpha, of(-scale, scale, -scale), of(0, -phiS, -phiSR), of(-phiSR, 0, -phiS));
    }

    /**
     * 创建一个二十面体的三角面顶点组
     * @param builder 顶点构建器
     * @param matrix4f 位置渲染矩阵
     */
    public static void regularIcosahedron(IVertexBuilder builder, Matrix4f matrix4f,
                                          float red, float green, float blue, float alpha) {
        // final double scale = (a * Math.sqrt(2)) / (Math.sqrt(5 + Math.sqrt(5)));

        triangularFace(builder, matrix4f, red, green, blue, alpha, of(0, 1, PHI), of(0, 1, -PHI), of(1, PHI, 0));
        triangularFace(builder, matrix4f, red, green, blue, alpha, of(0, 1, PHI), of(0, 1, -PHI), of(-1, PHI, 0));
        triangularFace(builder, matrix4f, red, green, blue, alpha, of(0, 1, PHI), of(1, PHI, 0), of(PHI, 0, 1));
        triangularFace(builder, matrix4f, red, green, blue, alpha, of(0, 1, PHI), of(-1, PHI, 0), of(-PHI, 0, 1));
        triangularFace(builder, matrix4f, red, green, blue, alpha, of(0, 1, -PHI), of(PHI, 0, -1), of(1, PHI, 0));

        triangularFace(builder, matrix4f, red, green, blue, alpha, of(0, 1, -PHI), of(-1, PHI, 0), of(-PHI, 0, -1));
        triangularFace(builder, matrix4f, red, green, blue, alpha, of(0, -1, PHI), of(0, -1, -PHI), of(1, -PHI, 0));
        triangularFace(builder, matrix4f, red, green, blue, alpha, of(0, -1, PHI), of(0, -1, -PHI), of(-1, -PHI, 0));
        triangularFace(builder, matrix4f, red, green, blue, alpha, of(0, -1, PHI), of(1, -PHI, 0), of(PHI, 0, 1));
        triangularFace(builder, matrix4f, red, green, blue, alpha, of(0, -1, PHI), of(-1, -PHI, 0), of(-PHI, 0, 1));

        triangularFace(builder, matrix4f, red, green, blue, alpha, of(0, -1, -PHI), of(PHI, 0, -1), of(1, -PHI, 0));
        triangularFace(builder, matrix4f, red, green, blue, alpha, of(0, -1, -PHI), of(-1, -PHI, 0), of(-PHI, 0, -1));
        triangularFace(builder, matrix4f, red, green, blue, alpha, of(1, PHI, 0), of(1, -PHI, 0), of(PHI, 0, 1));
        triangularFace(builder, matrix4f, red, green, blue, alpha, of(1, PHI, 0), of(PHI, 0, -1), of(1, -PHI, 0));
        triangularFace(builder, matrix4f, red, green, blue, alpha, of(-1, PHI, 0), of(-1, -PHI, 0), of(-PHI, 0, 1));

        triangularFace(builder, matrix4f, red, green, blue, alpha, of(-1, PHI, 0), of(-1, -PHI, 0), of(-PHI, 0, -1));
        triangularFace(builder, matrix4f, red, green, blue, alpha, of(PHI, 0, 1), of(PHI, 0, -1), of(-PHI, 0, 1));
        triangularFace(builder, matrix4f, red, green, blue, alpha, of(PHI, 0, 1), of(-PHI, 0, 1), of(0, 1, PHI));
        triangularFace(builder, matrix4f, red, green, blue, alpha, of(PHI, 0, -1), of(-PHI, 0, -1), of(0, 1, -PHI));
        triangularFace(builder, matrix4f, red, green, blue, alpha, of(-PHI, 0, 1), of(-PHI, 0, -1), of(0, -1, PHI));
    }

    public static void renderIcosahedron(IVertexBuilder builder, Matrix4f matrix4f, float size, int subdivisionLevel,
                                          float red, float green, float blue, float alpha) {
        // final double scale = (a * Math.sqrt(2)) / (Math.sqrt(5 + Math.sqrt(5)));
        float X = 0.525731112119133606f * size;
        float Z = 0.850650808352039932f * size;

        float[][] vertices = {
                {-X, 0.0f, Z}, {X, 0.0f, Z}, {-X, 0.0f, -Z}, {X, 0.0f, -Z},
                {0.0f, Z, X}, {0.0f, Z, -X}, {0.0f, -Z, X}, {0.0f, -Z, -X},
                {Z, X, 0.0f}, {-Z, X, 0.0f}, {Z, -X, 0.0f}, {-Z, -X, 0.0f}
        };

        int[][] faces = {
                {0, 4, 1}, {0, 9, 4}, {9, 5, 4}, {4, 5, 8}, {4, 8, 1},
                {8, 10, 1}, {8, 3, 10}, {5, 3, 8}, {5, 2, 3}, {2, 7, 3},
                {7, 10, 3}, {7, 6, 10}, {7, 11, 6}, {11, 0, 6}, {0, 1, 6},
                {6, 1, 10}, {9, 0, 11}, {9, 11, 2}, {9, 2, 5}, {7, 2, 11}
        };

        // subdivide(vertices, faces, subdivisionLevel);

        for (int[] face : faces) {
            renderTriangle(matrix4f, builder, vertices[face[0]], vertices[face[1]], vertices[face[2]],
                    red, green, blue, alpha);
        }
    }

    public static void renderSphere(IVertexBuilder builder, Matrix4f matrix4f, int latitudeBands, int longitudeBands, float radius,
                                    float r, float g, float b, float a) {
        float[][] vertices = longitudeSphereVertices(latitudeBands, longitudeBands, radius);
        int[][] faces = longitudeSphereFaces(latitudeBands, longitudeBands);

        for (int[] face : faces) {
            renderTriangle(matrix4f, builder, vertices[face[0]], vertices[face[1]], vertices[face[2]], r, g, b, a);
        }
    }

    public static void renderCylinder(IVertexBuilder builder, Matrix4f matrix4f, Vector3f bottomPos, Vector3f topPos,
                                      int segments, float radius, float height, Vector3i color, float alpha){
        renderCylinderSides(builder, matrix4f, bottomPos, topPos, radius, height, segments,
                (float) color.getX() / 255, (float) color.getY() / 255, (float) color.getZ() / 255, alpha);
    }

    public static void renderCylinder(IVertexBuilder builder, Matrix4f matrix4f, Vector3f bottomPos, Vector3f topPos,
                                      int segments, float radius, float height, float red, float green, float blue, float alpha){
        // renderCircle(builder, matrix4f, bottomPos, radius, segments, red, green, blue, alpha, true);
        // renderCircle(builder, matrix4f, topPos, radius, segments, red, green, blue, alpha, false);
        renderCylinderSides(builder, matrix4f, bottomPos, topPos, radius, height, segments, red, green, blue, alpha);
    }

    public static void renderCylinder(IVertexBuilder builder, Matrix4f matrix4f, int segments, float radius, float height,
                                      float red, float green, float blue, float alpha){
        renderCircle(builder, matrix4f, new Vector3f(0,0,0),
                radius, segments, red, green, blue, alpha, true);
        renderCircle(builder, matrix4f, new Vector3f(0, height, 0),
                radius, segments, red, green, blue, alpha, false);
        renderCylinderSides(builder, matrix4f, radius, height, segments, red, green, blue, alpha);
    }

    public static void renderCylinderNoCircle(IVertexBuilder builder, Matrix4f matrix4f, int segments, float radius, float height,
                                      float red, float green, float blue, float alpha){
        renderCylinderSides(builder, matrix4f, radius, height, segments, red, green, blue, alpha);
    }

    public static void renderCylinderLightmap(IVertexBuilder builder, Matrix4f matrix4f, int segments, float radius, float height,
                                      float red, float green, float blue, float alpha, int lightmap){
        renderCircleLightmap(builder, matrix4f, new Vector3f(0,0,0), segments,
                radius, red, green, blue, alpha, lightmap);
        renderCircleLightmap(builder, matrix4f, new Vector3f(0, height, 0), segments,
                radius, red, green, blue, alpha, lightmap);
        renderCylinderSides(builder, matrix4f, radius, height, segments, red, green, blue, alpha);
    }

    public static void renderCircleLightmap(IVertexBuilder builder, Matrix4f matrix, Vector3f center, int segments, float radius,
                                            float red, float green, float blue, float alpha, int lightmap) {
        for (int i = 0; i < segments; i++) {
            double angle1 = 2 * Math.PI * i / segments;
            double angle2 = 2 * Math.PI * (i + 1) / segments;
            double angle3 = 2 * Math.PI * (i + 2) / segments;
            double angle4 = 2 * Math.PI * (i + 3) / segments;

            float x1 = (float) Math.cos(angle1) * radius;
            float z1 = (float) Math.sin(angle1) * radius;
            float x2 = (float) Math.cos(angle2) * radius;
            float z2 = (float) Math.sin(angle2) * radius;

            float x3 = (float) Math.cos(angle3) * radius;
            float z3 = (float) Math.sin(angle3) * radius;
            float x4 = (float) Math.cos(angle4) * radius;
            float z4 = (float) Math.sin(angle4) * radius;

            // 三角形顶点：中心点，边缘点1，边缘点2
            builder.pos(matrix, center.getX(), center.getY(), center.getZ()).color(red, green, blue, alpha).lightmap(lightmap).endVertex();
            builder.pos(matrix, x1, center.getY(), z1).color(red, green, blue, alpha).lightmap(lightmap).endVertex();
            builder.pos(matrix, x2, center.getY(), z2).color(red, green, blue, alpha).lightmap(lightmap).endVertex();

            builder.pos(matrix, center.getX(), center.getY(), center.getZ()).color(red, green, blue, alpha).lightmap(lightmap).endVertex();
            builder.pos(matrix, x2, center.getY(), z2).color(red, green, blue, alpha).lightmap(lightmap).endVertex();
            builder.pos(matrix, x3, center.getY(), z3).color(red, green, blue, alpha).lightmap(lightmap).endVertex();

            builder.pos(matrix, center.getX(), center.getY(), center.getZ()).color(red, green, blue, alpha).lightmap(lightmap).endVertex();
            builder.pos(matrix, x3, center.getY(), z3).color(red, green, blue, alpha).lightmap(lightmap).endVertex();
            builder.pos(matrix, x4, center.getY(), z4).color(red, green, blue, alpha).lightmap(lightmap).endVertex();

            builder.pos(matrix, center.getX(), center.getY(), center.getZ()).color(red, green, blue, alpha).lightmap(lightmap).endVertex();
            builder.pos(matrix, x4, center.getY(), z4).color(red, green, blue, alpha).lightmap(lightmap).endVertex();
            builder.pos(matrix, x1, center.getY(), z1).color(red, green, blue, alpha).lightmap(lightmap).endVertex();
        }
    }

    public static void renderCircle(IVertexBuilder builder, Matrix4f matrix, Vector3f center, float radius, int segments, float red, float green, float blue, float alpha, boolean isBottom) {
        // 计算法线方向
        float normalY = isBottom ? -1.0f : 1.0f;

        for (int i = 0; i < segments; i++) {
            double angle1 = 2 * Math.PI * i / segments;
            double angle2 = 2 * Math.PI * (i + 1) / segments;
            double angle3 = 2 * Math.PI * (i + 2) / segments;
            double angle4 = 2 * Math.PI * (i + 3) / segments;

            float x1 = (float) Math.cos(angle1) * radius;
            float z1 = (float) Math.sin(angle1) * radius;
            float x2 = (float) Math.cos(angle2) * radius;
            float z2 = (float) Math.sin(angle2) * radius;

            float x3 = (float) Math.cos(angle3) * radius;
            float z3 = (float) Math.sin(angle3) * radius;
            float x4 = (float) Math.cos(angle4) * radius;
            float z4 = (float) Math.sin(angle4) * radius;

            // 三角形顶点：中心点，边缘点1，边缘点2
            builder.pos(matrix, center.getX(), center.getY(), center.getZ()).color(red, green, blue, alpha).normal(0.0f, normalY, 0.0f).endVertex();
            builder.pos(matrix, x1, center.getY(), z1).color(red, green, blue, alpha).normal(0.0f, normalY, 0.0f).endVertex();
            builder.pos(matrix, x2, center.getY(), z2).color(red, green, blue, alpha).normal(0.0f, normalY, 0.0f).endVertex();

            builder.pos(matrix, center.getX(), center.getY(), center.getZ()).color(red, green, blue, alpha).normal(0.0f, normalY, 0.0f).endVertex();
            builder.pos(matrix, x2, center.getY(), z2).color(red, green, blue, alpha).normal(0.0f, normalY, 0.0f).endVertex();
            builder.pos(matrix, x3, center.getY(), z3).color(red, green, blue, alpha).normal(0.0f, normalY, 0.0f).endVertex();

            builder.pos(matrix, center.getX(), center.getY(), center.getZ()).color(red, green, blue, alpha).normal(0.0f, normalY, 0.0f).endVertex();
            builder.pos(matrix, x3, center.getY(), z3).color(red, green, blue, alpha).normal(0.0f, normalY, 0.0f).endVertex();
            builder.pos(matrix, x4, center.getY(), z4).color(red, green, blue, alpha).normal(0.0f, normalY, 0.0f).endVertex();

            builder.pos(matrix, center.getX(), center.getY(), center.getZ()).color(red, green, blue, alpha).normal(0.0f, normalY, 0.0f).endVertex();
            builder.pos(matrix, x4, center.getY(), z4).color(red, green, blue, alpha).normal(0.0f, normalY, 0.0f).endVertex();
            builder.pos(matrix, x1, center.getY(), z1).color(red, green, blue, alpha).normal(0.0f, normalY, 0.0f).endVertex();
        }
    }

    private static void renderCylinderSides(IVertexBuilder vertexBuilder, Matrix4f matrix, float radius, float height,
                                            int segments, float red, float green, float blue, float alpha) {
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

            vertexBuilder.pos(matrix, x1, 0, z1 ).color(red, green, blue, alpha).normal(normalX, 0.0f, normalZ).endVertex();
            vertexBuilder.pos(matrix, x2, 0, z2).color(red, green, blue, alpha).normal(normalX, 0.0f, normalZ).endVertex();
            vertexBuilder.pos(matrix, x2, height, z2).color(red, green, blue, alpha).normal(normalX, 0.0f, normalZ).endVertex();
            vertexBuilder.pos(matrix, x1, height, z1).color(red, green, blue, alpha).normal(normalX, 0.0f, normalZ).endVertex();

            vertexBuilder.pos(matrix, x1, height, z1).color(red, green, blue, alpha).normal(normalX, 0.0f, normalZ).endVertex();
            vertexBuilder.pos(matrix, x2, height, z2).color(red, green, blue, alpha).normal(normalX, 0.0f, normalZ).endVertex();
            vertexBuilder.pos(matrix, x2, 0, z2).color(red, green, blue, alpha).normal(normalX, 0.0f, normalZ).endVertex();
            vertexBuilder.pos(matrix, x1, 0, z1).color(red, green, blue, alpha).normal(normalX, 0.0f, normalZ).endVertex();

        }
    }
    
    private static void renderCylinderSides(IVertexBuilder builder, Matrix4f matrix, Vector3f start, Vector3f end, float radius, float height, int segments, float red, float green, float blue, float alpha) {
        Vector3d direction = new Vector3d(end).subtract(new Vector3d(start)).normalize();

        // 计算一个垂直于方向的法线
        Vector3d perpendicular = new Vector3d(0, 1, 0);
        if (Math.abs(direction.dotProduct(perpendicular)) > 0.99) {
            perpendicular = new Vector3d(1, 0, 0);
        }
        Vector3d normal = perpendicular.crossProduct(direction).normalize();
        Vector3d binormal = direction.crossProduct(normal).normalize();
        for (int i = 0; i < segments; i++) {
            double theta0 = (2.0 * Math.PI / segments) * i;
            double theta1 = (2.0 * Math.PI / segments) * (i + 1);

            // 计算当前和下一个圆周上的点
            Vector3d p0 = normal.scale(Math.cos(theta0) * radius)
                    .add(binormal.scale(Math.sin(theta0) * radius))
                    .add(new Vector3d(start));

            Vector3d p1 = normal.scale(Math.cos(theta1) * radius)
                    .add(binormal.scale(Math.sin(theta1) * radius))
                    .add(new Vector3d(start));

            Vector3d p2 = normal.scale(Math.cos(theta1) * radius)
                    .add(binormal.scale(Math.sin(theta1) * radius))
                    .add(new Vector3d(end));

            Vector3d p3 = normal.scale(Math.cos(theta0) * radius)
                    .add(binormal.scale(Math.sin(theta0) * radius))
                    .add(new Vector3d(end));

            renderQuad(builder, matrix, p0, p1, p2, p3, red, green, blue, alpha);
            renderQuad(builder, matrix, p3, p2, p1, p0, red, green, blue, alpha);
        }
    }

    private static void renderQuad(IVertexBuilder builder, Matrix4f matrix, Vector3d p0, Vector3d p1, Vector3d p2, Vector3d p3,
                                   float red, float green, float blue, float alpha) {
        // 设置顶点，按逆时针顺序渲染四边面
        builder.pos(matrix, (float) p0.x, (float) p0.y, (float) p0.z).color(red, green, blue, alpha).endVertex();
        builder.pos(matrix, (float) p1.x, (float) p1.y, (float) p1.z).color(red, green, blue, alpha).endVertex();
        builder.pos(matrix, (float) p2.x, (float) p2.y, (float) p2.z).color(red, green, blue, alpha).endVertex();
        builder.pos(matrix, (float) p3.x, (float) p3.y, (float) p3.z).color(red, green, blue, alpha).endVertex();
    }

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

    private static void renderTriangle(Matrix4f matrix, IVertexBuilder vertexBuilder, float[] v1, float[] v2, float[] v3,
                                       float red, float green, float blue, float alpha) {
        addVertex(matrix, vertexBuilder, v1, red, green, blue, alpha);
        addVertex(matrix, vertexBuilder, v2, red, green, blue, alpha);
        addVertex(matrix, vertexBuilder, v3, red, green, blue, alpha);

        addVertex(matrix, vertexBuilder, v1, red, green, blue, alpha);
        addVertex(matrix, vertexBuilder, v3, red, green, blue, alpha);
        addVertex(matrix, vertexBuilder, v2, red, green, blue, alpha);

        addVertex(matrix, vertexBuilder, v3, red, green, blue, alpha);
        addVertex(matrix, vertexBuilder, v2, red, green, blue, alpha);
        addVertex(matrix, vertexBuilder, v1, red, green, blue, alpha);

        addVertex(matrix, vertexBuilder, v2, red, green, blue, alpha);
        addVertex(matrix, vertexBuilder, v1, red, green, blue, alpha);
        addVertex(matrix, vertexBuilder, v3, red, green, blue, alpha);
    }
    
    public static void renderCube(IVertexBuilder builder, Matrix4f matrix4f, Vector3f parameter, Vector3i color) {
        float x1 = 0, z1 = 0, y1 = 0;
        float x2 = parameter.getX(), y2 = parameter.getY(), z2 = parameter.getZ();
        addVertex(matrix4f, builder, x1, y1, z1, color.getX(), color.getY(), color.getZ());
        addVertex(matrix4f, builder, x2, y1, z1, color.getX(), color.getY(), color.getZ());
        addVertex(matrix4f, builder, x2, y2, z1, color.getX(), color.getY(), color.getZ());
        addVertex(matrix4f, builder, x1, y2, z1, color.getX(), color.getY(), color.getZ());

        addVertex(matrix4f, builder, x1, y1, z2, color.getX(), color.getY(), color.getZ());
        addVertex(matrix4f, builder, x2, y1, z2, color.getX(), color.getY(), color.getZ());
        addVertex(matrix4f, builder, x2, y2, z2, color.getX(), color.getY(), color.getZ());
        addVertex(matrix4f, builder, x1, y2, z2, color.getX(), color.getY(), color.getZ());

        addVertex(matrix4f, builder, x1, y1, z1, color.getX(), color.getY(), color.getZ());
        addVertex(matrix4f, builder, x1, y1, z2, color.getX(), color.getY(), color.getZ());
        addVertex(matrix4f, builder, x1, y2, z2, color.getX(), color.getY(), color.getZ());
        addVertex(matrix4f, builder, x1, y2, z1, color.getX(), color.getY(), color.getZ());

        addVertex(matrix4f, builder, x2, y1, z1, color.getX(), color.getY(), color.getZ());
        addVertex(matrix4f, builder, x2, y1, z2, color.getX(), color.getY(), color.getZ());
        addVertex(matrix4f, builder, x2, y2, z2, color.getX(), color.getY(), color.getZ());
        addVertex(matrix4f, builder, x2, y2, z1, color.getX(), color.getY(), color.getZ());

        addVertex(matrix4f, builder, x1, y2, z1, color.getX(), color.getY(), color.getZ());
        addVertex(matrix4f, builder, x2, y2, z1, color.getX(), color.getY(), color.getZ());
        addVertex(matrix4f, builder, x2, y2, z2, color.getX(), color.getY(), color.getZ());
        addVertex(matrix4f, builder, x1, y2, z2, color.getX(), color.getY(), color.getZ());

        addVertex(matrix4f, builder, x1, y1, z1, color.getX(), color.getY(), color.getZ());
        addVertex(matrix4f, builder, x2, y1, z1, color.getX(), color.getY(), color.getZ());
        addVertex(matrix4f, builder, x2, y1, z2, color.getX(), color.getY(), color.getZ());
        addVertex(matrix4f, builder, x1, y1, z2, color.getX(), color.getY(), color.getZ());
    }

    public static void renderCube(IVertexBuilder builder, Matrix4f matrix4f, Vector3f parameter, Vector3f startPos, Vector3f endPos, Vector3i color) {
        float x1 = startPos.getX(), z1 = startPos.getZ(), y1 = startPos.getY();
        float x2 = parameter.getX() + endPos.getX(), y2 = parameter.getY() + endPos.getY(), z2 = parameter.getZ() + endPos.getZ();
        addVertex(matrix4f, builder, x1, y1, z1, color.getX(), color.getY(), color.getZ());
        addVertex(matrix4f, builder, x2, y1, z1, color.getX(), color.getY(), color.getZ());
        addVertex(matrix4f, builder, x2, y2, z1, color.getX(), color.getY(), color.getZ());
        addVertex(matrix4f, builder, x1, y2, z1, color.getX(), color.getY(), color.getZ());

        addVertex(matrix4f, builder, x1, y1, z2, color.getX(), color.getY(), color.getZ());
        addVertex(matrix4f, builder, x2, y1, z2, color.getX(), color.getY(), color.getZ());
        addVertex(matrix4f, builder, x2, y2, z2, color.getX(), color.getY(), color.getZ());
        addVertex(matrix4f, builder, x1, y2, z2, color.getX(), color.getY(), color.getZ());

        addVertex(matrix4f, builder, x1, y1, z1, color.getX(), color.getY(), color.getZ());
        addVertex(matrix4f, builder, x1, y1, z2, color.getX(), color.getY(), color.getZ());
        addVertex(matrix4f, builder, x1, y2, z2, color.getX(), color.getY(), color.getZ());
        addVertex(matrix4f, builder, x1, y2, z1, color.getX(), color.getY(), color.getZ());

        addVertex(matrix4f, builder, x2, y1, z1, color.getX(), color.getY(), color.getZ());
        addVertex(matrix4f, builder, x2, y1, z2, color.getX(), color.getY(), color.getZ());
        addVertex(matrix4f, builder, x2, y2, z2, color.getX(), color.getY(), color.getZ());
        addVertex(matrix4f, builder, x2, y2, z1, color.getX(), color.getY(), color.getZ());

        addVertex(matrix4f, builder, x1, y2, z1, color.getX(), color.getY(), color.getZ());
        addVertex(matrix4f, builder, x2, y2, z1, color.getX(), color.getY(), color.getZ());
        addVertex(matrix4f, builder, x2, y2, z2, color.getX(), color.getY(), color.getZ());
        addVertex(matrix4f, builder, x1, y2, z2, color.getX(), color.getY(), color.getZ());

        addVertex(matrix4f, builder, x1, y1, z1, color.getX(), color.getY(), color.getZ());
        addVertex(matrix4f, builder, x2, y1, z1, color.getX(), color.getY(), color.getZ());
        addVertex(matrix4f, builder, x2, y1, z2, color.getX(), color.getY(), color.getZ());
        addVertex(matrix4f, builder, x1, y1, z2, color.getX(), color.getY(), color.getZ());
    }

    public static void renderCubeLightmap(IVertexBuilder builder, Matrix4f matrix4f, Vector3f parameter, Vector3f color, float alpha, int light) {
        float x1 = 0, z1 = 0, y1 = 0;
        float x2 = parameter.getX(), y2 = parameter.getY(), z2 = parameter.getZ();
        addVertexWithLight(matrix4f, builder, x1, y1, z1, color.getX(), color.getY(), color.getZ(), alpha, light);
        addVertexWithLight(matrix4f, builder, x2, y1, z1, color.getX(), color.getY(), color.getZ(), alpha, light);
        addVertexWithLight(matrix4f, builder, x2, y2, z1, color.getX(), color.getY(), color.getZ(), alpha, light);
        addVertexWithLight(matrix4f, builder, x1, y2, z1, color.getX(), color.getY(), color.getZ(), alpha, light);

        addVertexWithLight(matrix4f, builder, x1, y1, z2, color.getX(), color.getY(), color.getZ(), alpha, light);
        addVertexWithLight(matrix4f, builder, x2, y1, z2, color.getX(), color.getY(), color.getZ(), alpha, light);
        addVertexWithLight(matrix4f, builder, x2, y2, z2, color.getX(), color.getY(), color.getZ(), alpha, light);
        addVertexWithLight(matrix4f, builder, x1, y2, z2, color.getX(), color.getY(), color.getZ(), alpha, light);

        addVertexWithLight(matrix4f, builder, x1, y1, z1, color.getX(), color.getY(), color.getZ(), alpha, light);
        addVertexWithLight(matrix4f, builder, x1, y1, z2, color.getX(), color.getY(), color.getZ(), alpha, light);
        addVertexWithLight(matrix4f, builder, x1, y2, z2, color.getX(), color.getY(), color.getZ(), alpha, light);
        addVertexWithLight(matrix4f, builder, x1, y2, z1, color.getX(), color.getY(), color.getZ(), alpha, light);

        addVertexWithLight(matrix4f, builder, x2, y1, z1, color.getX(), color.getY(), color.getZ(), alpha, light);
        addVertexWithLight(matrix4f, builder, x2, y1, z2, color.getX(), color.getY(), color.getZ(), alpha, light);
        addVertexWithLight(matrix4f, builder, x2, y2, z2, color.getX(), color.getY(), color.getZ(), alpha, light);
        addVertexWithLight(matrix4f, builder, x2, y2, z1, color.getX(), color.getY(), color.getZ(), alpha, light);

        addVertexWithLight(matrix4f, builder, x1, y2, z1, color.getX(), color.getY(), color.getZ(), alpha, light);
        addVertexWithLight(matrix4f, builder, x2, y2, z1, color.getX(), color.getY(), color.getZ(), alpha, light);
        addVertexWithLight(matrix4f, builder, x2, y2, z2, color.getX(), color.getY(), color.getZ(), alpha, light);
        addVertexWithLight(matrix4f, builder, x1, y2, z2, color.getX(), color.getY(), color.getZ(), alpha, light);

        addVertexWithLight(matrix4f, builder, x1, y1, z1, color.getX(), color.getY(), color.getZ(), alpha, light);
        addVertexWithLight(matrix4f, builder, x2, y1, z1, color.getX(), color.getY(), color.getZ(), alpha, light);
        addVertexWithLight(matrix4f, builder, x2, y1, z2, color.getX(), color.getY(), color.getZ(), alpha, light);
        addVertexWithLight(matrix4f, builder, x1, y1, z2, color.getX(), color.getY(), color.getZ(), alpha, light);
    }

    private static void addVertex(Matrix4f matrix4f, IVertexBuilder builder, float x, float y, float z, int r, int g, int b) {
        addVertex(matrix4f, builder, x, y, z, r, g, b, 255);
    }

    private static void addVertex(Matrix4f matrix, IVertexBuilder vertexBuilder, float[] pos, float red, float green, float blue, float alpha) {
        vertexBuilder.pos(matrix, pos[0], pos[1], pos[2])
                .color(red, green, blue, alpha)
                .endVertex();
    }

    private static void addVertexWithLight(Matrix4f matrix, IVertexBuilder vertexBuilder, float x, float y, float z,
                                           float red, float green, float blue, float alpha, int light) {
        vertexBuilder.pos(matrix, x, y, z).lightmap(light)
                .color(red, green, blue, alpha)
                .endVertex();
    }

    private static void addVertex(Matrix4f matrix, IVertexBuilder vertexBuilder, float x, float y, float z, int red, int green, int blue, int alpha) {
        vertexBuilder.pos(matrix, x, y, z)
                .color(red, green, blue, alpha)
                .endVertex();
    }

    public static Vector3f of(double x, double y, double z) {
        return new Vector3f((float) x, (float) y, (float) z);
    }

    /**
     * 渲染三角面
     */
    public static void triangularFace(IVertexBuilder builder, Matrix4f matrix4f, float red, float green, float blue, float alpha,
                                      Vector3f a, Vector3f b, Vector3f c) {
        builder.pos(matrix4f, a.getX(), a.getY(), a.getZ()).color(red, green, blue, alpha).endVertex();
        builder.pos(matrix4f, b.getX(), b.getY(), b.getZ()).color(red, green, blue, alpha).endVertex();
        builder.pos(matrix4f, c.getX(), c.getY(), c.getZ()).color(red, green, blue, alpha).endVertex();

        builder.pos(matrix4f, c.getX(), c.getY(), c.getZ()).color(red, green, blue, alpha).endVertex();
        builder.pos(matrix4f, b.getX(), b.getY(), b.getZ()).color(red, green, blue, alpha).endVertex();
        builder.pos(matrix4f, a.getX(), a.getY(), a.getZ()).color(red, green, blue, alpha).endVertex();
    }

    public static void quadFace(IVertexBuilder builder, Matrix4f matrix, Vector3f leftUp, Vector3f rightUp, Vector3f rightDown, Vector3f leftDown, Vector4f color) {

        builder.pos(matrix, leftUp.getX(), leftUp.getY(), leftUp.getZ())
                .color(color.getX(), color.getY(), color.getZ(), color.getW()).endVertex();
        builder.pos(matrix, rightUp.getX(), rightUp.getY(), rightUp.getZ())
                .color(color.getX(), color.getY(), color.getZ(), color.getW()).endVertex();
        builder.pos(matrix, rightDown.getX(), rightDown.getY(), rightDown.getZ())
                .color(color.getX(), color.getY(), color.getZ(), color.getW()).endVertex();
        builder.pos(matrix, leftDown.getX(), leftDown.getY(), leftDown.getZ())
                .color(color.getX(), color.getY(), color.getZ(), color.getW()).endVertex();

        builder.pos(matrix, leftDown.getX(), leftDown.getY(), leftDown.getZ())
                .color(color.getX(), color.getY(), color.getZ(), color.getW()).endVertex();
        builder.pos(matrix, rightDown.getX(), rightDown.getY(), rightDown.getZ())
                .color(color.getX(), color.getY(), color.getZ(), color.getW()).endVertex();
        builder.pos(matrix, rightUp.getX(), rightUp.getY(), rightUp.getZ())
                .color(color.getX(), color.getY(), color.getZ(), color.getW()).endVertex();
        builder.pos(matrix, leftUp.getX(), leftUp.getY(), leftUp.getZ())
                .color(color.getX(), color.getY(), color.getZ(), color.getW()).endVertex();
    }

    public static void texturedCylinder(IVertexBuilder builder, MatrixStack matrixStack, int packedLight,
                                        float radius, float height,
                                        int segments, Color4i color) {
        Matrix4f matrix = matrixStack.getLast().getMatrix();
        Matrix3f normal = matrixStack.getLast().getNormal();
        for (int i = 0; i < segments; i++) {
            double angle1 = 2 * Math.PI * i / segments;
            double angle2 = 2 * Math.PI * (i + 1) / segments;

            float x1 = (float) Math.cos(angle1) * radius;
            float z1 = (float) Math.sin(angle1) * radius;
            float x2 = (float) Math.cos(angle2) * radius;
            float z2 = (float) Math.sin(angle2) * radius;

            float u0 = i / (float) segments;       // 当前分段起始U值
            float u1 = (i + 1) / (float) segments; // 当前分段结束U值
            float vBottom = 1.0f;                  // 底部V坐标 (1.0)
            float vTop = 0.0f;                     // 顶部V坐标 (0.0)

            Vector3f v3f = new Vector3f(0F,1F,0F);

            texVertex(matrix, normal, builder, packedLight, x1, 0, z1, u0, vBottom, v3f, color);
            texVertex(matrix, normal, builder, packedLight, x2, 0, z2, u1, vBottom, v3f, color);
            texVertex(matrix, normal, builder, packedLight, x2, height, z2, u1, vTop, v3f, color);
            texVertex(matrix, normal, builder, packedLight, x1, height, z1, u0, vTop, v3f, color);

//            builder.pos(matrix, x1, 0, z1)
//                    .color(color.r, color.g, color.b, color.a)
//                    .tex(u0, vBottom) // UV坐标
//                    .normal(normal, 0.0F, 1.0F, 0.0F)
//                    .lightmap(lightU, lightV)
//                    .overlay(OverlayTexture.NO_OVERLAY)
//                    .endVertex();
//
//            // 顶点2: 底部右前 (x2, 0, z2)
//            builder.pos(matrix, x2, 0, z2)
//                    .color(color.r, color.g, color.b, color.a)
//                    .tex(u1, vBottom) // UV坐标
//                    .normal(normal, 0.0F, 1.0F, 0.0F)
//                    .lightmap(lightU, lightV)
//                    .overlay(OverlayTexture.NO_OVERLAY)
//                    .endVertex();
//
//            // 顶点3: 顶部右前 (x2, height, z2)
//            builder.pos(matrix, x2, height, z2)
//                    .color(color.r, color.g, color.b, color.a)
//                    .tex(u1, vTop)   // UV坐标
//                    .normal(normal, 0.0F, 1.0F, 0.0F)
//                    .lightmap(lightU, lightV)
//                    .overlay(OverlayTexture.NO_OVERLAY)
//                    .endVertex();
//
//            // 顶点4: 顶部左前 (x1, height, z1)
//            builder.pos(matrix, x1, height, z1)
//                    .color(color.r, color.g, color.b, color.a)
//                    .tex(u0, vTop)   // UV坐标
//                    .normal(normal, 0.0F, 1.0F, 0.0F)
//                    .lightmap(lightU, lightV)
//                    .overlay(OverlayTexture.NO_OVERLAY)
//                    .endVertex();
        }
    }

    public static void texturedCircle(MatrixStack matrixStack, IVertexBuilder builder,
                                        int packedLight, Vector3f center, float radius, int segments, Color4i color) {
        Matrix4f matrix = matrixStack.getLast().getMatrix();
        for (int i = 0; i < segments; i++) {
            double angle1 = 2 * Math.PI * i / segments;
            double angle2 = 2 * Math.PI * (i + 1) / segments;
            double angle3 = 2 * Math.PI * (i + 2) / segments;
            double angle4 = 2 * Math.PI * (i + 3) / segments;

            float x1 = (float) Math.cos(angle1) * radius;
            float z1 = (float) Math.sin(angle1) * radius;
            float x2 = (float) Math.cos(angle2) * radius;
            float z2 = (float) Math.sin(angle2) * radius;

            float x3 = (float) Math.cos(angle3) * radius;
            float z3 = (float) Math.sin(angle3) * radius;
            float x4 = (float) Math.cos(angle4) * radius;
            float z4 = (float) Math.sin(angle4) * radius;

//            texVertex(matrix, matrixStack.getLast().getNormal(), builder, packedLight,
//                    center.getX(), center.getY(), center.getZ(), );

            // 三角形顶点：中心点，边缘点1，边缘点2
//            builder.pos(matrix, center.getX(), center.getY(), center.getZ()).color(color.r, color.g, color.b, color.a)
//                    .normal(0.0f, normalY, 0.0f).endVertex();
//            builder.pos(matrix, x1, center.getY(), z1).color(red, green, blue, alpha).normal(0.0f, normalY, 0.0f).endVertex();
//            builder.pos(matrix, x2, center.getY(), z2).color(red, green, blue, alpha).normal(0.0f, normalY, 0.0f).endVertex();
//
//            builder.pos(matrix, center.getX(), center.getY(), center.getZ()).color(red, green, blue, alpha).normal(0.0f, normalY, 0.0f).endVertex();
//            builder.pos(matrix, x2, center.getY(), z2).color(red, green, blue, alpha).normal(0.0f, normalY, 0.0f).endVertex();
//            builder.pos(matrix, x3, center.getY(), z3).color(red, green, blue, alpha).normal(0.0f, normalY, 0.0f).endVertex();
//
//            builder.pos(matrix, center.getX(), center.getY(), center.getZ()).color(red, green, blue, alpha).normal(0.0f, normalY, 0.0f).endVertex();
//            builder.pos(matrix, x3, center.getY(), z3).color(red, green, blue, alpha).normal(0.0f, normalY, 0.0f).endVertex();
//            builder.pos(matrix, x4, center.getY(), z4).color(red, green, blue, alpha).normal(0.0f, normalY, 0.0f).endVertex();
//
//            builder.pos(matrix, center.getX(), center.getY(), center.getZ()).color(red, green, blue, alpha).normal(0.0f, normalY, 0.0f).endVertex();
//            builder.pos(matrix, x4, center.getY(), z4).color(red, green, blue, alpha).normal(0.0f, normalY, 0.0f).endVertex();
//            builder.pos(matrix, x1, center.getY(), z1).color(red, green, blue, alpha).normal(0.0f, normalY, 0.0f).endVertex();
        }
    }

    private static void texVertex(Matrix4f matrix, Matrix3f normalMatrix, IVertexBuilder builder, int light,
                                  float x, float y, float z, float u, float v, Vector3f normal) {
        builder.pos(matrix, x, y, z)
                .color(255, 255, 255, 255)
                .tex(u, v)
                .overlay(OverlayTexture.NO_OVERLAY)
                .lightmap(light)
                .normal(normalMatrix, 0, 1, 0)
                .endVertex();
    }

    private static void texVertex(Matrix4f matrix, Matrix3f normalMatrix, IVertexBuilder builder, int light,
                                       float x, float y, float z, float u, float v, Vector3f normal, Color4i color) {
        builder.pos(matrix, x, y, z)
                .color(color.r, color.g, color.b, color.a)
                .tex(u, v)
                .overlay(OverlayTexture.NO_OVERLAY)
                .lightmap(light)
                .normal(normalMatrix, normal.getX(), normal.getY(), normal.getZ())
                .endVertex();
    }

    private static Vector3d cylPos(Vector3d center, Vector3d normal, Vector3d binormal,
                                   float radius, double theta) {
        return normal.scale(Math.cos(theta) * radius)
                .add(binormal.scale(Math.sin(theta) * radius))
                .add(center);
    }

    public static void renderCyl(IVertexBuilder builder, Matrix4f matrix,
                                 Vector3d start, Vector3d end,
                                 float radius, int segments,
                                 float red, float green, float blue, float alpha) {
        Vector3d direction = end.subtract(start);
        double length = direction.length();

        // 计算法线
        Vector3d normal = normal(direction);
        Vector3d binormal = direction.crossProduct(normal).normalize();

        for (int i = 0; i < segments; i++) {
            double theta0 = (2.0 * Math.PI / segments) * i;
            double theta1 = (2.0 * Math.PI / segments) * (i + 1);

            // 计算当前和下一个圆周上的点
            Vector3d p0 = cylPos(start, normal, binormal, radius, theta0);
            Vector3d p1 = cylPos(start, normal, binormal, radius, theta1);
            Vector3d p2 = cylPos(end, normal, binormal, radius, theta1);
            Vector3d p3 = cylPos(end, normal, binormal, radius, theta0);

            // 渲染四边形
            renderQuad(builder, matrix, p0, p1, p2, p3, red, green, blue, alpha);
            renderQuad(builder, matrix, p3, p2, p1, p0, red, green, blue, alpha);
        }
    }

    @SuppressWarnings("all")
    /**
     * 渲染四边形（使用三角形带）
     * @param builder 顶点构建器
     * @param matrix 变换矩阵
     * @param p0 点1
     * @param p1 点2
     * @param p2 点3
     * @param p3 点4
     * @param red 红色分量
     * @param green 绿色分量
     * @param blue 蓝色分量
     * @param alpha 透明度
     */
    private static void quad(IVertexBuilder builder, Matrix4f matrix,
                                   Vector3d p0, Vector3d p1, Vector3d p2, Vector3d p3,
                                   float red, float green, float blue, float alpha) {
        // 使用三角形带渲染四边形
        builder.pos(matrix, (float) p0.x, (float) p0.y, (float) p0.z)
                .color(red, green, blue, alpha)
                .endVertex();

        builder.pos(matrix, (float) p1.x, (float) p1.y, (float) p1.z)
                .color(red, green, blue, alpha)
                .endVertex();

        builder.pos(matrix, (float) p3.x, (float) p3.y, (float) p3.z)
                .color(red, green, blue, alpha)
                .endVertex();

        builder.pos(matrix, (float) p1.x, (float) p1.y, (float) p1.z)
                .color(red, green, blue, alpha)
                .endVertex();

        builder.pos(matrix, (float) p2.x, (float) p2.y, (float) p2.z)
                .color(red, green, blue, alpha)
                .endVertex();

        builder.pos(matrix, (float) p3.x, (float) p3.y, (float) p3.z)
                .color(red, green, blue, alpha)
                .endVertex();
    }

    private static Vector3d normal(Vector3d direction) {
        Vector3d up = new Vector3d(0, 1, 0);
        if (Math.abs(direction.dotProduct(up)) > 0.99) {
            up = new Vector3d(0, 0, 1);
        }
        return direction.crossProduct(up).normalize();
    }

    private static Vector3d binormal(Vector3d normal) {
        Vector3d up = new Vector3d(0, 1, 0);
        if (Math.abs(normal.dotProduct(up)) > 0.99) {
            up = new Vector3d(0, 0, 1);
        }
        return normal.crossProduct(up).normalize();
    }
}
