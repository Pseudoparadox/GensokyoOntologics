package com.github.fictology.gensokyoontology.registry;

import com.github.fictology.gensokyoontology.GensokyoOntology;
import com.github.fictology.gensokyoontology.util.GSKOUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Optional;

public class GSKOSoundEvents {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(
            Registries.SOUND_EVENT, GensokyoOntology.MODID);

    public static final DeferredHolder<SoundEvent, SoundEvent> MUSIC_GENSOKYO = register("music_gensokyo");
    public static final DeferredHolder<SoundEvent, SoundEvent> CICADA_AMBIENT = register("cicada_ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> BAMBOO_PARTRIDGE = register("bamboo_partridge");
    public static final DeferredHolder<SoundEvent, SoundEvent> MASTER_SPARK = register("master_spark");

    private static DeferredHolder<SoundEvent, SoundEvent> register(String name) {
        return SOUND_EVENTS.register(name, () -> new SoundEvent(GSKOUtil.key(name), Optional.of(1f)));
    }

}
