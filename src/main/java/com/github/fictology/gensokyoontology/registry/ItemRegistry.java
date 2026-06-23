package com.github.fictology.gensokyoontology.registry;

import com.github.fictology.gensokyoontology.GensokyoOntology;
import com.github.fictology.gensokyoontology.common.combat.SpellBehaviors;
import com.github.fictology.gensokyoontology.common.item.DanmakuItem;
import com.github.fictology.gensokyoontology.common.item.spellcard.SpellCardItem;
import com.github.fictology.gensokyoontology.common.item.touhou.*;
import com.github.fictology.gensokyoontology.util.GSKOUtil;
import com.github.fictology.gensokyoontology.util.StoneGambleHelper;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Supplier;


public final class ItemRegistry {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(GensokyoOntology.MODID);
    // ======================= GSKO杂项：装饰类方块 ==========================
    
    // --------------------------- 泥土石头类方块：----------------------------//
    public static final DeferredItem<BlockItem> DEFOLIATION_DIRT_ITEM = ITEMS.registerSimpleBlockItem(
            "defoliation_dirt", BlockRegistry.DEFOLIATION_DIRT);
    public static final DeferredItem<BlockItem> KAOLIN_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(
            "kaolin", BlockRegistry.KAOLIN);
    public static final DeferredItem<BlockItem> KAOLINITE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(
            "kaolinite", BlockRegistry.KAOLINITE);
    // ---------------------------- 树木类方块：------------------------------//
    //////////////////////////////// 樱花木 ////////////////////////////////
//    public static final DeferredItem<BlockItem> SAKURA_SAPLING_ITEM = ITEMS.registerSimpleBlockItem(
//            "sakura_sapling", BlockRegistry.SAKURA_SAPLING);
    public static final DeferredItem<BlockItem> SAKURA_LEAVES_ITEM = ITEMS.registerSimpleBlockItem(
            "sakura_leaves", BlockRegistry.SAKURA_LEAVES);
    public static final DeferredItem<BlockItem> SAKURA_LOG_ITEM = ITEMS.registerSimpleBlockItem(
            "sakura_log", BlockRegistry.SAKURA_LOG);
    public static final DeferredItem<BlockItem> SAKURA_PLANKS_ITEM = ITEMS.registerSimpleBlockItem(
            "sakura_planks", BlockRegistry.SAKURA_PLANKS);
    public static final DeferredItem<BlockItem> SAKURA_BUTTON_ITEM = ITEMS.registerSimpleBlockItem(
            "sakura_button", BlockRegistry.SAKURA_BUTTON);
    public static final DeferredItem<BlockItem> SAKURA_SLAB_ITEM = ITEMS.registerSimpleBlockItem(
            "sakura_slab", BlockRegistry.SAKURA_SLAB);
    public static final DeferredItem<BlockItem> SAKURA_STAIRS_ITEM = ITEMS.registerSimpleBlockItem(
            "sakura_stairs", BlockRegistry.SAKURA_STAIRS);
    public static final DeferredItem<BlockItem> SAKURA_DOOR_ITEM = ITEMS.registerSimpleBlockItem(
            "sakura_door", BlockRegistry.SAKURA_DOOR);
    public static final DeferredItem<BlockItem> SAKURA_FENCE_ITEM = ITEMS.registerSimpleBlockItem(
            "sakura_fence", BlockRegistry.SAKURA_FENCE);
    public static final DeferredItem<BlockItem> SAKURA_FENCE_GATE_ITEM = ITEMS.registerSimpleBlockItem(
            "sakura_fence_gate", BlockRegistry.SAKURA_FENCE_GATE);
    public static final DeferredItem<BlockItem> SAKURA_TRAPDOOR_ITEM = ITEMS.registerSimpleBlockItem(
            "sakura_trapdoor", BlockRegistry.SAKURA_TRAPDOOR);
    public static final DeferredItem<BlockItem> SAKURA_PRESSRUE_PLATE_ITEM = ITEMS.registerSimpleBlockItem(
            "sakura_pressure_plate", BlockRegistry.SAKURA_PRESSRUE_PLATE);
    //////////////////////////////// 榉树木 ////////////////////////////////
    // public static final DeferredItem<BlockItem> ZELKOVA_SAPLING_ITEM = ITEMS.registerSimpleBlockItem(
    //         "zelkova_sapling.json", BlockRegistry.ZELKOVA_SAPLING.codec(),
    //                 new Item.Properties()));
    public static final DeferredItem<BlockItem> ZELKOVA_LEAVES_ITEM = ITEMS.registerSimpleBlockItem(
            "zelkova_leaves", BlockRegistry.ZELKOVA_LEAVES);
    public static final DeferredItem<BlockItem> ZELKOVA_LOG_ITEM = ITEMS.registerSimpleBlockItem(
            "zelkova_log", BlockRegistry.ZELKOVA_LOG);
    public static final DeferredItem<BlockItem> ZELKOVA_PLANKS_ITEM = ITEMS.registerSimpleBlockItem(
            "zelkova_planks", BlockRegistry.ZELKOVA_PLANKS);
    public static final DeferredItem<BlockItem> ZELKOVA_BUTTON_ITEM = ITEMS.registerSimpleBlockItem(
            "zelkova_button", BlockRegistry.ZELKOVA_BUTTON);
    public static final DeferredItem<BlockItem> ZELKOVA_SLAB_ITEM = ITEMS.registerSimpleBlockItem(
            "zelkova_slab", BlockRegistry.ZELKOVA_SLAB);
    public static final DeferredItem<BlockItem> ZELKOVA_STAIRS_ITEM = ITEMS.registerSimpleBlockItem(
            "zelkova_stairs", BlockRegistry.ZELKOVA_STAIRS);
    public static final DeferredItem<BlockItem> ZELKOVA_DOOR_ITEM = ITEMS.registerSimpleBlockItem(
            "zelkova_door", BlockRegistry.ZELKOVA_DOOR);
    public static final DeferredItem<BlockItem> ZELKOVA_FENCE_ITEM = ITEMS.registerSimpleBlockItem(
            "zelkova_fence", BlockRegistry.ZELKOVA_FENCE);
    public static final DeferredItem<BlockItem> ZELKOVA_FENCE_GATE_ITEM = ITEMS.registerSimpleBlockItem(
            "zelkova_fence_gate", BlockRegistry.ZELKOVA_FENCE_GATE);
    public static final DeferredItem<BlockItem> ZELKOVA_TRAPDOOR_ITEM = ITEMS.registerSimpleBlockItem(
            "zelkova_trapdoor", BlockRegistry.ZELKOVA_TRAPDOOR);
    public static final DeferredItem<BlockItem> ZELKOVA_PRESSRUE_PLATE_ITEM = ITEMS.registerSimpleBlockItem(
            "zelkova_pressure_plate", BlockRegistry.ZELKOVA_PRESSRUE_PLATE);
    //////////////////////////////// 枫木 ////////////////////////////////
//    public static final DeferredItem<BlockItem> MAPLE_SAPLING_ITEM = ITEMS.registerSimpleBlockItem(
//            "maple_sapling", BlockRegistry.MAPLE_SAPLING);
    public static final DeferredItem<BlockItem> MAPLE_LEAVES_ITEM = ITEMS.registerSimpleBlockItem(
            "maple_leaves", BlockRegistry.MAPLE_LEAVES);
    public static final DeferredItem<BlockItem> MAPLE_LOG_ITEM = ITEMS.registerSimpleBlockItem(
            "maple_log", BlockRegistry.MAPLE_LOG);
    public static final DeferredItem<BlockItem> MAPLE_PLANKS_ITEM = ITEMS.registerSimpleBlockItem(
            "maple_planks", BlockRegistry.MAPLE_PLANKS);
    public static final DeferredItem<BlockItem> MAPLE_BUTTON_ITEM = ITEMS.registerSimpleBlockItem(
            "maple_button", BlockRegistry.MAPLE_BUTTON);
    public static final DeferredItem<BlockItem> MAPLE_SLAB_ITEM = ITEMS.registerSimpleBlockItem(
            "maple_slab", BlockRegistry.MAPLE_SLAB);
    public static final DeferredItem<BlockItem> MAPLE_STAIRS_ITEM = ITEMS.registerSimpleBlockItem(
            "maple_stairs", BlockRegistry.MAPLE_STAIRS);
    public static final DeferredItem<BlockItem> MAPLE_DOOR_ITEM = ITEMS.registerSimpleBlockItem(
            "maple_door", BlockRegistry.MAPLE_DOOR);
    public static final DeferredItem<BlockItem> MAPLE_FENCE_ITEM = ITEMS.registerSimpleBlockItem(
            "maple_fence", BlockRegistry.MAPLE_FENCE);
    public static final DeferredItem<BlockItem> MAPLE_FENCE_GATE_ITEM = ITEMS.registerSimpleBlockItem(
            "maple_fence_gate", BlockRegistry.MAPLE_FENCE_GATE);
    public static final DeferredItem<BlockItem> MAPLE_TRAPDOOR_ITEM = ITEMS.registerSimpleBlockItem(
            "maple_trapdoor", BlockRegistry.MAPLE_TRAPDOOR);
    public static final DeferredItem<BlockItem> MAPLE_PRESSURE_PLATE_ITEM = ITEMS.registerSimpleBlockItem(
            "maple_pressure_plate", BlockRegistry.MAPLE_PRESSURE_PLATE);
//    public static final DeferredItem<BlockItem> MAGIC_SAPLING_ITEM = ITEMS.registerSimpleBlockItem(
//            "magic_sapling", BlockRegistry.MAGIC_SAPLING);
    public static final DeferredItem<BlockItem> MAGIC_LEAVES_ITEM = ITEMS.registerSimpleBlockItem(
            "magic_leaves", BlockRegistry.MAGIC_LEAVES);
    public static final DeferredItem<BlockItem> MAGIC_LOG_ITEM = ITEMS.registerSimpleBlockItem(
            "magic_log", BlockRegistry.MAGIC_LOG);
    //////////////////////////////// 银杏木 ////////////////////////////////
    public static final DeferredItem<BlockItem> GINKGO_LEAVES_ITEM = ITEMS.registerSimpleBlockItem(
            "ginkgo_leaves", BlockRegistry.GINKGO_LEAVES);
    public static final DeferredItem<BlockItem> GINKGO_LOG_ITEM = ITEMS.registerSimpleBlockItem(
            "ginkgo_log", BlockRegistry.GINKGO_LOG);
    //////////////////////////////// 红杉木 ////////////////////////////////
    public static final DeferredItem<BlockItem> REDWOOD_LEAVES_ITEM = ITEMS.registerSimpleBlockItem(
            "redwood_leaves", BlockRegistry.REDWOOD_LEAVES);
    //////////////////////////////// 落叶堆 ////////////////////////////////
    public static final DeferredItem<BlockItem> GINKGO_LEAVES_PILE_ITEM = ITEMS.registerSimpleBlockItem(
            "ginkgo_leaves_pile", BlockRegistry.GINKGO_LEAVES_PILE);
    public static final DeferredItem<BlockItem> MAPLE_LEAVES_PILE_ITEM = ITEMS.registerSimpleBlockItem(
            "maple_leaves_pile", BlockRegistry.MAPLE_LEAVES_PILE);
    public static final DeferredItem<BlockItem> SAKURA_LEAVES_PILE_ITEM = ITEMS.registerSimpleBlockItem(
            "sakura_leaves_pile", BlockRegistry.SAKURA_LEAVES_PILE);
    public static final DeferredItem<BlockItem> ZELKOVA_LEAVES_PILE_ITEM = ITEMS.registerSimpleBlockItem(
            "zelkova_leaves_pile", BlockRegistry.ZELKOVA_LEAVES_PILE);
    // --------------------------- 草本植物类方块：----------------------------//
    public static final DeferredItem<BlockItem> BLUE_ROSE_ITEM = ITEMS.registerSimpleBlockItem("blue_rose_bush",
            BlockRegistry.BLUE_ROSE_BUSH);
    public static final DeferredItem<BlockItem> LYCORIS_RADIATA = ITEMS.registerSimpleBlockItem("lycoris_radiata",
            BlockRegistry.LYCORIS_RADIATA);
    public static final DeferredItem<BlockItem> WASABI = ITEMS.registerSimpleBlockItem(
            "wasabi", BlockRegistry.WASABI_BLOCK);
    // ------------------------------ 蘑菇方块 --------------------------------//
    public static final DeferredItem<BlockItem> BLUE_MUSHROOM_ITEM = ITEMS.registerSimpleBlockItem(
            "blue_mushroom_block", BlockRegistry.BLUE_MUSHROOM_BLOCK);
    public static final DeferredItem<BlockItem> PURPLE_MUSHROOM_ITEM = ITEMS.registerSimpleBlockItem(
            "purple_mushroom_block", BlockRegistry.PURPLE_MUSHROOM_BLOCK);
    ///////////////////////////////    工艺装饰类方块    //////////////////////////////////
    // public static final DeferredItem<BlockItem> CHIREIDEN_COLORED_GLASS = ITEMS.registerSimpleBlockItem(
    //         "chireiden_colored_glass", BlockRegistry.CHIREIDEN_COLORED_GLASS.codec(),
    //                 new Item.Properties()));
//    public static final DeferredItem<BlockItem> CLAY_ADOBE_ITEM = ITEMS.registerSimpleBlockItem(
//            "clay_adobe", BlockRegistry.CLAY_ADOBE_BLOCK);
//    public static final DeferredItem<BlockItem> HANIWA_ITEM = ITEMS.registerSimpleBlockItem(
//            "haniwa", BlockRegistry.HANIWA_BLOCK);
    // ======================= GSKO杂项：功能性方块 =========================//
    //----------------------------- 合成台 --------------------------//
//    public static final DeferredItem<BlockItem> DANMAKU_TABLE_ITEM = ITEMS.registerSimpleBlockItem(
//            "danmaku_table", BlockRegistry.DANMAKU_TABLE);
//    public static final DeferredItem<BlockItem> SAISEN_BOX_ITEM = ITEMS.registerSimpleBlockItem(
//            "saisen_box", BlockRegistry.SAISEN_BOX);
//    public static final DeferredItem<BlockItem> SPELL_CONSOLE_ITEM = ITEMS.registerSimpleBlockItem(
//            "spell_card_console", BlockRegistry.SPELL_CARD_CONSOLE);
    // -------------------------------- 矿石 ---------------------------------//
//    public static final DeferredItem<BlockItem> IZANO_OBJECT_ORE_ITEM = ITEMS.registerSimpleBlockItem(
//            "izano_object_ore", BlockRegistry.IZANO_OBJECT_ORE);
//    public static final DeferredItem<BlockItem> DRAGON_SPHERE_ORE_ITEM = ITEMS.registerSimpleBlockItem(
//            "dragon_sphere_ore", BlockRegistry.DRAGON_SPHERE_ORE);

//    public static final DeferredItem<BlockItem> JADE_ORE_ITEM = ITEMS.register(
//            "jade_ore", () -> new BlockItem(BlockRegistry.JADE_ORE.get(), new Item.Properties()
//                    .setId(GSKOUtil.resource(Registries.ITEM, "jade_ore"))
//                    .useBlockDescriptionPrefix()){
//                @Override
//                public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
//                    Level world = context.getLevel();
//                    Block block = world.getBlockState(context.getClickedPos()).getBlock();
//                    var player = context.getPlayer();
//                    Random random = new Random();
//
//                    var matchesStoneCutter = block == Blocks.STONECUTTER;
//                    if (!matchesStoneCutter) return super.useOn(context);
//                    if (context.getPlayer() == null) return super.useOn(context);
//
//                    context.getPlayer().playSound(SoundEvents.UI_STONECUTTER_TAKE_RESULT, 1.0f, 1.0f);
//                    var state = context.getLevel().getBlockState(context.getClickedPos());
//
//                    if (world.isClientSide()) return InteractionResult.PASS;
//                    if (!player.isScoping()) return InteractionResult.PASS;
//                    if (random.nextInt(6) != 1) return InteractionResult.FAIL;
//
//                    // 如果手里的石头多于十个则进行十连抽赌石
//                    if (context.getItemInHand().getCount() >= 10) {
//                        context.getItemInHand().shrink(10);
//                        for (int i = 0; i < 10; i++) {
//                            Block.dropResources(state, world, context.getClickedPos(), null, context.getPlayer(),
//                                    StoneGambleHelper.rollItemToDrop(StoneGambleHelper.GSKO_10_PROB));
//                        }
//                        return InteractionResult.SUCCESS;
//                    }
//                    context.getItemInHand().shrink(1);
//                    Block.dropResources(state, world, context.getClickedPos(), null, context.getPlayer(),
//                            StoneGambleHelper.rollItemToDrop(StoneGambleHelper.GSKO_1_PROB));
//                    return InteractionResult.SUCCESS;
//                }
//            });
//    public static final DeferredItem<BlockItem> IMMEMORIAL_ALLOY_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(
//            "immemorial_alloy_block", BlockRegistry.IMMEMORIAL_ALLOY_BLOCK);
    // ------------------------------- 技术性道具 ----------------------------------//
//    public static final DeferredItem<BlockItem> DISPOSABLE_SPAWNER_ITEM = ITEMS.registerSimpleBlockItem(
//            "disposable_spawner", BlockRegistry.DISPOSABLE_SPAWNER);

//    public static final DeferredItem<Item> DESTRUCTIVE_EYE = ITEMS.registerSimpleItem("destructive_eye",
//            Item.Properties::new);
    // ======================= GSKO杂项：道具类物品 =========================//
    // ----------------------- 东方project特殊功能道具 ----------------------//
    public static final DeferredItem<HakureiGohei> HAKUREI_GOHEI = ITEMS.registerItem("hakurei_gohei",
            HakureiGohei::new);
    public static final DeferredItem<MarisaHakkeiro> MARISA_HAKKEIRO = ITEMS.registerItem("marisa_hakkeiro",
            MarisaHakkeiro::new);

//    public static final DeferredItem<BlockItem> GAP_BLOCK = ITEMS.registerSimpleBlockItem(BlockRegistry.GAP_BLOCK);


