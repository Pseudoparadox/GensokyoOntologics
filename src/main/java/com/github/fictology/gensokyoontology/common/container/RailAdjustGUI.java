package com.github.fictology.gensokyoontology.common.container;

/*
public class RailAdjustGUI extends Screen {

    private static final ResourceLocation MAIN = new ResourceLocation(GensokyoOntology.MODID +
            "textures/client/rail_adjust_main.png");

    // private static final ResourceLocation ROTATE_ROLL = new ResourceLocation(GensokyoOntology.MODID +
    //         "textures/client/rail_rotate_roll.png");

    Button increaseRoll;
    Button decreaseRoll;
    Button increaseYaw;
    Button decreaseYaw;
    Button increasePitch;
    Button decreasePicth;

    Button smooth;
    Button reset;
    Button confirm;

    TextFieldWidget rotationRoll;
    TextFieldWidget rotationYaw;
    TextFieldWidget rotationPitch;

    public RailAdjustGUI() {
        super(new TranslationTextComponent("screen." + GensokyoOntology.MODID +
                "rail_adjust.title"));
    }

    @Override
    protected void init() {
        int x = this.width / 2 - 40;
        int y = 46;

        addAdjustButtons(x, y);
        addTextFields();
        addOperationalButtons(x, y);
        super.init();
    }

    public void addAdjustButtons(int x, int y) {

        increaseRoll = new Button(x + 120, y, 20, 20,
                new TranslationTextComponent("client." + GensokyoOntology.MODID +
                        ".button.increase_roll"), (button) -> {
        });

        decreaseRoll = new Button(x, y, 20, 20,
                new TranslationTextComponent("client." + GensokyoOntology.MODID +
                        ".button.decrease_roll"), (button) -> {
        });

        increaseYaw = new Button(x + 120, y + 30, 20, 20,
                new TranslationTextComponent("client." + GensokyoOntology.MODID +
                        ".button.increase_yaw"), (button) -> {
        });

        decreaseYaw = new Button(x, y + 30, 20, 20,
                new TranslationTextComponent("client." + GensokyoOntology.MODID +
                        ".button.decrease_yaw"), (button) -> {
        });

        increasePitch = new Button(x + 120, y + 60, 20, 20,
                new TranslationTextComponent("client." + GensokyoOntology.MODID +
                        ".button.decrease_yaw"), (button) -> {
        });

        decreasePicth = new Button(x, y + 60, 20, 20,
                new TranslationTextComponent("client." + GensokyoOntology.MODID +
                        ".button.decrease_yaw"), (button) -> {
        });

        increaseRoll.setMessage(new TranslationTextComponent("client." + GensokyoOntology.MODID +
                ".button.increase_roll.msg"));
        decreaseRoll.setMessage(new TranslationTextComponent("client." + GensokyoOntology.MODID +
                ".button.decrease_roll.msg"));

        increaseYaw.setMessage(new TranslationTextComponent("client." + GensokyoOntology.MODID +
                ".button.increase_yaw.msg"));
        decreaseYaw.setMessage(new TranslationTextComponent("client." + GensokyoOntology.MODID +
                ".button.decrease_yaw.msg"));

        increasePitch.setMessage(new TranslationTextComponent("client." + GensokyoOntology.MODID +
                ".button.increase_pitch.msg"));
        decreasePicth.setMessage(new TranslationTextComponent("client." + GensokyoOntology.MODID +
                ".button.decrease_pitch.msg"));

        this.addButton(increaseRoll);
        this.addButton(decreaseRoll);
        this.addButton(increaseYaw);
        this.addButton(decreaseYaw);
    }

    public void addOperationalButtons(int x, int y) {

        String prevRollData = this.rotationRoll.getText();
        String prevYawData = this.rotationYaw.getText();
        String prevPitchData = this.rotationPitch.getText();

        smooth = new Button(x, y + 150, 60, 20,
                new TranslationTextComponent("client." + GensokyoOntology.MODID +
                        ".button.smooth"), (button) -> {
        });
        reset = new Button(x + 70, y + 150, 60, 20,
                new TranslationTextComponent("client." + GensokyoOntology.MODID +
                        ".button.reset"), (button) -> {
        });
        confirm = new Button(x + 140, y + 150, 60, 20,
                new TranslationTextComponent("client." + GensokyoOntology.MODID +
                        ".button.confirm"), (button) -> {
            String rotYaw = this.rotationYaw.getText();
            String rotRoll = this.rotationRoll.getText();
            String rotPitch = this.rotationPitch.getText();

            String regex = "[^0-9]";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(regex);

            TransformFunction func = new TransformFunction();
            double roll;
            double yaw;
            double pitch;
            // rotRoll.matches("^-?\\d+$")
            // rotPitch.matches("^-?\\d+$")

            if (rotRoll.matches("^-?\\d+$")) {
                roll = Integer.parseInt(rotRoll);
            } else {
                String strRoll = m.replaceAll("").trim();
                roll = Integer.parseInt(strRoll);
            }

            if (rotYaw.matches("^-?\\d+$")) {
                yaw = Integer.parseInt(rotYaw);
            } else {
                String strYaw = m.replaceAll("").trim();
                yaw = Integer.parseInt(strYaw);
            }

            if (rotPitch.matches("^-?\\d+$")) {
                pitch = Integer.parseInt(rotPitch);
            } else {
                String strPitch = m.replaceAll("").trim();
                pitch = Integer.parseInt(strPitch);
            }

            Vector3d rotation = new Vector3d(roll, yaw, pitch);

            // Invoke RailRenderer Here----v

        });
    }

    public void addTextFields() {
        rotationRoll = new TextFieldWidget(this.font, this.width / 2 - 20, 46, 100, 20,
                new TranslationTextComponent("client." + GensokyoOntology.MODID +
                        "text_field.rotation_roll"));
        rotationYaw = new TextFieldWidget(this.font, this.width / 2 - 20, 76, 100, 20,
                new TranslationTextComponent("client." + GensokyoOntology.MODID +
                        "text_field.rotation_yaw"));
        rotationPitch = new TextFieldWidget(this.font, this.width / 2 - 20, 106, 100, 20,
                new TranslationTextComponent("client." + GensokyoOntology.MODID +
                        "text_field.rotation_pitch"));

        this.children.add(rotationRoll);
        this.children.add(rotationYaw);
        this.children.add(rotationPitch);
    }

    @Override
    @SuppressWarnings("all")
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        // this.drawString(matrixStack, this.font, new TranslationTextComponent("client." + GensokyoOntology.MODID + "main.test"),this.width / 2 - 10, 30, 0xeb0505);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public void renderBackground(MatrixStack matrixStack) {
        matrixStack.translate(0, 0, 0);
        super.renderBackground(matrixStack);
        this.getMinecraft().textureManager.bindTexture(MAIN);
        int texWidth = 166;
        int texHeight = 252;
        this.blit(matrixStack, 0, 0, 0, 0, texWidth, texHeight);

    }

    @Override
    protected void renderComponentHoverEffect(MatrixStack matrixStack, @Nullable Style style, int mouseX, int mouseY) {
        super.renderComponentHoverEffect(matrixStack, style, mouseX, mouseY);
    }

    @Override
    protected void renderTooltip(MatrixStack matrixStack, ItemStack itemStack, int mouseX, int mouseY) {
        // this.renderWrappedToolTip();
        // GuiUtils.drawHoveringText(itemStack,matrixStack,List<? extends TextProperties>);
        super.renderTooltip(matrixStack, itemStack, mouseX, mouseY);
    }

    public void renderAdjustButton() {

    }

    public static void open() {
        Minecraft.getInstance().displayGuiScreen(new RailAdjustGUI());
    }
}

 */
