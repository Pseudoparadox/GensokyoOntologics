package github.thelawf.gensokyoontology.api;

import net.minecraft.nbt.INBT;
import net.minecraftforge.common.util.INBTSerializable;

public interface INBTSynchornizable<T extends INBT, S extends INBTSynchornizable<T, S>> extends INBTSerializable<T> {
    S copy();
}
