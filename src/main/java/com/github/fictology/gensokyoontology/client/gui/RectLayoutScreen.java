package com.github.fictology.gensokyoontology.client.gui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class RectLayoutScreen<M extends AbstractContainerMenu> extends LayoutMenuScreen<M> {
    public int guiLeft;
    public int guiUp;

    public RectLayoutScreen(M menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    protected void setWidgetPos(AbstractWidget widget, float percentOfWidth, float percentOfHeight) {
        widget.setX((int) (this.windowWidth * percentOfWidth));
        widget.setY((int) (this.windowHeight * percentOfHeight));
    }

    protected void setWidgetSize(AbstractWidget widget, float percentOfWidth, float percentOfHeight) {
        widget.setWidth((int) (this.windowWidth * percentOfWidth));
        widget.setHeight((int) (this.windowHeight * percentOfHeight));
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractRenderState(graphics, mouseX, mouseY, a);
    }

}
