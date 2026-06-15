package com.github.fictology.gensokyoontology.common.entiy.misc;

import com.github.fictology.gensokyoontology.common.entiy.AffiliatedEntity;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class YinyangJade extends AffiliatedEntity {
    public YinyangJade(EntityType<?> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
    }

    @Override
    public Identifier getTexture() {
        return null;
    }

}
