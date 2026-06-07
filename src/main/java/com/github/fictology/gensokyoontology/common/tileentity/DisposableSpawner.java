package com.github.fictology.gensokyoontology.common.tileentity;

import com.github.fictology.gensokyoontology.registry.EntityRegistry;
import com.github.fictology.gensokyoontology.util.GSKOMathUtil;
import com.github.fictology.gensokyoontology.util.api.IRayTraceReader;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Predicate;

/**
 * 一次性刷怪笼
 */
public class DisposableSpawner extends BlockEntity implements IRayTraceReader {

    private EntityType<?> entityType;
    private boolean canContinueSpawn;

    public DisposableSpawner(BlockPos pos, BlockState state) {
        super(EntityRegistry.DISPOSABLE_SPAWNER.get(), pos, state);
        this.canContinueSpawn = true;
        this.entityType = EntityRegistry.FLANDRE_SCARLET.get();
    }

    public static void tick(Level level, BlockPos pos, BlockState state, final DisposableSpawner tile) {
        if (tile.level instanceof ServerLevel serverLevel) {

            Player player = tile.level.getNearestPlayer(tile.getBlockPos().getX(), tile.getBlockPos().getY(), tile.getBlockPos().getZ(), 60, false);
            if (player == null) return;

            Predicate<DisposableSpawner> predicate = tileEntity ->
                    tileEntity.getSpawnEntity() != null && !player.isCreative();
            tile.spawn(predicate, player);
        }
    }

    private void spawn(Predicate<DisposableSpawner> predicate, Player triggeredPlayer) {
        if (this.getSpawnEntity() != null && !triggeredPlayer.isCreative() && this.level instanceof ServerLevel serverLevel) {
            // var compound = this.saveAdditional(new CompoundTag());
            var blockPos = this.getBlockPos().mutable().move(GSKOMathUtil.randomRange(-3, 3), 1, GSKOMathUtil.randomRange(-3, 3));
            var entity = this.getSpawnEntity();
            entity.spawn(serverLevel, null, null, blockPos.mutable(), EntitySpawnReason.SPAWNER, false, false);
            // entity.ifPresent(type -> type.spawn(serverLevel, null, null, blockPos.mutable(), EntitySpawnReason.SPAWNER, false, false));

            this.canContinueSpawn = false;
            this.level.setBlockAndUpdate(this.getBlockPos(), Blocks.AIR.defaultBlockState());
        }
    }


    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }


    public void setEntityType(EntityType<?> entityTypeIn) {
        this.entityType = entityTypeIn;
    }

    public EntityType<?> getSpawnEntity() {
        return this.entityType;
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        Optional<EntityType<?>> entityOptional = EntityType.by(input);
        entityOptional.ifPresent(type -> this.entityType = entityOptional.get());
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putString("id", this.getSpawnEntity().toString());
        output.putBoolean("can_continue_spawn", this.canContinueSpawn);
    }
}
