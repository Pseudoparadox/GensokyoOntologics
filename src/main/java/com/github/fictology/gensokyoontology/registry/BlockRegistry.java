package com.github.fictology.gensokyoontology.registry;

import com.github.fictology.gensokyoontology.GensokyoOntology;
import com.github.fictology.gensokyoontology.common.block.DanmakuTableBlock;
import com.github.fictology.gensokyoontology.common.block.DisposableSpawnerBlock;
import com.github.fictology.gensokyoontology.common.block.GapBlock;
//import com.github.fictology.gensokyoontology.common.block.SpellCardConsoleBlock;
import com.github.fictology.gensokyoontology.common.block.decoration.SaisenBoxBlock;
import com.github.fictology.gensokyoontology.util.GSKOUtil;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;

public final class BlockRegistry {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(GensokyoOntology.MODID);
    public static final BlockBehaviour.Properties SAPLINGS_PROP = BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SAPLING);
    public static final BlockBehaviour.Properties LEAVES_PROP = BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES);
    public static final BlockBehaviour.Properties WOODS_PROP = BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD);
    public static final BlockBehaviour.Properties BTN_PROP = BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_BUTTON);
    public static final BlockBehaviour.Properties DOOR_PROP = BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_DOOR);
    public static final BlockBehaviour.Properties TRAPDOOR_PROP = BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_TRAPDOOR);
    public static final BlockBehaviour.Properties FENCE_PROP = BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_FENCE);
    public static final BlockBehaviour.Properties FENCE_GATE_PROP = BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_FENCE_GATE);
    public static final BlockBehaviour.Properties PRESSURE_PLATE_PROP = BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PRESSURE_PLATE);
    public static final BlockBehaviour.Properties SIGN_PROP = BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SIGN);
    //====================================   泥土石头类方块   ====================================//
    public static final DeferredBlock<Block> DEFOLIATION_DIRT = copy("defoliation_dirt", Blocks.DIRT);
    public static final DeferredBlock<Block> KAOLIN = copy("kaolin", Blocks.TERRACOTTA);
    public static final DeferredBlock<Block> KAOLINITE = copy("kaolinite", Blocks.STONE);
    ////////////////////////////////////////// 落叶堆 ///////////////////////////////////////////
    public static final DeferredBlock<Block> GINKGO_LEAVES_PILE = BLOCKS.registerBlock("ginkgo_leaves_pile",
            properties -> new TintedParticleLeavesBlock(0.1F, Blocks.LEAF_LITTER.properties()), properties -> properties);

    public static final DeferredBlock<Block> MAPLE_LEAVES_PILE = registerBushLike("maple_leaves_pile",
            properties -> new TintedParticleLeavesBlock(0.1F, Blocks.LEAF_LITTER.properties()));
    public static final DeferredBlock<Block> SAKURA_LEAVES_PILE = registerBushLike("sakura_leaves_pile",
            properties -> new TintedParticleLeavesBlock(0.1F, Blocks.LEAF_LITTER.properties()));
    public static final DeferredBlock<Block> ZELKOVA_LEAVES_PILE = registerBushLike("zelkova_leaves_pile",
            properties -> new TintedParticleLeavesBlock(0.1F, Blocks.LEAF_LITTER.properties()));
