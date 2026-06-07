package com.github.fictology.gensokyoontology.common.container.menu;

import com.github.fictology.gensokyoontology.registry.BlockRegistry;
import com.github.fictology.gensokyoontology.registry.MenuRegistry;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class DanmakuCraftMenu extends AbstractContainerMenu {
    public static final int RESULT_SLOT = 0;
    private static final int CRAFT_SLOT_START = 1;
    private static final int CRAFT_SLOT_COUNT = 25;
    private static final int CRAFT_SLOT_END = 26;
    private static final int INV_SLOT_START = 26;
    private static final int INV_SLOT_END = 53;
    private static final int HOT_BAR_START = 53;
    private static final int HOT_BAR_END = 62;
    protected final CraftingContainer craftSlots;
    private final ContainerLevelAccess access;
    private final Player player;
    private final int width = 5;
    private final int height = 5;
    private Container resultSlots = new ResultContainer();

    public DanmakuCraftMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, ContainerLevelAccess.NULL);
    }

    public DanmakuCraftMenu(int containerId, Inventory playerInventory, ContainerLevelAccess access) {
        super(MenuRegistry.DANMAKU_MENU.get(), containerId);
        this.access = access;
        this.player = playerInventory.player;
        this.craftSlots = new TransientCraftingContainer(this, width, height);
        this.addResultSlot(this.player, 124, 35);
        this.addCraftingGridSlots(30, 17);
        this.addStandardInventorySlots(playerInventory, 8, 84);
    }

    private void addCraftingGridSlots(int x, int y) {
        for (int i = 0; i < this.width; ++i) {
            for (int j = 0; j < this.height; ++j) {
                this.addSlot(new Slot(this.craftSlots, j + i * this.width, x + j * 18, y + i * 18));
            }
        }
    }

    private Slot addResultSlot(Player player, int x, int y) {
        return this.addSlot(new ResultSlot(player, this.craftSlots, this.resultSlots, 0, x, y));
    }

    public Slot getResultSlot() {
        return this.slots.get(0);
    }

    public List<Slot> getInputGridSlots() {
        return this.slots.subList(1, 26);
    }

    protected Player owner() {
        return this.player;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot) this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index == 0) {
                itemstack1.getItem().onCraftedBy(itemstack1, player);
                if (!this.moveItemStackTo(itemstack1, CRAFT_SLOT_END, HOT_BAR_END, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemstack1, itemstack);
            } else if (index >= 10 && index < 46) {
                if (!this.moveItemStackTo(itemstack1, CRAFT_SLOT_START, CRAFT_SLOT_END, false)) {
                    if (index < 37) {
                        if (!this.moveItemStackTo(itemstack1, HOT_BAR_START, HOT_BAR_END, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (!this.moveItemStackTo(itemstack1, INV_SLOT_START, INV_SLOT_END, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!this.moveItemStackTo(itemstack1, INV_SLOT_START, HOT_BAR_END, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
            if (index == 0) {
                player.drop(itemstack1, false);
            }
        }
        return itemstack;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(this.access, player, BlockRegistry.DANMAKU_TABLE.get());
    }
}
