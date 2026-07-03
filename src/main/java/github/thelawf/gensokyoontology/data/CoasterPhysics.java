package github.thelawf.gensokyoontology.data;

import com.mojang.datafixers.util.Pair;
import github.thelawf.gensokyoontology.api.Functions;
import github.thelawf.gensokyoontology.api.INBTWriter;
import github.thelawf.gensokyoontology.api.ISynchornizable;
import github.thelawf.gensokyoontology.api.util.Maybe;
import github.thelawf.gensokyoontology.common.entity.misc.RailEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.FloatNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.network.PacketBuffer;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class CoasterPhysics implements INBTWriter, ISynchornizable<CompoundNBT, CoasterPhysics> {
    private final CompoundNBT properties = new CompoundNBT();
    public static final String KEY_TYPE = "railType";
    public static final String KEY_VELOCITY = "velocity";
    public static final String KEY_ACCELERATION = "acceleration";
    public static final String KEY_MAX_SPEED = "maxSpeed";

    @SafeVarargs
    public static <T extends INBT> CoasterPhysics of(Supplier<Pair<String, T>>... setters){
        CoasterPhysics physics = new CoasterPhysics();
        for (Supplier<Pair<String, T>> setter : setters) {
            physics.set(setter.get().getFirst(), setter.get().getSecond());
        }
        return physics;
    }

    public static CoasterPhysics from(CompoundNBT properties){
        return new CoasterPhysics().setProperties(properties);
    }

    public static final CoasterPhysics ACCELERATION_STD = CoasterPhysics.of(
            () -> Pair.of(KEY_TYPE, IntNBT.valueOf(RailEntity.Info.ACCELERATION.ordinal())),
            () -> Pair.of(KEY_VELOCITY, FloatNBT.valueOf(0F)),
            () -> Pair.of(KEY_ACCELERATION, FloatNBT.valueOf(0.1F)),
            () -> Pair.of(KEY_MAX_SPEED, FloatNBT.valueOf(3F)));

    public static final CoasterPhysics DECELERATION_STD = CoasterPhysics.of(
            () -> Pair.of(KEY_TYPE, IntNBT.valueOf(RailEntity.Info.DECELERATION.ordinal())),
            () -> Pair.of(KEY_VELOCITY, FloatNBT.valueOf(0F)),
            () -> Pair.of(KEY_ACCELERATION, FloatNBT.valueOf(0.1F)),
            () -> Pair.of(KEY_MAX_SPEED, FloatNBT.valueOf(3F)));

    public static final CoasterPhysics UNIFORM_STD = CoasterPhysics.of(
            () -> Pair.of(KEY_TYPE, IntNBT.valueOf(RailEntity.Info.UNIFORM.ordinal())),
            () -> Pair.of(KEY_VELOCITY, FloatNBT.valueOf(0F)),
            () -> Pair.of(KEY_ACCELERATION, FloatNBT.valueOf(0F)),
            () -> Pair.of(KEY_MAX_SPEED, FloatNBT.valueOf(0.5F)));

    public static final CoasterPhysics INERTIAL_STD = CoasterPhysics.of(
            () -> Pair.of(KEY_TYPE, IntNBT.valueOf(RailEntity.Info.INERTIAL.ordinal())),
            () -> Pair.of(KEY_VELOCITY, FloatNBT.valueOf(0F)),
            () -> Pair.of(KEY_ACCELERATION, FloatNBT.valueOf(0F)),
            () -> Pair.of(KEY_MAX_SPEED, FloatNBT.valueOf(0.5F)));

    @Override
    public CoasterPhysics copy() {
        return new CoasterPhysics().setProperties(this.properties);
    }

    @Override
    public void write(PacketBuffer buf, CoasterPhysics value) {
        buf.writeCompoundTag(value.properties);
    }

    @Override
    public CoasterPhysics read(PacketBuffer buf) {
        return new CoasterPhysics().setProperties(buf.readCompoundTag());
    }

    @Override
    public CoasterPhysics copyValue(CoasterPhysics value) {
        return value.copy();
    }

    @Override
    public CompoundNBT serializeNBT() {
        return this.properties;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.setProperties(nbt);
    }

    private CoasterPhysics setProperties(CompoundNBT properties){
        this.properties.merge(properties);
        return this;
    }

    public <T extends INBT> CoasterPhysics set(String key, T inbt){
        this.properties.put(key, inbt);
        return this;
    }

    public <A> Maybe<A> tryGetValue(String key, Function<INBT, A> nbt2Any){
        return Maybe.ofNullable(this.properties.get(key)).map(nbt2Any);
    }
}
