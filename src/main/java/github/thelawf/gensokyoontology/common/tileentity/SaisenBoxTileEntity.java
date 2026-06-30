package github.thelawf.gensokyoontology.common.tileentity;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import github.thelawf.gensokyoontology.api.IRayTracer;
import github.thelawf.gensokyoontology.common.util.BlessType;
import github.thelawf.gensokyoontology.core.init.EffectRegistry;
import github.thelawf.gensokyoontology.core.init.ItemRegistry;
import github.thelawf.gensokyoontology.core.init.TileEntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.server.ServerWorld;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class SaisenBoxTileEntity extends TileEntity implements ITickableTileEntity, IRayTracer {
    private int count;
    private UUID ownerId;
    private UUID throwerId;
    public int ticks = 0;
    private static final List<Pair<Integer, BlessType>> BLESS_LIST = ImmutableList.of(
            Pair.of(50, BlessType.IMMUNE_POISON),
            Pair.of(100, BlessType.IMMUNE_BLOODY_MIST));
    public SaisenBoxTileEntity() {
        super(TileEntityRegistry.SAISEN_BOX_TILE_ENTITY.get());
    }

    @Override
    public void read(@NotNull BlockState state, @NotNull CompoundNBT nbt) {
        if (nbt.contains("count")) {
            this.count = nbt.getInt("count");
        }
        super.read(state, nbt);
    }

    @Override
    @NotNull
    public CompoundNBT write(@NotNull CompoundNBT compound) {
        super.write(compound);
        compound.putInt("count", this.count);
        return compound;
    }

    @Override
    public void tick() {
        AxisAlignedBB aabb = new AxisAlignedBB(getPos().up());

        if (world == null) return;
        List<ItemEntity> itemEntities = world.getEntitiesWithinAABB(ItemEntity.class, aabb, EntityPredicates.IS_ALIVE).stream()
                .filter(itemEntity -> itemEntity.getItem().getItem() == ItemRegistry.SILVER_COIN.get())
                .collect(Collectors.toList());

        itemEntities.forEach(this::tryApplyBless);
        markDirty();
    }

    public void addCoinCount(int count){
        this.count += count;
    }

    private void tryApplyBless(ItemEntity itemEntity) {
        if (itemEntity.getThrowerId() == null || !(world instanceof ServerWorld)) return;

        ServerWorld serverWorld = (ServerWorld) world;
        if (serverWorld.getEntityByUuid(itemEntity.getThrowerId()) instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) serverWorld.getEntityByUuid(itemEntity.getThrowerId());
            if (player == null) return;
            testCount(player, itemEntity);
            itemEntity.getItem().shrink(itemEntity.getItem().getCount());
        }
    }

    public int getCount() {
        return this.count;
    }

    public void testCount(PlayerEntity player, ItemEntity itemEntity) {
        for (int i = 0; i < BLESS_LIST.size(); i++) {
            this.addCoinCount(itemEntity.getItem().getCount());
            if (this.getCount() >= BLESS_LIST.get(i).getFirst()) {
                int duration = 6000 + 500 * i;
                player.addPotionEffect(new EffectInstance(EffectRegistry.HAKUREI_BLESS_EFFECT.get(), duration, i));
                return;
            }
        }
    }
}
