package com.github.fictology.gensokyoontology.common.tileentity;

import com.github.fictology.gensokyoontology.registry.EntityRegistry;
import com.github.fictology.gensokyoontology.registry.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class SaisenBoxTileEntity extends BlockEntity {

    public int ticks = 0;
    private int count;
    private UUID ownerId;
    private UUID throwerId;

    public SaisenBoxTileEntity(BlockPos pos, BlockState state) {
        super(EntityRegistry.SAISEN_BOX_TILE.get(), pos, state);
    }

    public static void tick(Level level, BlockState state, SaisenBoxTileEntity tile) {
        AABB aabb = new AABB(tile.getBlockPos().above());

        if (level == null) return;
        List<ItemEntity> itemEntities = level.getEntitiesOfClass(ItemEntity.class, aabb, Entity::isAlive).stream()
                .filter(itemEntity -> itemEntity.getItem().getItem() == ItemRegistry.SILVER_COIN.get())
                .toList();

        if (tile.tryApplyBless(itemEntities)) return;
        itemEntities.forEach(tile::tryApplyBless);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putInt("count", this.count);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        input.getInt("count").ifPresent(this::setCoinCount);
    }

    public void addCoinCount(int count) {
        this.count += count;
    }

    public void setCoinCount(int count) {
        this.count = count;
    }

    private void tryApplyBless(ItemEntity itemEntity) {
        if (itemEntity.getOwner() == null || !(this.level instanceof ServerLevel serverLevel)) return;

        if (itemEntity.getOwner() instanceof Player player) {
            testCount(player, itemEntity);
            itemEntity.getItem().shrink(itemEntity.getItem().getCount());
        }
    }

    private boolean tryApplyBless(List<ItemEntity> entities) {
        if (entities.stream().anyMatch(item -> item.getOwner() == null)) return false;
        var playerRef = new AtomicReference<Player>();
        entities.stream().allMatch(item -> {
            if (!(item.getOwner() instanceof Player player)) return false;
            playerRef.set(player);
            return true;
        });

        return false;
    }

    public int getCount() {
        return this.count;
    }

    public void testCount(Player player, ItemEntity itemEntity) {

    }
}
