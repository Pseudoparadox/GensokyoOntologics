package com.github.fictology.gensokyoontology.client.gui;

/*
@OnlyIn(Dist.CLIENT)
public class DanmakuCraftingScreen extends ContainerScreen<DanmakuCraftingScreen> {

    public static final ResourceLocation DANMAKU_CRAFTING_TEXTURE = new ResourceLocation(
            GensokyoOntology.MODID, "textures/gui/danmaku_crafting.png"
    );

    public DanmakuCraftingScreen(DanmakuCraftingContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.xSize = 217;
        this.ySize = 211;
        this.playerInventoryTitleX = 15;
        this.playerInventoryTitleY = 111;
    }

    @Override
    public void render(@NotNull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    @SuppressWarnings("deprecation")
    protected void drawGuiContainerBackgroundLayer(@NotNull MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1f, 1f, 1f, 1f);
        if (this.minecraft == null) return;
        this.minecraft.getTextureManager().bindTexture(DANMAKU_CRAFTING_TEXTURE);

        int left = this.guiLeft;
        int top = this.guiTop;
        this.blit(matrixStack, left, top, 0, 0, 256, 256);
    }
}

 */
