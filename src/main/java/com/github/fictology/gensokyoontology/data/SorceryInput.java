package com.github.fictology.gensokyoontology.data;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeInput;

public record SorceryInput(ItemStack stack1, ItemStack stack2, ItemStack stack3, ItemStack stack4, ItemStack stack5, ItemStack stack6) implements RecipeInput {
    @Override
    public ItemStack getItem(int i) {
        return switch (i){
            case 0 -> stack1;
            case 1 -> stack2;
            case 2 -> stack3;
            case 3 -> stack4;
            case 4 -> stack5;
            case 5 -> stack6;
            default -> ItemStack.EMPTY;
        };
    }

    public boolean allMatches(Ingredient ingredient){
        var matches = 0;
        if (ingredient.test(stack1)) matches++;
        if (ingredient.test(stack2)) matches++;
        if (ingredient.test(stack3)) matches++;
        if (ingredient.test(stack4)) matches++;
        if (ingredient.test(stack5)) matches++;
        if (ingredient.test(stack6)) matches++;
        return matches == 6;
    }

    @Override
    public int size() {
        return 6;
    }
    
    public class Builder{
        private ItemStack stack1 = ItemStack.EMPTY;
        private ItemStack stack2 = ItemStack.EMPTY;
        private ItemStack stack3 = ItemStack.EMPTY;
        private ItemStack stack4 = ItemStack.EMPTY;
        private ItemStack stack5 = ItemStack.EMPTY;
        private ItemStack stack6 = ItemStack.EMPTY;

        public SorceryInput build(){
            return new SorceryInput(stack1, stack2, stack3, stack4, stack5, stack6);
        }

        public Builder() {}

        public ItemStack stack1() {
            return stack1;
        }

        public void stack1(ItemStack stack1) {
            this.stack1 = stack1;
        }

        public ItemStack stack2() {
            return stack2;
        }

        public void stack2(ItemStack stack2) {
            this.stack2 = stack2;
        }

        public ItemStack stack3() {
            return stack3;
        }

        public void stack3(ItemStack stack3) {
            this.stack3 = stack3;
        }

        public ItemStack stack4() {
            return stack4;
        }

        public void stack4(ItemStack stack4) {
            this.stack4 = stack4;
        }

        public ItemStack stack5() {
            return stack5;
        }

        public void stack5(ItemStack stack5) {
            this.stack5 = stack5;
        }

        public ItemStack stack6() {
            return stack6;
        }

        public void stack6(ItemStack stack6) {
            this.stack6 = stack6;
        }
    }
}
