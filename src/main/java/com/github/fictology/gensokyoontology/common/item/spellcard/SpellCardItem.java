package com.github.fictology.gensokyoontology.common.item.spellcard;


import com.github.fictology.gensokyoontology.common.entiy.SpellCardEntity;
import com.github.fictology.gensokyoontology.registry.EntityRegistry;
import com.github.fictology.gensokyoontology.common.combat.BossSpell;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class SpellCardItem extends Item {

    private final BossSpell<LivingEntity> spellBehavior;

    public SpellCardItem(Properties properties, BossSpell<LivingEntity> spellBehavior) {
        super(properties);
        this.spellBehavior = spellBehavior;
    }

    @Override
    public @NotNull InteractionResult use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
        var stack = player.getItemInHand(hand);
        if (player.isCreative() && !player.getCooldowns().isOnCooldown(stack)) {
            var spellCard = new SpellCardEntity(EntityRegistry.SPELL_CARD.get(), level);
            // spellCard.init(player, SpellBehaviors.HELL_ECLIPSE);

            player.getCooldowns().addCooldown(stack, 800);
            return InteractionResult.SUCCESS;
        }

        if (player.getCooldowns().isOnCooldown(stack)) return InteractionResult.FAIL;
        spellBehavior.accept(level, player);
        player.getCooldowns().addCooldown(stack, 800);
        return InteractionResult.PASS;
    }
}
