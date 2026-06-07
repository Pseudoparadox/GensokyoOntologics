package com.github.fictology.gensokyoontology.common.tileentity;

/*
public class RailTileEntity extends TileEntity implements ITickableTileEntity {
    private float yaw = 0f;
    private float pitch = 0f;
    private float roll = 0f;
    private Vector3f controlPoint = new Vector3f(0,0,0);
    private Vector3f railPointOffset = new Vector3f(0,0,0);
    private Pose pose;
    private boolean shouldRender = false;
    private BlockPos targetRailPos = new BlockPos(0,0,0);
    @OnlyIn(Dist.CLIENT)
    private final List<Vector3d> positions = new ArrayList<>();
    @OnlyIn(Dist.CLIENT)
    private final List<Vector3f> rotations = new ArrayList<>();

    public RailTileEntity() {
        super(TileEntityRegistry.RAIL_TILE_ENTITY.codec());
        this.pose = this.toStartPos();
    }
    @Override
    @NotNull
    public CompoundNBT getUpdateTag() {
        CompoundNBT compound = new CompoundNBT();
        this.write(compound);
        return compound;
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        this.read(this.getBlockState(), pkt.getNbtCompound());
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT nbtTag = new CompoundNBT();
        this.write(nbtTag);
        return new SUpdateTileEntityPacket(this.pos, 1, this.getUpdateTag());
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public double getMaxRenderDistanceSquared() {
        return 128.0D;
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        this.read(state, tag);
    }

    @Override
    public void read(@NotNull BlockState state, @NotNull CompoundNBT nbt) {
        if (nbt.contains("yaw")) this.yaw = nbt.getFloat("yaw");
        if (nbt.contains("pitch")) this.pitch = nbt.getFloat("pitch");
        if (nbt.contains("roll")) this.roll = nbt.getFloat("roll");
        if (nbt.contains("shouldRender")) this.shouldRender = nbt.getBoolean("shouldRender");

        // if (nbt.contains("railX") && nbt.contains("railY") && nbt.contains("railZ"))
        //     this.railPointOffset = new Vector3f(nbt.getFloat("railX"), nbt.getFloat("railY"), nbt.getFloat("railZ"));
        if (nbt.contains("controlX") && nbt.contains("controlY") && nbt.contains("controlZ"))
            this.controlPoint = new Vector3f(nbt.getFloat("controlX"), nbt.getFloat("controlY"), nbt.getFloat("controlZ"));
        if (nbt.contains("targetX") && nbt.contains("targetY") && nbt.contains("targetZ"))
            this.targetRailPos = new BlockPos(nbt.getInt("targetX"), nbt.getInt("targetY"), nbt.getInt("targetZ"));
        super.read(state, nbt);
    }

    @Override
    @NotNull
    public CompoundNBT write(@NotNull CompoundNBT compound) {
        super.write(compound);
        compound.putFloat("yaw", this.yaw);
        compound.putFloat("roll", this.roll);
        compound.putFloat("pitch", this.pitch);

        // compound.putFloat("railX", this.railPointOffset.getX());
        // compound.putFloat("railY", this.railPointOffset.getY());
        // compound.putFloat("railZ", this.railPointOffset.getZ());
        compound.putFloat("controlX", this.controlPoint.getX());
        compound.putFloat("controlY", this.controlPoint.getY());
        compound.putFloat("controlZ", this.controlPoint.getZ());

        compound.putBoolean("shouldRender", this.shouldRender);
        compound.putInt("targetX", this.targetRailPos.getX());
        compound.putInt("targetY", this.targetRailPos.getY());
        compound.putInt("targetZ", this.targetRailPos.getZ());
        return super.write(compound);
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
        this.setPose(this.toStartPos());
    }
    public void setPitch(float pitch) {
        this.pitch = pitch;
        this.setPose(this.toStartPos());
    }
    public void setRoll(float roll) {
        this.roll = roll;
        this.setPose(this.toStartPos());
    }

    public void setControlPoint(Vector3f controlPoint) {
        this.controlPoint = controlPoint;
        this.setPose(this.toStartPos());
    }

    public float getRoll() {
        return this.roll;
    }
    public float getYaw() {
        return this.yaw;
    }
    public float getPitch() {
        return this.pitch;
    }

    public void setRotation(float roll, float yaw, float pitch) {
        this.setRoll(roll);
        this.setYaw(yaw);
        this.setPitch(pitch);
        this.setPose(this.toStartPos());
    }

    public Vector3f getControlPoint() {
        return this.controlPoint;
    }

    public void setTargetPos(BlockPos targetRailPos) {
        this.targetRailPos = targetRailPos;
        markDirty();
    }

    public void setControlPoint(float controlX, float controlY, float controlZ) {
        this.controlPoint = new Vector3f(controlX, controlY, controlZ);
    }

    public Vector3d getPosVec() {
        return new Vector3d(this.getPos().getX(), this.getPos().getX(), this.getPos().getX());
    }

    public Vector3d getTargetPosVec() {
        if (getTargetRailEntity() == null) {
            GSKOUtil.log("Target Rail is null, Pos: " + getTargetPos());
            return new Vector3d(0,0,0);
        }
        return getTargetRailEntity().getPosVec().subtract(new Vector3d(this.getPos().getX(), this.getPos().getX(), this.getPos().getX()));
    }

    public Pose getPose() {
        return this.pose;
    }

    public void setPose(Pose pose) {
        this.pose = pose;
    }
    public void setPose(org.joml.Vector3d translation, Matrix3d basis) {
        this.pose = new Pose(translation, basis);
    }

    // Vector3d look = Vector3d.fromPitchYaw(this.pitch, this.yaw);
    @OnlyIn(Dist.CLIENT)
    public Pose toStartPos() {
        Vector3d look = getRailFacing();
        // Vector3d offset = new Vector3d(0,0,1);

        Vector3d pivot = new Vector3d(0, 0, 0.3);
        Vector3d offset = Vector3d.fromPitchYaw(this.pitch, this.yaw).scale(Math.sqrt(2) / 2).add(pivot);
        // offset.add(new Vector3d(0.5, -0.5, -0.5).add(look));

        // offset.add(new Vector3d(0.5, -0.5, -0.5)
        //         .add(pivot.rotateYaw((float) Math.toRadians(this.yaw))
        //                 .rotatePitch((float) Math.toRadians(-this.roll))));
        Matrix3d matrix = new Matrix3d().rotateXYZ(Math.toRadians(this.roll), Math.toRadians(this.yaw - 90), Math.toRadians(this.pitch));
        return new Pose(jomlVec(offset), matrix);
    }

    @OnlyIn(Dist.CLIENT)
    public Pose toStartPosOffset() {
        Vector3d offset = getRailFacing();
        // Vector3d vec = new Vector3d(-0.5, 0.5, -0.5);
        // offset.add(new Vector3d(0.5, -0.5, -0.5)
        //         .add(vec.rotateYaw((float) Math.toRadians(this.yaw))
        //                 .rotatePitch((float) Math.toRadians(-this.roll))));
        Matrix3d matrix = new Matrix3d().rotateXYZ(Math.toRadians(this.roll), Math.toRadians(this.yaw - 90), Math.toRadians(this.pitch));
        return new Pose(jomlVec(offset), matrix);
    }


    @OnlyIn(Dist.CLIENT)
    public Pose toEndPos(BlockPos startPos) {
        Vector3f offset = new Vector3f(1,0,0);
        Vector3d pivot = new Vector3d(0.5, 0.5, -0.5);
        offset.add(new Vector3f(new Vector3d(-0.5, -0.5, 0.5).add(
                pivot.rotateYaw((float) Math.toRadians(this.yaw))
                        .rotatePitch((float) Math.toRadians(-this.roll)))));
        Matrix3d matrix = new Matrix3d().rotateXYZ(Math.toRadians(this.roll), Math.toRadians(this.yaw - 90), Math.toRadians(this.pitch));
        return new Pose(jomlVec(this.pos).sub(jomlVec(startPos)).add(jomlVec(offset)), matrix);
    }

    @OnlyIn(Dist.CLIENT)
    public Pose toEndPosOffset(BlockPos startPos) {
        Vector3d offset = new Vector3d(1,0,0);
        Vector3d pivot = new Vector3d(-0.5, 0.5, -0.5);
        offset = offset.add(new Vector3d(-0.5, -0.5, 0.5).add(
                pivot.rotateYaw((float) Math.toRadians(this.yaw))
                        .rotatePitch((float) Math.toRadians(this.roll))));
        Matrix3d matrix = new Matrix3d().rotateXYZ(Math.toRadians(this.roll), Math.toRadians(this.yaw - 90), Math.toRadians(this.pitch));
        return new Pose(jomlVec(this.pos).sub(jomlVec(startPos))
                .add(jomlVec(offset.rotateYaw((float) Math.toRadians(180)).add(1,0,1))), matrix);
    }

    public BlockPos getTargetPos() {
        return this.targetRailPos;
    }

    @OnlyIn(Dist.CLIENT)
    public Vector3d getRailFacing() {
        return Vector3d.fromPitchYaw(this.pitch, this.yaw).scale(Math.sqrt(3)/2);
    }
    public boolean shouldRender() {
        return this.shouldRender;
    }
    public void setShouldRender(boolean shouldRender) {
        this.shouldRender = shouldRender;
        markDirty();
    }

    public Vector3d getFacingVec() {
        return Vector3d.fromPitchYaw(this.pitch, this.yaw);
    }

    @Nullable
    public BlockState getTargetRail() {
        if (this.world != null) {
            return this.world.getBlockState(this.targetRailPos);
        }
        return null;
    }

    @Nullable
    public RailTileEntity getTargetRailEntity() {
        if (this.world != null) {
            return (RailTileEntity) this.world.getTileEntity(this.targetRailPos);
        }
        return null;
    }

    // TODO: 玩家设置起点轨道和终点轨道，其控制点由程序自行决定，通过交点/平行四边点的方法进行计算（MTR方案）
    @OnlyIn(Dist.CLIENT)
    public List<Vector3d> getBezierPos() {
        if(!this.shouldRender) return this.positions;
        Vector3d target = Vector3d.copyCentered(this.targetRailPos).subtract(Vector3d.copyCentered(this.pos));

        if (this.getTargetRailEntity() == null) return new ArrayList<>();
        RailTileEntity railTile = this.getTargetRailEntity();

        // Vector3d intersection = ConnectionUtil.getIntersection(this.pos, Vector3d.fromPitchYaw(this.pitch, this.yaw),
        //         this.targetRailPos, Vector3d.fromPitchYaw(railTile.getPitch(), railTile.getYaw()));
        Vector3d intersection = ConnectionUtil.getIntersection(BlockPos.ZERO, Vector3d.fromPitchYaw(this.pitch, this.yaw),
                new BlockPos(target.inverse()), Vector3d.fromPitchYaw(railTile.getPitch(), railTile.getYaw()));
        return CurveUtil.getBezierPos(this.positions, Vector3d.ZERO, target, intersection, 0.01F);
    }

    @OnlyIn(Dist.CLIENT)
    public List<Pair<Vector3d, Vector3d>> getConnections() {
        return ConnectionUtil.toConnectionVec(getBezierPos());
    }

    @OnlyIn(Dist.CLIENT)
    public List<Pair<Pair<Vector3d, Vector3d>, Vector3f>> getRotations() {
        List<Pair<Pair<Vector3d, Vector3d>, Vector3f>> list = new ArrayList<>();
        this.getBezierPos();
        List<Pair<Vector3d, Vector3d>> l = this.getConnections();
        List<Pair<Vector3d, Vector3f>> rl = ConnectionUtil.toRotationMap(this.positions, ConnectionUtil.getSegRotations(
                Vector3d.copyCentered(this.pos), Vector3d.copyCentered(this.targetRailPos),
                        0.01F, this.rotations));
        this.getConnections().forEach(p -> {
            Pair<Vector3d, Vector3d> addEntry = Pair.of(p.getFirst(), p.getSecond());
            Pair<Pair<Vector3d, Vector3d>, Vector3f> p2 = Pair.of(addEntry, rl.codec(l.indexOf(p)).getSecond());
            list.add(p2);
        });
        return list;
    }

    @OnlyIn(Dist.CLIENT)
    public Vector3f getRotation(Pair<Vector3d, Vector3d> entry) {
        for (Pair<Pair<Vector3d, Vector3d>, Vector3f> ppvvv : this.getRotations()) {
            if (ppvvv.getFirst().equals(entry)) return ppvvv.getSecond();
        }
        return null;
    }

    @OnlyIn(Dist.CLIENT)
    public Vector3f getRotation() {
        return new Vector3f(this.roll, this.yaw, this.pitch);
    }

    @Override
    public void tick() {
    }
    private org.joml.Vector3d jomlVec(Vector3f vec) {
        return new org.joml.Vector3d(vec.getX(), vec.getY(), vec.getZ());
    }
    private org.joml.Vector3d jomlVec(Vector3d vec) {
        return new org.joml.Vector3d(vec.x, vec.y, vec.z);
    }
    private org.joml.Vector3d jomlVec(BlockPos pos) {
        return new org.joml.Vector3d(pos.getX(), pos.getY(), pos.getZ());
    }

    private Vector3d mcVec(org.joml.Vector3d vec) {
        return new Vector3d(vec.x, vec.y, vec.z);
    }
    private org.joml.Vector3d normal(org.joml.Vector3d vector) {
        // 创建一个新的基准向量来确保与输入向量不平行
        org.joml.Vector3d reference = new org.joml.Vector3d(1, 0, 0);

        // 检查是否与 reference 平行（即使叉积结果为零向量）
        if (vector.normalize().equals(reference)) {
            // 若平行，则选择另一标准基向量
            reference = new org.joml.Vector3d(0, 1, 0);
        }

        // 计算法向，通过叉积得到
        org.joml.Vector3d normal = new org.joml.Vector3d();
        vector.cross(reference, normal).normalize(); // 叉积并归一化结果

        return normal;
    }

    private org.joml.Vector3d normal(org.joml.Vector3d vector, Direction.Axis axisX, Direction.Axis axisY, Direction.Axis axisZ) {
        org.joml.Vector3d normal = new org.joml.Vector3d();
        org.joml.Vector3d ref = new org.joml.Vector3d();

        org.joml.Vector3d x = new org.joml.Vector3d(1,0,0);
        org.joml.Vector3d y = new org.joml.Vector3d(0,1,0);
        org.joml.Vector3d z = new org.joml.Vector3d(0,0,1);

        ref  =  axisX != null && vector.normalize().equals(ref) ? x :
                axisY != null && vector.normalize().equals(ref) ? y :
                axisZ != null && vector.normalize().equals(ref) ? ref : z;
        vector.cross(ref, normal).normalize();
        return normal;
    }

    private Vector3d normal(Vector3d vector) {
        // 检查向量是否接近平行于Y轴
        Vector3d other;
        if (Math.abs(vector.y) < 0.99) {
            // 如果向量不平行于Y轴，使用Y轴进行叉积
            other = new Vector3d(0.0, 1.0, 0.0);
        } else {
            // 否则使用X轴进行叉积
            other = new Vector3d(1.0, 0.0, 0.0);
        }

        // 计算法向量
        Vector3d normal = vector.crossProduct(other);
        return normal.normalize();  // 单位化以得到归一化的法向量
    }
    // public void setRailPoint(float pitch, float yaw) {
    //     this.railPointOffset = new Vector3f(Vector3d.fromPitchYaw(this.pitch, this.yaw).scale(Math.sqrt(3)/2));
    // }
}

 */
