package github.thelawf.gensokyoontology.data;

import com.mojang.datafixers.util.Pair;
import github.thelawf.gensokyoontology.api.INBTWriter;
import github.thelawf.gensokyoontology.api.ISynchornizable;
import github.thelawf.gensokyoontology.api.util.Maybe;
import github.thelawf.gensokyoontology.common.entity.misc.CoasterVehicle;
import github.thelawf.gensokyoontology.common.entity.misc.RailEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.FloatNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Util;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

public class CoasterPhysics implements INBTWriter, ISynchornizable<CompoundNBT, CoasterPhysics> {
    private final CompoundNBT properties = new CompoundNBT();
    /** 匀速速度 */
    public static final String KEY_VELOCITY = "velocity";
    public static final String KEY_PROGRESS = "progress";
    public static final String KEY_ACCELERATION = "acceleration";
    public static final String KEY_MAX_SPEED = "maxSpeed";

    public static final CoasterPhysics ACCELERATION_STD = CoasterPhysics.of(
            Pair.of(KEY_PROGRESS, FloatNBT.valueOf(0F)),
            Pair.of(KEY_VELOCITY, FloatNBT.valueOf(0F)),
            Pair.of(KEY_ACCELERATION, FloatNBT.valueOf(0.1F)),
            Pair.of(KEY_MAX_SPEED, FloatNBT.valueOf(3F)));

    public static final CoasterPhysics DECELERATION_STD = CoasterPhysics.of(
            Pair.of(KEY_PROGRESS, FloatNBT.valueOf(0F)),
            Pair.of(KEY_VELOCITY, FloatNBT.valueOf(0F)),
            Pair.of(KEY_ACCELERATION, FloatNBT.valueOf(0.1F)),
            Pair.of(KEY_MAX_SPEED, FloatNBT.valueOf(3F)));

    public static final CoasterPhysics UNIFORM_STD = CoasterPhysics.of(
            Pair.of(KEY_PROGRESS, FloatNBT.valueOf(0F)),
            Pair.of(KEY_VELOCITY, FloatNBT.valueOf(0F)),
            Pair.of(KEY_ACCELERATION, FloatNBT.valueOf(0F)),
            Pair.of(KEY_MAX_SPEED, FloatNBT.valueOf(0.5F)));

    public static final CoasterPhysics INERTIAL_STD = CoasterPhysics.of(
            Pair.of(KEY_PROGRESS, FloatNBT.valueOf(0F)),
            Pair.of(KEY_VELOCITY, FloatNBT.valueOf(0F)),
            Pair.of(KEY_ACCELERATION, FloatNBT.valueOf(0F)),
            Pair.of(KEY_MAX_SPEED, FloatNBT.valueOf(0.5F)));

    public static final Map<RailEntity.Type, CoasterPhysics> PHYSICS_MAP = Util.make(() -> {
        Map<RailEntity.Type, CoasterPhysics> map = new HashMap<>();
        map.put(RailEntity.Type.ACCELERATION, ACCELERATION_STD);
        map.put(RailEntity.Type.DECELERATION, DECELERATION_STD);
        map.put(RailEntity.Type.UNIFORM, UNIFORM_STD);
        map.put(RailEntity.Type.INERTIAL, INERTIAL_STD);
        return map;
    });

    @SafeVarargs
    public static <T extends INBT> CoasterPhysics of(Pair<String, T>... setters){
        CoasterPhysics physics = new CoasterPhysics();
        for (Pair<String, T> setter : setters) {
            physics.set(setter.getFirst(), setter.getSecond());
        }
        return physics;
    }

    public static CoasterPhysics from(CompoundNBT properties){
        return new CoasterPhysics().setProperties(properties);
    }



    @Override
    public CoasterPhysics copy() {
        return new CoasterPhysics().setProperties(this.properties);
    }
    @SafeVarargs
    public final <T extends INBT> CoasterPhysics copyAndSet(Pair<String, T>... setter){
        CoasterPhysics physics = new CoasterPhysics().setProperties(this.properties);
        Stream.of(setter).forEach(p -> this.set(p.getFirst(), p.getSecond()));
        return this;
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
}