    // public static final DeferredItem<Item> EIRIN_YAGOKORO_ARROW = ITEMS.registerSimpleItem(
    //         "eirin_yagokoro_arrow", () -> new EirinYagokoroArrow(new Item.Properties()
    //                 ));
    public static final DeferredItem<AyaFans> AYA_FANS = ITEMS.registerItem("aya_fans",
            AyaFans::new, () -> new Item.Properties().stacksTo(1));
//    public static final DeferredItem<KoishiEyeOpen> KOISHI_EYE_OPEN = ITEMS.registerItem("koishi_eye_open",
//            KoishiEyeOpen::new);
    public static final DeferredItem<KoishiEyeClosed> KOISHI_EYE_CLOSED = ITEMS.registerItem("koishi_eye_closed",
            KoishiEyeClosed::new);
    // ----------------------------------- 杂项物品 --------------------------------------//
    public static final DeferredItem<Item> SILVER_COIN = ITEMS.registerSimpleItem("silver_coin", Item.Properties::new);
    // public static final DeferredItem<Item> HOTSPRING_BUCKET = ITEMS.registerSimpleItem("hotspring_bucket",
    //         () -> new BucketItem(FluidRegistry.HOT_SPRING_TYPE, new Item.Properties()
    //                 .stacksTo(1).craftRemainder(BUCKET)));
    
