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
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.*;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Predicate;

public class RailRenderer extends EntityRenderer<RailEntity> {
    public static final double RAIL_WIDTH = 0.5;
    public static final float RAIL_RADIUS = 0.07F;
    public static final float SEGMENTS = 32;
    public static final float SCALE = 20F;

    public static final ResourceLocation TEXTURE = GSKOUtil.withRL("textures/entity/entity_blank.png");

    public RailRenderer(EntityRendererManager manager) {
        super(manager);
    }

    @NotNull
    @Override
    public ResourceLocation getEntityTexture(@NotNull RailEntity entity) {
        return TEXTURE;
    }

    // avg: 1/seg, 1 / lenOfEach
    @Override
    public void render(@NotNull RailEntity startRail, float entityYaw, float partialTicks, @NotNull MatrixStack matrixStack, IRenderTypeBuffer bufferIn, int light) {
        super.render(startRail, entityYaw, partialTicks, matrixStack, bufferIn, light);
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getTextureManager().bindTexture(TEXTURE);
        ClientPlayerEntity player = minecraft.player;

        IVertexBuilder builder = bufferIn.getBuffer(RenderType.getEntityCutoutNoCull(TEXTURE));
        IVertexBuilder buffer = bufferIn.getBuffer(GSKORenderTypes.MULTI_FACE_SOLID);

        Maybe<Entity> maybe = startRail.getNextRailClient(Maybe.empty());
        if (!maybe.isPresent()) {
            this.renderUnconnectedTrack(buffer, matrixStack, startRail);
            return;
        }
        if (!(maybe.get() instanceof RailEntity)) return;
        Predicate<PlayerEntity> isHoldingThese = p ->
                p.getHeldItemMainhand().getItem() == ItemRegistry.RAIL_WRENCH.get() ||
                p.getHeldItemMainhand().getItem() == ItemRegistry.RAIL_CONNECTOR.get() ||
                p.getHeldItemMainhand().getItem() == ItemRegistry.TRACK_PLACER.get() ||
                p.getHeldItemMainhand().getItem() == ItemRegistry.TRACK_REMOVER.get();

        if (player != null && isHoldingThese.test(player)){
            this.renderUnconnectedTrack(buffer, matrixStack, startRail);
        }

        maybe.ifPresent(nextRail -> this.renderHermite3(startRail, (RailEntity) nextRail, builder, matrixStack));
//        RailEntity targetRail = (RailEntity) maybe.get();
//        this.renderHermite3(startRail, targetRail, builder, matrixStack);
    }

