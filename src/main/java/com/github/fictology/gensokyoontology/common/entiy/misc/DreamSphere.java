package com.github.fictology.gensokyoontology.common.entiy.misc;

import com.github.fictology.gensokyoontology.common.entiy.AffiliatedEntity;
import com.github.fictology.gensokyoontology.registry.DataRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.joml.Vector4i;

import java.util.HashMap;
import java.util.Map;

public class DreamSphere extends AffiliatedEntity {
    public static final EntityDataAccessor<Integer> DATA_INDEX = SynchedEntityData.defineId(DreamSphere.class,
            EntityDataSerializers.INT);
    public static final Map<Integer, Vector4i> INDEX_2_COLOR = Util.make(() -> {
        var map = new HashMap<Integer, Vector4i>();
        map.put(0, new Vector4i(255, 0, 0, 255));
        map.put(1, new Vector4i(0, 255, 0, 255));
        map.put(2, new Vector4i(0, 0, 255, 255));
        return map;
    });
    public DreamSphere(EntityType<?> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
        this.setData(DataRegistry.NBT_DATA, new CompoundTag());
        this.putIntTag(this, DataRegistry.NBT_DATA.get(), "size", 1);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_INDEX, 0);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput compound) {
        super.addAdditionalSaveData(compound);
        this.addToSyncData("index", compound, this.entityData, DATA_INDEX);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput compound) {
        super.readAdditionalSaveData(compound);
        this.setFromSyncData("index", compound, this.entityData, DATA_INDEX);
    }

    @Override
    public Identifier getTexture() {
        return null;
    }
}
