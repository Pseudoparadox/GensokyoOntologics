package com.github.fictology.gensokyoontology.common.entiy.ai.goal;

import com.github.fictology.gensokyoontology.common.entiy.monster.RetreatableEntity;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.pathfinder.PathFinder;

/**
 * Copy from <a href="https://github.com/TartaricAcid/TouhouLittleMaid/blob/1.16.5/src/main/java/com/github/tartaricacid/touhoulittlemaid/entity/ai/goal/FairyAttackGoal.java#L12">车万女仆中有关妖精AI的GitHub仓库界面</a>
 * <br>
 */
public class DamakuAttackGoal extends Goal {
    private final RetreatableEntity entity;
    private final double minDistance;
    private final double speedIn;
    private final int delay = 30;
    private final int continuing = 5;
    public PathFinder path;

    public DamakuAttackGoal(RetreatableEntity entity, double minDistance, double speedIn) {
        this.entity = entity;
        this.minDistance = minDistance;
        this.speedIn = speedIn;
    }

    @Override
    public boolean canUse() {
        var target = this.entity.getTarget();
        return target != null && target.isAlive() &&
                !this.entity.doesTargetBelieveBuddhism(target) &&
                target.level().getDifficulty() != Difficulty.PEACEFUL;
    }

    @Override
    public void tick() {
        var target = this.entity.getTarget();
        if (target == null || !target.isAlive()) {
            return;
        }
        this.entity.getLookControl().setLookAt(target, 30.0F, 30.0F);
        double distance = this.entity.distanceTo(target);
        if (this.entity.getSensing().hasLineOfSight(target) && distance >= minDistance) {
            this.entity.getNavigation().moveTo(target, this.speedIn);
        } else if (distance < minDistance) {
            this.entity.getNavigation().stop();
            var motion = entity.getDeltaMovement();
            entity.setDeltaMovement(motion.x, 0, motion.z);
            entity.setNoGravity(true);
            if (entity.tickCount % delay == 0) {
                entity.danmakuAttack(target);
            }
        }
    }

    @Override
    public boolean canContinueToUse() {
        var target = this.entity.getTarget();
        if (target == null || !target.isAlive() || target.level().getDifficulty() == Difficulty.PEACEFUL) {
            return false;
        } else {
            boolean isPlayerAndCanNotBeAttacked = target instanceof Player
                    && (target.isSpectator() || ((Player) target).isCreative());
            return !isPlayerAndCanNotBeAttacked;
        }
    }

}
