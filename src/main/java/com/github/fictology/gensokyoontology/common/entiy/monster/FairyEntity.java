//package com.github.fictology.gensokyoontology.common.entiy.monster;
//
//import com.github.fictology.gensokyoontology.common.combat.DanmakuUtil;
//import com.github.fictology.gensokyoontology.common.entiy.ai.goal.DamakuAttackGoal;
//import com.github.fictology.gensokyoontology.common.entiy.misc.Danmaku;
//import com.github.fictology.gensokyoontology.registry.ItemRegistry;
//import com.github.fictology.gensokyoontology.api.V3f;
//import net.minecraft.server.level.ServerLevel;
//import net.minecraft.util.Mth;
//import net.minecraft.world.entity.AgeableMob;
//import net.minecraft.world.entity.EntityType;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.entity.ai.goal.Goal;
//import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
//import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
//import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
//import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
//import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
//import net.minecraft.world.entity.ai.navigation.PathNavigation;
//import net.minecraft.world.entity.animal.FlyingAnimal;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.phys.Vec3;
//import org.jetbrains.annotations.Nullable;
//
//import java.util.EnumSet;
//import java.util.List;
//import java.util.Random;
//
//public class FairyEntity extends RetreatableEntity implements FlyingAnimal {
//
//    private static final int MAX_LIVING_TICK = 3000;
//
//    public FairyEntity(EntityType<FairyEntity> entityTypeIn, Level worldIn) {
//        super(entityTypeIn, worldIn);
//        this.setNoGravity(true);
//        // this.getAttributes().assignBaseValues(Attributes.MAX_HEALTH.value());
//    }
//
//    @Override
//    protected void registerGoals() {
//        this.goalSelector.addGoal(1, new DamakuAttackGoal(this, 100, 0.6f));
//        this.goalSelector.addGoal(2, new WaterAvoidingRandomFlyingGoal(this, 0.8f));
//        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0f));
//        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
//
//        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
//    }
//
//
//    @Override
//    protected PathNavigation createNavigation(Level level) {
//        var navigation = new FlyingPathNavigation(this, level);
//        navigation.setCanOpenDoors(false);
//        navigation.setCanFloat(true);
//        return navigation;
//    }
//
//    @Override
//    public void tick() {
//        super.tick();
//        if (this.tickCount >= MAX_LIVING_TICK) {
//            this.setRemoved(RemovalReason.DISCARDED);
//        }
//    }
//
//
//    @Override
//    public void checkDespawn() {
//        super.checkDespawn();
//    }
//
//    @Override
//    public void setNoGravity(boolean noGravity) {
//        super.setNoGravity(true);
//    }
//
//    @Override
//    public boolean isFood(ItemStack itemStack) {
//        return false;
//    }
//
//    @Nullable
//    @Override
//    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
//        return null;
//    }
//
//    @Override
//    public boolean isFlying() {
//        return true;
//    }
//
//    @Override
//    public void danmakuAttack(LivingEntity target) {
//        Random r1 = new Random(this.getUUID().getMostSignificantBits());
//        switch (r1.nextInt(4)) {
//            case 0:
//                aimedShot(target);
//                break;
//            case 1:
//                oddAimedShot(target);
//                break;
//            case 2:
//                spiralShot();
//                break;
//            default:
//                sphereShot();
//                break;
//        }
//    }
//
//    private Danmaku randomSelect() {
//        Random r2 = new Random(this.getUUID().getLeastSignificantBits());
//        return switch (r2.nextInt(4)) {
//            case 0 -> Danmaku.create(this.level(), ItemRegistry.SMALL_SHOT_RED.get(), this);
//            case 1 -> Danmaku.create(this.level(), ItemRegistry.SCALE_SHOT_RED.get(), this);
//            case 2 -> Danmaku.create(this.level(), ItemRegistry.RICE_SHOT_PURPLE.get(), this);
//            default -> Danmaku.create(this.level(),ItemRegistry.SMALL_SHOT_BLUE.get(), this);
//        };
//    }
//
//    private void aimedShot(LivingEntity target) {
//        var direction = DanmakuUtil.getAimedVec(this, target);
//        var danmaku = randomSelect();
//        danmaku.shoot(direction.x, direction.y, direction.z, 0.7f, 0f);
//        this.level().addFreshEntity(danmaku);
//
//    }
//
//    private void oddAimedShot(Vec3 shootVec) {
//        var danmaku = randomSelect();
//        danmaku.shoot(shootVec.x, shootVec.y, shootVec.z, 0.7f, 0f);
//        this.level().addFreshEntity(danmaku);
//    }
//
//    private void oddAimedShot(LivingEntity target) {
//        aimedShot(target);
//        var shootVec = DanmakuUtil.getAimedVec(this, target);
//        oddAimedShot(shootVec.yRot(Mth.PI / 90));
//        oddAimedShot(shootVec.yRot(-Mth.PI / 90));
//    }
//
//    private void spiralShot() {
//        for (int i = 0; i < 4; i++) {
//            var shootVec = new Vec3(V3f.XP.cast()).yRot((float) Math.PI / 36 * tickCount)
//                    .xRot((float) Math.PI / 36 * tickCount).yRot((float) Math.PI / 2 * i);
//            var danmaku = randomSelect();
//            danmaku.shoot(shootVec.x, shootVec.y, shootVec.z, 0.7f, 0f);
//            this.level().addFreshEntity(danmaku);
//        }
//
//    }
//
//    private void sphereShot() {
//        List<Vec3> coordinates = DanmakuUtil.spheroidPos(1, 20);
//        coordinates.forEach(vector3d -> {
//            var danmaku = randomSelect();
//            danmaku.shoot(vector3d.x, vector3d.y, vector3d.z, 0.7f, 0f);
//            this.level().addFreshEntity(danmaku);
//        });
//    }
//
//    /**
//     * Copy below from Minecraft vanilla {@link net.minecraft.world.entity.monster.Ghast}
//     */
//    static class RandomFlyGoal extends Goal {
//        private final FairyEntity parentEntity;
//
//        public RandomFlyGoal(FairyEntity fairy) {
//            this.parentEntity = fairy;
//            this.setFlags(EnumSet.of(Flag.MOVE));
//        }
//
//        /**
//         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
//         * method as well.
//         */
//        public boolean shouldExecute() {
//            var control = this.parentEntity.getMoveControl();
//            if (!control.hasWanted()) {
//                return true;
//            } else {
//                double d0 = control.getWantedX() - this.parentEntity.getX();
//                double d1 = control.getWantedY() - this.parentEntity.getY();
//                double d2 = control.getWantedZ() - this.parentEntity.getZ();
//                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
//                return d3 < 1.0D || d3 > 3600.0D;
//            }
//        }
//
//        /**
//         * Returns whether an in-progress EntityAIBase should continue executing
//         */
//        public boolean shouldContinueExecuting() {
//            return false;
//        }
//
//        /**
//         * Execute a one shot task or start executing a continuous task
//         */
//        public void startExecuting() {
//            var random = this.parentEntity.getRandom();
//            double d0 = this.parentEntity.getX() + (double) ((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
//            double d1 = this.parentEntity.getY() + (double) ((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
//            double d2 = this.parentEntity.getZ() + (double) ((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
//            this.parentEntity.getMoveControl().setWantedPosition(d0, d1, d2, 0.45D);
//        }
//
//        @Override
//        public boolean canUse() {
//            return false;
//        }
//    }
//
//
//}