//    public static final DeferredBlock<Block> MAGIC_SAPLING = register("magic_sapling", p -> new SaplingBlock(
//            GSKOTreeGrower.MAGIC, p), SAPLINGS_PROP);
    public static final DeferredBlock<Block> MAGIC_LEAVES = leaves("magic_leaves");
    public static final DeferredBlock<Block> MAGIC_PLANKS = register("magic_planks", BlockRegistry::block, WOODS_PROP);
    public static final DeferredBlock<Block> MAGIC_LOG = register("magic_log", BlockRegistry::log, WOODS_PROP);
    public static final DeferredBlock<Block> MAGIC_BUTTON = register("magic_button", BlockRegistry::button, BTN_PROP);
    public static final DeferredBlock<Block> MAGIC_SLAB = register("magic_slab", BlockRegistry::slab, WOODS_PROP);
    public static final DeferredBlock<Block> MAGIC_STAIRS = register("magic_stairs", BlockRegistry::stair, WOODS_PROP);
    public static final DeferredBlock<Block> MAGIC_DOOR = register("magic_door", BlockRegistry::door, DOOR_PROP);
    /////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////                     装饰类方块                          /////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    public static final DeferredBlock<Block> MAGIC_FENCE = register("magic_fence", BlockRegistry::fence, FENCE_PROP);
    public static final DeferredBlock<Block> MAGIC_FENCE_GATE = register("magic_fence_gate", BlockRegistry::fenceGate, FENCE_GATE_PROP);
    public static final DeferredBlock<Block> MAGIC_TRAPDOOR = register("magic_trapdoor", BlockRegistry::trapdoor, TRAPDOOR_PROP);

    private static Block trapdoor(BlockBehaviour.Properties properties) {
        return new TrapDoorBlock(BlockSetType.ACACIA, properties);
    }

    public static final DeferredBlock<Block> MAGIC_SIGN_BLOCK = register("magic_sign", BlockRegistry::standingSign, SIGN_PROP);
    ////////////////////////////////////////// 樱花树木 //////////////////////////////////////////
//    public static final DeferredBlock<Block> SAKURA_SAPLING = register(
//            "sakura_sapling", p -> new SaplingBlock(GSKOTreeGrower.SAKURA, p), SAPLINGS_PROP);
    public static final DeferredBlock<Block> SAKURA_LEAVES = leaves("sakura_leaves");
    public static final DeferredBlock<Block> SAKURA_PLANKS = register("sakura_planks", BlockRegistry::block, WOODS_PROP);

    ////////////////////////////////////////// 魔法树木 //////////////////////////////////////////
    public static final DeferredBlock<Block> SAKURA_LOG = register("sakura_log", BlockRegistry::log, WOODS_PROP);
    public static final DeferredBlock<Block> SAKURA_BUTTON = register("sakura_button", BlockRegistry::button, BTN_PROP);
    public static final DeferredBlock<Block> SAKURA_STAIRS = register("sakura_stairs", BlockRegistry::slab, WOODS_PROP);
    public static final DeferredBlock<Block> SAKURA_SLAB = register("sakura_slab", BlockRegistry::stair, WOODS_PROP);
    public static final DeferredBlock<Block> SAKURA_DOOR = register("sakura_door", BlockRegistry::door, DOOR_PROP);
    public static final DeferredBlock<Block> SAKURA_FENCE = register("sakura_fence", BlockRegistry::fence, FENCE_PROP);
    public static final DeferredBlock<Block> SAKURA_FENCE_GATE = register("sakura_fence_gate", BlockRegistry::fenceGate, FENCE_GATE_PROP);
    public static final DeferredBlock<Block> SAKURA_TRAPDOOR = register("sakura_trapdoor", BlockRegistry::trapdoor, TRAPDOOR_PROP);
    public static final DeferredBlock<Block> SAKURA_SIGN_BLOCK = register("sakura_sign", BlockRegistry::standingSign, SIGN_PROP);
    public static final DeferredBlock<Block> SAKURA_PRESSRUE_PLATE = register("sakura_pressure_plate", BlockRegistry::pressurePlate, PRESSURE_PLATE_PROP);