    // public static final DeferredItem<Item> SAKE_BUCKET = ITEMS.registerSimpleItem("sake_bucket",
    //         () -> new BucketItem(FluidRegistry.SAKE_WINE_SOURCE, new Item.Properties()
    //                 .stacksTo(1).containerItem(BUCKET)));
    
    // public static final DeferredItem<Item> PAPER_PULP_BUCKET = ITEMS.registerSimpleItem("paper_pulp_bucket",
    //         () -> new BucketItem(FluidRegistry.PAPER_PULP_SOURCE, new Item.Properties()
    //                 .stacksTo(1).containerItem(BUCKET)));
    // ========================== GSKO杂项：合成消耗品 =========================//
//    public static final DeferredItem<Item> BAT_WING = ITEMS.registerSimpleItem("bat_wing", Item.Properties::new);
//    public static final DeferredItem<Item> ISHI_ZAKURA_FRAGMENT = ITEMS.registerSimpleItem("ishi_zakura_fragment",
//            Item.Properties::new);
    // public static final DeferredItem<Item> ISHI_ZAKURA = ITEMS.registerSimpleItem("ishi_zakura",
    //         BlockRegistry.ISHI_ZAKURA.get(), new Item.Properties()));
    public static final DeferredItem<Item> CHERRY_BLOSSOM = ITEMS.registerSimpleItem("cherry_blossom",
            Item.Properties::new);
    public static final DeferredItem<Item> WANDERING_SOUL = ITEMS.registerSimpleItem("wandering_soul",
            Item.Properties::new);
    public static final DeferredItem<Item> WASHI_PAPER = ITEMS.registerSimpleItem("washi_paper",
            Item.Properties::new);
    public static final DeferredItem<Item> IZANO_OBJECT = ITEMS.registerSimpleItem("izano_object",
            Item.Properties::new);

