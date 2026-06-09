package com.github.fictology.gensokyoontology.util;

import com.github.fictology.gensokyoontology.GensokyoOntology;
import com.mojang.logging.LogUtils;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;

public final class GSKOUtil {
    public static <T> ResourceKey<T> resource(ResourceKey<? extends Registry<T>> parent, String name) {
        return ResourceKey.create(parent, key(name));
    }

    public static void showMsg(Player p, String msg, boolean actionBar) {
        p.sendSystemMessage(Component.literal(msg));
    }

    public static void showMsg(Player p, Object msg, boolean actionBar) {
        p.sendSystemMessage(Component.literal(msg.toString()));
    }

    public static void showMsg(Player p, String prefix, String suffix, boolean actionBar) {
        p.sendSystemMessage(Component.translatable(affixKey(prefix, suffix).toLanguageKey()));
    }

    public static Identifier affixKey(String prefix, String suffix) {
        return Identifier.parse(prefix + "." + GensokyoOntology.MODID + "." + suffix);
    }

    public static Identifier key(String keyName) {
        return Identifier.fromNamespaceAndPath(GensokyoOntology.MODID, keyName);
    }

    public static String affix(String prefix, String suffix) {
        return prefix + "." + GensokyoOntology.MODID + "." + suffix;
    }

    public static String identifier(String name) {
        return GensokyoOntology.MODID + ":" + name;
    }

    public static Component translate(String prefix, String suffix) {
        return Component.translatable(affixKey(prefix, suffix).toLanguageKey(), suffix);
    }

    public static BlockPos getStructurePos(ServerLevel level, Registry<Structure> structureRegistry) {
        Registry<Structure> registry = level.registryAccess().lookupOrThrow(Registries.STRUCTURE);
        // level.getChunkSource().getGenerator().findNearestMapStructure(level, HolderSet.direct(o -> registry))
        return null;
    }

    public static Advancement getAdvancement(ServerPlayer serverPlayer, Identifier retreatLilywhite) {
        return null;
    }

    public static void info(String msg){
        LogUtils.getLogger().info(msg);
    }

    public static void warn(String msg){
        LogUtils.getLogger().warn(msg);
    }

    public static void error(String msg){
        LogUtils.getLogger().error(msg);
    }

    public static <K,V> K getKeyByValue(Map<K,V> map, V value){
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (Objects.equals(entry.getValue(), value)) {
                return entry.getKey();
            }
        }
        return null;
    }

    @NotNull
    public static <K,V> K getKeyByValueOrDefault(Map<K,V> map, K defaultKey, V value){
        return getKeyByValue(map, value) == null ? defaultKey : Objects.requireNonNull(getKeyByValue(map, value));
    }
}
