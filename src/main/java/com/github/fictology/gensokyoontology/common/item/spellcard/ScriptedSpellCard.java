package com.github.fictology.gensokyoontology.common.item.spellcard;

/*
public class ScriptedSpellCard extends SpellCardItem {

    @Override
    @NotNull
    public ActionResult<ItemStack> onItemRightClick(@NotNull World worldIn, @NotNull PlayerEntity playerIn, @NotNull Hand handIn) {
        if (playerIn.getCooldownTracker().hasCooldown(this))
            return ActionResult.resultPass(playerIn.getHeldItem(handIn));

        ItemStack stack = playerIn.getHeldItem(handIn);
        if (stack.getTag() != null && stack.getTag().contains("scripts")) {
            ScriptedSpellCardEntity scriptedSpellCard = new ScriptedSpellCardEntity(worldIn, playerIn, stack.getTag());
            worldIn.addEntity(scriptedSpellCard);

            playerIn.getCooldownTracker().setCooldown(this, 1200);
        }

        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    protected void applySpell(World worldIn, PlayerEntity playerIn) {

    }
}

 */
