package github.thelawf.gensokyoontology.client.renderer.entity.misc;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import github.thelawf.gensokyoontology.api.Color4f;
import github.thelawf.gensokyoontology.api.Color4i;
import github.thelawf.gensokyoontology.api.util.Maybe;
import github.thelawf.gensokyoontology.client.GSKORenderTypes;
import github.thelawf.gensokyoontology.common.entity.misc.RailEntity;
import github.thelawf.gensokyoontology.common.util.GSKOUtil;
import github.thelawf.gensokyoontology.common.util.math.*;
import github.thelawf.gensokyoontology.core.init.ItemRegistry;
import github.thelawf.gensokyoontology.data.HermiteNodeInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.*;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class RailRenderer extends EntityRenderer<RailEntity> {
    public static final double RAIL_WIDTH = 0.5;
    public static final float RAIL_RADIUS = 0.07F;
    public static final int SEGMENTS = 32;
    public static final float SCALE = 20F;
    // 枕木间距（方块）
    public final float SLEEPER_SPACING = 1.5F;

    public static final Minecraft MINECRAFT = Minecraft.getInstance();

    public static final ResourceLocation TEXTURE = GSKOUtil.withRL("textures/entity/entity_blank.png");

    public RailRenderer(EntityRendererManager manager) {
        super(manager);
    }

    @NotNull
    @Override
    public ResourceLocation getEntityTexture(@NotNull RailEntity entity) {
        return TEXTURE;
    }

    @Override
    public void render(@NotNull RailEntity startRail, float entityYaw, float partialTicks, @NotNull MatrixStack matrixStack, IRenderTypeBuffer bufferIn, int light) {
        super.render(startRail, entityYaw, partialTicks, matrixStack, bufferIn, light);
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getTextureManager().bindTexture(TEXTURE);
        ClientPlayerEntity player = minecraft.player;

        IVertexBuilder builder = bufferIn.getBuffer(RenderType.getEntityCutoutNoCull(TEXTURE));
        IVertexBuilder buffer  = bufferIn.getBuffer(GSKORenderTypes.MULTI_FACE_SOLID);

        Maybe<Entity> maybe = startRail.getNextRailClient(Maybe.empty());
        Color4f railColor = startRail.getInfo().color.toColor4f();
        Predicate<PlayerEntity> holdWrench = p ->
                p.getHeldItemMainhand().getItem() == ItemRegistry.RAIL_WRENCH.get();
        Predicate<PlayerEntity> holdConnector = p ->
                p.getHeldItemMainhand().getItem() == ItemRegistry.RAIL_CONNECTOR.get();

        Predicate<PlayerEntity> holdTracks = p ->
                p.getHeldItemMainhand().getItem() == ItemRegistry.TRACK_PLACER.get() ||
                        p.getHeldItemMainhand().getItem() == ItemRegistry.TRACK_REMOVER.get();
        if (player != null && holdTracks.or(holdConnector).test(player)) this.renderFacingArrow(startRail, builder, matrixStack);
        if (!maybe.isPresent()) {
            this.renderUnconnectedTrack(buffer, matrixStack, startRail);
            return;
        }
        if (!(maybe.get() instanceof RailEntity)) return;


        if (player != null && holdTracks.or(holdWrench).or(holdConnector).test(player)){
            this.renderUnconnectedTrack(buffer, matrixStack, startRail);
        }

        if (player == null) railColor = Color4i.MAGENTA.toColor4f();
        if (player != null && holdTracks.or(holdWrench).or(holdConnector).negate().test(player)) railColor = Color4i.RED.toColor4f();
        Color4f color = railColor;

        maybe.ifPresent(nextRail -> this.renderHermite3(startRail, (RailEntity) nextRail, color, builder, matrixStack));
        if (player != null && holdWrench.test(player)) this.renderRotateFrame(startRail, bufferIn, matrixStack, light);
//        RailEntity targetRail = (RailEntity) maybe.get();
//        this.renderHermite3(startRail, targetRail, builder, matrixStack);
    }

    private void renderRotateFrame(RailEntity rail, IRenderTypeBuffer buffer, @NotNull MatrixStack matrixStack, int light) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        matrixStack.push();
        matrixStack.rotate(rail.getRotation());
        itemRenderer.renderItem(ItemRegistry.ROTATE_FRAME.get().getDefaultInstance(),
                ItemCameraTransforms.TransformType.GUI,
                light, OverlayTexture.NO_OVERLAY, matrixStack, buffer);
        matrixStack.pop();
    }

    public void renderHermite3(RailEntity startRail, RailEntity targetRail, Color4f railColor, IVertexBuilder builder, MatrixStack matrixStack) {
        // 1. 获取世界坐标下的起点和终点
        Vector3d startPos = startRail.getPositionVec();
        Vector3d endPos = targetRail.getPositionVec();

        Vector3d startOffset = Vector3d.ZERO;
        Vector3d endOffset = endPos.subtract(startPos);

        // 3. 获取旋转信息
        Quaternion startRot = startRail.getRotation();
        Quaternion endRot = targetRail.getRotation();

        // 预计算全局桶滚角度变化范围
        float maxRollChange = 180.0f;
        float startRoll = RotMatrix.from(startRot).toEulerAngle().roll();
        float endRoll = RotMatrix.from(endRot).toEulerAngle().roll();
        float rollDelta = MathHelper.wrapDegrees(endRoll - startRoll);
        if (Math.abs(rollDelta) > maxRollChange) {
            rollDelta = Math.signum(rollDelta) * maxRollChange;
        }

        final int segments = 32;
        // 保存初始矩阵状态，以便在渲染完这一整条轨道后恢复
        matrixStack.push();
        HermiteNodeInfo nodeInfo = HermiteNodeInfo.of(RailEntity.Info.DECELERATION,
                        startRail.getPosition(), targetRail.getPosition().subtract(startRail.getPosition()),
                        startRail.getRotation(), targetRail.getRotation())
                .setPrevScale(startRail.isAutoScale() ? (int) rescaleTangent(startPos, endPos) : startRail.getScale0())
                .setNextScale(startRail.isAutoScale() ? (int) rescaleTangent(startPos, endPos) : targetRail.getScale1());
        this.renderSegments(segments, railColor, builder, matrixStack, startRoll, rollDelta, nodeInfo);
        this.renderSleepers(builder, matrixStack, new Color4f(0.62F, 0,0,1), nodeInfo);
        matrixStack.pop();
    }

    public void renderSegments(int segments, Color4f railColor,  IVertexBuilder builder, MatrixStack matrixStack,
                               float startRoll, float rollDelta, HermiteNodeInfo node){
        Vector3d prevLeft = null;
        Vector3d prevRight = null;
        Vector3d prevGirder = null;

        Vector3d startOffset    = Vector3d.ZERO;
        Vector3d endOffset      = node.getEndOffset();
        Vector3f startDirection = node.prevOrientation();
        Vector3f endDirection   = node.nextOrientation();
        Quaternion startRot     = node.rotation0();
        Quaternion endRot       = node.rotation1();

        for (int i = 0; i < segments; i++) {
            float t0 = (float) i / segments;
            float t1 = (float) (i + 1) / segments;
            float currentRoll = startRoll + t0 * rollDelta;

            // 使用相对偏移量计算曲线点
            Vector3d prev = CurveUtil.hermite3(startOffset, endOffset, startDirection, endDirection, t0);
            Vector3d next = CurveUtil.hermite3(startOffset, endOffset, startDirection, endDirection, t1);

            Quaternion rotation = GSKOMathUtil.slerp(startRot, endRot, t0);
            RotMatrix rotMatrix = new RotMatrix(rotation);

            // 应用桶滚旋转到法线方向
            Vector3f normal = rotMatrix.normal(); // 获取法线（X轴）
            Vector3f binormal = rotMatrix.binormal(); // 获取副法线（Y轴）
            Vector3f tangentAxis = rotMatrix.tangent(); // 获取切线（Z轴）

            // 创建桶滚四元数并应用到法线
            Quaternion roll = new Quaternion(tangentAxis, currentRoll, true);
            Vector3f rolledNormal = GSKOMathUtil.rotateVector(roll, normal);

            // 重新构建旋转矩阵
            RotMatrix matrix = new RotMatrix();
            matrix.setColumn(0, new Vector3d(rolledNormal));     // X轴 = 滚动后的法线
            matrix.setColumn(1, new Vector3d(binormal));         // Y轴 = 副法线
            matrix.setColumn(2, new Vector3d(tangentAxis));      // Z轴 = 切线

            Vector3d offset      = new Vector3d(0, 0.2, 0);
            Vector3d leftPivot   = new Vector3d(-RAIL_WIDTH,0, 0);
            Vector3d rightPivot  = new Vector3d(RAIL_WIDTH, 0, 0);
            Vector3d girderOffset = new Vector3d(0, 0, 0);

            // 转换到世界坐标（因为外层 matrixStack 已经 translate 过了，这里只需乘 rotMatrix）
            Vector3d leftStart = prevLeft == null ? matrix.multiply(leftPivot).add(prev).add(offset) : prevLeft;
            Vector3d rightStart = prevRight == null ? matrix.multiply(rightPivot).add(prev).add(offset) : prevRight;
            Vector3d leftEnd = matrix.multiply(leftPivot).add(next).add(offset);
            Vector3d rightEnd = matrix.multiply(rightPivot).add(next).add(offset);

            Vector3d girderStart = prevGirder == null ? rotMatrix.multiply(girderOffset).add(prev) : prevGirder;
            Vector3d girderNext = rotMatrix.multiply(girderOffset).add(next);

            float scaleLeft = (float) (1F / segments / (leftStart.distanceTo(leftEnd)));
            float scaleRight = (float) (1F / segments / (rightStart.distanceTo(rightEnd)));

            // 左轨右轨
            GeometryUtil.renderClippedCylinder(builder, matrixStack.getLast().getMatrix(),
                    leftStart.scale(node.shouldFlipNormal() ? -1 : 1), leftEnd.scale(node.shouldFlipNormal() ? -1 : 1),
                    new Vector3d(rolledNormal),  RAIL_RADIUS, 8, railColor);
            GeometryUtil.renderClippedCylinder(builder, matrixStack.getLast().getMatrix(),
                    rightStart.scale(node.shouldFlipNormal() ? -1 : 1), rightEnd.scale(node.shouldFlipNormal() ? -1 : 1),
                    new Vector3d(rolledNormal), RAIL_RADIUS, 8, railColor);

            // 承重轨
            GeometryUtil.renderClippedCylinder(builder, matrixStack.getLast().getMatrix(),
                    girderStart, girderNext, new Vector3d(rolledNormal), RAIL_RADIUS, 4,
                    new Color4f(0.82F, 0, 0, 1));

            prevLeft = leftEnd;
            prevRight = rightEnd;
            prevGirder = girderNext;
        }
    }

    public void renderSleepers(IVertexBuilder builder, MatrixStack matrixStackIn, Color4f color, HermiteNodeInfo node){
        // 计算样条曲线总长度
        float totalLength = CurveUtil.hermiteLength(node, 0.0F, 1.0F, SEGMENTS);
        int count = (int) Math.ceil(totalLength / SLEEPER_SPACING);
        matrixStackIn.push();
        for (int i = 0; i < count; i++) {
            float arc0 = i / (count - 1F) * totalLength;
            float arc1 = (i + 1) / (count - 1F) * totalLength;
            float t0 = CurveUtil.hermiteProgress(node, arc0, SEGMENTS);
            float t1 = CurveUtil.hermiteProgress(node, arc1, SEGMENTS);

            Vector3d prev = CurveUtil.hermite3(Vector3d.ZERO, node.getEndOffset(), node.prevOrientation(), node.nextOrientation(), t0);
            Vector3d next = CurveUtil.hermite3(Vector3d.ZERO, node.getEndOffset(), node.prevOrientation(), node.nextOrientation(), t1);

            Vector3d tangent = CurveUtil.hermiteTangent(Vector3d.ZERO, node.getEndOffset(),
                    node.orientation0(), node.orientation1(), t0).normalize();

            Vector3d leftUp    = new Vector3d(-RAIL_WIDTH, 0, 0);
            Vector3d rightUp   = new Vector3d(RAIL_WIDTH, 0, 0);
            Vector3d rightDown = new Vector3d(RAIL_WIDTH - 0.1F, -0.25F, 0);
            Vector3d leftDown  = new Vector3d(-RAIL_WIDTH + 0.1F, -0.25F, 0);

            Quaternion rotation = GSKOMathUtil.slerp(node.rotation0(), node.rotation1(), t0);
            RotMatrix rotMatrix = RotMatrix.from(rotation);
            Vector3d offset = rotMatrix.multiply(new Vector3d(0, 0.1, 0));

            GeometryUtil.quadFace(builder, matrixStackIn.getLast().getMatrix(),
                    new Vector3f(rotMatrix.multiply(leftUp).add(prev).add(offset)),
                    new Vector3f(rotMatrix.multiply(rightUp).add(prev).add(offset)),
                    new Vector3f(rotMatrix.multiply(rightDown).add(prev).add(offset)),
                    new Vector3f(rotMatrix.multiply(leftDown).add(prev).add(offset)),
                    new Vector4f(color.r, color.g, color.b, color.a));
        }

        matrixStackIn.pop();
    }

    public void renderFacingArrow(RailEntity rail, IVertexBuilder builder, MatrixStack matrixStack){
        matrixStack.push();
        matrixStack.rotate(Vector3f.XP.rotationDegrees(-90));
        matrixStack.rotate(rail.getRotation());
        GeometryUtil.renderCylinderSides(builder, matrixStack.getLast().getMatrix(), 0.02F, 1F, 10,
                0, 1, 1, 1);
        matrixStack.translate(0,1,0);
        GeometryUtil.renderCone(builder, matrixStack.getLast().getMatrix(), 10, new Vector3f(0, 0.1F, 0),
                new Vector3f(), 0.05F, Color4i.CYAN.toColor4f());
        matrixStack.pop();
    }

    public void renderUnconnectedTrack(IVertexBuilder builder, MatrixStack matrixStackIn, RailEntity rail) {
        float r1 = 195, g1 = 35, b1 = 35, r2 = 155, g2 = 23, b2 = 23;
        float rf1 = r1 / 255, gf1 = g1 / 255, bf1 = b1 / 255, rf2 = r2 / 255, gf2 =  g2 / 255, bf2 = b2 / 255;

        Quaternion rotation = rail.getRotation();
        matrixStackIn.push();
        matrixStackIn.rotate(rotation);
        matrixStackIn.translate(0, 0.5F, 0);

        matrixStackIn.push();
        matrixStackIn.translate(-0.5, 0, -0.5);
        GeometryUtil.renderCyl(builder, matrixStackIn.getLast().getMatrix(),
                new Vector3d(0,0,0),
                new Vector3d(0, 0, 1),
                RAIL_RADIUS, 12, rf1, gf1, bf1, 1F);

        GeometryUtil.renderCyl(builder, matrixStackIn.getLast().getMatrix(),
                new Vector3d(1,0,0),
                new Vector3d(1, 0, 1),
                RAIL_RADIUS, 12, rf1, gf1, bf1, 1F);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(-0.5, 0, -0.5);
        GeometryUtil.quadFace(builder, matrixStackIn.getLast().getMatrix(),
                new Vector3f(0F,0,0.2F),
                new Vector3f(1F,0,0.2F),
                new Vector3f(0.8F,-0.15F,0.2F),
                new Vector3f(0.2F,-0.15F,0.2F),
                new Vector4f(rf2, gf2, bf2, 1));
        GeometryUtil.quadFace(builder, matrixStackIn.getLast().getMatrix(),
                new Vector3f(0F,0,0.7F),
                new Vector3f(1F,0,0.7F),
                new Vector3f(0.8F,-0.15F,0.7F),
                new Vector3f(0.2F,-0.15F,0.7F),
                new Vector4f(rf2, gf2, bf2, 1));
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(-0.2, -0.1, -0.5);
        matrixStackIn.translate(0, -0.15, 0);
        GeometryUtil.renderCube(builder, matrixStackIn.getLast().getMatrix(),
                new Vector3f(0.4F, 0.15F, 1F),
                new Vector3i(r1, g1, b1));
        matrixStackIn.pop();
        matrixStackIn.pop();
    }

    private float rescaleTangent(Vector3d start, Vector3d end) {
        double distance = start.distanceTo(end);
        float scale;
        if (distance < 20.0) {
            scale = (float) distance * 0.8f;
        } else if (distance < 50.0) {
            scale = (float) distance * 0.5f;
        } else {
            scale = (float) distance * 0.2f;
        }

        scale = Math.max(1.0f, Math.min(50.0f, scale));
        return scale;
    }
}
