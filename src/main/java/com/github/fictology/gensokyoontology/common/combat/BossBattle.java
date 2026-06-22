package com.github.fictology.gensokyoontology.common.combat;

import com.github.fictology.gensokyoontology.common.entiy.misc.Danmaku;
import com.github.fictology.gensokyoontology.common.entiy.monster.RumiaEntity;
import com.github.fictology.gensokyoontology.common.entiy.monster.YoukaiEntity;
import com.github.fictology.gensokyoontology.registry.ItemRegistry;
import net.minecraft.util.Mth;

public final class BossBattle {
    /** 在Goal中添加该空白阶段以便让玩家拥有反击BOSS的喘息时间，降低玩家BOSS战生存难度。 */
    public static final YoukaiCombat.SorceryAction<YoukaiEntity> BLANK_PHASE = (world, youkaiEntity) -> {};

    public static final YoukaiCombat.TargetAction<RumiaEntity> WALL_SHOOT_RUMIA = (world, rumia, target) -> {
        if (target == null) return;
        if (rumia.tickCount % 20 != 0) return;
        for (int i = -4; i <= 4; i++) {
            for (int j = -4; j < 4; j++) {
                var shootVec = DanmakuUtil.getAimedVec(rumia, target)
                        .yRot(Mth.DEG_TO_RAD * (j * 5))
                        .add(0, i * 0.1, 0)
                        .normalize();
                Danmaku.create(world, ItemRegistry.SMALL_SHOT_BLUE.get(), rumia)
                        .damage(3F)
                        .shoot(shootVec.x, shootVec.y, shootVec.z, 0.6F, 1F);
            }
        }
    };

    public static final YoukaiCombat.TimerAction<RumiaEntity> DARK_BORDER_LINE = (world, rumiaEntity, target, currentTimer) -> {
        if (target == null) return;
        if (rumiaEntity.tickCount % 7 != 0) return;
        int unit = currentTimer.get() % 10;
        int increment = currentTimer.get() % 10 > 5 ? 3 * unit : -3 * unit;
        var vector3d = DanmakuUtil.getAimedVec(rumiaEntity, target).yRot(DanmakuUtil.rad(increment) * rumiaEntity.tickCount);

        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j < 2; j++) {
                var shootVec = vector3d
                        .yRot(DanmakuUtil.rad(j * 5))
                        .add(0, i * 0.1, 0).normalize();
                Danmaku.create(world, ItemRegistry.SMALL_SHOT_GREEN.get(), rumiaEntity)
                        .damage(3F)
                        .shoot(shootVec, 0.55F);
            }
        }
        currentTimer.set(currentTimer.get() + 1);
    };
}
