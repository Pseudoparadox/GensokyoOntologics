package com.github.fictology.gensokyoontology.registry;

import com.github.fictology.gensokyoontology.GensokyoOntology;
import com.github.fictology.gensokyoontology.data.Identities;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public final class AttachRegistry {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS = DeferredRegister.create(
            NeoForgeRegistries.ATTACHMENT_TYPES, GensokyoOntology.MODID);
    public static final Supplier<AttachmentType<Identities>> IDENTITY = ATTACHMENTS.register("identity",
            () -> AttachmentType.serializable(() -> new Identities(10000, 0, 1)).build());
    public static final Supplier<AttachmentType<CompoundTag>> NBT_DATA = ATTACHMENTS.register("nbt_data",
            () -> AttachmentType.builder(() -> new CompoundTag()).build());
    public static final Supplier<AttachmentType<Integer>> POWER = ATTACHMENTS.register("power",
            () -> AttachmentType.builder(() -> 0).build());
}
