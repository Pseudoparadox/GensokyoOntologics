//package com.github.fictology.gensokyoontology.common.block;
//
//
//import com.github.fictology.gensokyoontology.registry.EntityRegistry;
//import com.mojang.serialization.MapCodec;
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.HolderLookup;
//import net.minecraft.core.registries.Registries;
//import net.minecraft.server.level.ServerLevel;
//import net.minecraft.world.InteractionHand;
//import net.minecraft.world.InteractionResult;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.SpawnEggItem;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.BaseEntityBlock;
//import net.minecraft.world.level.block.EntityBlock;
//import net.minecraft.world.level.block.entity.BlockEntity;
//import net.minecraft.world.level.block.entity.BlockEntityTicker;
//import net.minecraft.world.level.block.entity.BlockEntityType;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraft.world.level.gameevent.GameEventListener;
//import net.minecraft.world.phys.BlockHitResult;
//import org.jetbrains.annotations.Nullable;
//
//public class DisposableSpawnerBlock extends BaseEntityBlock implements EntityBlock {
//
//    public DisposableSpawnerBlock(Properties p) {
//        super(p);
//    }
//
//    @Override
//    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
//        if (stack.getItem() instanceof SpawnEggItem spawnEgg) {
//            var spawner = (DisposableSpawner) level.getBlockEntity(pos);
//
//            if (spawner == null) return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
//            spawner.setEntityType(SpawnEggItem.getType(stack));
//            player.getItemInHand(hand).shrink(1);
//        }
//        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
//    }
//
//    @Override
//    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
//        return new DisposableSpawner(pos, state);
//    }
//
//    @Nullable
//    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
////        return createTickerHelper(type, EntityRegistry.DISPOSABLE_SPAWNER.get(), DisposableSpawner::tick);
//        return null;
//    }
//
//    @Nullable
//    @Override
//    public <T extends BlockEntity> GameEventListener getListener(ServerLevel level, T blockEntity) {
//        return super.getListener(level, blockEntity);
//    }
//
//    @Override
//    protected MapCodec<? extends BaseEntityBlock> codec() {
//        return simpleCodec(DisposableSpawnerBlock::new);
//    }
//}