//    public static final DeferredBlock<Block> ZELKOVA_SAPLING = register(
//            "zelkova_sapling", p -> new SaplingBlock(GSKOTreeGrower.ZELKOVA, p), SAPLINGS_PROP);
    public static final DeferredBlock<Block> ZELKOVA_LEAVES = leaves("zelkova_leaves");
    public static final DeferredBlock<Block> ZELKOVA_PLANKS = register("zelkova_planks", BlockRegistry::block, WOODS_PROP);
    public static final DeferredBlock<Block> ZELKOVA_LOG = register("zelkova_log", BlockRegistry::log, WOODS_PROP);
    public static final DeferredBlock<Block> ZELKOVA_BUTTON = register("zelkova_button", BlockRegistry::button, BTN_PROP);
    public static final DeferredBlock<Block> ZELKOVA_STAIRS = register("zelkova_stairs", BlockRegistry::stair, WOODS_PROP);
    public static final DeferredBlock<Block> ZELKOVA_SLAB = register("zelkova_slab", BlockRegistry::slab, WOODS_PROP);
    public static final DeferredBlock<Block> ZELKOVA_DOOR = register("zelkova_door", BlockRegistry::door, DOOR_PROP);
    public static final DeferredBlock<Block> ZELKOVA_FENCE = register("zelkova_fence", BlockRegistry::fence, FENCE_PROP);
    public static final DeferredBlock<Block> ZELKOVA_FENCE_GATE = register("zelkova_fence_gate", BlockRegistry::fenceGate, FENCE_GATE_PROP);
    public static final DeferredBlock<Block> ZELKOVA_TRAPDOOR = register("zelkova_trapdoor", BlockRegistry::trapdoor, TRAPDOOR_PROP);
    public static final DeferredBlock<Block> ZELKOVA_PRESSRUE_PLATE = register("zelkova_pressure_plate", BlockRegistry::pressurePlate, PRESSURE_PLATE_PROP);
    public static final DeferredBlock<Block> ZELKOVA_SIGN_BLOCK = register("zelkova_sign", BlockRegistry::standingSign, SIGN_PROP);
    ////////////////////////////////////////// 枫木 //////////////////////////////////////////
//    public static final DeferredBlock<Block> MAPLE_SAPLING = register(
//            "maple_sapling", p -> new SaplingBlock(GSKOTreeGrower.MAPLE, p), SAPLINGS_PROP);
    public static final DeferredBlock<Block> MAPLE_LEAVES = leaves("maple_leaves");
    public static final DeferredBlock<Block> MAPLE_PLANKS = register("maple_planks", BlockRegistry::block, WOODS_PROP);


    ////////////////////////////////////////// 榉树木 //////////////////////////////////////////
    public static final DeferredBlock<Block> MAPLE_LOG = register("maple_log", BlockRegistry::log, WOODS_PROP);
    public static final DeferredBlock<Block> MAPLE_BUTTON = register("maple_button", BlockRegistry::button, BTN_PROP);
    public static final DeferredBlock<Block> MAPLE_STAIRS = register("maple_stairs", BlockRegistry::stair, WOODS_PROP);
    public static final DeferredBlock<Block> MAPLE_SLAB = register("maple_slab", BlockRegistry::slab, WOODS_PROP);
    public static final DeferredBlock<Block> MAPLE_DOOR = register("maple_door", BlockRegistry::door, DOOR_PROP);
    public static final DeferredBlock<Block> MAPLE_FENCE = register("maple_fence", BlockRegistry::fence, FENCE_PROP);
    public static final DeferredBlock<Block> MAPLE_FENCE_GATE = register("maple_fence_gate", BlockRegistry::fenceGate, FENCE_GATE_PROP);
    public static final DeferredBlock<Block> MAPLE_TRAPDOOR = register("maple_trapdoor", BlockRegistry::trapdoor, TRAPDOOR_PROP);
    ////////////////////////////////////////// 银杏木 //////////////////////////////////////////
    public static final DeferredBlock<Block> GINKGO_LEAVES = leaves("ginkgo_leaves");
    public static final DeferredBlock<Block> GINKGO_LOG = register("ginkgo_log", BlockRegistry::log, WOODS_PROP);
    ////////////////////////////////////////// 红杉木 //////////////////////////////////////////
    public static final DeferredBlock<Block> REDWOOD_LEAVES = leaves("redwood_leaves");
    public static final DeferredBlock<Block> MAPLE_PRESSURE_PLATE = register("maple_pressure_plate", BlockRegistry::pressurePlate, PRESSURE_PLATE_PROP);
    public static final DeferredBlock<Block> MAPLE_SIGN_BLOCK = register("maple_sign", BlockRegistry::standingSign, SIGN_PROP);
    /////////////////////////////     蘑菇方块      ////////////////////////////////
    public static final DeferredBlock<Block> BLUE_MUSHROOM_BLOCK = register("blue_mushroom_block", HugeMushroomBlock::new, Blocks.RED_MUSHROOM_BLOCK);
    public static final DeferredBlock<Block> PURPLE_MUSHROOM_BLOCK = register(
            "purple_mushroom_block", HugeMushroomBlock::new, Blocks.RED_MUSHROOM_BLOCK);
    /////////////////////////////     草本植物     ////////////////////////////////
    public static final DeferredBlock<Block> BLUE_ROSE_BUSH = register("blue_rose_bush", TallFlowerBlock::new, Blocks.ROSE_BUSH);
    public static final DeferredBlock<Block> LYCORIS_RADIATA = register("lycoris_radiata", BushBlock::new, Blocks.DANDELION);
    public static final DeferredBlock<Block> WASABI_BLOCK = register("wasabi", BushBlock::new, Blocks.DANDELION);
    /////////////////////////////     工艺装饰类方块     ////////////////////////////////
    // public static final DeferredBlock<GlassBlock> CHIREIDEN_COLORED_GLASS = BLOCKS.copy(
    //         "chireiden_colored_glass", ChireitenColoredGlassBlock::new);
