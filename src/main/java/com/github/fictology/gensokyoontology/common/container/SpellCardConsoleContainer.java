package com.github.fictology.gensokyoontology.common.container;

/*
public class SpellCardConsoleContainer extends ScriptBuilderContainer {
    public final IInventory consoleStacks = new Inventory(31);
    private final TileEntity tileEntity;

    public SpellCardConsoleContainer(int id, Player player, Level level, BlockPos blockPos) {
        super(ContainerRegistry.SPELL_CONSOLE_CONTAINER.get(), player.getInventory(), id);
        this.tileEntity = level.getTileEntity(blockPos);

        this.addPlayerInventorySlots(47, 168);
        if (this.tileEntity != null) {
            this.tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(itemHandler -> {
                int index = 0;
                for (int i = 0; i < 10; i++) {
                    for (int j = 0; j < 3; j++) {
                        // createSlot(new Slot(consoleStacks, index, ((i * 18) + 22), ((j * 23) + 25)));
                        addArgsSlot(itemHandler, index, (i * 18) + 22, (j * 23) + 25);
                        index++;
                    }
                }
                addArgsSlot(itemHandler, itemHandler.getSlots() - 1, 224, 71);
            });
        }
    }

    public TileEntity getTileEntity() {
        return this.tileEntity;
    }

    private void addArgsSlot(IItemHandler itemHandler, int index, int xPos, int yPos) {
        createSlot(new SlotItemHandler(itemHandler, index, xPos, yPos));
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }

    public boolean isAllowedItem(int index) {
        AtomicBoolean flag = new AtomicBoolean();
        if (this.tileEntity != null) {
            this.tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(itemHandler -> {
                flag.set(itemHandler.getStackInSlot(index).isEmpty());
            });
        }
        return flag.get();
    }

    public boolean hasAllowedTag(int index) {
        AtomicBoolean flag = new AtomicBoolean();
        if (this.tileEntity != null) {
            this.tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(itemHandler -> {
                CompoundTag nbt = getTag(index);
                boolean contains = nbt.contains("type") && nbt.contains("value") || nbt.contains("name");
                flag.set(contains);
            });
        }
        return flag.get();
    }

    public CompoundTag getTag(int index) {
        AtomicReference<CompoundTag> nbtAtom = new AtomicReference<>();
        if (this.tileEntity != null) {
            this.tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(itemHandler -> {
                nbtAtom.set(itemHandler.getStackInSlot(index).getTag());
            });
        }
        return nbtAtom.get();
    }

    public ItemStack getOutputStack() {
        AtomicReference<ItemStack> stackAtom = new AtomicReference<>();
        if (this.tileEntity != null) {
            this.tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(itemHandler -> {
                stackAtom.set(itemHandler.getStackInSlot(itemHandler.getSlots() - 1));
            });
        }
        return stackAtom.get();
    }

    @Override
    public @NotNull ItemStack transferStackInSlot(@NotNull PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack stack = slot.getStack();
            itemstack = stack.copy();
            if (index == 0) slot.onSlotChange(stack, itemstack);
            else if (index >= 10 && index < 67) {
                if (!this.mergeItemStack(stack, 1, 10, true)) {
                    if (!this.mergeItemStack(stack, 10, 37, true)) return ItemStack.EMPTY;
                }
            }
            if (stack.isEmpty()) slot.putStack(ItemStack.EMPTY);
            else slot.onSlotChanged();

            if (stack.getCount() == itemstack.getCount()) return ItemStack.EMPTY;
            ItemStack itemStack2 = slot.onTake(playerIn, stack);
            if (index == 0) playerIn.dropItem(itemStack2, false);
        }
        return itemstack;
    }
}

 */
