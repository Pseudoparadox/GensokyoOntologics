package com.github.fictology.gensokyoontology.util;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;

public interface IDataSetterGetter {
    default void setIntData(SynchedEntityData data, EntityDataAccessor<Integer> accessor, int value){
        data.set(accessor, value);
    }
    default int getIntData(SynchedEntityData data, EntityDataAccessor<Integer> accessor){
        return data.get(accessor);
    }
    default void setFloatData(SynchedEntityData data, EntityDataAccessor<Float> accessor, float value){
        data.set(accessor, value);
    }
    default float getFloatData(SynchedEntityData data, EntityDataAccessor<Float> accessor){
        return data.get(accessor);
    }

    default void setUUID(SynchedEntityData data, EntityDataAccessor<Integer> accessor, int value){
        data.set(accessor, value);
    }
    default int getUUID(SynchedEntityData data, EntityDataAccessor<Integer> accessor){
        return data.get(accessor);
    }
}