//    public static final DeferredBlock<Block> CLAY_ADOBE_BLOCK = register("clay_adobe", ClayAdobeBlock::new, Blocks.TERRACOTTA);
//    public static final DeferredBlock<Block> ADOBE_TILE_BLOCK = register("adobe_tile", AdobeTileBlock::new, Blocks.TERRACOTTA);
//    public static final DeferredBlock<Block> HANIWA_BLOCK = register("haniwa", HaniwaBlock::new, Blocks.FLOWER_POT);
    ///////////////////////////////////////////////////////////////////////////////////
    /////////////                     实用类方块                          ///////////////
    ///////////////////////////////////////////////////////////////////////////////////
//    public static final DeferredBlock<Block> BISHAMONTEN_PAGODA = register("bishamonten_pagoda", BishamontenPagoda::new, Blocks.BEDROCK);
    public static final DeferredBlock<Block> ISHI_ZAKURA = copy("ishi_zakura", Blocks.STONE);
    // ============================== 矿石类方块 ================================== //
    public static final DeferredBlock<Block> IZANO_OBJECT_ORE = ore("izano_object_ore", 3, 7, Blocks.IRON_ORE);
    public static final DeferredBlock<Block> DRAGON_SPHERE_ORE = ore("dragon_sphere_ore", 5, 10, Blocks.DIAMOND_ORE);
    public static final DeferredBlock<Block> CRIMSON_ALLOY_BLOCK = copy("crimson_alloy_block", Blocks.NETHERITE_BLOCK);
    public static final DeferredBlock<Block> CRIMSON_METAL_BLOCK = copy("crimson_metal_block", Blocks.ANCIENT_DEBRIS);
    public static final DeferredBlock<Block> IMMEMORIAL_ALLOY_BLOCK = copy("immemorial_alloy_block", Blocks.ANCIENT_DEBRIS);
    public static final DeferredBlock<Block> JADE_ORE = copy("jade_ore", Blocks.DEEPSLATE);
    public static final DeferredBlock<Block> JADE_BLOCK = copy("jade_block", Blocks.GLASS);
    // ==============================流体方块 ================================== //
    // public static final DeferredBlock<HotSpringBlock> HOT_SPRING_BLOCK = register("hot_spring_block",
    //         () -> new HotSpringBlock(FluidRegistry.HOTSPRING_SOURCE, BlockBehaviour.Properties.ofFullCopy(Blocks.WATER)));
    public static final DeferredBlock<Block> ONION_CROP_BLOCK = copy("onion_crop", Blocks.CARROTS);
    /// 方块实体
    public static final DeferredBlock<Block> DANMAKU_TABLE = register("danmaku_table", DanmakuTableBlock::new, Blocks.CRAFTING_TABLE);
    // public static final DeferredBlock<Block> SORCERY_EXTRACTOR = BLOCKS.copy("sorcery_extractor", SorceryExtractorBlock::new);
    public static final DeferredBlock<Block> DISPOSABLE_SPAWNER = register("disposable_spawner", DisposableSpawnerBlock::new, Blocks.SPAWNER);
    // public static final DeferredBlock<Block> SPACE_FISSURE_BLOCK = BLOCKS.copy("space_fissure_block", SpaceFissureBlock::new);
    public static final DeferredBlock<Block> GAP_BLOCK = register("gap", GapBlock::new, Blocks.NETHER_PORTAL);
    public static final DeferredBlock<Block> SAISEN_BOX = register("saisen_box", SaisenBoxBlock::new, WOODS_PROP);
