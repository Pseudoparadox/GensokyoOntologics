package com.github.fictology.gensokyoontology.common.combat;

import com.github.fictology.gensokyoontology.common.entiy.misc.Danmaku;
import com.github.fictology.gensokyoontology.common.entiy.misc.Laser;
import com.github.fictology.gensokyoontology.registry.ItemRegistry;
import com.github.fictology.gensokyoontology.util.GSKOMathUtil;
import com.github.fictology.gensokyoontology.api.BossSpell;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class SpellBehaviors {
    public static final BossSpell<LivingEntity> HELL_ECLIPSE = (level, living) -> {
        var fakeLunar = Danmaku.create(level, ItemRegistry.FAKE_LUNAR_ITEM.get(), living);
        var ticksExisted = living.tickCount;

        float angle = (float) (Math.PI / 60 * ticksExisted);
        float lunarAngle = (float) ((level.getGameTime() * 0.1f) % (Math.PI * 2));
        float speed = 0.2F;

        var center = new Vec3(1, 0, 0);
        var local = center.add(4, 0, 0).yRot(angle);
        var global = local.add(living.getPosition(0f));
        var lunarPos = fakeLunar.getPosition(0f);
        var lunarMotion = new Vec3(Math.cos(lunarAngle) * speed, 0, Math.sin(lunarAngle) * speed);

        fakeLunar.setPos(lunarPos.x + lunarMotion.x, lunarPos.y, lunarPos.z + lunarMotion.z);
        fakeLunar.setDeltaMovement(lunarMotion);

        for (int i = 0; i < 8; i++) {
            var smallShot = Danmaku.create(level, ItemRegistry.SMALL_SHOT_RED.get(), living);
            Vec3 vector3d = center.yRot((float) (Math.PI / 4 * i)).yRot((float) (Math.PI / 100 * ticksExisted));
            smallShot.setOldPosAndRot(new Vec3(global.x, global.y, global.z), (float) center.y, (float) center.z);
            smallShot.setNoGravity(true);
            smallShot.shoot(vector3d.x, 0, vector3d.z, 0.5F, 0F);
            level.addFreshEntity(smallShot);
        }
    };

    public static final BossSpell<LivingEntity> WAVE_PARTICLE = (level, living) -> {
        var ticksExisted = living.tickCount;
        for (int i = 0; i < 3; i++) {
            var riceShot = Danmaku.create(level, ItemRegistry.RICE_SHOT_PURPLE.get(), living).from(living);
            var muzzle = living.getLookAngle().yRot((float) Math.PI * 2 / 3 * i);
            var shootAngle = muzzle.yRot((float) (Math.PI / 180 * ticksExisted * ((float) ticksExisted / 5)));

            riceShot.shoot(shootAngle.x, 0, shootAngle.z, 0.6f, 0f);
            level.addFreshEntity(riceShot);
        }
    };

    public static final BossSpell<Entity> MOBIUS_RING = (level, living) -> {
        var horizonVec = new Vec3(0, 0, 1);
        var ticksExisted = living.tickCount;
        horizonVec = horizonVec.scale(6);

        // 创建竖圆：
        var verticalVec = new Vec3(0, 0, 1);
        verticalVec = verticalVec.scale(3);

        var velocity = 0.6f;
        var rotation = (float) (Math.PI / 80 * 2 * ticksExisted);

        horizonVec = horizonVec.yRot(rotation);

        // createMobius(level, living, verticalVec, velocity);
        var horizonVecNew = horizonVec.yRot((float) Math.PI);
        // createMobius(level, living, verticalVec, velocity);
    };

    public static final BossSpell<Entity> REMILIA_LASER_SPIRAL = (level, remilia) -> {
        int count = 25;
        Vec3 initRot = new Vec3(1, 0, 0);
        Vec3 position = remilia.getPosition(0);

        for (int i = remilia.tickCount; i < remilia.tickCount + count; i++) {
            int unit = i - remilia.tickCount;
            // initRot = initRot.rotatePitch((float) Math.PI / 25 * unit);
            initRot = initRot.yRot((float) Math.PI / count);
            position = position.add(new Vec3(0, 0.5, 0));

            var direction = GSKOMathUtil.toYawPitch(initRot);
            var laser = new Laser(remilia.level(), remilia);
            laser.setOldPosAndRot(new Vec3(position.x, position.y, position.z), direction.y, direction.x);
            laser.init(500, 30, 100);
            level.addFreshEntity(laser);
        }
    };
    public static final BossSpell<LivingEntity> MANIA_DEPRESS = (level, living) -> {
        var pinkPositions = DanmakuUtil.getHeartLinePos(0.3f, 0.11);
        var aquaPositions = DanmakuUtil.getHeartLinePos(1.8f, 0.11);
        var ticksExisted = living.tickCount;

        int shootInterval = 15;
        int ratio = ticksExisted / shootInterval;

        pinkPositions = DanmakuUtil.getRotatedPos(pinkPositions, (float) Math.PI / 12 * ratio, 0f);
        aquaPositions = DanmakuUtil.getRotatedPos(aquaPositions, (float) Math.PI / 2, 0f);
        aquaPositions = DanmakuUtil.getRotatedPos(aquaPositions, (float) Math.PI / 6 * ratio, 0f);

        if (ticksExisted % shootInterval == 0) {

            for (Vec3 vector3d : pinkPositions) {
                var danmaku = Danmaku.create(level, ItemRegistry.HEART_SHOT_PINK.get(), living);
                var shootVec = new Vec3(vector3d.x, vector3d.y, vector3d.z);

                danmaku.shoot(shootVec.x, shootVec.y, shootVec.z, 0.55f, 0f);
                level.addFreshEntity(danmaku);
            }

            for (Vec3 vector3d : pinkPositions) {
                var danmaku = Danmaku.create(level, ItemRegistry.HEART_SHOT_PINK.get(), living);
                var shootVec = new Vec3(vector3d.x, vector3d.y, vector3d.z);

                danmaku.shoot(shootVec.x, shootVec.y, shootVec.z, 0.55f, 0f);
                level.addFreshEntity(danmaku);
            }
        }
    };

    /*
    private static void createMobius(Level level, Entity living, Vec3 verticalVec, float velocity) {
        for (var item : ItemRegistry.SMALL_SHOTS) {
            var smallShot = Danmaku.create(level, item, living);

            verticalVec = verticalVec.xRot((float) Math.PI / ItemRegistry.SMALL_SHOTS.indexOf(item));
            verticalVec = verticalVec.yRot((float) Math.PI / 80 * 2 * living.tickCount);
            smallShot.shoot((float) verticalVec.x, (float) verticalVec.y, (float) verticalVec.z, velocity, 0f);

            level.addFreshEntity(smallShot);
        }
    }

     */
}
