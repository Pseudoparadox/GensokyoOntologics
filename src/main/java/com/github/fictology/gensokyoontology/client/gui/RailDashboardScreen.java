package com.github.fictology.gensokyoontology.client.gui;

/*
public class RailDashboardScreen extends LineralLayoutScreen {
    private Vector3f controlPoint;
    private BlockPos railPos;
    private Vector3f rotation;
    private float radius;
    private Slider yawSlider;
    private Slider pitchSlider;
    private Slider rollSlider;

    private TextFieldWidget xInput;
    private TextFieldWidget yInput;
    private TextFieldWidget zInput;
    private CheckboxButton shouldRender;
    private static final TranslationTextComponent ANGLE_X = GSKOUtil.fromLocaleKey("gui.", ".silder_prefix.angle_x");
    private static final TranslationTextComponent ANGLE_Y = GSKOUtil.fromLocaleKey("gui.", ".silder_prefix.angle_y");
    private static final TranslationTextComponent ANGLE_Z = GSKOUtil.fromLocaleKey("gui.", ".silder_prefix.angle_z");
    private static final ITextComponent ROLL_LABEL   = GensokyoOntology.fromLocaleKey("gui.", ".label.roll") ;
    private static final ITextComponent YAW_LABEL    = GensokyoOntology.fromLocaleKey("gui.", ".label.yaw");
    private static final ITextComponent PITCH_LABEL  = GensokyoOntology.fromLocaleKey("gui.", ".label.pitch") ;
    private static final ITextComponent X_LABEL = GensokyoOntology.fromLocaleKey("gui.", ".label.x");
    private static final ITextComponent Y_LABEL = GensokyoOntology.fromLocaleKey("gui.", ".label.y");
    private static final ITextComponent Z_LABEL = GensokyoOntology.fromLocaleKey("gui.", ".label.z");
    public static final ITextComponent TITLE = GensokyoOntology.fromLocaleKey("gui.", ".rail_dashboard.title");

    public RailDashboardScreen(BlockPos pos, float roll, float yaw, float pitch, Vector3f controlPoint) {
        super(TITLE);
        this.railPos = pos;
        this.rotation = new Vector3f(roll, yaw, pitch);
        this.controlPoint = controlPoint;
    }

    // public RailDashboardScreen(RailAdjustContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
    //     super(screenContainer, inv, titleIn);
    //     this.rotation = screenContainer.getRotation();
    //     this.railPos = screenContainer.getRailPos();
    // }

    private void onRollSlide(Slider slider) {
        // this.rollInput.setText(formatAs(slider.getIncident()));
        this.rotation.setX((float) slider.getIncident());
        this.sendPacketToServer();
    }
    private void onYawSlide(Slider slider) {
        // this.yawInput.setText(formatAs(slider.getIncident()));
        this.rotation.setY((float) slider.getIncident());
        this.sendPacketToServer();
    }
    private void onPitchSlide(Slider slider) {
        // this.pitchInput.setText(formatAs(slider.getIncident()));
        this.rotation.setZ((float) slider.getIncident());
        this.sendPacketToServer();
    }

    private void onXInput(String input) {
        // this.pitchInput.setText(formatAs(slider.getIncident()));
        if (Objects.equals(input, "")) return;
        float value;
        try {
            value = Float.parseFloat(input);
        }catch (Exception ignored){
            return;
        }
        this.controlPoint.setX(value);
        this.sendPacketToServer();
    }

    private void onYInput(String input) {
        // this.pitchInput.setText(formatAs(slider.getIncident()));
        if (Objects.equals(input, "")) return;
        float value;
        try {
            value = Float.parseFloat(input);
        }catch (Exception ignored){
            return;
        }
        this.controlPoint.setY(value);
        this.sendPacketToServer();
    }

    private void onZInput(String input) {
        // this.pitchInput.setText(formatAs(slider.getIncident()));
        if (Objects.equals(input, "")) return;
        float value;
        try {
            value = Float.parseFloat(input);
        }catch (Exception ignored){
            return;
        }
        this.controlPoint.setZ(value);
        this.sendPacketToServer();
    }

    @Override
    protected void init() {
        super.init();

        this.rollSlider = new Slider(50, 30, 120, 25, ANGLE_X, withText("°"),
                0, 360, (int) this.rotation.getX(), true, true, iPressable -> {}, this::onRollSlide);
        this.yawSlider = new Slider(50, 60, 120, 25, ANGLE_Y, withText("°"),
                0, 360, (int) this.rotation.getY(), true, true, iPressable -> {}, this::onYawSlide);
        this.pitchSlider = new Slider(50, 90, 120, 25, ANGLE_Z, withText("°"),
                0, 360, (int) this.rotation.getZ(), true, true, iPressable -> {}, this::onPitchSlide);
        this.xInput = new TextFieldWidget(this.font, 50, 120, 120, 25, withText(String.valueOf(controlPoint.getX())));
        this.yInput = new TextFieldWidget(this.font, 50, 150, 120, 25, withText(String.valueOf(controlPoint.getY())));
        this.zInput = new TextFieldWidget(this.font, 50, 180, 120, 25, withText(String.valueOf(controlPoint.getZ())));
        this.xInput.setResponder(this::onXInput);
        this.yInput.setResponder(this::onYInput);
        this.zInput.setResponder(this::onZInput);

        this.rollSlider.showDecimal = false;
        this.pitchSlider.showDecimal = false;
        this.yawSlider.showDecimal = false;

        this.addButton(this.rollSlider);
        this.addButton(this.yawSlider);
        this.addButton(this.pitchSlider);
        this.children.add(this.xInput);
        this.children.add(this.yInput);
        this.children.add(this.zInput);

        // this.rollInput = new TextFieldWidget(this.font, 50, 30, 40, 25, withText(this.rollSlider.getValueInt() + "°"));
        // this.yawInput = new TextFieldWidget(this.font, 50, 60, 40, 25, withText(this.yawSlider.getValueInt() + "°"));
        // this.pitchInput = new TextFieldWidget(this.font, 50, 90, 40, 25, withText(this.pitchSlider.getValueInt() + "°"));

        // this.rollInput.setResponder(this::onRollInputChanged);
        // this.yawInput.setResponder(this::onYawInputChanged);
        // this.pitchInput.setResponder(this::onPitchInputChanged);

        // this.children.add(this.rollInput);
        // this.children.add(this.yawInput);
        // this.children.add(this.pitchInput);
    }

    private void onRollInputChanged(String text) {
        this.rollSlider.set((int) parseDouble(text));
        this.sendPacketToServer();
    }

    private void onYawInputChanged(String text) {
        this.yawSlider.set((int) parseDouble(text));
        this.sendPacketToServer();
    }

    private void onPitchInputChanged(String text) {
        this.pitchSlider.set((int) parseDouble(text));
        this.sendPacketToServer();
    }

    /*
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        // 处理文本框的键盘输入
        if (this.yawInput.keyPressed(keyCode, scanCode, modifiers) || this.yawInput.canWrite()) {
            return true;
        }
        if (this.rollInput.keyPressed(keyCode, scanCode, modifiers) || this.rollInput.canWrite()) {
            return true;
        }
        if (this.pitchInput.keyPressed(keyCode, scanCode, modifiers) || this.pitchInput.canWrite()) {
            return true;
        }

        // 处理其他按键输入
        return super.keyPressed(keyCode, scanCode, modifiers);
    }



    @Override
    public void render(@NotNull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        // this.renderAbsoluteXY(WIDGETS, matrixStack, mouseX, mouseY, partialTicks);
        this.renderBackground(matrixStack);
        drawString(matrixStack, this.font, ROLL_LABEL, 10, 40, WHITE);
        drawString(matrixStack, this.font, YAW_LABEL, 10, 70, WHITE);
        drawString(matrixStack, this.font, PITCH_LABEL, 10, 100, WHITE);
        drawString(matrixStack, this.font, X_LABEL, 10, 130, WHITE);
        drawString(matrixStack, this.font, Y_LABEL, 10, 160, WHITE);
        drawString(matrixStack, this.font, Z_LABEL, 10, 190, WHITE);

        // this.rollInput.render(matrixStack, mouseX, mouseY, partialTicks);
        // this.yawInput.render(matrixStack, mouseX, mouseY, partialTicks);
        // this.pitchInput.render(matrixStack, mouseX, mouseY, partialTicks);

        this.rollSlider.render(matrixStack, mouseX, mouseY, partialTicks);
        this.yawSlider.render(matrixStack, mouseX, mouseY, partialTicks);
        this.pitchSlider.render(matrixStack, mouseX, mouseY, partialTicks);
        this.xInput.render(matrixStack, mouseX, mouseY, partialTicks);
        this.yInput.render(matrixStack, mouseX, mouseY, partialTicks);
        this.zInput.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    public String formatAs(double original) {
        return (int) original + "°";
    }

    public String formatAs(float original) {
        return (int) original + "°";
    }

    public String formatAs(String original) {
        return (int) parseFloat(original) + "°";
    }

    @Override
    public void renderBackground(@NotNull MatrixStack matrixStack) {

    }

    public void setRotation(float roll, float yaw, float pitch) {
        this.rotation = new Vector3f(roll, yaw, pitch);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
    private void sendPacketToServer() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putLong("pos", this.railPos.toLong());
        nbt.putFloat("roll", this.rotation.getX());
        nbt.putFloat("yaw", this.rotation.getY());
        nbt.putFloat("pitch", this.rotation.getZ());
        nbt.putFloat("radius", this.radius);

        // nbt.putFloat("railX", this.railPos.getX());
        // nbt.putFloat("railY", this.railPos.getY());
        // nbt.putFloat("railZ", this.railPos.getZ());
        nbt.putFloat("controlX", this.controlPoint.getX());
        nbt.putFloat("controlY", this.controlPoint.getY());
        nbt.putFloat("controlZ", this.controlPoint.getZ());
        GSKONetworking.CHANNEL.sendToServer(new CAdjustRailPacket(nbt));
    }

    private void sendPacketToClient () {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putBoolean("shouldRender", true);

        nbt.putFloat("roll", this.rotation.getX());
        nbt.putFloat("yaw", this.rotation.getY());
        nbt.putFloat("pitch", this.rotation.getZ());

        ClientPlayNetHandler handler = Minecraft.getInstance().getConnection();

    }
}
*/