    public void renderHermite3(RailEntity startRail, RailEntity targetRail, IVertexBuilder builder, MatrixStack matrixStack) {
        // 1. 获取世界坐标下的起点和终点
        Vector3d startWorldPos = startRail.getPositionVec();
        Vector3d endWorldPos = targetRail.getPositionVec();

        // 2. 计算相对偏移量（局部坐标），这是修复断开的关键！
        // 无论前面连了多少段，当前这段总是从 (0,0,0) 画到 endWorldPos.subtract(startWorldPos)
        Vector3d startOffset = Vector3d.ZERO;
        Vector3d endOffset = endWorldPos.subtract(startWorldPos);

        // 3. 获取旋转信息
        Quaternion startRot = startRail.getRotation();
        Quaternion endRot = targetRail.getRotation();

        // 预计算全局桶滚角度变化范围
        float maxRollChange = 180.0f;
        float startRoll = GSKOMathUtil.getEulerAngle(startRot).roll();
        float endRoll = GSKOMathUtil.getEulerAngle(endRot).roll();
        float rollDelta = MathHelper.wrapDegrees(endRoll - startRoll);
        if (Math.abs(rollDelta) > maxRollChange) {
            rollDelta = Math.signum(rollDelta) * maxRollChange;
        }

        // 获取方向向量
        Vector3f startDirection = startRail.getOrientation().copy();
        Vector3f endDirection = targetRail.getOrientation().copy();
        startDirection.mul(SCALE);
        endDirection.mul(SCALE);

        final int segments = 32;
        // 临时变量存储上一段的端点，用于连线
        Vector3d prevLeft = null;
        Vector3d prevRight = null;

        // 保存初始矩阵状态，以便在渲染完这一整条轨道后恢复
        matrixStack.push();
        this.renderSegments(segments, startOffset, endOffset, startDirection, endDirection, startRot, endRot, builder, matrixStack,
                startRoll, rollDelta);

//        for (int i = 0; i < segments; i++) {
//            float t0 = (float) i / segments;
//            float t1 = (float) (i + 1) / segments;
//
//            // 使用相对偏移量计算曲线点
//            Vector3d prev = CurveUtil.hermite3(startOffset, endOffset, startDirection, endDirection, t0);
//            Vector3d next = CurveUtil.hermite3(startOffset, endOffset, startDirection, endDirection, t1);
//
//            // 计算切向量（用于旋转）
//            // 注意：这里使用局部坐标计算切线，因为矩阵已经平移了
//            Vector3d tangentVec = tangent(startOffset, endOffset, startDirection, endDirection, t0).normalize();
//
//            // 应用桶滚 (Roll)
//            float currentRoll = startRoll + t0 * rollDelta;
//
//            // 使用球面线性插值(SLERP)平滑旋转
//            Quaternion currentRotation = GSKOMathUtil.slerp(startRot, endRot, t0);
//
//            // 构建旋转矩阵 - 使用轨道实体的旋转
//            RotMatrix rotMatrix = new RotMatrix(currentRotation);
//
//            // 应用桶滚旋转到法线方向
//            Vector3f normal = rotMatrix.normal(); // 获取法线（X轴）
//            Vector3f binormal = rotMatrix.binormal(); // 获取副法线（Y轴）
//            Vector3f tangentAxis = rotMatrix.tangent(); // 获取切线（Z轴）
//
//            // 创建桶滚四元数并应用到法线
//            Quaternion rollQuat = new Quaternion(tangentAxis, currentRoll, true);
//            Vector3f rolledNormal = GSKOMathUtil.rotateVector(rollQuat, normal);
//
//            // 重新构建旋转矩阵
//            RotMatrix matrix = new RotMatrix();
//            matrix.setColumn(0, new Vector3d(rolledNormal));     // X轴 = 滚动后的法线
//            matrix.setColumn(1, new Vector3d(binormal));         // Y轴 = 副法线
//            matrix.setColumn(2, new Vector3d(tangentAxis));      // Z轴 = 切线
//
//            Vector3d localLeftOffset  = new Vector3d(-RAIL_WIDTH,0, 0);
//            Vector3d localRightOffset = new Vector3d(RAIL_WIDTH, 0, 0);
//
//            // 转换到世界坐标（因为外层 matrixStack 已经 translate 过了，这里只需乘 rotMatrix）
//            Vector3d leftStart = matrix.multiply(localLeftOffset).add(prev);
//            Vector3d rightStart = matrix.multiply(localRightOffset).add(prev);
//            Vector3d leftEnd = matrix.multiply(localLeftOffset).add(next);
//            Vector3d rightEnd = matrix.multiply(localRightOffset).add(next);
//
//            Color4f color = new Color4i(255, 0, 0, 255).toColor4f();
//
//            // 渲染左轨道
//            GeometryUtil.renderCyl(builder, matrixStack.getLast().getMatrix(),
//                    leftStart, leftEnd,
//                    RAIL_RADIUS, 8,
//                    color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
//
//            // 渲染右轨道
//            GeometryUtil.renderCyl(builder, matrixStack.getLast().getMatrix(),
//                    rightStart, rightEnd,
//                    RAIL_RADIUS, 8,
//                    color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
//        }
//        // 恢复矩阵状态
        matrixStack.pop();
    }

