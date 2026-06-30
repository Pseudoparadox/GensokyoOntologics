package github.thelawf.gensokyoontology.client.renderer.entity.misc;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import github.thelawf.gensokyoontology.api.Color4i;
import github.thelawf.gensokyoontology.common.entity.misc.LunarFall;
import github.thelawf.gensokyoontology.common.util.GSKOUtil;
import github.thelawf.gensokyoontology.common.util.math.GSKOMathUtil;
import github.thelawf.gensokyoontology.common.util.math.GeometryUtil;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import org.jetbrains.annotations.NotNull;

public class LunarFallRenderer extends EntityRenderer<LunarFall> {

    public static final ResourceLocation BEAM_TEX = GSKOUtil.withRL("textures/entity/beam_cutout.png");
    public static final ResourceLocation IMPACT_TEX = GSKOUtil.withRL("textures/entity/impact.png");

    public LunarFallRenderer(EntityRendererManager renderManager) {
        super(renderManager);
    }

    @Override
    public @NotNull ResourceLocation getEntityTexture(LunarFall entity) {
        int a = 0;
        return entity.ticksExisted < LunarFall.PREPARATION ? BEAM_TEX : IMPACT_TEX;
    }

    @Override
    public void render(LunarFall entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        int ticks = entityIn.ticksExisted;
        int impactTicks = LunarFall.MAX_TICK - LunarFall.PREPARATION;
        int presentTicks = ticks - LunarFall.PREPARATION;

        float height = GSKOMathUtil.lerpTicks(partialTicks, LunarFall.PREPARATION, ticks, 0, -50);
        float radius = GSKOMathUtil.lerpTicks(partialTicks, impactTicks, presentTicks,0, LunarFall.MAX_RADIUS);

        if (ticks > LunarFall.FALL_START && ticks <= LunarFall.PREPARATION) {
            matrixStackIn.push();
            matrixStackIn.translate(0, 50, 0);
            IVertexBuilder beamVertex = bufferIn.getBuffer(RenderType.getEntityTranslucentCull(this.getEntityTexture(entityIn)));
            GSKOMathUtil.rotateMatrixToLookVec(matrixStackIn, new Vector3d(0, -1, 0));
//            GeometryUtil.renderCylinder(bufferIn.getBuffer(RenderType.getLightning()), matrixStackIn.getLast().getMatrix(),
//                     16, 0.2F, height, 140F/ 255, 90F / 255, 230F / 255, 180F / 255);

            GeometryUtil.renderCylinder(bufferIn.getBuffer(RenderType.getLightning()), matrixStackIn.getLast().getMatrix(),
                    6, .58F, height, 1, 1, 1, 1);
            GeometryUtil.texturedCylinder(beamVertex, matrixStackIn, packedLightIn,
                    .6F, height, 6, Color4i.of(140, 90, 230, 150));
            matrixStackIn.pop();
            return;
        }

        if (ticks > LunarFall.PREPARATION) {
            matrixStackIn.push();
            matrixStackIn.translate(0, 0.5F, 0);
            matrixStackIn.rotate(Vector3f.XP.rotationDegrees(90));
            matrixStackIn.scale(radius, radius, radius);
            IVertexBuilder impactVert = bufferIn.getBuffer(RenderType.getEntityTranslucentCull(this.getEntityTexture(entityIn)));
            this.renderImpact(matrixStackIn.getLast().getMatrix(), impactVert, packedLightIn);
            matrixStackIn.pop();
        }

    }

    private void renderImpact(Matrix4f matrix, IVertexBuilder builder, int packedLightIn) {
        this.vertex(matrix, builder, -0.5f, -0.5f, 0, 0f, 1f, packedLightIn);
        this.vertex(matrix, builder, -0.5f,  0.5f, 0, 0f, 0f, packedLightIn);
        this.vertex(matrix, builder,  0.5f,  0.5f, 0, 1f, 0f, packedLightIn);
        this.vertex(matrix, builder,  0.5f, -0.5f, 0, 1f, 1f, packedLightIn);

        this.vertex(matrix, builder,  0.5f, -0.5f, 0, 1f, 1f, packedLightIn);
        this.vertex(matrix, builder,  0.5f,  0.5f, 0, 1f, 0f, packedLightIn);
        this.vertex(matrix, builder, -0.5f,  0.5f, 0, 0f, 0f, packedLightIn);
        this.vertex(matrix, builder, -0.5f, -0.5f, 0, 0f, 1f, packedLightIn);
    }

    public void vertex(Matrix4f matrix, IVertexBuilder builder, float x, float y, float z, float u, float v, int packedLight) {
        builder.pos(matrix, x, y, z)
                .color(255, 255, 255, 255) // 白色，无颜色变化
                .tex(u, v)
                .overlay(OverlayTexture.NO_OVERLAY)
                .lightmap(packedLight) // 光照信息
                .normal(0, 1, 0) // 法向量（不重要，因为是2D）
                .endVertex();
    }
}
