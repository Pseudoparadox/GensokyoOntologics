package com.github.fictology.gensokyoontology.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4i;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 极简 Wavefront OBJ 解析器（仅 triangulate后的 .obj，不含smoothing group复杂处理）
 * 产出：Triangle列表 → 供实体 submitCustomGeometry 逐帧写入 buffer
 */
public class ObjMesh {

    public static class Triangle {
        public final Vector3f[] v = new Vector3f[3]; // 顶点
        public final Vector2f[] t = new Vector2f[3]; //
        public final Vector3f   n;            // face normal（用叉积算，不依赖obj的vn）
        public float kdR = 1f, kdG = 1f, kdB = 1f, d = 1f; // d=不透明度
        public Triangle() { n = new Vector3f(); }
    }

    public static class MtlInfo {
        String name;
        float kdR = 1f, kdG = 1f, kdB = 1f;
        float d = 1f;
    }

    private final List<Triangle> trises = new ArrayList<>();

    public List<Triangle> tris() { return trises; }

    public static ObjMesh load(Identifier modelPath) {
        var mesh = new ObjMesh();

        Map<String, MtlInfo> mtlLib = new LinkedHashMap<>();
        String pendingMtlFile = null;
        var positions = new ArrayList<Vector3f>();
        var uvs      = new ArrayList<Vector2f>();

        MtlInfo currentMtl = null;

        try {
            var resOpt = Minecraft.getInstance().getResourceManager().getResource(modelPath);
            if (resOpt.isEmpty()) throw new RuntimeException("Missing OBJ resource: " + modelPath);
            var res = resOpt.get();

            var br = new BufferedReader(new InputStreamReader(res.open(), StandardCharsets.UTF_8));
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;

                String[] p = line.split("\\s+");

                if (p[0].equals("mtllib")) {
                    String mtlFileName = p[1];
                    Identifier mtlId = resolveSibling(modelPath, mtlFileName);
                    try {
                        var mtlResOpt = Minecraft.getInstance().getResourceManager().getResource(mtlId);
                        if (mtlResOpt.isPresent()) {
                            mtlLib = parseMtl(mtlResOpt.get(), mtlLib);
                        }
                    } catch (Exception ignored) {
                    }
                    continue;
                }

                if (p[0].equals("usemtl")) {
                    String mtlName = p[1];
                    currentMtl = mtlLib.get(mtlName);
                    continue;
                }

                if (p[0].startsWith("o ") || p[0].startsWith("g ") ||
                        p[0].equals("o") || p[0].equals("g") ||
                        p[0].startsWith("s ")) {
                    continue;
                }

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
                        if (p.length != 4) continue;
                        int[] vi = new int[3];
                        int[] ti = new int[3];
                        for (int i = 0; i < 3; i++) {
                            String[] sp = p[i + 1].split("/");
                            vi[i] = Integer.parseInt(sp[0]) - 1;
                            ti[i] = (sp.length >= 2 && !sp[1].isEmpty())
                                    ? Integer.parseInt(sp[1]) - 1 : 0;
                        }
                        Triangle tri = new Triangle();
                        if (currentMtl != null) {
                            tri.kdR = currentMtl.kdR;
                            tri.kdG = currentMtl.kdG;
                            tri.kdB = currentMtl.kdB;
                            tri.d   = currentMtl.d;
                        }
                        for (int i = 0; i < 3; i++) {
                            tri.v[i] = new Vector3f(positions.get(vi[i]));
                            tri.t[i] = new Vector2f(
                                    uvs.size() > ti[i] ? uvs.get(ti[i]) : new Vector2f(0, 0));
                        }
                        Vector3f e1 = new Vector3f(tri.v[1]).sub(tri.v[0]);
                        Vector3f e2 = new Vector3f(tri.v[2]).sub(tri.v[0]);
                        tri.n.set(e1).cross(e2).normalize();
                        if (tri.n.lengthSquared() == 0) tri.n.set(0, 1, 0);
                        mesh.trises.add(tri);
                    }
                }
            }
            br.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return mesh;
    }

    public ByteBuffer toByteBuffer() {
        final int FLOATS_PER_VERT = 8; // 3+3+2
        final int STRIDE = FLOATS_PER_VERT * 4; // 32
        var bb = ByteBuffer.allocateDirect(trises.size() * 3 * STRIDE)
                .order(ByteOrder.nativeOrder());

        for (Triangle tri : trises) {
            for (int i = 0; i < 3; i++) {
                Vector3f p = tri.v[i];
                Vector3f n = tri.n;
                Vector2f u = tri.t[i];

                bb.putFloat(p.x);
                bb.putFloat(p.y);
                bb.putFloat(p.z);

                bb.putFloat(u.x);
                bb.putFloat(u.y);

                bb.putFloat(n.x);
                bb.putFloat(n.y);
                bb.putFloat(n.z);

            }
        }
        bb.rewind();
        return bb;
    }


    private static Map<String, MtlInfo> parseMtl(
            Resource mtlRes,
            Map<String, MtlInfo> out) throws IOException {

        MtlInfo cur = null;
        try (var br = new BufferedReader(
                new InputStreamReader(mtlRes.open(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                String[] tk = line.split("\\s+");

                switch (tk[0]) {
                    case "newmtl" -> {
                        if (cur != null) out.put(cur.name, cur);
                        cur = new MtlInfo();
                        cur.name = tk[1];
                    }
                    case "Kd" -> {
                        if (cur == null) break;
                        cur.kdR = floatOr(tk, 1, 1f);
                        cur.kdG = floatOr(tk, 2, 1f);
                        cur.kdB = floatOr(tk, 3, 1f);
                        if (tk.length <= 2) cur.kdG = cur.kdB = cur.kdR;
                    }
                    case "d" -> {
                        if (cur == null) break;
                        for (int i = 1; i < tk.length; i++)
                            try { cur.d = Float.parseFloat(tk[i]); break; }
                            catch (NumberFormatException ignored) {}
                    }
                    case "Tr" -> {
                        if (cur == null) break;
                        float tr = 0f;
                        for (int i = 1; i < tk.length; i++)
                            try { tr = Float.parseFloat(tk[i]); break; }
                            catch (NumberFormatException ignored) {}
                        cur.d = 1f - tr;
                    }

                }
            }
            if (cur != null) out.put(cur.name, cur);
        }
        return out;
    }

    private static float floatOr(String[] tk, int idx, float fallback) {
        if (idx < tk.length) try { return Float.parseFloat(tk[idx]); } catch (Exception ignored) {}
        return fallback;
    }

    /**
     * 根据 obj 的 Identifier，解析同级目录下的兄弟文件
     * 例：modelPath = "modid:models/chara/remilia.obj"
     *     sibling  = "remilia.mtl"
     *  ⇒  "modid:models/chara/remilia.mtl"
     */
    private static Identifier resolveSibling(Identifier modelPath, String siblingFileName) {
        String full = modelPath.getPath();       // e.g. "models/chara/remilia.obj"
        int slash = full.lastIndexOf('/');
        String dir = slash >= 0 ? full.substring(0, slash + 1) : ""; // "models/chara/"
        return Identifier.fromNamespaceAndPath(modelPath.getNamespace(), dir + siblingFileName);
    }

    public int vertexCount() {
        return trises.size() * 3;
    }

    public int stride() {
        return 32;
    }


    public void renderMtl(PoseStack.Pose pose, VertexConsumer vc) {
        for (Triangle tri : trises) {
            var r = tri.kdR;
            var g = tri.kdG;
            var b = tri.kdB;
            var a = tri.d;          // d = 不透明度

            for (int i = 0; i < 3; i++) {
                vc.addVertex(pose, tri.v[i].x, tri.v[i].y, tri.v[i].z)
                        .setColor(r, g, b, a)
                        .setUv(tri.t[i].x, tri.t[i].y)
                        .setNormal(tri.n.x, tri.n.y, tri.n.z);
            }
        }
    }

    public void render(PoseStack.Pose pose, VertexConsumer vc) {
        for (Triangle triangle : trises) {
            for (int i = 0; i < 3; i++) {
                vc.addVertex(pose, triangle.v[i].x, triangle.v[i].y, triangle.v[i].z)
                        .setUv(triangle.t[i].x, triangle.t[i].y)
                        .setNormal(triangle.n.x, triangle.n.y, triangle.n.z);
            }
        }
    }

    public void render(PoseStack.Pose pose, VertexConsumer vc, Vector4i vertColor) {
        for (Triangle triangle : trises) {
            for (int i = 0; i < 3; i++) {
                vc.addVertex(pose, triangle.v[i].x, triangle.v[i].y, triangle.v[i].z)
                        .setColor(vertColor.x, vertColor.y, vertColor.z, vertColor.w)
                        .setUv(triangle.t[i].x, triangle.t[i].y)
                        .setNormal(triangle.n.x, triangle.n.y, triangle.n.z);
            }
        }
    }

    public void render(PoseStack.Pose pose, VertexConsumer vc, boolean uv) {
        for (Triangle triangle : trises) {
            for (int i = 0; i < 3; i++) {
                vc.addVertex(pose, triangle.v[i].x, triangle.v[i].y, triangle.v[i].z)
                        .setColor(255, 255, 255, 255)
                        .setUv(triangle.t[i].x, triangle.t[i].y)
                        .setUv2(0, 0)
                        .setNormal(triangle.n.x, triangle.n.y, triangle.n.z);
            }
        }
    }

    public ObjMesh vertex(PoseStack.Pose pose, VertexConsumer vc, AtomicReference<VertexConsumer> ref) {
        for (Triangle triangle : trises) {
            for (int i = 0; i < 3; i++) {
                vc.addVertex(pose, triangle.v[i].x, triangle.v[i].y, triangle.v[i].z);
                ref.set(vc);
            }
        }
        return this;
    }

    public ObjMesh uv(PoseStack.Pose pose, VertexConsumer vc, AtomicReference<VertexConsumer> ref) {
        for (Triangle triangle : trises) {
            for (int i = 0; i < 3; i++) {
                vc.setUv(triangle.t[i].x, triangle.t[i].y);
                ref.set(vc);
            }
        }
        return this;
    }

    public ObjMesh uv2(VertexConsumer vc, AtomicReference<VertexConsumer> ref) {
        for (Triangle triangle : trises) {
            for (int i = 0; i < 3; i++) {
                vc.setUv2(1, 1);
                ref.set(vc);
            }
        }
        return this;
    }

    public ObjMesh color(VertexConsumer vc, AtomicReference<VertexConsumer> ref) {
        for (Triangle triangle : trises) {
            for (int i = 0; i < 3; i++) {
                vc.setColor(1F, 1F, 1F, 1F);
                ref.set(vc);
            }
        }
        return this;
    }
    public void build(PoseStack.Pose pose, VertexConsumer vc){
        this.render(pose, vc);
    }
}