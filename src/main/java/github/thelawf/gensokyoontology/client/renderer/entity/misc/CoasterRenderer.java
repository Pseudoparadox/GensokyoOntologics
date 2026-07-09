package github.thelawf.gensokyoontology.client.renderer.entity.misc;

import com.mojang.blaze3d.matrix.MatrixStack;
import github.thelawf.gensokyoontology.client.model.CoasterModel;
import github.thelawf.gensokyoontology.common.entity.misc.CoasterVehicle;
import github.thelawf.gensokyoontology.common.entity.misc.RailEntity;
import github.thelawf.gensokyoontology.common.network.GSKONetworking;
import github.thelawf.gensokyoontology.common.util.GSKOUtil;
import github.thelawf.gensokyoontology.common.util.math.DerivativeInfo;
import github.thelawf.gensokyoontology.common.util.math.TimeDifferential;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MinecartRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

public class CoasterRenderer extends EntityRenderer<CoasterVehicle> {
    public static final ResourceLocation COASTER_TEXTURE = GSKOUtil.withRL("textures/entity/coaster_vehicle.png");
    private final CoasterModel model;
    public CoasterRenderer(EntityRendererManager renderManager) {
        super(renderManager);
        this.model = new CoasterModel();
    }

    @Nonnull
    @Override
    public ResourceLocation getEntityTexture(CoasterVehicle entity) {
        return COASTER_TEXTURE;
    }

    @Override
    public void render(CoasterVehicle entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        matrixStackIn.push();
        Vector3d renderPos = entityIn.getClientEyePosition(partialTicks)
                .subtract(0, entityIn.getEyeHeight(), 0)
                .subtract(entityIn.getPositionVec());
        matrixStackIn.translate(renderPos.x, renderPos.y - 1.25D, renderPos.z);
        matrixStackIn.rotate(entityIn.partialRotation);
        this.model.render(matrixStackIn, bufferIn.getBuffer(RenderType.getEntityTranslucentCull(COASTER_TEXTURE)),
                packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStackIn.pop();
    }
}
