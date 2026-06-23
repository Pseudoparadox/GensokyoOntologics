//package com.github.fictology.gensokyoontology.common.tileentity;
//
//import com.github.fictology.gensokyoontology.registry.EntityRegistry;
//import com.github.fictology.gensokyoontology.util.GSKOLevelUtil;
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.HolderLookup;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.resources.ResourceKey;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.entity.BlockEntity;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraft.world.level.storage.ValueInput;
//import net.minecraft.world.level.storage.ValueOutput;
//import org.jetbrains.annotations.NotNull;
//
//public class GapTileEntity extends BlockEntity {// extends BlockEntity {
//    private static final int MAX_COOLDOWN_TICK = 400;
//    private boolean allowTeleport = false;
//    private BlockPos destinationPos;
//    private ResourceKey<Level> destinationLevel;
//    private int cooldown = 0;
//
//    public GapTileEntity(BlockPos pos, BlockState blockState) {
//        super(EntityRegistry.GAP_ENTITY.get(), pos, blockState);
//    }
//
//
//    public static <T extends BlockEntity> void tick(Level level, BlockPos blockPos, BlockState blockState, T t) {
//        if (level != null && !level.isClientSide()) {
//            var be = level.getBlockEntity(blockPos);
//            if (be instanceof GapTileEntity gap) {
//                if (gap.cooldown > 0) {
//                    gap.cooldown--;
//                } else {
//                    gap.cooldown = MAX_COOLDOWN_TICK;
//                }
//            }
//        }
//    }
//
//    @Override
//    protected void loadAdditional(ValueInput input) {
//        super.loadAdditional(input);
//        input.getLong("DestinationPos").ifPresent(pos -> this.destinationPos = BlockPos.of(pos));
//        input.getString("DestinationLevel").ifPresent(s -> this.destinationLevel = GSKOLevelUtil.getLevelDimension(s));
//
//        input.getInt("Cooldown").ifPresent(cd -> this.cooldown = cd);
//        this.allowTeleport = input.getBooleanOr("AllowTeleport", false);
//    }
//
//    @Override
//    protected void saveAdditional(ValueOutput output) {
//        super.saveAdditional(output);
//        if (checkNonNull()){
//            output.putString("DestinationLevel", this.destinationLevel.identifier().toString());
//            output.putLong("DestinationPos", this.destinationPos.asLong());
//        }
//        output.putString("DestinationLevel", "");
//        output.putLong("DestinationPos", 0);
//        output.putBoolean("AllowTeleport", this.allowTeleport);
//        output.putInt("Cooldown", this.cooldown);
//    }
//
//
//    private boolean checkNonNull() {
//        return this.destinationLevel != null && this.destinationPos != null;
//    }
//
//    public BlockPos getDestinationPos() {
//        return this.destinationPos;
//    }
//
//    public void setDestinationPos(BlockPos destinationPos) {
//        this.destinationPos = destinationPos;
//    }
//
//    public ResourceKey<Level> getDestinationLevel() {
//        return this.destinationLevel;
//    }
//
//    public void setDestinationLevel(ResourceKey<Level> destinationLevel) {
//        this.destinationLevel = destinationLevel;
//        this.allowTeleport = true;
//    }
//
//    public boolean isAllowTeleport() {
//        return this.allowTeleport;
//    }
//
//    public void setAllowTeleport(boolean isAllowTeleport) {
//        this.allowTeleport = isAllowTeleport;
//    }
//
//    public int getCooldown() {
//        return this.cooldown;
//    }
//
//    public void setCooldown(int cooldown) {
//        this.cooldown = cooldown;
//    }
//}
//
//
