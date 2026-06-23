package com.github.fictology.gensokyoontology.common.item.touhou;

import com.github.fictology.gensokyoontology.api.IHasCooldown;
import com.github.fictology.gensokyoontology.util.GSKOMathUtil;
import com.github.fictology.gensokyoontology.util.GSKOUtil;
import com.github.fictology.gensokyoontology.api.IRayTraceReader;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class AyaFans extends Item implements IRayTraceReader, IHasCooldown {
    public AyaFans(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (player.getCooldowns().isOnCooldown(player.getItemInHand(hand))) return InteractionResult.PASS;

        var aabb = new AABB(player.getPosition(0).subtract(new Vec3(5, 10, 5)),
                player.getPosition(0).add(new Vec3(5, 10, 5)));

        var lookVec = player.getLookAngle();

        Predicate<LivingEntity> predicate = living -> !(living instanceof Player);
        getEntityWithinSphere(level, LivingEntity.class, predicate, aabb, 12F).forEach(living -> {
            living.knockback(3.0f, -lookVec.x, -lookVec.z);
        });

        AABB box = new AABB(player.getPosition(0).subtract(new Vec3(12, 12, 12)),
                player.getPosition(0).add(new Vec3(12, 12, 12)));

        getEntityWithinSphere(level, Projectile.class, box, 12).forEach(projectile ->
                applyProjectileKnockback(projectile, 3.0f, -lookVec.x, -lookVec.z));

        if (level.isClientSide()) {
            for (int i = 0; i < GSKOMathUtil.randomRange(30, 60); i++) {
                var random = new Random();
                level.addParticle(ParticleTypes.CLOUD, player.getX(), player.getY(), player.getZ(),
                        lookVec.x + random.nextDouble(), lookVec.y + random.nextDouble(), lookVec.z + random.nextDouble());
            }
        }

        this.setCD(player, player.getItemInHand(hand), 240);
        return super.use(level, player, hand);
    }

    private void applyProjectileKnockback(Projectile projectile, float strength, double ratioX, double ratioZ) {
        if (!(strength <= 0.0F)) {
            Vec3 vector3d = projectile.getDeltaMovement();
            Vec3 vector3d1 = (new Vec3(ratioX, 0.0D, ratioZ)).normalize().scale(strength);
            projectile.setDeltaMovement(vector3d.x / 2.0D - vector3d1.x, projectile.onGround() ? Math.min(0.4D, vector3d.y / 2.0D + (double) strength) : vector3d.y, vector3d.z / 2.0D - vector3d1.z);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
        tooltipAdder.accept(GSKOUtil.translate("tooltip", "aya_fans"));
        super.appendHoverText(stack, context, tooltipDisplay, tooltipAdder, flag);
    }

}