    // public static final DeferredItem<Item> PAPER_SHIDE = ITEMS.registerSimpleItem("paper_shide",
    //         new Item.Properties());
//    public static final DeferredItem<Item> DRAGON_SPHERE_FRAGMENT = ITEMS.registerSimpleItem("dragon_sphere_fragment",
//            Item.Properties::new);
//    public static final DeferredItem<Item> DRAGON_SPHERE = ITEMS.registerSimpleItem("dragon_sphere",
//            Item.Properties::new);
//    public static final DeferredItem<Item> CRIMSON_ALLOY_INGOT = ITEMS.registerSimpleItem("crimson_alloy_ingot",
//            Item.Properties::new);
//    public static final DeferredItem<Item> CRIMSON_ALLOY_FRAGMENT = ITEMS.registerSimpleItem("crimson_alloy_fragment",
//            Item.Properties::new);
    ////////////////////////////////////  各个等级的玉石  ///////////////////////////////////////
    public static final DeferredItem<Item> JADE_LEVEL_B = ITEMS.registerSimpleItem("jade_level_b",
            Item.Properties::new);
    public static final DeferredItem<Item> JADE_LEVEL_A = ITEMS.registerSimpleItem("jade_level_a",
            Item.Properties::new);
    public static final DeferredItem<Item> JADE_LEVEL_S = ITEMS.registerSimpleItem("jade_level_s",
            Item.Properties::new);
    public static final DeferredItem<Item> JADE_LEVEL_SS = ITEMS.registerSimpleItem("jade_level_ss",
            Item.Properties::new);
    public static final DeferredItem<Item> JADE_LEVEL_SSS = ITEMS.registerSimpleItem("jade_level_sss",
            Item.Properties::new);
    public static final DeferredItem<Item> ORB_JADE = ITEMS.registerSimpleItem("orb_jade",
            Item.Properties::new);
    public static final DeferredItem<Item> DARK_SPIRIT = ITEMS.registerSimpleItem("dark_spirit",
            Item.Properties::new);
    public static final DeferredItem<Item> LIGHT_SPIRIT = ITEMS.registerSimpleItem("light_spirit",
            Item.Properties::new);
    // ---------------------------- 食物原材料 -----------------------------//
//    public static final DeferredItem<Item> KITCHEN_KNIFE = ITEMS.registerSimpleItem("kitchen_knife", Item.Properties::new);
//    public static final DeferredItem<Item> BUTTER = ITEMS.registerSimpleItem("butter",
//            Item.Properties::new);
//    public static final DeferredItem<Item> MILK_BOTTLE = ITEMS.registerSimpleItem("milk_bottle", Item.Properties::new);
//    public static final DeferredItem<Item> SQUID_TENTACLE = ITEMS.registerSimpleItem("squid_tentacle", Item.Properties::new);
// public static final DeferredItem<BlockItem> ONION = ITEMS.registerSimpleBlockItem("onion", BlockRegistry.ONION_CROP_BLOCK);
//    public static final DeferredItem<Item> YATTSUME_UNA =
//            ITEMS.registerSimpleItem("yattsume_una", Item.Properties::new);
    // ------------------------------- 食物 -------------------------------//
//    public static final DeferredItem<Item> YATTSUME_UNA_YAKI = ITEMS.registerSimpleItem("yattsume_una_yaki",
//            Item.Properties::new);
//    public static final DeferredItem<Item> KOISHI_HAT_MOUSSE = ITEMS.registerSimpleItem("koishi_hat_mousse", Item.Properties::new);
//    public static final DeferredItem<Item> CAKE_SCARLET_DEMON = ITEMS.registerSimpleItem("cake_scarlet_demon", Item.Properties::new);
//    public static final DeferredItem<Item> LINGOAME = ITEMS.registerSimpleItem("lingoame", Item.Properties::new);
//    public static final DeferredItem<Item> TAKO_YAKI = ITEMS.registerSimpleItem("tako_yaki", Item.Properties::new);
//    public static final DeferredItem<Item> WHITE_SNOW = ITEMS.registerSimpleItem("white_snow", Item.Properties::new);
//    public static final DeferredItem<Item> BURGER_MEAT_RAW = ITEMS.registerSimpleItem("burger_meat_raw", Item.Properties::new);
//    public static final DeferredItem<Item> BURGER_MEAT = ITEMS.registerSimpleItem("burger_meat", Item.Properties::new);
    //////////////////////////////////// 被遗忘的传说 /////////////////////////////////
    public static final DeferredItem<Item> TALES_SCARLET_MIST = ITEMS.registerSimpleItem(
            "oblivious_tales_scarlet_mist", Item.Properties::new);
    public static final DeferredItem<Item> TALES_SPRING_SNOWS = ITEMS.registerSimpleItem(
            "oblivious_tales_spring_snows", Item.Properties::new);
    public static final DeferredItem<Item> TALES_IMPERISHABLE_NIGHT = ITEMS.registerSimpleItem(
            "oblivious_tales_imperishable_night", Item.Properties::new);
    public static final DeferredItem<Item> TALES_OCCULT_BALL = ITEMS.registerSimpleItem(
            "oblivious_tales_occult_ball", Item.Properties::new);
    // ============================ GSKO生物刷怪蛋 ================================//

    public static final DeferredItem<Item> HAKURE_REIMU_SPAWN_EGG = ITEMS.registerItem(
            "hakurei_reimu_spawn_egg", p -> new SpawnEggItem(p.spawnEgg(EntityRegistry.HAKUREI_REIMU.get())));

