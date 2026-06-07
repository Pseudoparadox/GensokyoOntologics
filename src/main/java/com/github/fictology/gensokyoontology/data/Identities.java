package com.github.fictology.gensokyoontology.data;

import com.github.fictology.gensokyoontology.util.GSKOUtil;
import com.github.fictology.gensokyoontology.util.Incident;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;

import java.util.List;
import java.util.function.Function;

public record Identities(int lifeTime, float power, int incidentSet) implements ValueIOSerializable {

    public static final List<Pair<String, Float>> DATA = List.of(
            Pair.of(GSKOUtil.identifier("power"), 0f),
            Pair.of(GSKOUtil.identifier("buddhism"), 0f)
    );

    public Identities removeIncident(Incident incident){
        if (!this.hasIncident(incident)) return this;
        else {
            var current = this.incidentSet;
            current &= ~incident.index;
            return new Identities(this.lifeTime, this.power, current);
        }
    }

    public boolean hasIncident(Incident incident) {
        return (this.incidentSet & incident.index) == incident.index;
    }

    @Override
    public void serialize(ValueOutput output) {

    }

    @Override
    public void deserialize(ValueInput input) {

    }

    public static class Builder{
        public int lifeTime;
        public float power;
        public int incidentIndex;
        public Builder(){}

        public Identities build(){
            return new Identities(lifeTime, power, incidentIndex);
        }

        public Builder lifeTime(int lifeTime){
            this.lifeTime = lifeTime;
            return this;
        }

        public Builder incident(Incident incident){
            this.incidentIndex |= incident.ordinal();
            return this;
        }

        public Builder power(float power){
            this.power = power;
            return this;
        }
    }

    public static Builder builder(){
        return new Builder();
    }

    public static final Codec<Identities> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            addIntEntry("lifeTime", Identities::lifeTime),
            addFloatEntry("power", Identities::power),
            addIntEntry("incident", Identities::incidentSet)
    ).apply(instance, Identities::new));

    public static final StreamCodec<ByteBuf, Identities> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, Identities::lifeTime,
            ByteBufCodecs.FLOAT, Identities::power,
            ByteBufCodecs.INT, Identities::incidentSet,
            Identities::new);

    public static <V> RecordCodecBuilder<V, Integer> addIntEntry(String key, Function<V, Integer> getter){
        return Codec.INT.fieldOf(key).forGetter(getter);
    }
    public static <V> RecordCodecBuilder<V, Float> addFloatEntry(String key, Function<V, Float> getter){
        return Codec.FLOAT.fieldOf(key).forGetter(getter);
    }


    public static <T> StreamCodec<ByteBuf, Float> bufCodec(){
        return ByteBufCodecs.FLOAT;
    }

}
