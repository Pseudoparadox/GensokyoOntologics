package com.github.fictology.gensokyoontology.common.combat;

import com.github.fictology.gensokyoontology.util.GSKOUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;

public class GSKODamage {
    public static final ResourceKey<DamageType> DANMAKU_TYPE = create("danmaku");
    public static final ResourceKey<DamageType> LASER = create("laser");
    public static final ResourceKey<DamageType> HAKUREI_POWER = create("hakurai_power");
    public static final ResourceKey<DamageType> EYE = create("eye");


    private static ResourceKey<DamageType> create(String name) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, GSKOUtil.key(name));
    }

}

