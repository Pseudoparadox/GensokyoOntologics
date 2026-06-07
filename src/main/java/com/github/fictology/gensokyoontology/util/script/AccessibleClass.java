package com.github.fictology.gensokyoontology.util.script;

import com.github.fictology.gensokyoontology.util.GSKOUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.neoforged.fml.util.ObfuscationReflectionHelper;

import java.lang.reflect.Field;
import java.util.List;

public enum AccessibleClass {
    EMPTY(GSKOUtil.identifier("empty_class"), List.of()),
    THIS(GSKOUtil.identifier("this_instance"), List.of()),
    INT(GSKOUtil.identifier("int_class"), List.of()),
    FLOAT(GSKOUtil.identifier("float_class"), List.of()),
    DOUBLE(GSKOUtil.identifier("double_class"), List.of()),
    STRING(GSKOUtil.identifier("string_class"), List.of()),
    ITEM_STACK(GSKOUtil.identifier("item_stack_class"), List.of()),
    DANMAKU(GSKOUtil.identifier("danmaku_class"), List.of(getField(Entity.class, "tickCount"))),
    VEC3(GSKOUtil.identifier("vec3_class"), List.of(getField(Vec3.class, "x"), getField(Vec3.class, "y"), getField(Vec3.class, "z"))),
    VEC2(GSKOUtil.identifier("vec2_class"), List.of(getField(Vec2.class, "x"), getField(Vec2.class, "y"))),
    LEVEL(GSKOUtil.identifier("level_class"), List.of()),
    SERVER_LEVEL(GSKOUtil.identifier("server_level_class"), List.of()),
    ENTITY(GSKOUtil.identifier("entity_class"), List.of(getField(Entity.class, "tickCount"))),
    LIVING_ENTITY(GSKOUtil.identifier("living_entity_class"), List.of(getField(Entity.class, "tickCount"))),
    PLAYER(GSKOUtil.identifier("player_class"), List.of(getField(Entity.class, "tickCount")));
    final String className;
    final List<Field> fields;

    AccessibleClass(String className, List<Field> fields) {
        this.className = className;
        this.fields = fields;
    }

    public static <T> Field getField(Class<T> c, String fieldName){
        return ObfuscationReflectionHelper.findField(c, fieldName);
    }

}