    public static final DeferredItem<Item> FAIRY_SPAWN_EGG = registerSpawnEgg(
            "fairy_spawn_egg", EntityRegistry.FAIRY_ENTITY);
    public static final DeferredItem<Item> LILY_WHITE_SPAWN_EGG = registerSpawnEgg(
            "lily_white_spawn_egg", EntityRegistry.LILY_WHITE);
    public static final DeferredItem<Item> REMILIA_SCARLET_SAWN_EGG = registerSpawnEgg(
            "remilia_scarlet_spawn_egg", EntityRegistry.REMILIA_SCARLET);
    public static final DeferredItem<Item> FLANDRE_SCARLET_SPAWN_EGG = registerSpawnEgg(
            "flandre_scarlet_spawn_egg", EntityRegistry.FLANDRE_SCARLET);
    // ======================== GSKO战斗类物品 ============================//
    // ----------------------------- 符卡 --------------------------------//
//    public static final DeferredItem<Item> SPELL_CARD_BLANK = registerSpell("spell_card_blank", SpellCardItem::new, (_l, _p) -> {});
//    public static final DeferredItem<Item> SC_DREAM_SEAL = ITEMS.registerSimpleItem("sc_dream_seal", Item.Properties::new);
//    public static final DeferredItem<Item> SC_HYPERBOLOID_LASER = registerSpell("sc_hyperboloid_laser", SpellCardItem::new, (_l, _p) -> {});
//    public static final DeferredItem<Item> SC_WAVE_AND_PARTICLE = registerSpell("sc_wave_and_particle", SpellCardItem::new, SpellBehaviors.WAVE_PARTICLE);
//    public static final DeferredItem<Item> SC_IDO_NO_KAIHO = registerSpell("sc_ido_no_kaiho", SpellCardItem::new, (_l, _p) -> {});
//    public static final DeferredItem<Item> SC_SUPER_EGO = registerSpell("sc_super_ego", SpellCardItem::new, (_l, _p) -> {});
//    public static final DeferredItem<Item> SC_HELL_ECLIPSE = registerSpell("sc_hell_eclipse", SpellCardItem::new, (_l, _p) -> {});
//    public static final DeferredItem<Item> SC_MOBIUS_RING_WORLD = registerSpell("sc_mobius_ring_world", SpellCardItem::new, (_l, _p) -> {});
//    public static final DeferredItem<Item> SC_FULL_CHERRY_BLOSSOM = registerSpell("sc_full_cherry_blossom", SpellCardItem::new, (_l, _p) -> {});
//    public static final DeferredItem<Item> SC_RORSHACH_DANMAKU = registerSpell("sc_rorshach_danmaku", SpellCardItem::new, (_l, _p) -> {});
//    public static final DeferredItem<Item> SCRIPTED_SPELL_CARD = registerSpell("scripted_spell_card", SpellCardItem::new, (_l, _p) -> {});
    
    // --------------------- 投掷物：弹幕 阴阳玉 灵符 -----------------------//
    public static final DeferredItem<DanmakuItem> DANMAKU_SHOT = registerDanmaku("danmaku_shot");

    // public static final DeferredItem<Item> SC_HANA_SHIGURE = copy(
    //         "sc_hana_shigure", SC_HanaShigure::new);
    // public static final DeferredItem<Item> SC_MANIA_DEPRESS = copy(
    //         "sc_mania_depress", SC_ManiaDepress::new);
    // public static final DeferredItem<Item> SC_GALACTIC_SPIRAL_ARMS = copy(
    //         "sc_galactic_spiral_arms", SC_GalacticSpiralArms::new);
    /////////////////////////// 所有的灰色弹幕 ////////////////////////////////
    public static final DeferredItem<DanmakuItem> LARGE_SHOT = registerDanmaku("large_shot");
    public static final DeferredItem<DanmakuItem> SMALL_SHOT = registerDanmaku("small_shot");
    public static final DeferredItem<DanmakuItem> RICE_SHOT = registerDanmaku("rice_shot");
    public static final DeferredItem<DanmakuItem> SCALE_SHOT = registerDanmaku("scale_shot");
    public static final DeferredItem<DanmakuItem> TALISMAN_SHOT = registerDanmaku("talisman_shot");
    /////////////////////////// 所有颜色的大弹 ////////////////////////////////
    public static final DeferredItem<DanmakuItem> LARGE_SHOT_RED = registerDanmaku("large_shot_red");
    public static final DeferredItem<DanmakuItem> LARGE_SHOT_ORANGE = registerDanmaku("large_shot_orange");
    public static final DeferredItem<DanmakuItem> LARGE_SHOT_YELLOW = registerDanmaku("large_shot_yellow");
    public static final DeferredItem<DanmakuItem> LARGE_SHOT_GREEN = registerDanmaku("large_shot_green");
    public static final DeferredItem<DanmakuItem> LARGE_SHOT_AQUA = registerDanmaku("large_shot_aqua");
    public static final DeferredItem<DanmakuItem> LARGE_SHOT_BLUE = registerDanmaku("large_shot_blue");
    public static final DeferredItem<DanmakuItem> LARGE_SHOT_PURPLE = registerDanmaku("large_shot_purple");
    public static final DeferredItem<DanmakuItem> LARGE_SHOT_MAGENTA = registerDanmaku("large_shot_magenta");
    /////////////////////////// 所有颜色的小弹 ////////////////////////////////
    public static final DeferredItem<DanmakuItem> SMALL_SHOT_RED = registerDanmaku("small_shot_red");
    public static final DeferredItem<DanmakuItem> SMALL_SHOT_ORANGE = registerDanmaku("small_shot_orange");
    public static final DeferredItem<DanmakuItem> SMALL_SHOT_YELLOW = registerDanmaku("small_shot_yellow");
    public static final DeferredItem<DanmakuItem> SMALL_SHOT_GREEN = registerDanmaku("small_shot_green");
    public static final DeferredItem<DanmakuItem> SMALL_SHOT_AQUA = registerDanmaku("small_shot_aqua");
    public static final DeferredItem<DanmakuItem> SMALL_SHOT_BLUE = registerDanmaku("small_shot_blue");
    public static final DeferredItem<DanmakuItem> SMALL_SHOT_PURPLE = registerDanmaku("small_shot_purple");
    // public static final List<Item> SMALL_SHOTS = List.of(SMALL_SHOT_RED.get(), SMALL_SHOT_ORANGE.get(),
    //         SMALL_SHOT_YELLOW.get(), SMALL_SHOT_GREEN.get(), SMALL_SHOT_AQUA.get(), SMALL_SHOT_BLUE.get(),
    //         SMALL_SHOT_PURPLE.get());
    public static final DeferredItem<DanmakuItem> SMALL_SHOT_MAGENTA = registerDanmaku("small_shot_magenta");

