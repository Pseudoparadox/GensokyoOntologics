package github.thelawf.gensokyoontology.common.container;

import com.google.common.collect.Maps;
import github.thelawf.gensokyoontology.api.client.AbstractContainer;
import github.thelawf.gensokyoontology.api.INBTReader;
import github.thelawf.gensokyoontology.api.util.IntRange;
import github.thelawf.gensokyoontology.common.nbt.GSKONBTUtil;
import github.thelawf.gensokyoontology.common.util.GSKOUtil;
import github.thelawf.gensokyoontology.core.RecipeRegistry;
import github.thelawf.gensokyoontology.core.init.ContainerRegistry;
import github.thelawf.gensokyoontology.data.recipe.KogasaSmithingRecipe;
import github.thelawf.gensokyoontology.data.recipe.RecastEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class KogasaSmithingContainer extends AbstractContainer implements INBTReader {
    private final CraftResultInventory craftInv = new CraftResultInventory();
    private final Inventory inventory = new Inventory(4);
    private final CraftResultInventory resultInv = new CraftResultInventory();
    private final World world;
    private final PlayerInventory playerInv;
    private int matchCount;

    //TODO:
    // 1. 测试这个容器类能否正常合成
    // 2. 光与暗的灵魂还没有获取方式
    // 3. 添加更多道具能力接口
    // 4. 红魔馆能不能把最后的围墙修好
    public KogasaSmithingContainer(PlayerInventory playerInv, int id) {
        super(ContainerRegistry.KOGASA_SMITHING_CONTAINER.get(), id);
        this.playerInv = playerInv;
        this.world = playerInv.player.world;
        this.addPlayerInventorySlots(this.playerInv, 8, 110, 168);

        this.addSlot(new Slot(this.craftInv, 0, 52, 76){
            @Override
            public void onSlotChanged() {
                super.onSlotChanged();
                KogasaSmithingContainer.this.setResultInv();
            }

            @Override
            public ItemStack onTake(PlayerEntity thePlayer, ItemStack stack) {
                KogasaSmithingContainer.this.removeMaterialWhenTaken();
                return super.onTake(thePlayer, stack);
            }

            @Override
            public int getSlotStackLimit() {
                return 1;
            }
        });

        for (int i = 0; i < 4; i++) {
            this.addSlot(new Slot(this.inventory, i, 52 + 18 * i, 22) {
                @Override
                public void onSlotChanged() {
                    super.onSlotChanged();
                    KogasaSmithingContainer.this.trySmithing();
                }

                @Override
                public ItemStack onTake(PlayerEntity thePlayer, ItemStack stack) {
                    KogasaSmithingContainer.this.trySmithing();
                    return super.onTake(thePlayer, stack);
                }

                @Override
                public int getSlotStackLimit() {
                    return 1;
                }

            });
        }

        this.addSlot(new Slot(this.resultInv, 0, 106, 76){
            @Override
            public boolean canTakeStack(PlayerEntity playerIn) {
                return super.canTakeStack(playerIn);
            }

            @Override
            public ItemStack onTake(PlayerEntity thePlayer, ItemStack stack) {
                KogasaSmithingContainer.this.consumeAllMaterials();
                return super.onTake(thePlayer, stack);
            }

            @Override
            public int getSlotStackLimit() {
                return 1;
            }
        });
    }

    @Override
    public void onCraftMatrixChanged(IInventory inventoryIn) {
        super.onCraftMatrixChanged(inventoryIn);
        this.trySmithing();
    }

    private void setResultInv() {
        this.resultInv.setInventorySlotContents(0, this.craftInv.getStackInSlot(0));
    }

    private void removeMaterialWhenTaken() {
        ItemStack smithingItem = this.inventory.getStackInSlot(0);
        this.resultInv.setInventorySlotContents(0, ItemStack.EMPTY);
        List<ItemStack> materials = this.getNBTList(smithingItem, "materials", Type.COMPOUND).stream()
                .map(inbt -> ItemStack.read(Type.COMPOUND.cast(inbt))).collect(Collectors.toList());
        materials.forEach(stack ->
                this.inventory.setInventorySlotContents(materials.indexOf(stack) + 1, ItemStack.EMPTY));
    }

    private void consumeAllMaterials() {
        for (int i = 0; i < this.inventory.getSizeInventory(); i++) {
            this.inventory.setInventorySlotContents(i, ItemStack.EMPTY);
        }
        this.craftInv.setInventorySlotContents(0, ItemStack.EMPTY);
    }

    /**
     * 1. 首先获取所有重铸配方的列表，遍历容器中的所有材料<br>
     * 2. 如果该材料和配方中的材料匹配，则将该材料、配方中的NBT数据、重铸类型字符串全部打包为RecastEntry中间数据类放入列表并返回<br>
     * 3. 再次获取容器中的所有材料，如果该材料和配方中的材料匹配，则根据重铸类型字符串获取到重铸等级，情况如下：<br><br>
     *   A. 如果重铸类型为原版附魔，则获取附魔等级，再根据材料数量累加得到最终等级，累加次数为材料数量<br>
     *   B. 如果重铸类型为幻想存有论魔咒，则返回0。<br><br>
     * 4. 通过一个双重迭代，从RECAST_ACTIONS中查表，查询对应的重铸业务逻辑，情况如下：<br><br>
     *   A. 如果重铸类型为原版附魔，则根据上下文提供的附魔等级为CraftingResultSlot内的物品添加附魔效果<br>
     *   B. 如果重铸类型为幻想存有论魔咒，则直接为CraftingResultSlot内的物品添加RecastEntry的NBT数据<br>
     */
    public void trySmithing(){
        this.detectAndSendChanges();
        if (this.world.isRemote) return;

        ServerWorld serverWorld = (ServerWorld)this.world;
        List<RecastEntry> entries = this.getRecastList(serverWorld.getRecipeManager());
        Map<RecastEntry, Integer> map = this.replaceEntryIf(entries);
        map.forEach((entry, count) -> RECAST_ACTIONS.getOrDefault(entry.getKey(),
                        (m, c) -> {}).accept(map, this.resultInv));
    }

    public Map<RecastEntry, Integer> replaceEntryIf(List<RecastEntry> list){
        Map<RecastEntry, Integer> map = new HashMap<>();
        list.forEach(entry -> map.put(entry, this.getMaterialCount(entry)));
        return map;
    }

    public int getMaterialCount(RecastEntry entry) {
        this.matchCount = 0;
        for (int i = 0; i < this.inventory.getSizeInventory(); i++) {
            if (this.inventory.getStackInSlot(i).getItem() == entry.getMaterial()) {
                this.matchCount += entry.remapEnchantLevel();
            }
        }
        return this.matchCount;
    }

    private List<Item> materials() {
        return GSKOUtil.toItemList(this.inventory)
                .stream().map(ItemStack::getItem).collect(Collectors.toList());
    }

    private List<RecastEntry> getRecastList(RecipeManager manager) {
        List<RecastEntry> list = new ArrayList<>();

        this.materials().forEach(item -> list.add(manager.getRecipesForType(RecipeRegistry.KOGASA_SMITHING).stream()
                .filter(recipe -> recipe.isValidMaterial(item))
                .map(KogasaSmithingRecipe::getRecastEntry)
                .findFirst().orElse(RecastEntry.EMPTY)));

        return list;
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }

    @Override
    public void onContainerClosed(PlayerEntity player) {
        super.onContainerClosed(player);
        this.clearContainer(player, player.world, this.playerInv);
    }

    @Override
    public IntRange getContainerSlotRange() {
        return new IntRange(0, 4);
    }

    public static final Map<ResourceLocation, BiConsumer<Map<RecastEntry, Integer>, CraftResultInventory>>
            RECAST_ACTIONS = Util.make(Maps.newHashMap(), map -> {
                map.put(new ResourceLocation("minecraft:enchantment"), GSKONBTUtil::mergeEnchantment);
                map.put(new ResourceLocation("gensoyoontology:spell"), GSKONBTUtil::mergeSpell);
    }
    );

}
