package com.github.fictology.gensokyoontology.common.combat;

import com.github.fictology.gensokyoontology.common.entiy.misc.Danmaku;
import com.github.fictology.gensokyoontology.common.entiy.misc.Laser;
import com.github.fictology.gensokyoontology.common.entiy.misc.YinyangJade;
import com.github.fictology.gensokyoontology.common.entiy.monster.FlandreScarletEntity;
import com.github.fictology.gensokyoontology.common.entiy.monster.RumiaEntity;
import com.github.fictology.gensokyoontology.common.entiy.monster.YoukaiEntity;
import com.github.fictology.gensokyoontology.data.EventCallbackInfo;
import com.github.fictology.gensokyoontology.registry.ItemRegistry;
import com.github.fictology.gensokyoontology.util.GSKOUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.projectile.Projectile;

public final class BossBattle {
    /** 在Goal中添加该空白阶段以便让玩家拥有反击BOSS的喘息时间，降低玩家BOSS战生存难度。 */
    public static final YoukaiCombat.SorceryAction<YoukaiEntity> BLANK_PHASE = (world, youkaiEntity) -> {};

    public static final YoukaiCombat.SorceryAction<FlandreScarletEntity> SPHERE_SHOOT_FLANDRE = (level, flandre) -> {
        if (flandre.tickCount % 40 != 0) return;
        DanmakuUtil.spheroidPos(1F, 10).forEach(shootVec ->
                Danmaku.shoot(flandre, ItemRegistry.SMALL_SHOT_RED.get(), shootVec, 0.5F));
    };

    public static final YoukaiCombat.EventAction<FlandreScarletEntity> FORBIDDEN_LAEVATEIN = (flandre, event, timer) -> {
        if (!(flandre.level() instanceof ServerLevel serverLevel)) return;
        if (timer.get() == 0){
            var laser = new Laser(serverLevel, flandre);
            laser.setLifespan(400);
            laser.setXRot(flandre.getXRot());
            laser.setYRot(flandre.getYRot());
            serverLevel.addFreshEntity(laser);
            event.callbackData().putInt("laser", laser.getId());
            return;
        }
        var entity = serverLevel.getEntity(event.callbackData().getIntOr("laser", -1));
        if (!(entity instanceof Laser laser)) return;
        if (timer.get() > laser.getPreparation()){
            entity.setYRot(timer.get() * 2);
        }
    };

    public static final YoukaiCombat.TargetAction<RumiaEntity> WALL_SHOOT_RUMIA = (level, rumia, target) -> {
        if (target == null) return;
        if (rumia.tickCount % 50 != 0) return;
        if (!(level instanceof ServerLevel serverLevel)) return;
        for (int i = -3; i <= 3; i++) {
            for (int j = -3; j < 3; j++) {
                var shootVec = DanmakuUtil.getAimedVec(rumia, target)
                        .yRot(Mth.DEG_TO_RAD * (j * 10))
                        .add(0, i * 0.5, 0)
                        .normalize();

                Projectile.spawnProjectileUsingShoot(Danmaku::new, serverLevel, ItemRegistry.SMALL_SHOT_GREEN.toStack(),
                        rumia, shootVec.x, shootVec.y, shootVec.z, 0.4F, 0F);
            }
        }
    };

    public static final YoukaiCombat.TimerAction<RumiaEntity> DARK_BORDER_LINE = (world, rumiaEntity, target, currentTimer) -> {
        if (target == null) return;
        if (rumiaEntity.tickCount % 14 != 0) return;
        int unit = currentTimer.get() % 10;
        int increment = currentTimer.get() % 10 > 5 ? 3 * unit : -3 * unit;
        var vector3d = DanmakuUtil.getAimedVec(rumiaEntity, target).yRot(DanmakuUtil.rad(increment) * rumiaEntity.tickCount);

        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j < 2; j++) {
                var shootVec = vector3d
                        .yRot(DanmakuUtil.rad(j * 5))
                        .add(0, i * 0.1, 0).normalize();
                Danmaku.shoot(rumiaEntity, ItemRegistry.SMALL_SHOT_BLUE.get(), shootVec, 0.4F);
            }
        }
        currentTimer.set(currentTimer.get() + 1);
    };
}
