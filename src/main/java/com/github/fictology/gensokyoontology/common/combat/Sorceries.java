package com.github.fictology.gensokyoontology.common.combat;

import com.github.fictology.gensokyoontology.common.entiy.misc.Danmaku;
import com.github.fictology.gensokyoontology.common.entiy.misc.DestructiveEyeEntity;
import com.github.fictology.gensokyoontology.common.entiy.misc.Laser;
import com.github.fictology.gensokyoontology.common.entiy.monster.YoukaiEntity;
import com.github.fictology.gensokyoontology.registry.EntityRegistry;
import com.github.fictology.gensokyoontology.registry.ItemRegistry;
import com.github.fictology.gensokyoontology.util.GSKOMathUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;

public final class Sorceries {
    private static final int MAX_DISTANCE = 8;
    public static final YoukaiCombat.SorceryAction<YoukaiEntity> DESTRUCTIVE_EYE = (level, entity) -> {
        var target = entity.getTarget();
        var list = new ArrayList<Entity>();

        if (target == null) return;
        if (!(level instanceof ServerLevel serverLevel)) return;

        serverLevel.getAllEntities().forEach(list::add);
        boolean canSummon = list.stream().filter(e -> e.getType() == EntityRegistry.DESTRUCTIVE_EYE.get()).count() <= 20;

        if (canSummon) {
            for (int i = 0; i < 3; i++) {
//                var eye = new DestructiveEyeEntity(entity.level());
//                var vector3d = DanmakuUtil.getRandomPosWithin(MAX_DISTANCE, DanmakuUtil.Plane.XYZ).add(target.getPosition(0));
//                eye.setOldPosAndRot(new Vec3(vector3d.x, vector3d.y, vector3d.z), 0F, 0F);
//                level.addFreshEntity(eye);
            }
        }

        if (entity.tickCount >= 500) entity.nextPhase();
    };

    public static final YoukaiCombat.EventAction<YoukaiEntity> BECOME_BAT = (youkai, event, timer) -> {
        if (!(youkai.level() instanceof ServerLevel serverLevel)) return;
        if (timer.get() == 0){
            var bat = EntityType.BAT.spawn(serverLevel, youkai.getOnPos().above(), EntitySpawnReason.EVENT);
            if (bat == null) return;
            youkai.startRiding(bat);
            youkai.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY));
            youkai.setInvulnerable(true);
            return;
        }
        if (timer.get() >= event.maxTick()){
            var bat = youkai.getControllingPassenger();
            if (bat == null) return;
            bat.remove(Entity.RemovalReason.DISCARDED);
            youkai.removeAllEffects();
            youkai.setInvulnerable(false);
            return;
        }

        if (youkai.tickCount % 18 != 0) return;
        DanmakuUtil.spheroidPos(1, 10).forEach(vec3 -> {
            var shot = Danmaku.create(serverLevel, ItemRegistry.SMALL_SHOT_RED.get(), youkai);
            shot.shoot(vec3.x, vec3.y, vec3.z, 0.7f, 0f);
            serverLevel.addFreshEntity(shot);
        });
    };

    public static final YoukaiCombat.SorceryAction<YoukaiEntity> LASER_RAIN = (level, youkai) -> {
        if (!(level instanceof ServerLevel serverLevel)) return;
        if (youkai.tickCount % 18 == 0) {
            var laser = new Laser(level, youkai);
            var target = youkai.getTarget();
            if (target == null) return;

            var vec2 = GSKOMathUtil.toYawPitch(new Vec3(0, -1, 0));
            var height = target.getOnPos().above(10);
            var randomPos = new Vec3(GSKOMathUtil.randomRange(0, 5), height.getY(), GSKOMathUtil.randomRange(0,5));
            laser.setOldPosAndRot(randomPos, vec2.y, vec2.x);
            serverLevel.addFreshEntity(laser);
        }
    };

    public static final YoukaiCombat.SorceryAction<YoukaiEntity> SNOW_STORM = (level, youkai) -> {
        if (!(level instanceof ServerLevel serverLevel)) return;
        var crystalVec = new Vec3(1,0,0);
        var rotSpeed = 0.01f;
        var factor = youkai.tickCount % 400;
        var orbs = DanmakuUtil.spheroidPos(5, 16);

        // 前10秒递增后10秒递减
        for (int i = 0; i < 6; i++) {
            final float angle = Mth.PI / 6 * i + (factor > 200 ? rotSpeed * factor : -rotSpeed * factor);
            crystalVec = crystalVec.yRot(angle);
            crystalVec = crystalVec.xRot(angle);
            var crystals = Danmaku.create(level, ItemRegistry.RICE_SHOT_BLUE.get(), youkai);
            crystals.shoot(crystalVec.x, crystalVec.y, crystalVec.z, 0.5f, 0f);
            level.addFreshEntity(crystals);
        }

        if (youkai.tickCount % 40 == 0){
            orbs.forEach(vec3 -> {
                var orb = Danmaku.create(level, ItemRegistry.SMALL_SHOT_BLUE.get(), youkai);
                orb.shoot(vec3.x, vec3.y, vec3.z, 0.7f, 0f);
                level.addFreshEntity(orb);
            });
        }
    };


}
