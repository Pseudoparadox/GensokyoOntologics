package github.thelawf.gensokyoontology.api.entity;

import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.EntityDataManager;

public interface IEntityDataAccessible {
    default void setFloat(DataParameter<Float> dataParameter, EntityDataManager dataManager, float value){
        dataManager.set(dataParameter, value);
    }
    default float getFloat(DataParameter<Float> dataParameter, EntityDataManager dataManager){
        return dataManager.get(dataParameter);
    }
}
