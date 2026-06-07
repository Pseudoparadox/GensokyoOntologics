package com.github.fictology.gensokyoontology.common.entiy.monster;

import com.github.fictology.gensokyoontology.common.combat.DanmakuUtil;
import com.github.fictology.gensokyoontology.util.GSKOMathUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class RetreatableEntity extends TamableAnimal {
    protected RetreatableEntity(EntityType<? extends TamableAnimal> type, Level worldIn) {
        super(type, worldIn);
    }


    public Vec3 getAimedVec(LivingEntity target) {
        return DanmakuUtil.getAimedVec(this, target);
    }

    public Vec2 getAimedAngle(LivingEntity target) {
        return GSKOMathUtil.toYawPitch(getAimedVec(target));
    }

    public final float getAimedYaw() {
        return GSKOMathUtil.toYawPitch(this.getLookAngle()).y;
    }

    public final float getAimedPitch() {
        return GSKOMathUtil.toYawPitch(this.getLookAngle()).x;
    }

    public abstract void danmakuAttack(LivingEntity target);

    public boolean doesTargetBelieveBuddhism(Entity target) {
        AtomicBoolean condition = new AtomicBoolean();
        if (target != null) {
            // target.getCapability(GSKOCapabilities.IDENTITY).ifPresent(belief ->
            //         condition.set(belief.getValue(IdentityType.BUDDHISM) > 20));
        }
        return condition.get();
    }

    public enum Animation {
        IDLE,
        WALKING,
        DIVING,
        FLYING,
        SITTING,
        SPELL_CARD_ATTACK
    }
}