//    public static final DeferredBlock<Block> SPELL_CARD_CONSOLE = register("spell_card_console", SpellCardConsoleBlock::new, Blocks.ENDER_CHEST);
    // public static final DeferredBlock<Block> COASTER_RAIL = register("coaster_rail", p -> block(Blocks.IRON_BARS));
    public static BlockBehaviour.Properties property(Block block) {
        return block.properties();
    }

    public static DeferredBlock<Block> leaves(String name) {
        return register(name, p -> new UntintedParticleLeavesBlock(0.1f, ColorParticleOption.create(ParticleTypes.TINTED_LEAVES, -9399763), p), LEAVES_PROP);
    }

    public static RotatedPillarBlock log(BlockBehaviour.Properties properties) {
        return new RotatedPillarBlock(properties);
    }

    public static ButtonBlock button(BlockBehaviour.Properties properties) {
        return new ButtonBlock(BlockSetType.ACACIA, 20, properties);
    }

    public static Block block(Block block) {
        return new Block(BlockBehaviour.Properties.ofFullCopy(block));
    }

    public static <B extends Block> DeferredBlock<Block> copy(String name, Block fromBlock) {
        return BLOCKS.registerBlock(name, properties -> new Block(property(fromBlock).setId(
                ResourceKey.create(Registries.BLOCK, GSKOUtil.key(name)))));
    }
    
    public static DeferredBlock<Block> register(String name, Function<BlockBehaviour.Properties, Block> func, Block fromBlock) {
        return BLOCKS.registerBlock(name, p -> func.apply(property(fromBlock).setId(
                ResourceKey.create(Registries.BLOCK, GSKOUtil.key(name)))));
    }

    public static DeferredBlock<Block> register(String name, Function<BlockBehaviour.Properties, Block> func, BlockBehaviour.Properties properties) {
        return BLOCKS.registerBlock(name, p -> func.apply(properties.setId(
                ResourceKey.create(Registries.BLOCK, GSKOUtil.key(name)))));
    }
    
    private static DeferredBlock<Block> registerBushLike(String name, Function<BlockBehaviour.Properties, Block> func) {
        return BLOCKS.registerBlock(name, p -> func.apply(LEAVES_PROP.setId(
                ResourceKey.create(Registries.BLOCK, GSKOUtil.key(name)))));
    }
    
    public static Block block(BlockBehaviour.Properties properties) {
        return new Block(properties);
    }

    public static SlabBlock slab(BlockBehaviour.Properties p) {
        return new SlabBlock(WOODS_PROP);
    }
    public static StairBlock stair(BlockBehaviour.Properties p) {
        return new StairBlock(Blocks.ACACIA_STAIRS.defaultBlockState(), p);
    }

    public static DoorBlock door(BlockBehaviour.Properties p) {
        return new DoorBlock(BlockSetType.ACACIA, WOODS_PROP);
    }

    public static FenceBlock fence(BlockBehaviour.Properties p) {
        return new FenceBlock(WOODS_PROP);
    }

    public static FenceGateBlock fenceGate(BlockBehaviour.Properties p) {
        return new FenceGateBlock(WoodType.ACACIA, WOODS_PROP);
    }

    public static TrapDoorBlock trapDoor(BlockBehaviour.Properties p) {
        return new TrapDoorBlock(BlockSetType.ACACIA, WOODS_PROP);
    }

    public static StandingSignBlock standingSign(BlockBehaviour.Properties p) {
        return new StandingSignBlock(WoodType.ACACIA, WOODS_PROP);
    }

    public static PressurePlateBlock pressurePlate(BlockBehaviour.Properties p) {
        return new PressurePlateBlock(BlockSetType.ACACIA, p);
    }

    public static DeferredBlock<Block> ore(String name, int min, int max, Block fromBlock) {
        return register(name, p -> new DropExperienceBlock(UniformInt.of(min, max), property(fromBlock)), fromBlock);
    }
}
