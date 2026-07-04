package github.thelawf.gensokyoontology.data.world;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;
import org.jetbrains.annotations.NotNull;

/**
 * 具体流程：
 * 1. 击杀蝙蝠，制作蝙蝠标本
 * 2. 蝙蝠标本将会告知你红魔馆的位置，并尝试带着你飞向红魔馆
 * 3. 往赛钱箱投入32枚硬币，获取30分钟的
 * 获取博丽的祝福
 * Flandre被梦想封印打中之后会
 *
 */
public class GSKOWorldSavedData extends WorldSavedData {
    private static final String NAME = "GSKOWorldSavedData";
    private float power;
    public GSKOWorldSavedData() {
        super(NAME);
    }
    public GSKOWorldSavedData(String name) {
        super(name);
    }

    public static GSKOWorldSavedData getInstance(World worldIn) {
        if (!(worldIn instanceof ServerWorld)) {
            throw new RuntimeException("Attempted to get the data from a client world. This is wrong.");
        }
        ServerWorld serverWorld = (ServerWorld) worldIn;
        DimensionSavedDataManager storage = serverWorld.getSavedData();
        return storage.getOrCreate(GSKOWorldSavedData::new, NAME);
    }

    public void writePower(float power) {
        this.power = power;
    }

    public float getPower() {
        return this.power;
    }

    @Override
    public void read(CompoundNBT nbt) {
        this.power = nbt.getFloat("power");
    }

    @Override
    @NotNull
    public CompoundNBT write(CompoundNBT compound) {
        compound.putFloat("power", this.power);
        return compound;
    }


}
