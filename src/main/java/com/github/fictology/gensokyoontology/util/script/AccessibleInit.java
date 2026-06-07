package com.github.fictology.gensokyoontology.util.script;

import com.github.fictology.gensokyoontology.common.entiy.misc.Danmaku;
import com.github.fictology.gensokyoontology.util.GSKOUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.neoforged.fml.util.ObfuscationReflectionHelper;

import java.lang.reflect.Constructor;

public enum AccessibleInit {
    NULL(GSKOUtil.identifier("null"), null),
    VEC3_INIT(GSKOUtil.identifier("vec3_init"), getConstructor(Vec3.class, double.class, double.class, double.class)),
    VEC2_INIT(GSKOUtil.identifier("vec2_init"), getConstructor(Vec2.class, float.class, float.class)),
    DANMAKU(GSKOUtil.identifier("danmaku_init"), getConstructor(Danmaku.class, ServerLevel.class, Item.class, LivingEntity.class));


    final String className;
    final Constructor<?> constructor;

    AccessibleInit(String className, Constructor<?> constructor) {
        this.className = className;
        this.constructor = constructor;
    }


    private static <T> Constructor<T> getConstructor(Class<T> c, Class<?>... inits){
        return ObfuscationReflectionHelper.findConstructor(c, inits);
    }

    public Object createInstance(Object... objects) throws Exception{
        return this.constructor.newInstance(objects);
    }
}
