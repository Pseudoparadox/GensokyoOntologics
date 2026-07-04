package github.thelawf.gensokyoontology.common.entity;

import github.thelawf.gensokyoontology.common.entity.misc.RailEntity;
import github.thelawf.gensokyoontology.common.util.math.CurveUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

public class CoasterVehicleEntity extends Entity {

    // 当前所在的轨道实体
    private RailEntity currentRail;
    // 在轨道上的进度 (0.0 - 1.0)
    private float progress = 0.0f;
    // 当前速度 (blocks/tick)
    private float velocity = 0.0f;
    // 最大速度限制
    private float maxVelocity = 2.0f;
    // 当前加速度
    private float acceleration = 0.0f;
    // 进入匀速轨道时的初始速度（用于保持恒定）
    private float constantSpeedEntryVelocity = 0.0f;
    // 是否在匀速轨道上
    private boolean isOnConstantSpeedRail = false;

    // 轨道物理参数
    private static final float ACCEL_GRAVITY = 0.05f;      // 重力加速度
    private static final float FRICTION_COEFFICIENT = 0.98f; // 摩擦系数
    private static final float AIR_RESISTANCE = 0.99f;     // 空气阻力
    private static final float CONSTANT_SPEED_THRESHOLD = 0.01f; // 匀速轨道速度保持阈值

    public CoasterVehicleEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    protected void registerData() {
        // 注册实体数据
    }

    @Override
    protected void readAdditional(CompoundNBT compound) {
        this.progress = compound.getFloat("progress");
        this.velocity = compound.getFloat("velocity");
        this.acceleration = compound.getFloat("acceleration");
        this.constantSpeedEntryVelocity = compound.getFloat("constantSpeedEntryVelocity");
        this.isOnConstantSpeedRail = compound.getBoolean("isOnConstantSpeedRail");
    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {
        compound.putFloat("progress", this.progress);
        compound.putFloat("velocity", this.velocity);
        compound.putFloat("acceleration", this.acceleration);
        compound.putFloat("constantSpeedEntryVelocity", this.constantSpeedEntryVelocity);
        compound.putBoolean("isOnConstantSpeedRail", this.isOnConstantSpeedRail);
    }

    @Override
    public @NotNull IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.currentRail == null || this.world.isRemote) {
            return;
        }

        // 获取当前轨道类型

        // 根据轨道类型更新物理
        this.updatePhysics();

        // 更新位置
        this.updatePosition();

