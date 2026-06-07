package com.github.fictology.gensokyoontology.util.script;

import com.github.fictology.gensokyoontology.common.entiy.misc.Danmaku;
import com.github.fictology.gensokyoontology.util.GSKOUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.fml.util.ObfuscationReflectionHelper;

import java.lang.reflect.Method;

public enum AccessibleMethod {
    VOID(GSKOUtil.identifier("void_method"), null),
    DANMAKU_CREATE(GSKOUtil.identifier("danmaku_create"), getMethod(Danmaku.class, "create",
            ServerLevel.class, LivingEntity.class, ItemStack.class)),
    PROJECTILE_SHOOT(GSKOUtil.identifier("projectile_shoot"), getMethod(Projectile.class, "shoot",
            double.class, double.class, double.class, float.class, float.class)),
    PROJECTILE_SET_MOTION(GSKOUtil.identifier("projectile_set_motion"), getMethod(Entity.class, "setDeltaMovement",
            Vec3.class)),
    LEVEL_GET_ENTITY(GSKOUtil.identifier("level_get_entity"), getMethod(ServerLevel.class, "getAllEntities")),
    LEVEL_ADD_ENTITY(GSKOUtil.identifier("level_add_entity"), getMethod(ServerLevel.class, "addFreshEntity",
            Entity.class)),
    ENTITY_TICK(GSKOUtil.identifier("entity_tick"), getMethod(Entity.class, "tick")),
    ENTITY_SET_POS(GSKOUtil.identifier("entity_tick"), getMethod(Entity.class, "setPos", Vec3.class)),
    ENTITY_SET_ROT(GSKOUtil.identifier("entity_tick"), getMethod(Entity.class, "setOldRot", float.class, float.class));
    final String methodName;
    final Method method;

    AccessibleMethod(String methodName, Method method) {
        this.methodName = methodName;
        this.method = method;
    }

    private static <T> Method getMethod(Class<T> c, String methodName){
        return ObfuscationReflectionHelper.findMethod(c, methodName);
    }

    private static <T> Method getMethod(Class<T> c, String methodName, Class<?>... classes){
        return ObfuscationReflectionHelper.findMethod(c, methodName, classes);
    }

    public String methodName() {
        return methodName;
    }

}
