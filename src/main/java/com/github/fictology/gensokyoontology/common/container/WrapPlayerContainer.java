package com.github.fictology.gensokyoontology.common.container;

import net.minecraft.core.NonNullList;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;
import net.neoforged.neoforge.transfer.item.VanillaContainerWrapper;

public abstract class WrapPlayerContainer extends SimpleContainer {
    public final NonNullList<Slot> slots = NonNullList.create();
    protected final VanillaContainerWrapper playerInv;
    private final NonNullList<ItemStack> itemStacks = NonNullList.create();
    private final int size;

    public WrapPlayerContainer(int size, Inventory playerInv, ItemStack stack) {
        super(size);
        this.size = size;
        this.playerInv = (VanillaContainerWrapper) VanillaContainerWrapper.of(playerInv);
    }

    protected Slot createSlot(Slot slot) {
        slot.index = this.slots.size();
        this.slots.add(slot);
        this.itemStacks.add(slot.getItem());
        return slot;
    }

    protected int addSlotRange(int index, int x, int y, int amount, int dx) {
        for (int i = 0; i < amount; i++) {
            this.addSlot(index, x, y);
            x += dx;
            index++;
        }
        return index;
    }

    protected Slot addSlot(int index, int x, int y) {
        return this.createSlot(new ResourceHandlerSlot(this.playerInv, (i, r, a) -> {}, index, x, y));
    }

    protected void addSlotBox(int index, int x, int y, int horAmount, int verAmount, int dx, int dy) {
        for (int j = 0; j < verAmount; j++) {
            index = addSlotRange(index, x, y, horAmount, dx);
            y += dy;
        }
    }

    protected void addPlayerInventorySlots(int xStart, int yStart) {
        addSlotBox(9, xStart, yStart, 9, 3, 18, 18);
        yStart += 58;
        addSlotRange(0, xStart, yStart, 9, 18);
    }
}
