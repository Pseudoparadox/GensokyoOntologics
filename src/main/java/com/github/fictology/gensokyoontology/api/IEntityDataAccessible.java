package com.github.fictology.gensokyoontology.api;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.attachment.AttachmentType;

public interface IEntityDataAccessible {
    default void addToSyncData(String key, ValueOutput output,  SynchedEntityData data, EntityDataAccessor<Integer> accessor){
        output.putInt(key, data.get(accessor));
    }
    default void setFromSyncData(String key, ValueInput input, SynchedEntityData data, EntityDataAccessor<Integer> accessor){
        data.set(accessor, input.getIntOr(key, 0));
    }

    default void setIntSyncData(SynchedEntityData data, EntityDataAccessor<Integer> accessor, int value){
        data.set(accessor, value);
    }

    default int getIntSyncData(SynchedEntityData data, EntityDataAccessor<Integer> accessor){
        return data.get(accessor);
    }
    default void putIntTag(Entity entity, AttachmentType<CompoundTag> tagAttach, String key, int value){
        entity.getData(tagAttach).putInt(key, value);
    }
    default void getIntTag(Entity entity, AttachmentType<CompoundTag> tagAttach, String key, int defaultValue){
        entity.getData(tagAttach).getIntOr(key, defaultValue);
    }
}
