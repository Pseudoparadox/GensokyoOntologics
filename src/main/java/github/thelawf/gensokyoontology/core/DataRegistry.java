package github.thelawf.gensokyoontology.core;

import github.thelawf.gensokyoontology.GensokyoOntology;
import github.thelawf.gensokyoontology.data.CoasterPhysics;
import github.thelawf.gensokyoontology.data.GSKOSerializers;
import github.thelawf.gensokyoontology.data.HermiteNodeInfo;
import net.minecraft.network.datasync.IDataSerializer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DataSerializerEntry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class DataRegistry {
    public static final DeferredRegister<DataSerializerEntry> SERIALIZERS = DeferredRegister.create(
            ForgeRegistries.DATA_SERIALIZERS, GensokyoOntology.MODID);

    public static final RegistryObject<DataSerializerEntry> QUAT_SERIALIZER = SERIALIZERS.register(
            "quaternion", () -> new DataSerializerEntry(GSKOSerializers.QUATERNION));
    public static final RegistryObject<DataSerializerEntry> VECTOR3F = SERIALIZERS.register(
            "vector3f", () -> new DataSerializerEntry(GSKOSerializers.VECTOR3F));
    public static final RegistryObject<DataSerializerEntry> CATMULL_ROM = SERIALIZERS.register(
            "catmull_rom", () -> new DataSerializerEntry(GSKOSerializers.CATMULL_ROM));
    public static final RegistryObject<DataSerializerEntry> HERMITE_SPLINE = SERIALIZERS.register(
            "hermite_spline", () -> new DataSerializerEntry(HermiteNodeInfo.EMPTY));
    public static final RegistryObject<DataSerializerEntry> COASTER_PHYSICS = SERIALIZERS.register(
            "coaster_physics", () -> new DataSerializerEntry(CoasterPhysics.INERTIAL_STD));

    public static final RegistryObject<DataSerializerEntry> ORDER_SERIALIZER = SERIALIZERS.register(
            "villager_order", () -> new DataSerializerEntry(GSKOSerializers.VILLAGER_ORDER));
}
