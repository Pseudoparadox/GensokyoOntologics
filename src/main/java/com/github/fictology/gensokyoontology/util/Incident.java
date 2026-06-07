package com.github.fictology.gensokyoontology.util;

import net.minecraft.util.StringRepresentable;
import net.neoforged.fml.common.asm.enumextension.IExtensibleEnum;
import net.neoforged.fml.common.asm.enumextension.NamedEnum;
import net.neoforged.fml.common.asm.enumextension.NetworkedEnum;
import org.jetbrains.annotations.NotNull;

@NamedEnum
@NetworkedEnum(NetworkedEnum.NetworkCheck.BIDIRECTIONAL)
public enum Incident implements StringRepresentable, IExtensibleEnum {
    NONE(0, "none"),
    SCARLET_MIST(1, "scarlet_mist"),
    SPRING_SNOW(2, "spring_snow"),
    IMPERISHABLE_NIGHT(4, "imperishable_night"),
    REALITY(8, "reality");

    public final int index;
    private final String keyName;

    Incident(int index, String keyName) {
        this.index = index;
        this.keyName = keyName;
    }


    @Override
    public @NotNull String getSerializedName() {
        return GSKOUtil.affix("enum", "incidents." + this.keyName);
    }
}
