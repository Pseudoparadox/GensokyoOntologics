package com.github.fictology.gensokyoontology.common.block.decoration;


/*
public class CoasterRailBlock extends BaseEntityBlock {
    public static final HashMap<Vector2f, Float> VECTOR2F_MAPPING = Util.make(new HashMap<>(), map -> {
        map.put(new Vector2f(-22.5F, 22.5F), 270F);
        map.put(new Vector2f(22.5F, 45F + 22.5F), 45F);
        map.put(new Vector2f(45F + 22.5F, 90F + 22.5F), 0F);
        map.put(new Vector2f(90F + 22.5F, 135F + 22.5F), 135F);
        map.put(new Vector2f(135F + 22.5F, 180F), 90F);
        map.put(new Vector2f(-180F, -180F + 22.5F), 90F);
        map.put(new Vector2f(-180F + 22.5F, -135F + 22.5F), 225F);
        map.put(new Vector2f(-135F + 22.5F, -90F + 22.5F), 180F);
        map.put(new Vector2f(-90F + 22.5F, -45F + 22.5F), 315F);
    });
    public static final HashMap<Direction, AxisAngle4d> DIRECTION_MAPPING = Util.make(new HashMap<>(), map -> {

    });
    public static final VoxelShape SHAPE = box(0, 0, 0, 16, 6, 16);

    public CoasterRailBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return null;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (!(level.getBlockEntity(pos) instanceof RailTileEntity)) return;
        RailTileEntity railTile = (RailTileEntity) level.getTileEntity(pos);
        if (railTile == null) return;
        if (placer == null) return;

        // FIXME: ADD Math Util
        Vector2f rotation = GSKOMathUtil.toYawPitch(placer.getLookVec());
        float yaw = 0F;
        for (Map.Entry<Vector2f, Float> entry : VECTOR2F_MAPPING.entrySet()) {
            if (rotation.x > entry.getKey().x && rotation.x <= entry.getKey().y) {
                yaw = entry.getValue();
                break;
            }
        }
        railTile.setYaw(yaw);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!(level.getBlockEntity(pos) instanceof RailTileEntity railTile))
            return super.useItemOn(state, level, pos, player, handIn, hit);
        if (railTile == null) return super.useItemOn(state, level, pos, player, handIn, hit);
        if (!level.isClientSide && stack.getItem() == ItemRegistry.RAIL_WRENCH.get()) {
            new RailDashboardScreen(pos, (int) railTile.getRoll(), (int) railTile.getYaw(), (int) railTile.getPitch(),
                    railTile.getControlPoint()).open();
            // NetworkHooks.openGui((ServerPlayerEntity) player, RailAdjustContainer.create(level, pos), railTile.getPos());
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    /*
    private Pose withPose(LivingEntity living) {
        Matrix3d matrix = new Matrix3d().identity();
        Direction dir = GSKOMathUtil.toDirection(living.getLookVec());
        AxisAngle4d aa4d = new AxisAngle4d();
        return new Pose(n);
    }


    @Override
    protected @NotNull RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }


    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return null;
    }
}*/
