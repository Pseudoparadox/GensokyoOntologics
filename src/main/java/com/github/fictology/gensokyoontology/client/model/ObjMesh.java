package com.github.fictology.gensokyoontology.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.resources.Identifier;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 极简 Wavefront OBJ 解析器（仅 triangulate后的 .obj，不含smoothing group复杂处理）
 * 产出：Triangle列表 → 供实体 submitCustomGeometry 逐帧写入 buffer
 */
public class ObjMesh implements SubmitNodeCollector.CustomGeometryRenderer {

    public static class Triangle {
        public final Vector3f[] v = new Vector3f[3]; // 顶点
        public final Vector2f[] t = new Vector2f[3]; //
        public final Vector3f   n;            // face normal（用叉积算，不依赖obj的vn）
        public Triangle() { n = new Vector3f(); }
    }

    private final List<Triangle> trises = new ArrayList<>();

    public List<Triangle> tris() { return trises; }

    // ---------- 加载 ----------
    public static ObjMesh load(Identifier modelPath) {
        var mesh = new ObjMesh();
        var res = Minecraft.getInstance().getResourceManager().getResource(modelPath).orElseThrow();
        var positions = new ArrayList<Vector3f>();
        var uvs       = new ArrayList<Vector2f>();
        // vn 暂不读（我们叉积算normal）

        try {
            var br = new BufferedReader(new InputStreamReader(res.open(), StandardCharsets.UTF_8));
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#") || line.startsWith("mtllib") || line.startsWith("usemtl") || line.startsWith("o ") || line.startsWith("g "))
                    continue;

                String[] p = line.split("\\s+");

                switch (p[0]) {
                    case "v" -> {
                        float x = Float.parseFloat(p[1]);
                        float y = Float.parseFloat(p[2]);
                        float z = Float.parseFloat(p[3]);
                        positions.add(new Vector3f(x, y, z));
                    }
                    case "vt" -> {
                        float u = Float.parseFloat(p[1]);
                        float v = p.length > 2 ? Float.parseFloat(p[2]) : 0f;
                        uvs.add(new Vector2f(u, v));
                    }
                    case "f" -> {
                        // 只接受 triangulated: f v1/vt1 v2/vt2 v3/vt3
                        if (p.length != 4) continue;
                        int[] vi = new int[3];
                        int[] ti = new int[3];
                        for (int i = 0; i < 3; i++) {
                            String[] sp = p[i + 1].split("/");
                            vi[i] = Integer.parseInt(sp[0]) - 1;              // 1-indexed
                            ti[i] = (sp.length >= 2 && !sp[1].isEmpty())
                                    ? Integer.parseInt(sp[1]) - 1 : 0;
                        }
                        Triangle triangle = new Triangle();
                        for (int i = 0; i < 3; i++) {
                            triangle.v[i] = new Vector3f(positions.get(vi[i]));
                            triangle.t[i] = new Vector2f(uvs.size() > ti[i] ? uvs.get(ti[i]) : new Vector2f(0, 0));
                        }
                        // 面法线（JOML叉积）
                        Vector3f e1 = new Vector3f(triangle.v[1]).sub(triangle.v[0]);
                        Vector3f e2 = new Vector3f(triangle.v[2]).sub(triangle.v[0]);
                        triangle.n.set(e1).cross(e2).normalize();
                        if (triangle.n.lengthSquared() == 0) triangle.n.set(0, 1, 0);
                        mesh.trises.add(triangle);
                    }
                }
            }
            br.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return mesh;
    }

    public ByteBuffer toByteBufferPosNormTex() {
        final int FLOATS_PER_VERT = 8; // 3+3+2
        final int STRIDE = FLOATS_PER_VERT * 4; // 32
        var bb = ByteBuffer.allocateDirect(trises.size() * 3 * STRIDE)
                .order(ByteOrder.nativeOrder());

        for (Triangle tri : trises) {
            for (int i = 0; i < 3; i++) {
                Vector3f p = tri.v[i];
                Vector3f n = tri.n;          // 你加载时已经算好了面法线
                Vector2f u = tri.t[i];

                // P
                bb.putFloat(p.x);
                bb.putFloat(p.y);
                bb.putFloat(p.z);
                // UV
                bb.putFloat(u.x);
                bb.putFloat(u.y);
                // N
                bb.putFloat(n.x);
                bb.putFloat(n.y);
                bb.putFloat(n.z);

            }
        }
        bb.rewind();
        return bb;
    }

    public int vertexCount() {
        return trises.size() * 3;
    }

    public int stride() {
        return 32;
    }

    // ============================================================
    //  写入 VertexConsumer —— 对接 submitCustomGeometry 的回调
    // ============================================================
    public void render(PoseStack.Pose pose, VertexConsumer vc) {
        for (Triangle triangle : trises) {
            for (int i = 0; i < 3; i++) {
                // VertexConsumer 对应 POSITION_TEX_COLOR layout:
                // vertex(posX,posY,posZ,  colorRGBA,  u,v,   packedOverlay?)
                // 下面用最常用的 4-float color=white, overlay=0 的写法
                vc.addVertex(pose, triangle.v[i].x, triangle.v[i].y, triangle.v[i].z)
                        .setColor(255, 255, 255, 255)
                        .setUv(triangle.t[i].x, triangle.t[i].y)
                        .setNormal(triangle.n.x, triangle.n.y, triangle.n.z)
                        .setLight(0xF000F0)          // 全亮（你也可以从packedLight传进来）
                        .setOverlay(0);
            }
        }
    }
}