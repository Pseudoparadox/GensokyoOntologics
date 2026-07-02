package github.thelawf.gensokyoontology.api;

import net.minecraft.nbt.INBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.IDataSerializer;
import net.minecraftforge.common.util.INBTSerializable;

public interface ISynchornizable<T extends INBT, S extends ISynchornizable<T, S>> extends INBTSerializable<T>, IDataSerializer<S> {
    S copy();
}
