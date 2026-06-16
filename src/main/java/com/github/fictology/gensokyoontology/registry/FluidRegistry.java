package com.github.fictology.gensokyoontology.registry;


import com.github.fictology.gensokyoontology.GensokyoOntology;
import com.github.fictology.gensokyoontology.util.GSKOUtil;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.WaterFluid;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public final class FluidRegistry {
    public static final FlowingFluid HOTSPRING_SOURCE = register("hot_spring_fluid", new WaterFluid.Source());
    public static final FlowingFluid HOTSPRING_FLOWING = register("hot_spring_fluid_flowing", new WaterFluid.Flowing());



    private static <T extends Fluid> T register(String key, T fluid) {
        return Registry.register(BuiltInRegistries.FLUID, key, fluid);
    }
    public static final DeferredRegister<FluidType> FLUIDS = DeferredRegister.create(
            NeoForgeRegistries.FLUID_TYPES, GensokyoOntology.MODID);
    public static final DeferredHolder<FluidType, FluidType> HOT_SPRING_TYPE = DeferredHolder.create(
            NeoForgeRegistries.Keys.FLUID_TYPES, GSKOUtil.key("hot_spring"));
    public static final DeferredHolder<Fluid, Fluid> HOT_SPRING = DeferredHolder.create(
            Registries.FLUID, GSKOUtil.key("hot_spring"));
    public static final DeferredHolder<Fluid, Fluid> FLOWING_HOT_SPRING = DeferredHolder.create(
            Registries.FLUID, GSKOUtil.key("flowing_hot_spring"));
    public static final BaseFlowingFluid.Properties HOT_SPRING_PROPERTIES = new BaseFlowingFluid.Properties(
            HOT_SPRING_TYPE,
            HOT_SPRING, FLOWING_HOT_SPRING);
    /*
    public static final ResourceLocation STILL_HOTSPRING_TEX = new ResourceLocation(
            GensokyoOntology.MODID, "tileentity/water_still");
    public static final ResourceLocation FLOW_HOTSPRING_TEX = new ResourceLocation(
            GensokyoOntology.MODID, "tileentity/water_flow");

    public static final ResourceLocation SAKE_WINE_STILL_TEX = STILL_HOTSPRING_TEX;
    public static final ResourceLocation SAKE_WINE_FLOW_TEX = FLOW_HOTSPRING_TEX;

    public static final ResourceLocation PAPER_PULP_STILL_TEX = STILL_HOTSPRING_TEX;
    public static final ResourceLocation PAPER_PULP_FLOW_TEX = FLOW_HOTSPRING_TEX;

    private static <T extends Fluid> T copy(String id, T fluid) {
        return Registry.copy(BuiltInRegistries.FLUID, id, fluid);
    }
    public static final RegistryObject<FlowingFluid> PAPER_PULP_SOURCE = FLUIDS.copy(
            "paper_pulp_fluid",
            () -> new ForgeFlowingFluid.Source(FluidRegistry.PAPER_PULP_PROPERTIES));
    public static final RegistryObject<FlowingFluid> PAPER_PULP_FLOWING = FLUIDS.copy(
            "paper_pulp_fluid_flowing",
            () -> new ForgeFlowingFluid.Flowing(FluidRegistry.PAPER_PULP_PROPERTIES));

    public static final RegistryObject<FlowingFluid> SAKE_WINE_SOURCE = FLUIDS.copy(
            "sake_wine_fluid", () -> new ForgeFlowingFluid.Source(FluidRegistry.SAKE_WINE_PROPERTIES));
    public static final RegistryObject<FlowingFluid> SAKE_WINE_FLOWING = FLUIDS.copy(
            "sake_wine_fluid_flowing", () -> new ForgeFlowingFluid.Flowing(FluidRegistry.SAKE_WINE_PROPERTIES));



    public static final ForgeFlowingFluid.Properties SAKE_WINE_PROPERTIES = new ForgeFlowingFluid.Properties(
            SAKE_WINE_SOURCE, SAKE_WINE_FLOWING, FluidAttributes.builder(SAKE_WINE_STILL_TEX, SAKE_WINE_FLOW_TEX)
            .color(0xFF8888BB)
            .density(3000)
            .viscosity(3800))
            .bucket(ItemRegistry.SAKE_BUCKET)
            .block(BlockRegistry.SAKE_WINE_BLOCK)
            .slopeFindDistance(3).explosionResistance(100F);

    public static final ForgeFlowingFluid.Properties PAPER_PULP_PROPERTIES = new ForgeFlowingFluid.Properties(
            PAPER_PULP_SOURCE, PAPER_PULP_FLOWING, FluidAttributes.builder(PAPER_PULP_STILL_TEX, PAPER_PULP_FLOW_TEX)
            .color(0xDDE1C699)
            .density(6000)
            .viscosity(2800))
            .bucket(ItemRegistry.PAPER_PULP_BUCKET)
            .block(BlockRegistry.PAPER_PULP_BLOCK)
            .slopeFindDistance(3).explosionResistance(100F);

     */
}