    ////////////////////////////// 所有颜色的环玉  /////////////////////////////////
    // public static final DeferredItem<Item> CIRCLE_SHOT = copy("rice_shot",
    //         DanmakuItem::new);
    // public static final DeferredItem<Item> CIRCLE_SHOT_GREEN = copy("circle_shot_green",
    //         () -> new DanmakuItem(danmaku(EntityRegistry.CICLE_SHOT.get())));

    public static final DeferredItem<DanmakuItem> CIRCLE_SHOT_BLUE = registerDanmaku("circle_shot_blue");
    public static final DeferredItem<DanmakuItem> CIRCLE_SHOT_MAGENTA = registerDanmaku("circle_shot_magenta");

    ////////////////////////////// 所有颜色的米弹  /////////////////////////////////
    public static final DeferredItem<DanmakuItem> RICE_SHOT_RED = registerDanmaku("rice_shot_red");
    public static final DeferredItem<DanmakuItem> RICE_SHOT_BLUE = registerDanmaku("rice_shot_blue");
    public static final DeferredItem<DanmakuItem> RICE_SHOT_PURPLE = registerDanmaku("rice_shot_purple");

    ////////////////////////////// 所有颜色的鳞弹  /////////////////////////////////
    public static final DeferredItem<DanmakuItem> SCALE_SHOT_RED = registerDanmaku("scale_shot_red");
    public static final DeferredItem<DanmakuItem> SCALE_SHOT_YELLOW = registerDanmaku("scale_shot_yellow");
    public static final DeferredItem<DanmakuItem> SCALE_SHOT_GREEN = registerDanmaku("scale_shot_green");
    public static final DeferredItem<DanmakuItem> SCALE_SHOT_BLUE = registerDanmaku("scale_shot_blue");
    public static final DeferredItem<DanmakuItem> SCALE_SHOT_PURPLE = registerDanmaku("scale_shot_purple");

    ////////////////////////////// 所有颜色的心弹  /////////////////////////////////
    public static final DeferredItem<DanmakuItem> HEART_SHOT = registerDanmaku("heart_shot");
    public static final DeferredItem<DanmakuItem> HEART_SHOT_PINK = registerDanmaku("heart_shot_pink");
    public static final DeferredItem<DanmakuItem> HEART_SHOT_RED = registerDanmaku("heart_shot_red");
    public static final DeferredItem<DanmakuItem> HEART_SHOT_AQUA = registerDanmaku("heart_shot_aqua");
    public static final DeferredItem<DanmakuItem> HEART_SHOT_BLUE = registerDanmaku("heart_shot_blue");

    ////////////////////////////// 所有颜色的小型星弹  /////////////////////////////////
    public static final DeferredItem<DanmakuItem> SMALL_STAR_SHOT = registerDanmaku("small_star_shot");
    public static final DeferredItem<DanmakuItem> SMALL_STAR_SHOT_RED = registerDanmaku("small_star_shot_red");
    public static final DeferredItem<DanmakuItem> SMALL_STAR_SHOT_YELLOW = registerDanmaku("small_star_shot_yellow");
    public static final DeferredItem<DanmakuItem> SMALL_STAR_SHOT_GREEN = registerDanmaku("small_star_shot_green");
    public static final DeferredItem<DanmakuItem> SMALL_STAR_SHOT_AQUA = registerDanmaku("small_star_shot_aqua");
    public static final DeferredItem<DanmakuItem> SMALL_STAR_SHOT_BLUE = registerDanmaku("small_star_shot_blue");
    public static final DeferredItem<DanmakuItem> SMALL_STAR_SHOT_PURPLE = registerDanmaku("small_star_shot_purple");

    ////////////////////////////// 所有颜色的大型星弹  /////////////////////////////////
    public static final DeferredItem<DanmakuItem> LARGE_STAR_SHOT        = registerDanmaku("large_star_shot");
    public static final DeferredItem<DanmakuItem> LARGE_STAR_SHOT_RED    = registerDanmaku("large_star_shot_red");
    public static final DeferredItem<DanmakuItem> LARGE_STAR_SHOT_YELLOW = registerDanmaku("large_star_shot_yellow");
    public static final DeferredItem<DanmakuItem> LARGE_STAR_SHOT_GREEN  = registerDanmaku("large_star_shot_green");
    public static final DeferredItem<DanmakuItem> LARGE_STAR_SHOT_AQUA   = registerDanmaku("large_star_shot_aqua");
    public static final DeferredItem<DanmakuItem> LARGE_STAR_SHOT_BLUE   = registerDanmaku("large_star_shot_blue");
    public static final DeferredItem<DanmakuItem> LARGE_STAR_SHOT_PURPLE = registerDanmaku("large_star_shot_purple");

    ////////////////////////////// 所有颜色的札弹  /////////////////////////////////
    public static final DeferredItem<DanmakuItem> TALISMAN_SHOT_RED    = registerDanmaku("talisman_shot_red");
    public static final DeferredItem<DanmakuItem> TALISMAN_SHOT_GREEN  = registerDanmaku("talisman_shot_green");
    public static final DeferredItem<DanmakuItem> TALISMAN_SHOT_AQUA   = registerDanmaku("talisman_shot_aqua");
    public static final DeferredItem<DanmakuItem> TALISMAN_SHOT_BLUE   = registerDanmaku("talisman_shot_blue");
    public static final DeferredItem<DanmakuItem> TALISMAN_SHOT_PURPLE = registerDanmaku("talisman_shot_purple");

    ////////////////////////////// 所有颜色的阴阳玉 ///////////////////////////////////
    public static final DeferredItem<YinyangJadeItem> YINYANG_JADE_BLACK  = ITEMS.registerItem("yinyang_jade_black", YinyangJadeItem::new);
    public static final DeferredItem<YinyangJadeItem> YINYANG_JADE_RED    = ITEMS.registerItem("yinyang_jade_red", YinyangJadeItem::new);
    public static final DeferredItem<YinyangJadeItem> YINYANG_JADE_YELLOW = ITEMS.registerItem("yinyang_jade_yellow", YinyangJadeItem::new);
    public static final DeferredItem<YinyangJadeItem> YINYANG_JADE_GREEN  = ITEMS.registerItem("yinyang_jade_green", YinyangJadeItem::new);
    public static final DeferredItem<YinyangJadeItem> YINYANG_JADE_AQUA   = ITEMS.registerItem("yinyang_jade_aqua", YinyangJadeItem::new);
    public static final DeferredItem<YinyangJadeItem> YINYANG_JADE_BLUE   = ITEMS.registerItem("yinyang_jade_blue", YinyangJadeItem::new);
    public static final DeferredItem<YinyangJadeItem> YINYANG_JADE_PURPLE = ITEMS.registerItem("yinyang_jade_purple", YinyangJadeItem::new);
    //////////////////////////// 道具：B点、残机 ////////////////////////////////
    public static final DeferredItem<DanmakuItem> FAKE_LUNAR_ITEM = registerDanmaku("fake_lunar");

