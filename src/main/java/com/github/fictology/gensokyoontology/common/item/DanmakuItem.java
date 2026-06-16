package com.github.fictology.gensokyoontology.common.item;


import com.github.fictology.gensokyoontology.common.entiy.misc.Danmaku;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

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
            Projectile.spawnProjectileFromRotation(Danmaku::new, serverLevel, itemStack, player, 1.0F, 0.6F, 1.0F);
        }

        itemStack.consume(1, player);
        return InteractionResult.SUCCESS;
    }

    @Override
    public @NonNull Projectile asProjectile(@NonNull Level level, @NonNull Position position, ItemStack itemStack, @NonNull Direction direction) {
        return new Danmaku(level, itemStack.getItem());
    }
}
