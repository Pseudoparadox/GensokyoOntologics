package github.thelawf.gensokyoontology.client.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import github.thelawf.gensokyoontology.client.gui.screen.script.LineralLayoutScreen;
import github.thelawf.gensokyoontology.common.network.GSKONetworking;
import github.thelawf.gensokyoontology.common.network.packet.CAdjustRailPacket;
import github.thelawf.gensokyoontology.common.util.GSKOUtil;
import github.thelawf.gensokyoontology.common.util.math.EulerAngle;
import github.thelawf.gensokyoontology.common.util.math.RotMatrix;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class RailDashboardScreen extends LineralLayoutScreen {

    private final int startEntityId;
    private final BlockPos targetPos;
    private Quaternion rotation;

    private final java.util.Map<Integer, TextFieldWidget> axisFieldMap = new java.util.HashMap<>();
    private static final TranslationTextComponent
            QY = GSKOUtil.fromLocaleKey("gui.", ".axis.yaw"),
            QX = GSKOUtil.fromLocaleKey("gui.", ".axis.pitch"),
            QZ = GSKOUtil.fromLocaleKey("gui.", ".axis.roll"),
            APPLY   = GSKOUtil.fromLocaleKey("gui.", ".button.apply"),
            PREVIEW = GSKOUtil.fromLocaleKey("gui.", ".button.preview"),
            RESET   = GSKOUtil.fromLocaleKey("gui.", ".button.reset");

    public static final ITextComponent TITLE = GSKOUtil.translateText("gui.", ".rail_dashboard.title");
    private static final float[] STEPS = {90f, 10f, 1f};
    private final Quaternion initRotation;


    public RailDashboardScreen(BlockPos pos, Quaternion rotation, int startEntityId) {
        super(TITLE);
        this.targetPos = pos;
        this.rotation = rotation.copy();
        this.initRotation = rotation.copy();
        this.startEntityId = startEntityId;
    }

    public RailDashboardScreen(BlockPos pos, RotMatrix matrix, int startEntityId) {
        this(pos, matrix.toQuaternion(), startEntityId);
    }

    private void applyDelta(float dYaw, float dPitch, float dRoll) {
        Quaternion qz = new Quaternion(Vector3f.ZP, dRoll,  true);
        Quaternion qx = new Quaternion(Vector3f.XP, dPitch, true);
        Quaternion qy = new Quaternion(Vector3f.YP, dYaw,   true);

        qz.multiply(qx);
        qz.multiply(qy);      // Qz * Qx * Qy
        qz.multiply(rotation);
        this.rotation = qz;
    }

    private void step(int axis, float sign, float mag) {
        float dY = 0, dP = 0, dR = 0;
        switch (axis) {
            case 0 : dP = sign * mag; break;
            case 1 : dY = sign * mag; break;
            case 2 : dR = sign * mag; break;
        }
        this.applyDelta(dY, dP, dR);
        this.setRotationText();
        this.sendPacketToServer();
    }

    private void setRotationText() {
        EulerAngle e = RotMatrix.from(this.rotation).toEulerAngle();
        float p = e.pitch(), y = e.yaw(), r = e.roll();

        TextFieldWidget fp = axisFieldMap.get(0);
        TextFieldWidget fy = axisFieldMap.get(1);
        TextFieldWidget fr = axisFieldMap.get(2);

        if (fp != null) fp.setText(String.format("%s %.1f", QX.getString(),p));
        if (fy != null) fy.setText(String.format("%s %.1f", QY.getString(),y));
        if (fr != null) fr.setText(String.format("%s %.1f", QZ.getString(),r));
    }

    private void sendPacketToServer() {
        GSKONetworking.CHANNEL.sendToServer(new CAdjustRailPacket(targetPos, rotation, startEntityId));
    }

    @Override
    protected void init() {
        super.init();
        int baseY = 20;
        int btnW = 30;
        int gap = 2;
        int labelW = 100;
        int rowH = 22;

        int y = baseY;
        int x = 50;
        this.addStepButtons(x, y, 0, QX, btnW, gap, labelW);
        y += rowH;
        this.addStepButtons(x, y, 1, QY, btnW, gap, labelW);
        y += rowH;
        this.addStepButtons(x, y, 2, QZ, btnW, gap, labelW);

        y += rowH + 6;
        this.addButton(new Button(125, y, 70, 20, RESET, b -> {
            this.rotation = Quaternion.ONE;
            this.setRotationText();
            sendPacketToServer();
        }));
    }

    /**
     * 生成一行：[-90] [-10] [-1]  [Label]  [+1] [+10] [+90]
     * axis: 0=Pitch,1=Yaw,2=Roll
     */
    private void addStepButtons(int startX, int y, int axis,
                                ITextComponent label, int bw, int gap, int lw) {
        int x = startX;
        for (float mag : STEPS) {
            this.addButton(new Button(x, y, bw, 20, GSKOUtil.stringText("-" + (int) mag),
                    b -> step(axis, -1, mag)));
            x += bw + gap;
        }

        TextFieldWidget field = new TextFieldWidget(font, x, y, lw, 20, label);
        field.setMaxStringLength(100);
        field.setTextColor(0xFFFFFF);
        field.setDisabledTextColour(0x888888);

        // 初始值：从当前四元数反算
        EulerAngle e = RotMatrix.from(rotation).toEulerAngle();
        float initVal;
        switch (axis) {
            case 0 : initVal = e.pitch(); break;
            case 1 : initVal = e.yaw(); break;
            case 2 : initVal = e.roll(); break;
            default : initVal = 0; break;
        }
        field.setText(String.format("%s %.1f", label.getString(), initVal));
        axisFieldMap.put(axis, field);
        this.addButton(field);
        x += lw + gap;


        for (float mag : STEPS) {
            this.addButton(new Button(x, y, bw, 20, GSKOUtil.stringText("+" + (int) mag),
                    b -> step(axis, +1, mag)));
            x += bw + gap;
        }
    }

    @Override
    public void render(@NotNull MatrixStack ms, int mx, int my, float pt) {
        super.render(ms, mx, my, pt);
        for (Map.Entry<Integer, TextFieldWidget> entry : axisFieldMap.entrySet()) {
            TextFieldWidget f = entry.getValue();
            if (f.getText().isEmpty() && !f.isFocused()) {
                String[] names = {QX.getString(), QY.getString(), QZ.getString()};
                drawString(ms, font, names[entry.getKey()] + " °", f.x + 4, f.y + 6, 0x666666);
            }
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        // 回车 commit 当前聚焦的文本框
        for (Map.Entry<Integer, TextFieldWidget> entry : axisFieldMap.entrySet()) {
            TextFieldWidget f = entry.getValue();
            if (f.isFocused()) {
                if (keyCode == 257 || keyCode == 335 /* ENTER */) {
                    commitAxisField(entry.getKey());
                    f.changeFocus(false); // 失焦
                }
                return super.keyPressed(keyCode, scanCode, modifiers);
            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void onClose() {
        // 关界面时把所有文本框刷进去，避免玩家输完直接 Esc 丢数据
        for (int axis : axisFieldMap.keySet()) {
            commitAxisField(axis);
        }
        super.onClose();
    }

    /** 把某个轴的文本框值按绝对值写回四元数（ZXY 顺规） */
    private void commitAxisField(int axis) {
        TextFieldWidget f = axisFieldMap.get(axis);
        if (f == null) return;

        String raw = f.getText();

        // 把 "Yaw 90.0" / "Pitch -10.0" 里的非数字、非负号、非 '.' 全剥掉
        String num = raw.replaceAll("[^\\-\\d.]", "");
        if (num.isEmpty()) {
            this.setRotationText();
            return;
        }

        float val;
        try {
            val = Float.parseFloat(num);
        } catch (NumberFormatException ex) {
            this.setRotationText();
            return;
        }

        EulerAngle cur = RotMatrix.from(rotation).toEulerAngle();
        EulerAngle next;
        if (axis == 0)      next = EulerAngle.of(cur.yaw(),   val, cur.roll());
        else if (axis == 1) next = EulerAngle.of(val,         cur.pitch(), cur.roll());
        else                next = EulerAngle.of(cur.yaw(),   cur.pitch(), val);

        this.rotation = next.toQuaternion();
        sendPacketToServer();
    }
}