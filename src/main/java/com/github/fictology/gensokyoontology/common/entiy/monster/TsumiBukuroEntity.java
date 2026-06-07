package com.github.fictology.gensokyoontology.common.entiy.monster;


import com.github.fictology.gensokyoontology.common.entiy.AbstractHumanEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class TsumiBukuroEntity extends AbstractHumanEntity {

    public TsumiBukuroEntity(EntityType<? extends AgeableMob> type, Level worldIn) {
        super(type, worldIn);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return null;
    }
}
