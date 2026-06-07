package com.github.fictology.gensokyoontology.common.entiy.misc;


/*
public class CursedBatEntity extends Bat implements FlyingAnimal {
    private static final EntityDataAccessor<BlockPos> MANSION_POS = SynchedEntityData.defineId(CursedBatEntity.class,
            EntityDataSerializers.BLOCK_POS);
    private static final EntityDataAccessor<Boolean> HAS_DESTINATION = SynchedEntityData.defineId(CursedBatEntity.class,
            EntityDataSerializers.BOOLEAN);

    private boolean hasDestination;
    private BlockPos mansionPos = new BlockPos(0, 0, 0);

    public CursedBatEntity(World worldIn, BlockPos mansionPos) {
        super(EntityRegistry.CURSED_BAT.get(), worldIn);
        this.mansionPos = mansionPos;
        this.setMansionPos(this.mansionPos);
    }

    public CursedBatEntity(EntityType<CursedBatEntity> type, World world) {
        super(type, world);
        this.setMansionPos(this.mansionPos);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new BatFlyToTargetGoal(this));
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.copy(MANSION_POS, this.mansionPos);
        this.dataManager.copy(HAS_DESTINATION, this.hasDestination);
    }

    public BlockPos getMansionPos() {
        return this.dataManager.get(MANSION_POS);
    }

    public void setMansionPos(BlockPos mansionPos) {
        this.mansionPos = mansionPos;
        this.dataManager.set(MANSION_POS, this.mansionPos);
    }

    public boolean hasDestination() {
        return this.hasDestination;
    }

    public void setHasDestination(boolean hasDestination) {
        this.hasDestination = hasDestination;
        this.dataManager.set(HAS_DESTINATION, hasDestination);
    }

    @Override
    public void writeAdditional(@NotNull CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putLong("mansion_pos", this.mansionPos.toLong());
        compound.putBoolean("has_destination", this.hasDestination);
    }

    @Override
    public void readAdditional(@NotNull CompoundNBT compound) {
        super.readAdditional(compound);
        if (compound.contains("mansion_pos")) {
            this.setMansionPos(BlockPos.fromLong(compound.getLong("mansion_pos")));
        }
        if (compound.contains("has_destination")) {
            this.hasDestination = compound.getBoolean("mansion_pos");
            this.dataManager.set(HAS_DESTINATION, this.hasDestination);
        }
    }

    @Override
    public void tick() {
        if (net.minecraftforge.common.ForgeHooks.onLivingUpdate(this)) return;
        if (!this.world.isRemote) {
            this.setFlag(6, this.isGlowing());
        }

        this.baseTick();

        if (!this.world.isRemote) {
            int i = this.getArrowCountInEntity();
            if (i > 0) {
                if (this.arrowHitTimer <= 0) {
                    this.arrowHitTimer = 20 * (30 - i);
                }

                --this.arrowHitTimer;
                if (this.arrowHitTimer <= 0) {
                    this.setArrowCountInEntity(i - 1);
                }
            }

            int j = this.getBeeStingCount();
            if (j > 0) {
                if (this.beeStingRemovalCooldown <= 0) {
                    this.beeStingRemovalCooldown = 20 * (30 - j);
                }

                --this.beeStingRemovalCooldown;
                if (this.beeStingRemovalCooldown <= 0) {
                    this.setBeeStingCount(j - 1);
                }
            }

            if (this.ticksExisted % 20 == 0) {
                this.getCombatTracker().reset();
            }

            if (!this.glowing) {
                boolean flag = this.isPotionActive(Effects.GLOWING);
                if (this.getFlag(6) != flag) {
                    this.setFlag(6, flag);
                }
            }

        }

        this.livingTick();
        double d0 = this.getPosX() - this.prevPosX;
        double d1 = this.getPosZ() - this.prevPosZ;
        float f = (float) (d0 * d0 + d1 * d1);
        float f1 = this.renderYawOffset;
        float f2 = 0.0F;
        this.prevOnGroundSpeedFactor = this.onGroundSpeedFactor;
        float f3 = 0.0F;
        if (f > 0.0025000002F) {
            f3 = 1.0F;
            f2 = (float) Math.sqrt((double) f) * 3.0F;
            float f4 = (float) MathHelper.atan2(d1, d0) * (180F / (float) Math.PI) - 90.0F;
            float f5 = MathHelper.abs(MathHelper.wrapDegrees(this.rotationYaw) - f4);
            if (95.0F < f5 && f5 < 265.0F) {
                f1 = f4 - 180.0F;
            } else {
                f1 = f4;
            }
        }

        if (this.swingProgress > 0.0F) {
            f1 = this.rotationYaw;
        }

        if (!this.onGround) {
            f3 = 0.0F;
        }

        this.onGroundSpeedFactor += (f3 - this.onGroundSpeedFactor) * 0.3F;
        this.world.getProfiler().startSection("headTurn");
        f2 = this.updateDistance(f1, f2);
        this.world.getProfiler().endSection();
        this.world.getProfiler().startSection("rangeChecks");

        while (this.rotationYaw - this.prevRotationYaw < -180.0F) {
            this.prevRotationYaw -= 360.0F;
        }

        while (this.rotationYaw - this.prevRotationYaw >= 180.0F) {
            this.prevRotationYaw += 360.0F;
        }

        while (this.renderYawOffset - this.prevRenderYawOffset < -180.0F) {
            this.prevRenderYawOffset -= 360.0F;
        }

        while (this.renderYawOffset - this.prevRenderYawOffset >= 180.0F) {
            this.prevRenderYawOffset += 360.0F;
        }

        while (this.rotationPitch - this.prevRotationPitch < -180.0F) {
            this.prevRotationPitch -= 360.0F;
        }

        while (this.rotationPitch - this.prevRotationPitch >= 180.0F) {
            this.prevRotationPitch += 360.0F;
        }

        while (this.rotationYawHead - this.prevRotationYawHead < -180.0F) {
            this.prevRotationYawHead -= 360.0F;
        }

        while (this.rotationYawHead - this.prevRotationYawHead >= 180.0F) {
            this.prevRotationYawHead += 360.0F;
        }

        this.world.getProfiler().endSection();
        this.movedDistance += f2;
        if (this.isElytraFlying()) {
            ++this.ticksElytraFlying;
        } else {
            this.ticksElytraFlying = 0;
        }

        if (this.isSleeping()) {
            this.rotationPitch = 0.0F;
        }

        if (!this.world.isRemote && !this.hasDestination) {
            ServerWorld serverWorld = (ServerWorld) this.world;
            this.setMansionPos(serverWorld.getStructureLocation(StructureRegistry.SCARLET_DEVIL_MANSION.get(),
                    this.getPosition(), 100, false));
            this.setHasDestination(true);
        }

        final float speed = 0.6f;
        // Vector3d vector3d = new Vector3d(this.getMansionPos().getX(), this.getMansionPos().getY(), this.getMansionPos().getZ());
        // this.getLookController().setLookPosition(vector3d);
        // this.getNavigator().tryMoveToXYZ(vector3d.x, vector3d.y, vector3d.z, speed);
        // this.setNoGravity(true);
        this.move(MoverType.SELF, Vector3d.copyCentered(this.getMansionPos().subtract(this.getPosition()))
                .normalize().scale(speed));
        this.setIsBatHanging(!this.getIsBatHanging());
    }

    @Override
    protected void updateAITasks() {

    }

    @Override
    public boolean isFlying() {
        return true;
    }

    static class BatFlyToTargetGoal extends Goal {
        private final CursedBatEntity bat;
        private Path path;

        public BatFlyToTargetGoal(CursedBatEntity bat) {
            this.bat = bat;
        }

        @Override
        public void tick() {
            super.tick();
            Vector3d vector3d = new Vector3d(this.bat.getMansionPos().getX(), this.bat.getMansionPos().getY(), this.bat.getMansionPos().getZ());
            final float speed = 0.6f;
            this.bat.getLookController().setLookPosition(vector3d);
            this.bat.getNavigator().tryMoveToXYZ(vector3d.x, vector3d.y, vector3d.z, speed);
            this.bat.setNoGravity(true);

            Vector3d pos = this.bat.getPositionVec();
            this.bat.world.addParticle(ParticleTypes.CRIMSON_SPORE, pos.x, pos.y, pos.z, 0, 0, 0);
        }

        @Override
        public boolean shouldExecute() {
            return this.bat.hasDestination();
        }
    }
}

 */
