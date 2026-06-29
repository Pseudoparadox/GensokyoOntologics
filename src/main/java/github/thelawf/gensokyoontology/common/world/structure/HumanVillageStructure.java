package github.thelawf.gensokyoontology.common.world.structure;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import github.thelawf.gensokyoontology.GensokyoOntology;
import github.thelawf.gensokyoontology.core.init.EntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.jigsaw.JigsawManager;
import net.minecraft.world.gen.feature.structure.*;
import net.minecraft.world.gen.feature.template.TemplateManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HumanVillageStructure extends Structure<NoFeatureConfig> {

    private static final ImmutableList<MobSpawnInfo.Spawners> HUMAN_RESIDENTS = ImmutableList.of(
            new MobSpawnInfo.Spawners(EntityRegistry.HUMAN_RESIDENT_ENTITY.get(), 1, 3, 5));

    public HumanVillageStructure(Codec<NoFeatureConfig> codec) {
        super(codec);
    }

    @Override
    @NotNull
    public GenerationStage.Decoration getDecorationStage() {
        return GenerationStage.Decoration.SURFACE_STRUCTURES;
    }

    @Override
    public List<MobSpawnInfo.Spawners> getDefaultSpawnList() {
        return new ArrayList<>();
    }

    @Override
    @NotNull
    public IStartFactory<NoFeatureConfig> getStartFactory() {
        return HumanVillageStructure.Start::new;
    }

    /**
     * isFeaturedChunk()
     * <br>
     * 用于判断传入的区块是否是可以生成特征的区块
     *
     * @param chunkGenerator 区块生成器
     * @param provider       生物群系提供器
     * @param seed           地图种子
     * @param chunkRandom    随机种子
     * @param chunkX         区块坐标X
     * @param chunkZ         区块坐标Z
     * @param biome          生物群系
     * @param chunkPos       区块坐标
     * @param config         特征设置
     * @return 可以生成建筑就返回true
     */
    @Override
    protected boolean func_230363_a_(@NotNull ChunkGenerator chunkGenerator, @NotNull BiomeProvider provider,
                                     long seed, @NotNull SharedSeedRandom chunkRandom, int chunkX, int chunkZ,
                                     @NotNull Biome biome, @NotNull ChunkPos chunkPos, @NotNull NoFeatureConfig config) {
        BlockPos centerOfChunk = new BlockPos((chunkX << 4) + 7, 0, (chunkZ << 4) + 7);
        int landHeight = chunkGenerator.getHeight(centerOfChunk.getX(), centerOfChunk.getZ(),
                Heightmap.Type.WORLD_SURFACE_WG);

        // getBaseColumn
        IBlockReader columnOfBlocks = chunkGenerator.func_230348_a_(centerOfChunk.getX(), centerOfChunk.getZ());
        BlockState topBlock = columnOfBlocks.getBlockState(centerOfChunk.up(landHeight));

        return topBlock.getFluidState().isEmpty();
    }

    public static class Start extends StructureStart<NoFeatureConfig> {

        public Start(Structure<NoFeatureConfig> p_i225876_1_, int p_i225876_2_, int p_i225876_3_, MutableBoundingBox p_i225876_4_, int p_i225876_5_, long p_i225876_6_) {
            super(p_i225876_1_, p_i225876_2_, p_i225876_3_, p_i225876_4_, p_i225876_5_, p_i225876_6_);
        }

        @Override
        public void func_230364_a_(DynamicRegistries dynamicRegistry, ChunkGenerator chunkGenerator, TemplateManager templateManagerIn,
                                   int chunkX, int chunkZ, Biome biome, NoFeatureConfig config) {
            int x = (chunkX << 4) + 7;
            int z = (chunkZ << 4) + 7;
            BlockPos pos = new BlockPos(x, 0, z);

            // addPieces() Method
            JigsawManager.func_242837_a(dynamicRegistry,
                    new VillageConfig(() -> dynamicRegistry.getRegistry(Registry.JIGSAW_POOL_KEY)
                            .getOrDefault(new ResourceLocation(GensokyoOntology.MODID, "human_village/start_pool")),
                            10), AbstractVillagePiece::new, chunkGenerator, templateManagerIn,
                    pos, this.components, this.rand, false, true);

            this.components.forEach(piece -> piece.offset(0, 1, 0));
            this.components.forEach(piece -> piece.getBoundingBox().minX -= 1);

            this.recalculateStructureSize();
        }
    }
}
