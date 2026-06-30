package github.thelawf.gensokyoontology.common.entity.trade;

import com.google.common.collect.ImmutableMap;
import github.thelawf.gensokyoontology.core.init.ItemRegistry;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MerchantOffer;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.Util;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GSKOTrades {

    public static final VillagerTrades.ITrade[] HUMAN_FARMER_TRADE = new VillagerTrades.ITrade[]{
            new ItemForCoinTrade(ItemRegistry.TAKO_YAKI.get(), ItemRegistry.COPPER_COIN.get(), 1, 5),
            new ItemForCoinTrade(ItemRegistry.YATTSUME_UNA_YAKI.get(), ItemRegistry.COPPER_COIN.get(), 1, 6)
    };

    public static final VillagerTrades.ITrade[] HUMAN_JEWELER_TRADE = new VillagerTrades.ITrade[]{
            new ItemForCoinTrade(ItemRegistry.JADE_LEVEL_S.get(), ItemRegistry.GOLDEN_COIN.get(), 1, 18),
            new ItemForCoinTrade(ItemRegistry.JADE_LEVEL_SS.get(), ItemRegistry.EMERALD_COIN.get(), 1, 18),
            new ItemForCoinTrade(ItemRegistry.JADE_LEVEL_SSS.get(), ItemRegistry.DIAMOND_COIN.get(), 1, 5)
    };



    public static final Map<VillagerProfession, VillagerTrades.ITrade[]> TRADE_MAP = Util.make(() -> {
        Map<VillagerProfession, VillagerTrades.ITrade[]> map = new HashMap<>();
        map.put(VillagerProfession.MASON, HUMAN_JEWELER_TRADE);
        map.put(VillagerProfession.FARMER, HUMAN_FARMER_TRADE);
        return map;
    });


    static class ItemForCoinTrade implements VillagerTrades.ITrade{
        IItemProvider tradeItem;
        IItemProvider coinItem;
        int count;
        int bidPrice;
        public ItemForCoinTrade(IItemProvider tradeItemIn, IItemProvider coinItem, int countIn, int bidPriceIn) {
            this.tradeItem = tradeItemIn;
            this.coinItem = coinItem;
            this.count = countIn;
            this.bidPrice = bidPriceIn;
        }

        @Nullable
        @Override
        public MerchantOffer getOffer(Entity trader, Random rand) {
            ItemStack buyingStack = new ItemStack(this.tradeItem);
            ItemStack sellingStack = new ItemStack(ItemRegistry.COPPER_COIN.get());
            buyingStack.grow(this.count);
            sellingStack.grow(this.bidPrice);
            return new MerchantOffer(buyingStack, sellingStack, 12, 1, 0.2F);
        }
    }

    static class CoinForItemTrade implements VillagerTrades.ITrade {
        IItemProvider coinItem;
        IItemProvider goodsItem;
        int priceCount;
        int goodsCount;
        public CoinForItemTrade(IItemProvider coinItem, IItemProvider goodsItem, int priceCount, int goodsCount) {
            this.coinItem = coinItem;

            this.priceCount = priceCount;
            this.goodsCount = goodsCount;
        }

        @Nullable
        @Override
        public MerchantOffer getOffer(Entity trader, Random rand) {
            ItemStack buyingStack = new ItemStack(this.coinItem);
            ItemStack sellingStack = new ItemStack(ItemRegistry.COPPER_COIN.get());
            buyingStack.grow(this.priceCount);
            sellingStack.grow(this.goodsCount);
            return new MerchantOffer(buyingStack, sellingStack, 12, 1, 0.2F);
        }
    }
    public static Int2ObjectMap<VillagerTrades.ITrade[]> gatAsIntMap(ImmutableMap<Integer, VillagerTrades.ITrade[]> p_221238_0_) {
        return new Int2ObjectOpenHashMap<>(p_221238_0_);
    }
}