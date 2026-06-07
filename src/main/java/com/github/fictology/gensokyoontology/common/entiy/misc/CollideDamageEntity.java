package com.github.fictology.gensokyoontology.common.entiy.misc;


import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public abstract class CollideDamageEntity extends Entity {
    public CollideDamageEntity(EntityType<?> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
    }
}
