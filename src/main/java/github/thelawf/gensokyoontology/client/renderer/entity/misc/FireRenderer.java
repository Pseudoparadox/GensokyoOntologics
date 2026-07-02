package github.thelawf.gensokyoontology.client.renderer.entity.misc;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import github.thelawf.gensokyoontology.common.entity.misc.FireEntity;
import github.thelawf.gensokyoontology.common.util.GSKOUtil;
import github.thelawf.gensokyoontology.common.util.math.GSKOMathUtil;
import github.thelawf.gensokyoontology.common.util.math.GeometryUtil;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;

import java.awt.*;

public class FireRenderer extends EntityRenderer<FireEntity> {

    public static final ResourceLocation TEXTURE = GSKOUtil.withRL("textures/entity/fire");

    protected FireRenderer(EntityRendererManager renderManager) {
        super(renderManager);
    }

    @Override
    public ResourceLocation getEntityTexture(FireEntity entity) {
        return TEXTURE;
    }

    @Override
    public void render(FireEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);

        float radius = GSKOMathUtil.lerpTicks(partialTicks, 45, entityIn.ticksExisted, 0.8F, 0F);
        float pos = GSKOMathUtil.lerpTicks(partialTicks, 45, entityIn.ticksExisted, 0F, 1.2F);
        Color whiteLight = GSKOMathUtil.kelvinToRGB(6500);
        Color warmLight = GSKOMathUtil.kelvinToRGB(2600);
        float r = GSKOMathUtil.lerpTicks(partialTicks, 45, entityIn.ticksExisted, whiteLight.getRed() / 255F, warmLight.getRed() / 255F);
        float g = GSKOMathUtil.lerpTicks(partialTicks, 45, entityIn.ticksExisted, whiteLight.getGreen() / 255F, warmLight.getGreen() / 255F);
        float b = GSKOMathUtil.lerpTicks(partialTicks, 45, entityIn.ticksExisted, whiteLight.getBlue() / 255F, warmLight.getBlue() / 255F);

        IVertexBuilder builder = bufferIn.getBuffer(RenderType.getLightning());
        GeometryUtil.renderCircle(builder, matrixStackIn.getLast().getMatrix(), 6, new Vector3f(0,0,0), radius,
                r, g, b, 0.9F, false);
    }
}