    public void renderSegments(int segments, Vector3d startOffset, Vector3d endOffset, Vector3f startDirection, Vector3f endDirection,
                               Quaternion startRot, Quaternion endRot, IVertexBuilder builder, MatrixStack matrixStack,
                               float startRoll, float rollDelta){
        for (int i = 0; i < segments; i++) {
            float t0 = (float) i / segments;
            float t1 = (float) (i + 1) / segments;

            // 使用相对偏移量计算曲线点
            Vector3d prev = CurveUtil.hermite3(startOffset, endOffset, startDirection, endDirection, t0);
            Vector3d next = CurveUtil.hermite3(startOffset, endOffset, startDirection, endDirection, t1);

            // 计算切向量（用于旋转）
            // 注意：这里使用局部坐标计算切线，因为矩阵已经平移了
            Vector3d tangentVec = tangent(startOffset, endOffset, startDirection, endDirection, t0).normalize();

            // 应用桶滚 (Roll)
            float currentRoll = startRoll + t0 * rollDelta;

            // 使用球面线性插值(SLERP)平滑旋转
            Quaternion currentRotation = GSKOMathUtil.slerp(startRot, endRot, t0);

            // 构建旋转矩阵 - 使用轨道实体的旋转
            RotMatrix rotMatrix = new RotMatrix(currentRotation);

            // 应用桶滚旋转到法线方向
            Vector3f normal = rotMatrix.normal(); // 获取法线（X轴）
            Vector3f binormal = rotMatrix.binormal(); // 获取副法线（Y轴）
            Vector3f tangentAxis = rotMatrix.tangent(); // 获取切线（Z轴）

            // 创建桶滚四元数并应用到法线
            Quaternion rollQuat = new Quaternion(tangentAxis, currentRoll, true);
            Vector3f rolledNormal = GSKOMathUtil.rotateVector(rollQuat, normal);

            // 重新构建旋转矩阵
            RotMatrix matrix = new RotMatrix();
            matrix.setColumn(0, new Vector3d(rolledNormal));     // X轴 = 滚动后的法线
            matrix.setColumn(1, new Vector3d(binormal));         // Y轴 = 副法线
            matrix.setColumn(2, new Vector3d(tangentAxis));      // Z轴 = 切线

            Vector3d localLeftOffset  = new Vector3d(-RAIL_WIDTH,0, 0);
            Vector3d localRightOffset = new Vector3d(RAIL_WIDTH, 0, 0);

            // 转换到世界坐标（因为外层 matrixStack 已经 translate 过了，这里只需乘 rotMatrix）
            Vector3d leftStart = matrix.multiply(localLeftOffset).add(prev);
            Vector3d rightStart = matrix.multiply(localRightOffset).add(prev);
            Vector3d leftEnd = matrix.multiply(localLeftOffset).add(next);
            Vector3d rightEnd = matrix.multiply(localRightOffset).add(next);

            Color4f color = new Color4i(255, 0, 0, 255).toColor4f();

            // 渲染左轨道
//            GeometryUtil.renderCyl(builder, matrixStack.getLast().getMatrix(),
//                    leftStart, leftEnd,
//                    RAIL_RADIUS, 8,
//                    color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
            GeometryUtil.renderClippedCylinder(builder, matrixStack.getLast().getMatrix(),
                    leftStart, leftEnd, new Vector3d(rolledNormal),  RAIL_RADIUS, 8,
                    color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());

            GeometryUtil.renderClippedCylinder(builder, matrixStack.getLast().getMatrix(),
                    rightStart, rightEnd, new Vector3d(rolledNormal), RAIL_RADIUS, 8,
                    color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());

            // 渲染右轨道
//            GeometryUtil.renderCyl(builder, matrixStack.getLast().getMatrix(),
//                    rightStart, rightEnd,
//                    RAIL_RADIUS, 8,
//                    color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        }
    }

    public void renderUnconnectedTrack(IVertexBuilder builder, MatrixStack matrixStackIn, RailEntity rail) {
        float r1 = 195, g1 = 35, b1 = 35, r2 = 155, g2 = 23, b2 = 23;
        float rf1 = r1 / 255, gf1 = g1 / 255, bf1 = b1 / 255, rf2 = r2 / 255, gf2 =  g2 / 255, bf2 = b2 / 255;

        Quaternion rotation = rail.getRotation();
        matrixStackIn.push();
        matrixStackIn.rotate(rotation);
        matrixStackIn.translate(0, 0.5F, 0);

        matrixStackIn.push();
        matrixStackIn.translate(-0.5, 0.1, -0.5);
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
        matrixStackIn.translate(-0.2, -0.2, -0.5);
        matrixStackIn.translate(0, -0.15, 0);
        GeometryUtil.renderCube(builder, matrixStackIn.getLast().getMatrix(),
                new Vector3f(0.4F, 0.15F, 1F),
                new Vector3i(r1, g1, b1));
        matrixStackIn.pop();
        matrixStackIn.pop();
    }

    private Vector3d tangent(Vector3d start, Vector3d end, Vector3f startDirection, Vector3f endDirection, float t) {
        // 使用中心差分法（提高平滑性）
        Vector3d prev = CurveUtil.hermite3(start, end, startDirection, endDirection, Math.max(0, t));
        Vector3d next = CurveUtil.hermite3(start, end, startDirection, endDirection, Math.min(1, t + 0.001F));
        return next.subtract(prev).normalize();
    }
}