        // 检查是否到达轨道末端
        this.checkNextRail();
    }

    private void updatePhysics() {
        switch (this.currentRail.getRailType()) {
            case ACCELERATION:
                this.updateAccelerateRail();
                isOnConstantSpeedRail = false;
                break;
            case DECELERATION:
                this.updateDecelerateRail();
                isOnConstantSpeedRail = false;
                break;
            case INERTIAL:
                this.updateInertiaRail();
                isOnConstantSpeedRail = false;
                break;
            case UNIFORM:
                this.updateConstantSpeedRail();
                break;
        }
    }

    /**
     * 加速轨道：按加速度加速直到达到最高限速
     */
    private void updateAccelerateRail() {
        // 获取轨道的加速参数
        float trackAcceleration = 0.1f; // 可以从轨道实体获取
        float maxSpeed = 2.0f;         // 可以从轨道实体获取

        // 应用加速度
        this.acceleration = trackAcceleration;
        this.velocity += this.acceleration;

        // 限制最大速度
        if (this.velocity > maxSpeed) {
            this.velocity = maxSpeed;
        }

        // 应用摩擦和空气阻力
        this.velocity *= FRICTION_COEFFICIENT;
        this.velocity *= AIR_RESISTANCE;
    }

    /**
     * 减速轨道：根据轨道长度衰减速度，使载具停在末端
     */
    private void updateDecelerateRail() {
        if (this.currentRail == null) return;

        // 计算剩余距离
        float remainingDistance = (1.0f - this.progress) * getRailLength();

        // 计算需要的减速度，使载具刚好停在末端
        if (remainingDistance > 0.1f) {
            this.acceleration = -(this.velocity * this.velocity) / (2 * remainingDistance);
            this.velocity += this.acceleration;
        } else {
            // 接近末端，强制减速到0
            this.velocity *= 0.9f;
            if (Math.abs(this.velocity) < CONSTANT_SPEED_THRESHOLD) {
                this.velocity = 0.0f;
            }
        }

        // 确保速度不为负
        if (this.velocity < 0) {
            this.velocity = 0;
        }
    }

    /**
     * 惯性轨道：根据真实物理效果加速和减速
     */
    private void updateInertiaRail() {
        if (this.currentRail == null) return;

        // 计算轨道斜率（用于重力加速/减速）
        Vector3d startPos = this.currentRail.getPositionVec();
        Vector3d endPos = this.currentRail.getNextRail().map(Entity::getPositionVec).orElse(startPos);
        Vector3d direction = endPos.subtract(startPos);

        // 计算坡度（y方向变化）
        float slope = (float) (direction.y / direction.length());

        // 重力影响：下坡加速，上坡减速
        this.acceleration = ACCEL_GRAVITY * slope;

        // 应用加速度
        this.velocity += this.acceleration;

        // 应用摩擦和空气阻力
        this.velocity *= FRICTION_COEFFICIENT;
        this.velocity *= AIR_RESISTANCE;

        // 防止反向滑动（在平坦轨道上）
        if (Math.abs(slope) < 0.1f && Math.abs(this.velocity) < CONSTANT_SPEED_THRESHOLD) {
            this.velocity = 0;
        }
    }

    /**
     * 匀速轨道：保持进入时的速度不变
     */
    private void updateConstantSpeedRail() {
        if (this.currentRail == null) return;

        // 第一次进入匀速轨道时，记录进入速度
        if (!isOnConstantSpeedRail) {
            this.constantSpeedEntryVelocity = this.velocity;
            isOnConstantSpeedRail = true;
        }

        // 保持进入时的速度不变
        this.velocity = this.constantSpeedEntryVelocity;

        // 匀速轨道上不应用任何加速度或力
        this.acceleration = 0.0f;

        // 轻微应用摩擦和空气阻力，但保持速度恒定
        float dampedVelocity = this.velocity * FRICTION_COEFFICIENT * AIR_RESISTANCE;

        // 如果速度衰减超过阈值，则恢复到进入速度
        if (Math.abs(dampedVelocity - this.constantSpeedEntryVelocity) > CONSTANT_SPEED_THRESHOLD) {
            this.velocity = this.constantSpeedEntryVelocity;
        }
    }

    private void updatePosition() {
        if (this.currentRail == null) return;

        // 更新进度
        float deltaProgress = this.velocity / getRailLength();
        this.progress += deltaProgress;

        // 限制进度在 [0, 1] 范围内
        if (this.progress < 0) this.progress = 0;
        if (this.progress > 1) this.progress = 1;

        // 计算Hermite曲线上的位置
        Vector3d newPosition = calculatePositionOnRail(this.currentRail, this.progress);
        this.setPosition(newPosition.x, newPosition.y, newPosition.z);

        // 计算并应用旋转（使载具面向运动方向）
        this.updateRotation();
    }

    private Vector3d calculatePositionOnRail(RailEntity rail, float t) {
        Vector3d startPos = rail.getPositionVec();
        Vector3d endPos = rail.getNextRail().map(Entity::getPositionVec).orElse(startPos);

        // 获取方向向量
        Vector3f startDir = rail.getOrientation().copy();
        Vector3f endDir = rail.getNextRail()
                .map(r -> r.getOrientation().copy())
                .orElse(startDir);

        // 使用Hermite插值
        return CurveUtil.hermite3(startPos, endPos, startDir, endDir, t);
    }

    private void updateRotation() {
        if (this.currentRail == null || this.velocity == 0) return;

        // 计算切线方向
        Vector3d tangent = calculateTangentOnRail(this.currentRail, this.progress);

        // 将切线转换为旋转
        float yaw = (float) Math.toDegrees(Math.atan2(tangent.z, tangent.x));
        float pitch = (float) Math.toDegrees(Math.asin(tangent.y / tangent.length()));

        this.rotationYaw = yaw;
        this.rotationPitch = pitch;
    }

    private Vector3d calculateTangentOnRail(RailEntity rail, float t) {
        Vector3d startPos = rail.getPositionVec();
        Vector3d endPos = rail.getNextRail().map(Entity::getPositionVec).orElse(startPos);

        Vector3f startDir = rail.getOrientation().copy();
        Vector3f endDir = rail.getNextRail()
                .map(r -> r.getOrientation().copy())
                .orElse(startDir);

        // 使用Hermite曲线的导数作为切线
        return CurveUtil.hermiteTangent(startPos, endPos, new Vector3d(startDir), new Vector3d(endDir), t).normalize();
    }

    private float getRailLength() {
        if (this.currentRail == null) return 1.0f;

        Vector3d startPos = this.currentRail.getPositionVec();
        Vector3d endPos = this.currentRail.getNextRail().map(Entity::getPositionVec).orElse(startPos);
        return (float) startPos.distanceTo(endPos);
    }

    private void checkNextRail() {
        if (this.progress >= 1.0f && this.currentRail != null) {
            // 尝试切换到下一个轨道
            this.currentRail.getNextRail().ifPresent(nextRail -> {
                if (nextRail instanceof RailEntity) {
                    this.currentRail = nextRail;
                    this.progress = 0.0f;

                    // 根据新轨道类型调整速度
                    adjustVelocityForNewRail();
                }
            });
        }
    }

    private void adjustVelocityForNewRail() {
        RailEntity.Type newType = this.currentRail.getRailType();

        switch (newType) {
            case ACCELERATION:
                // 加速轨道，保持或增加速度
                break;
            case DECELERATION:
                // 减速轨道，准备减速
                break;
            case INERTIAL:
                // 惯性轨道，保持当前速度
                break;
            case UNIFORM:
                // 匀速轨道，记录进入速度
                this.constantSpeedEntryVelocity = this.velocity;
                this.isOnConstantSpeedRail = true;
                break;
        }
    }

    // 公共方法：设置当前轨道
    public void setCurrentRail(RailEntity rail) {
        this.currentRail = rail;
        this.progress = 0.0f;
    }

    public float getVelocity() {
        return this.velocity;
    }

    public void setVelocity(float velocity) {
        this.velocity = velocity;
    }


    // 公共方法：检查是否在匀速轨道上
    public boolean isOnConstantSpeedRail() {
        return this.isOnConstantSpeedRail;
    }

    // 公共方法：获取匀速轨道进入速度
    public float getConstantSpeedEntryVelocity() {
        return this.constantSpeedEntryVelocity;
    }
}