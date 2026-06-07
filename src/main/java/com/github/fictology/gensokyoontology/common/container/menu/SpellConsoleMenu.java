//package com.github.fictology.gensokyoontology.common.container.menu;
//
//import com.github.fictology.gensokyoontology.registry.BlockRegistry;
//import com.github.fictology.gensokyoontology.registry.MenuRegistry;
//import net.minecraft.world.Container;
//import net.minecraft.world.SimpleContainer;
//import net.minecraft.world.entity.player.Inventory;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.inventory.AbstractContainerMenu;
//import net.minecraft.world.inventory.ContainerLevelAccess;
//import net.minecraft.world.inventory.Slot;
//import net.minecraft.world.item.ItemStack;
//
//public class SpellConsoleMenu extends AbstractContainerMenu {
//    public static final int WIDTH = 10;
//    public static final int HEIGHT = 6;
//    private final Container container;
//    private final ContainerLevelAccess access;
//    private Player player;
//
//    public SpellConsoleMenu(int containerId, Inventory playerInv) {
//        this(containerId, playerInv, ContainerLevelAccess.NULL, new SimpleContainer(ItemStack.EMPTY));
//
//    }
//
//    public SpellConsoleMenu(int containerId, Inventory playerInventory, ContainerLevelAccess access, Container container) {
//        super(MenuRegistry.SPELL_MENU.get(), containerId);
//        this.access = access;
//        this.player = playerInventory.player;
//        this.container = container;
//        this.addScriptSlots(0, 0);
//        this.addStandardInventorySlots(playerInventory, 8, 84);
//    }
//
//    private void addScriptSlots(int x, int y) {
//        for (int i = 0; i < WIDTH; ++i) {
//            for (int j = 0; j < HEIGHT; ++j) {
//                this.addSlot(new Slot(this.container, j + i * WIDTH, x + j * 18, y + i * 18));
//            }
//        }
//    }
//
//    @Override
//    public ItemStack quickMoveStack(Player player, int i) {
//        return null;
//    }
//
//    @Override
//    public boolean stillValid(Player player) {
//        return stillValid(this.access, player, BlockRegistry.SPELL_CARD_CONSOLE.get());
//    }
//
//    public ItemStack getOutputStack() {
//        return container.getItem(0);
//    }
//}
