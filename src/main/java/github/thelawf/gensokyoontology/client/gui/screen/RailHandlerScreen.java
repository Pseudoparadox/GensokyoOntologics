package github.thelawf.gensokyoontology.client.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import github.thelawf.gensokyoontology.client.gui.screen.script.LineralLayoutScreen;
import github.thelawf.gensokyoontology.common.network.GSKONetworking;
import github.thelawf.gensokyoontology.common.network.packet.CAdjustRailPacket;
import github.thelawf.gensokyoontology.common.util.GSKOUtil;
import github.thelawf.gensokyoontology.common.util.math.EulerAngle;
import github.thelawf.gensokyoontology.common.util.math.GSKOMathUtil;
import github.thelawf.gensokyoontology.common.util.math.RotMatrix;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.client.gui.widget.Slider;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class RailHandlerScreen extends LineralLayoutScreen {
    private final int startEntityId;
    private Quaternion rotation;
    private final BlockPos targetPos;
    private EulerAngle eulerAngle;

    private final Vector3d initHandleValue;
    private final Vector3d nextHandleValue;

    private EulerAngle lastEulerAngle;
    private boolean isInGimbalLock = false;

    private Slider pitchHandle;
    private Slider yawHandle;
    private Slider rollHandle;

    private Button resetX0;
    private Button resetY0;
    private Button resetZ0;

    // 添加轨道预览按钮
    private Button applyButton;
    private Button previewButton;

    private static final TranslationTextComponent QX = GSKOUtil.fromLocaleKey("gui.", ".silder_prefix.qx");
    private static final TranslationTextComponent QY = GSKOUtil.fromLocaleKey("gui.", ".silder_prefix.qy");
    private static final TranslationTextComponent QZ = GSKOUtil.fromLocaleKey("gui.", ".silder_prefix.qz");

    private static final TranslationTextComponent RX = GSKOUtil.fromLocaleKey("gui.", ".button.reset_x");
    private static final TranslationTextComponent RY = GSKOUtil.fromLocaleKey("gui.", ".button.reset_y");
    private static final TranslationTextComponent RZ = GSKOUtil.fromLocaleKey("gui.", ".button.reset_z");

    private static final TranslationTextComponent APPLY = GSKOUtil.fromLocaleKey("gui.", ".button.apply");
    private static final TranslationTextComponent PREVIEW = GSKOUtil.fromLocaleKey("gui.", ".button.preview");

    public static final ITextComponent TITLE = GSKOUtil.translateText("gui.", ".rail_dashboard.title");

    public RailHandlerScreen(BlockPos pos, Quaternion rotation, int startEntityId) {
        super(TITLE);
        this.targetPos = pos;
        this.rotation = rotation;
        this.eulerAngle = EulerAngle.from(this.rotation);
        this.startEntityId = startEntityId;

        this.initHandleValue = new RotMatrix(this.rotation).toHandleValue();
        this.nextHandleValue = new Vector3d(this.initHandleValue.x, this.initHandleValue.y, this.initHandleValue.z);
    }

    public RailHandlerScreen(BlockPos pos, RotMatrix matrix, int startEntityId) {
        super(TITLE);
        this.targetPos = pos;
        this.rotation = matrix.toQuaternion();
        this.startEntityId = startEntityId;

        this.initHandleValue = matrix.toHandleValue();
        this.nextHandleValue = new Vector3d(this.initHandleValue.x, this.initHandleValue.y, this.initHandleValue.z);
    }

    private void onPitchSlide(Slider slider) {
        updateRotationFromSliders(0);
    }

    private void onYawSlide(Slider slider) {
        updateRotationFromSliders(1);
    }

    private void onRollSlide(Slider slider) {
        updateRotationFromSliders(2);
    }

    private void updateRotationFromSliders(int axis) {
        double pitch = this.pitchHandle == null ? 0 : this.pitchHandle.getValue();
        double yaw = this.yawHandle == null ? 0 : this.yawHandle.getValue();
        double roll = this.rollHandle == null ? 0 : this.rollHandle.getValue();

        // 将当前四元数转换为欧拉角
        EulerAngle current = EulerAngle.from(rotation);

        // 根据拖动的轴和增量调整欧拉角
        float newYaw = current.yaw();
        float newPitch = current.pitch();
        float newRoll = current.roll();

        switch (axis) {
            case 0: // X/Pitch
                newPitch += pitch;
                break;
            case 1: // Y/Yaw
                newYaw += yaw;
                break;
            case 2: // Z/Roll
                newRoll += roll;
                break;
        }

        // 创建新的欧拉角并转换为四元数
        EulerAngle newAngles = EulerAngle.of(newYaw, newPitch, newRoll);
        this.rotation = newAngles.toQuaternion();

        // 更新显示值
        this.setSliderValue();
        this.sendPacketToServer();
    }

    private void onResetXSlide(Button btn) {
        this.pitchHandle.setValue(0);
        updateRotationFromSliders(0);
    }

    private void onResetYSlide(Button btn) {
        this.yawHandle.setValue(0);
        updateRotationFromSliders(1);
    }

    private void onResetZSlide(Button btn) {
        this.rollHandle.setValue(0);
        updateRotationFromSliders(2);
    }

    @Override
    protected void init() {
        super.init();
        double x = this.initHandleValue.x;
        double y = this.initHandleValue.y;
        double z = this.initHandleValue.z;

        // 创建滑块，确保范围正确
        this.pitchHandle = new Slider(50, 20, 180, 20, QX, withText("°"),
                -180, 180, x,
                true, true, iPressable -> {}, this::onPitchSlide);

        this.yawHandle = new Slider(50, 45, 180, 20, QY, withText("°"),
                -180, 180, y,
                true, true, iPressable -> {}, this::onYawSlide);

        this.rollHandle = new Slider(50, 70, 180, 20, QZ, withText("°"),
                -180, 180, z,
                true, true, iPressable -> {}, this::onRollSlide);

        this.resetX0 = new Button(250, 20, 60, 20, RX, this::onResetXSlide);
        this.resetY0 = new Button(250, 45, 60, 20, RY, this::onResetYSlide);
        this.resetZ0 = new Button(250, 70, 60, 20, RZ, this::onResetZSlide);

        // 添加应用和预览按钮
        // this.applyButton = new Button(50, 100, 80, 20, APPLY, this::onApplyButton);
        // this.previewButton = new Button(140, 100, 80, 20, PREVIEW, this::onPreviewButton);

        this.addButton(this.pitchHandle);
        this.addButton(this.yawHandle);
        this.addButton(this.rollHandle);

        this.addButton(this.resetX0);
        this.addButton(this.resetY0);
        this.addButton(this.resetZ0);

        // this.addButton(this.applyButton);
    }

    @Override
    public void render(@NotNull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderBackground(matrixStack);

        this.pitchHandle.render(matrixStack, mouseX, mouseY, partialTicks);
        this.yawHandle.render(matrixStack, mouseX, mouseY, partialTicks);
        this.rollHandle.render(matrixStack, mouseX, mouseY, partialTicks);

        this.resetX0.render(matrixStack, mouseX, mouseY, partialTicks);
        this.resetY0.render(matrixStack, mouseX, mouseY, partialTicks);
        this.resetZ0.render(matrixStack, mouseX, mouseY, partialTicks);

        // this.applyButton.render(matrixStack, mouseX, mouseY, partialTicks);
        // this.previewButton.render(matrixStack, mouseX, mouseY, partialTicks);

        // 显示当前旋转值
//            String rotationText = String.format("Rotation: Yaw=%.1f°, Pitch=%.1f°, Roll=%.1f°",
//                    this.eulerAngle.yaw(), this.eulerAngle.pitch(), this.eulerAngle.roll());
    }

    private float to3Digits(float value) {
        BigDecimal b = new BigDecimal(value);
        return b.setScale(2, RoundingMode.HALF_UP).floatValue();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private void sendPacketToServer() {
        // GSKONetworking.CHANNEL.sendToServer(new CAdjustRailPacket(this.targetPos, this.rotation, this.startEntityId, , , ));
    }

    private float value(Slider slider) {
        if (slider == null) return 0;
        slider.setValue(this.to3Digits((float) slider.getValue()));
        return GSKOMathUtil.normalize(this.to3Digits((float) slider.getValue()), -180, 180);
    }

    private void setSliderValue() {
        if (this.yawHandle == null) return;
        if (this.rollHandle == null) return;
        if (this.pitchHandle == null) return;

        this.pitchHandle.setValue(MathHelper.wrapDegrees(this.eulerAngle.pitch()));
        this.yawHandle.setValue(MathHelper.wrapDegrees(this.eulerAngle.yaw()));
        this.rollHandle.setValue(MathHelper.wrapDegrees(this.eulerAngle.roll()));
    }

    private EulerAngle getEulerAngleFrom(Slider xHandle, Slider yHandle, Slider zHandle) {
        return EulerAngle.of(
                xHandle == null ? 0F : (float) xHandle.getValue(),
                yHandle == null ? 0F : (float) yHandle.getValue(),
                zHandle == null ? 0F : (float) zHandle.getValue());
    }

}
