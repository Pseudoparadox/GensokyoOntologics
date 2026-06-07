package com.github.fictology.gensokyoontology.client.gui;


import com.github.fictology.gensokyoontology.util.api.IInputParser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public abstract class LayoutMenuScreen<M extends AbstractContainerMenu> extends AbstractContainerScreen<M> implements IInputParser {
    protected static final int WHITE = 16777215;
    protected static final int DARK_GRAY = 5592405;
    protected int windowWidth;
    protected int windowHeight;
    protected int titleX;
    protected int titleY;
    protected int xSize;
    protected int ySize;
    protected int invTitleX;
    protected int invTitleY;

    public LayoutMenuScreen(M menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.windowWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        this.windowHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();
    }
}
