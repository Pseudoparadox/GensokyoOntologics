package com.github.fictology.gensokyoontology.registry;

import com.github.fictology.gensokyoontology.GensokyoOntology;
import com.github.fictology.gensokyoontology.common.entiy.HakureiReimuEntity;
import com.github.fictology.gensokyoontology.common.entiy.misc.*;
import com.github.fictology.gensokyoontology.common.entiy.monster.*;
import com.github.fictology.gensokyoontology.common.entiy.SpellCardEntity;
import com.github.fictology.gensokyoontology.common.tileentity.DisposableSpawner;
import com.github.fictology.gensokyoontology.common.tileentity.GapTileEntity;
import com.github.fictology.gensokyoontology.common.tileentity.SaisenBoxTileEntity;
import com.github.fictology.gensokyoontology.util.GSKOUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class EntityRegistry {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, GensokyoOntology.MODID);
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.createEntities(GensokyoOntology.MODID);

    public static final Supplier<BlockEntityType<GapTileEntity>> GAP_ENTITY = BLOCK_ENTITIES.register("gap_tile",
            () -> new BlockEntityType<>(GapTileEntity::new, BlockRegistry.GAP_BLOCK.get()));
    public static final Supplier<BlockEntityType<DisposableSpawner>> DISPOSABLE_SPAWNER = BLOCK_ENTITIES.register(
            "disposable_tile",
            () -> new BlockEntityType<>(DisposableSpawner::new, BlockRegistry.DISPOSABLE_SPAWNER.get()));
    public static final Supplier<EntityType<Danmaku>> FAKE_LUNAR = register("fake_lunar", Danmaku::new,
            MobCategory.MISC, 2f, 2f);
    public static final Supplier<BlockEntityType<SaisenBoxTileEntity>> SAISEN_BOX_TILE = BLOCK_ENTITIES.register(
            "saisen_box_tile", () -> new BlockEntityType<>(SaisenBoxTileEntity::new, BlockRegistry.SAISEN_BOX.get()));
    public static final Supplier<EntityType<MasterSparkEntity>> MASTER_SPARK_ENTITY = register("master_spark",
            MasterSparkEntity::new, MobCategory.MISC, 3.5f, 3.5f);
    public static final Supplier<EntityType<DreamSealEntity>> DREAM_SEAL = register("dream_seal",
            DreamSealEntity::new, MobCategory.MISC, 1f, 1f);
    public static final Supplier<EntityType<FairyEntity>> FAIRY_ENTITY = register(
            "fairy", FairyEntity::new, MobCategory.MONSTER, 0.54f, 1.5f);
    public static final Supplier<EntityType<TsumiBukuroEntity>> TSUMI_BUKURO_ENTITY = register(
            "tsumi_bukuro", TsumiBukuroEntity::new, MobCategory.CREATURE, 0.66f, 1.8f);
    public static final Supplier<EntityType<LilyWhiteEntity>> LILY_WHITE = register(
            "lily_white", LilyWhiteEntity::new, MobCategory.CREATURE, 0.56f, 1.54f);
    public static final Supplier<EntityType<FlandreScarletEntity>> FLANDRE_SCARLET = register(
            "flandre_scarlet", FlandreScarletEntity::new, MobCategory.MONSTER, 0.58f, 1.58f);
    public static final Supplier<EntityType<RemiliaScarletEntity>> REMILIA_SCARLET = register(
            "remilia_scarlet", RemiliaScarletEntity::new, MobCategory.MONSTER, 0.58f, 1.58f);
    // public static final Supplier<EntityType<KomeijiKoishiEntity>> KOMEIJI_KOISHI = register(
    //         "komeiji_koishi", KomeijiKoishiEntity::new, MobCategory.MONSTER, 0.58f, 1.58f);
    public static final Supplier<EntityType<HakureiReimuEntity>> HAKUREI_REIMU = register(
            "hakurei_reimu", HakureiReimuEntity::new, MobCategory.CREATURE, 0.6f, 1.61f);
    public static final Supplier<EntityType<DestructiveEyeEntity>> DESTRUCTIVE_EYE = register(
            "destructive_eye", DestructiveEyeEntity::new, MobCategory.MISC, 3f, 3f);
    public static final Supplier<EntityType<LaserSourceEntity>> LASER_SOURCE = register(
            "laser_source", LaserSourceEntity::new, MobCategory.MISC, 1f, 1f);
    public static final Supplier<EntityType<Danmaku>> DANMAKU = register("danmaku", Danmaku::new,
            MobCategory.MISC, 1f, 1f);
    public static final Supplier<EntityType<SpellCardEntity>> SPELL_CARD = register("spell_card", SpellCardEntity::new,
            MobCategory.MISC, 1f, 1f);

    public static ResourceKey<EntityType<?>> byName(String entityName) {
        return ResourceKey.create(Registries.ENTITY_TYPE, GSKOUtil.key(entityName));
    }

    public static <T extends Entity> Supplier<EntityType<T>> register(String registryName, EntityType.EntityFactory<T> factory, MobCategory category, float width, float height) {
        return ENTITIES.register(registryName, () -> EntityType.Builder.of(factory, category).sized(width, height).build(byName(registryName)));
    }


    // public static <T extends Entity> EntityType.EntityFactory<SpellCardEntity> factory(Supplier<Item> supplier) {
    //     return (entityType, level) -> new SpellCardEntity(entityType, level, supplier.codec());
    // }


}