    // public static final List<Item> SHOTS = List.of(SMALL_SHOTS)
    public static final DeferredItem<Item> POWER_ITEM = ITEMS.registerSimpleItem("power_item", Item.Properties::new);
    public static final DeferredItem<Item> BOMB_FRAGMENT = ITEMS.registerSimpleItem("bomb_fragment", Item.Properties::new);
    public static final DeferredItem<Item> LIFE_FRAGMENT = ITEMS.registerSimpleItem("life_fragment", Item.Properties::new);
    public static final DeferredItem<Item> BOMB_ITEM = ITEMS.registerSimpleItem("bomb_item", Item.Properties::new);
    public static final DeferredItem<Item> EXTEND_ITEM = ITEMS.registerSimpleItem("extend_item", Item.Properties::new);
    // ------------------------------- 装备 -------------------------------//
    /*
    public static final DeferredItem<Item> JADE_AXE = ITEMS.registerSimpleItem("jade_axe",
            new Item.Properties().axe(GSKOToolMaterial.JADE, 5f, -2.8f));
    public static final DeferredItem<Item> JADE_HOE = ITEMS.registerSimpleItem("jade_hoe",
            new Item.Properties().hoe(GSKOToolMaterial.JADE, 3f, 0f));
    public static final DeferredItem<Item> JADE_PICKAXE = ITEMS.registerSimpleItem("jade_pickaxe",
            new Item.Properties().pickaxe(GSKOToolMaterial.JADE, 1.5f, -2.6f));
    public static final DeferredItem<Item> JADE_SHOVEL = ITEMS.registerSimpleItem("jade_shovel",
            new Item.Properties().pickaxe(GSKOToolMaterial.JADE, 2f, -2.8f));
    public static final DeferredItem<Item> JADE_SWORD = ITEMS.registerSimpleItem("jade_sword",
            new Item.Properties().sword(GSKOToolMaterial.JADE, 4f, -2f));
    public static final DeferredItem<Item> JADE_HELMET = ITEMS.registerSimpleItem("jade_helmet",
            new Item.Properties().humanoidArmor(GSKOArmorMaterial.JADE, ArmorType.HELMET));
    public static final DeferredItem<Item> JADE_CHESTPLATE = ITEMS.registerSimpleItem("jade_chestplate",
            new Item.Properties().humanoidArmor(GSKOArmorMaterial.JADE, ArmorType.CHESTPLATE));
    public static final DeferredItem<Item> JADE_LEGGINGS = ITEMS.registerSimpleItem("jade_leggings",
            new Item.Properties().humanoidArmor(GSKOArmorMaterial.JADE, ArmorType.LEGGINGS));
    public static final DeferredItem<Item> JADE_BOOTS = ITEMS.registerSimpleItem("jade_boots",
            new Item.Properties().humanoidArmor(GSKOArmorMaterial.JADE, ArmorType.BOOTS));

     */
//    public static final DeferredItem<Item> RAIL_WRENCH = ITEMS.registerSimpleItem("rail_wrench",
//            Item.Properties::new);
    // ====================================== 技术性物品 ====================================== //
//    public static final DeferredItem<Item> DREAM_SEAL_ITEM = ITEMS.registerSimpleItem("dream_seal", Item.Properties::new);
//    public static final DeferredItem<Item> SPHERE_EFFECT_ITEM = ITEMS.registerSimpleItem("sphere", Item.Properties::new);
    /*
    public static final DeferredItem<Item> RAIL_CONNECTOR = ITEMS.registerSimpleItem("rail_connector", () -> new Item(
            new Item.Properties()) {
        @Override
        public @NotNull InteractionResult onItemUse(@NotNull UseOnContext context) {
            Level world = context.getLevel();
            Player player = context.getPlayer();
            BlockState blockState = world.getBlockState(context.getPos());
            ItemStack connector = context.getItem();

            if (player == null) return InteractionResult.FAIL;
            if (blockState.getBlock() != BlockRegistry.COASTER_RAIL.get()) return InteractionResult.CONSUME;

            TileEntity tile = null;
            if (connector.getTag() != null)
                tile = world.getTileEntity(BlockPos.fromLong(connector.getTag().getLong("startPos")));
            if (!(tile instanceof RailTileEntity)) return super.onItemUse(context);

            BlockPos pos = BlockPos.fromLong(connector.getTag().getLong("startPos"));
            RailTileEntity startRail = (RailTileEntity) world.getTileEntity(pos);
            if (startRail == null) return super.onItemUse(context);

            startRail.setTargetPos(context.getPos());
            startRail.setShouldRender(true);

            ItemStack stack = new ItemStack(ItemRegistry.COASTER_RAIL_ITEM.get());
            context.getItem().shrink(1);
            player.addItemStackToInventory(stack);
            return InteractionResult.SUCCESS;
        }
    });
    
     
    
    public static final DeferredItem<BlockItem> COASTER_RAIL_ITEM = ITEMS.registerSimpleItem("coaster_rail", 
            BlockRegistry.COASTER_RAIL.get(), new Item.Properties()) {
        @Override
        public @NotNull InteractionResult onItemUse(@NotNull UseOnContext context) {
            if (!Screen.hasShiftDown()) return super.onItemUse(context);
            Level world = context.getLevel();
            Player player = context.getPlayer();
            BlockState blockState = world.getBlockState(context.getPos());

            if (player == null) return super.onItemUse(context);
            if (blockState.getBlock() != BlockRegistry.COASTER_RAIL.get()) return super.onItemUse(context);

            ItemStack stack = new ItemStack(ItemRegistry.RAIL_CONNECTOR.get());
            CompoundTag Tag = new CompoundTag();
            Tag.putLong("startPos", context.getPos().toLong());
            stack.setTag(Tag);

            context.getItem().shrink(1);
            player.addItemStackToInventory(stack);
            return InteractionResult.SUCCESS;
        }

        @Override
        public void addInformation(@NotNull ItemStack stack, @Nullable Level worldIn, @NotNull List<ITextComponent> tooltip, @NotNull ITooltipFlag flagIn) {
            super.addInformation(stack, worldIn, tooltip, flagIn);
            if (stack.getTag() == null) return;
            if (!stack.getTag().contains("startPos")) {
                tooltip.add(GensokyoOntology.fromLocaleKey("tooltip.", ".coaster_rail.usage"));
            }
            ;
            BlockPos pos = BlockPos.fromLong(stack.getTag().getLong("startPos"));
            tooltip.add(GensokyoOntology.fromLocaleKey("tooltip.", ".coaster_rail.start_pos"));
            tooltip.add(new StringTextComponent("(" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + ")"));
        }
    });
    public static final DeferredItem<Item> CONST_BUILDER = ITEMS.registerSimpleItem("const_builder", new Item.Properties());
    public static final DeferredItem<Item> V3D_BUILDER = ITEMS.registerSimpleItem("vector3d_builder", () -> new ScriptBuilderItem() {
        @Override
        public void openScriptEditGUI(Level world, Player player, ItemStack stack) {
            if (!world.isRemote) player.openContainer(V3DBContainer.create("vector3d_builder"));
        }
    });
    public static final DeferredItem<Item> V3D_INVOKER = ITEMS.registerSimpleItem("v3d_invoker", () -> new DynamicScriptItem() {
        @Override
        public void addDynamicData(Level world, Player player, ItemStack stack, Dynamic<ITag> dynamic) {

        }

        @Override
        public void openScriptEditGUI(Level world, Player player, ItemStack stack) {
            if (!world.isRemote) player.openContainer(V3dInvokerContainer.create());
        }
    });
    public static final DeferredItem<Item> STATIC_INVOKER = ITEMS.registerSimpleItem("static_invoker", () -> new DynamicScriptItem() {
        @Override
        public void addDynamicData(Level world, Player player, ItemStack stack, Dynamic<ITag> dynamic) {

        }

        @Override
        public void openScriptEditGUI(Level world, Player player, ItemStack stack) {
            if (!world.isRemote) player.openContainer(StaticInvokerContainer.create());
        }
    });
    public static final DeferredItem<Item> DANMAKU_BUILDER = ITEMS.registerSimpleItem("danmaku_builder", () -> new ScriptBuilderItem() {
        @Override
        public void openScriptEditGUI(Level world, Player player, ItemStack stack) {
            if (!world.isRemote) player.openContainer(DanmakuBuilderContainer.create("danmaku_builder"));
        }

        @Override
        public void addInformation(ItemStack stack, @Nullable Level worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
            // TranslationTextComponent entityText = GensokyoOntology.fromLocaleKey("tooltip.",".danmaku_entity");
            // TranslationTextComponent colorText = GensokyoOntology.fromLocaleKey("tooltip.",".danmaku_color");
            // TranslationTextComponent typeText = GensokyoOntology.fromLocaleKey("tooltip.",".danmaku_type");
            if (stack.getTag() != null) {
                CompoundTag Tag = stack.getTag();
                tooltip.add(GSKOUtil.fromLocaleFormat("tooltip.", ".danmaku_entity",
                        new TranslationTextComponent(Tag.getString("type"))));

                if (Tag.contains("danmakuType")) {
                    DanmakuType type = DanmakuType.valueOf(Tag.getString("danmakuType").toUpperCase());
                    // tooltip.add(typeText);
                    tooltip.add(GSKOUtil.fromLocaleFormat("tooltip.", ".danmaku_type", type.toTextComponent()));
                }
                if (Tag.contains("danmakuColor")) {

                    DanmakuColor color = DanmakuColor.valueOf(Tag.getString("danmakuColor").toUpperCase());
                    tooltip.add(GSKOUtil.fromLocaleFormat("tooltip.", ".danmaku_color", color.toTextComponent()));
                    // tooltip.add(colorText);
                    // tooltip.add(colorId.toTextComponent());
                } else if (GSKOTagUtil.containsAllowedType(Tag)) {
                    GSKOTagUtil.getMemberValues(Tag).forEach(s -> tooltip.add(new StringTextComponent(s)));
                }
            }
        }
    });
    public static final DeferredItem<Item> BINARY_OPERATION_BUILDER = ITEMS.registerSimpleItem("binary_operation_builder", () -> new DynamicScriptItem() {
        @Override
        public void addDynamicData(Level world, Player player, ItemStack stack, Dynamic<ITag> dynamic) {

        }

        @Override
        public void openScriptEditGUI(Level world, Player player, ItemStack stack) {
            // minecraft.displayGuiScreen(new DanmakuBuilderScreen(title, stack, world, player));
            if (!world.isRemote) player.openContainer(BinaryOperationContainer.create());
        }

        @Override
        public void addInformation(ItemStack stack, @Nullable Level worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
            if (stack.getTag() == null) return;
            CompoundTag Tag = stack.getTags();
            tooltip.add(OPERATION_TYPE_TIP);
            tooltip.add(BinaryOperation.valueOf(GSKOScriptUtil.getScriptValue(Tag).getString("operation")
                    .toUpperCase()).toTextComponent());
            tooltip.add(LEFT_TYPE_TIP);
            tooltip.add(new StringTextComponent(TYPE_HIGHLIGHT + GSKOScriptUtil.getOptLeft(Tag).getString("type")));
            tooltip.add(RIGHT_TYPE_TIP);
            tooltip.add(new StringTextComponent(TYPE_HIGHLIGHT + GSKOScriptUtil.getOptRight(Tag).getString("type")));
        }
    });
    public static final DeferredItem<Item> TIME_STAMP = ITEMS.registerSimpleItem("time_stamp", () -> new ScriptReadOnlyItem() {
        @Override
        public void addReadOnlyData(Level world, Player player, ItemStack stack) {
            CompoundTag Tag = new CompoundTag();
            Tag.putString("type", "time_stamp");
            Tag.putString("name", "ticksExisted");
            Tag.putString("value", "increasedByTick");
            stack.setTag(Tag);
        }
    });
    
     */

    public static <I extends Item & ItemLike> DeferredItem<Item> registerSpell(String name, BiFunction<Item.Properties, SpellBehaviors.Spell<LivingEntity>, I> func, SpellBehaviors.Spell<LivingEntity> behavior) {
        return ITEMS.registerItem(name, properties -> func.apply(properties, behavior));
    }

    public static <E extends Entity> DeferredItem<Item> registerSpawnEgg(String name, Supplier<EntityType<E>> entityGetter){
        return ITEMS.registerItem(
                name, p -> new SpawnEggItem(p.spawnEgg(entityGetter.get())));
    }

    public static DeferredItem<DanmakuItem> registerDanmaku(String name){
        return ITEMS.registerItem(name, DanmakuItem::new);
    }

    public static  <T> DeferredItem<BlockItem> registerBlockItem(String name, Supplier<? extends Block> block, Item.Properties properties,
                                                     DataComponentType<T> component, T comInst) {
        return ITEMS.register(name, (key) ->
                new BlockItem(block.get(), properties.setId(ResourceKey.create(Registries.ITEM, key))
                        .component(component, comInst)
                        .useBlockDescriptionPrefix()));
    }
}
