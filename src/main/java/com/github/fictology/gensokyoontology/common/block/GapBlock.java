package com.github.fictology.gensokyoontology.common.block;

import com.github.fictology.gensokyoontology.common.tileentity.GapTileEntity;
import com.github.fictology.gensokyoontology.data.GapInfo;
import com.github.fictology.gensokyoontology.registry.BlockRegistry;
import com.github.fictology.gensokyoontology.registry.DataRegistry;
import com.github.fictology.gensokyoontology.util.GSKOUtil;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

// FIXME: Add INBTWriter

public class GapBlock extends Block implements EntityBlock {

    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
    protected static final VoxelShape SUKIMA_PLANE_X = box(-4.0D, 0.0D, 4.0D, 20.0D, 16.0D, 4.0D);
    protected static final VoxelShape SUKIMA_PLANE_Z = box(4.0D, 0.0D, -4.0D, 4.0D, 16.0D, 20.0D);
    private BlockPos tilePos;

    public GapBlock(Properties p) {
        super(p);
        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.EAST));
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return simpleCodec(GapBlock::new);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
    }

    private VoxelShape getVoxel(BlockState state){
        return switch (state.getValue(FACING)){
            case EAST, WEST, DOWN, UP -> SUKIMA_PLANE_X;
            case NORTH, SOUTH -> SUKIMA_PLANE_Z;
        };
    }

    @Override
    protected @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return this.getVoxel(state);
    }

    @Override
    protected @NotNull VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return this.getVoxel(state);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, @NotNull ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);

        if (!(placer instanceof Player)) return;
        Player player = (Player) placer;

        if (stack.get(DataRegistry.GAP_INFO.get()) != GapInfo.EMPTY) {
            GapInfo info = stack.get(DataRegistry.GAP_INFO.get());
            setBlockTileSecond(player, level, pos, info);
            stack.set(DataRegistry.GAP_INFO.get(), GapInfo.EMPTY);
            stack.shrink(1);
            return;
        }

        setBlockTileFirst(level, player, pos);
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {
        super.entityInside(state, level, pos, entity, effectApplier, isPrecise);
        if (!level.isClientSide() && entity instanceof ServerPlayer serverPlayer) {
            var serverLevel = (ServerLevel) level;
            tryTeleport(serverLevel, serverPlayer, pos);
        }
    }

    private void setBlockTileFirst(Level worldIn, Player player, BlockPos firstPos) {
        if (!worldIn.isClientSide()) {
            ServerLevel serverLevel = (ServerLevel) worldIn;
            ResourceKey<Level> departureLevel = serverLevel.dimension();

            ItemStack itemStack = player.getItemInHand(InteractionHand.MAIN_HAND);
            itemStack.set(DataRegistry.GAP_INFO, new GapInfo(departureLevel, firstPos, true));
            itemStack.grow(1);
            worldIn.setBlock(firstPos, BlockRegistry.GAP_BLOCK.get().defaultBlockState(), 2);
            // player.sendMessage(GensokyoOntology.fromLocaleKey("msg.", ".gap_block.set_first_gap"), player.getUniqueID());
            // player.sendMessage(new StringTextComponent(firstPos.getCoordinatesAsString()), player.getUniqueID());
            // player.sendMessage(GensokyoOntology.fromLocaleKey("msg.", ".gap_block.in_dimension"), player.getUniqueID());
            // player.sendMessage(new TranslationTextComponent(departureLevel.getLocation().toString()), player.getUniqueID());
        }

        if (worldIn.getBlockEntity(firstPos) instanceof GapTileEntity gapTile) {
            gapTile.setChanged();
        }
    }

    private void setBlockTileSecond(Player player, Level worldIn, BlockPos secondPos, GapInfo info) {
        worldIn.setBlock(secondPos, BlockRegistry.GAP_BLOCK.get().defaultBlockState(), 2);

        if (!worldIn.isClientSide() && worldIn.getBlockEntity(secondPos) instanceof GapTileEntity) {
            if (info == null) return;
            if (info.departureWorld() == null) return;
            ResourceKey<Level> departureKey = ResourceKey.create(Registries.DIMENSION, info.departureWorld().identifier());
            if (worldIn.getServer() == null) return;
            ServerLevel depatureLevel = worldIn.getServer().getLevel(departureKey);
            if (depatureLevel == null) return;

            var arrivalLevel = (ServerLevel) worldIn;
            var arrivalKey = arrivalLevel.dimension();
            var firstPos = info.departurePos();

            var secondPlacedSukima = (GapTileEntity) arrivalLevel.getBlockEntity(secondPos);
            var firstPlacedSukima = (GapTileEntity) depatureLevel.getBlockEntity(firstPos);

            if (secondPlacedSukima != null) {
                // player.sendMessage(new StringTextComponent("2nd Gap is Present"), player.getUniqueID());
                secondPlacedSukima.setDestinationPos(firstPos);
                secondPlacedSukima.setDestinationLevel(departureKey);
                secondPlacedSukima.setChanged();

            }
            if (firstPlacedSukima != null) {
                // player.sendMessage(new StringTextComponent("1st Gap is Present"), player.getUniqueID());
                firstPlacedSukima.setDestinationPos(secondPos);
                firstPlacedSukima.setDestinationLevel(arrivalKey);
                firstPlacedSukima.setChanged();

                // player.sendMessage(GensokyoOntology.fromLocaleKey("msg.", ".gap_block.set_second_gap"), player.getUniqueID());
                // player.sendMessage(new StringTextComponent("§3" + firstPos.getCoordinatesAsString()), player.getUniqueID());
                // player.sendMessage(GensokyoOntology.fromLocaleKey("msg.", ".gap_block.in_dimension"), player.getUniqueID());
                // player.sendMessage(new TranslationTextComponent("§a" + arrivalKey.getLocation()), player.getUniqueID());
            }
        }
    }

    public void tryTeleport(ServerLevel departureLevel, ServerPlayer serverPlayer, BlockPos depaturePos) {
        if (departureLevel.getBlockEntity(depaturePos) instanceof GapTileEntity) {
            GapTileEntity departureGap = getGapTile(departureLevel, depaturePos);
            ServerLevel destinationLevel = departureLevel.getServer().getLevel(departureGap.getDestinationLevel());

            if (departureGap.getCooldown() > 1) return;
            if (destinationLevel == null) {
                GSKOUtil.showMsg(serverPlayer, "msg", "gap_block.tp_fail.dest_not_present", true);
                return;
            }

            if (getGapTile(destinationLevel, departureGap.getDestinationPos()) == null) {
                serverPlayer.sendSystemMessage(GSKOUtil.translate("msg.", ".gap_block.teleport_fail.arrival_gap_not_present"), true);
                return;
            }
            GapTileEntity arrivalGap = getGapTile(destinationLevel, departureGap.getDestinationPos());
            arrivalGap.setCooldown(400);
            // TeleportHelper.applyGapTeleport(serverPlayer, destinationLevel, departureGap);

            if (departureGap.getDestinationPos() == BlockPos.ZERO) {
                serverPlayer.sendSystemMessage(GSKOUtil.translate("msg.", ".gap_block.teleport_fail.illegal_position"), true);
            }
        }
    }

    public GapTileEntity getGapTile(ServerLevel serverLevel, BlockPos pos) {
        return (GapTileEntity) serverLevel.getBlockEntity(pos);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new GapTileEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return GapTileEntity::tick;
    }

    @Override
    public @Nullable <T extends BlockEntity> GameEventListener getListener(ServerLevel level, T blockEntity) {
        return EntityBlock.super.getListener(level, blockEntity);
    }

}
