package com.github.fictology.gensokyoontology.common.item;


import com.github.fictology.gensokyoontology.common.entiy.misc.Danmaku;
import com.github.fictology.gensokyoontology.registry.DataRegistry;
import com.github.fictology.gensokyoontology.registry.Expressions;
import com.github.fictology.gensokyoontology.registry.ItemRegistry;
import com.github.fictology.gensokyoontology.util.script.*;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.throwableitemprojectile.Snowball;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DanmakuItem extends Item implements ProjectileItem {

    public DanmakuItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        level.playSound(
                null,
                player.getX(),
                player.getY(),
                player.getZ(),
                SoundEvents.SNOWBALL_THROW,
                SoundSource.NEUTRAL,
                0.5F,
                0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F)
        );
        if (level instanceof ServerLevel serverLevel) {
            Projectile.spawnProjectileFromRotation(Danmaku::new, serverLevel, itemStack, player, 0.0F, 0.6F, 1.0F);
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        itemStack.consume(1, player);
        return InteractionResult.SUCCESS;
    }

    @Override
    public Projectile asProjectile(Level level, Position position, ItemStack itemStack, Direction direction) {
        return new Danmaku(level, itemStack.getItem());
    }
}
