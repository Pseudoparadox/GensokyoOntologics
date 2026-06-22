package com.github.fictology.gensokyoontology.data;

import com.github.fictology.gensokyoontology.util.GSKOUtil;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.util.Mth;

import java.util.concurrent.atomic.AtomicReference;

public record EventCallbackInfo(String name, int currentTick, int maxTick, CompoundTag callbackData) implements CustomPacketPayload {

    public static final EventCallbackInfo EMPTY = new EventCallbackInfo("empty", 0, 0, new CompoundTag());
    public static final Type<EventCallbackInfo> TYPE = new Type<>(GSKOUtil.key("anim_callback"));

    public static EventCallbackInfo onEnter(String name, int maxTick){
        return EMPTY.newInstance(name, 0, maxTick, new CompoundTag()).addListener("on_enter", 0);
    }
    public static EventCallbackInfo onExit(String name, int currentTick, int maxTick){
        return EMPTY.newInstance(name, currentTick, maxTick, new CompoundTag()).addListener("on_exit", 0);
    }

    public static EventCallbackInfo cancelEventFromTo(String name, int currentTick, int maxTick){
        return EMPTY.newInstance(name, currentTick, maxTick, new CompoundTag()).addListener("???", 0);
    }

    public static final Codec<EventCallbackInfo> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    Codec.STRING.fieldOf("anim_name").forGetter(EventCallbackInfo::name),
                    Codec.INT.fieldOf("current_tick").forGetter(EventCallbackInfo::currentTick),
                    Codec.INT.fieldOf("max_tick").forGetter(EventCallbackInfo::maxTick),
                    CompoundTag.CODEC.fieldOf("callback_data").forGetter(EventCallbackInfo::callbackData))
            .apply(instance, EventCallbackInfo::new));

    public static final StreamCodec<FriendlyByteBuf, EventCallbackInfo> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, EventCallbackInfo::name,
            ByteBufCodecs.INT, EventCallbackInfo::currentTick,
            ByteBufCodecs.INT, EventCallbackInfo::maxTick,
            ByteBufCodecs.COMPOUND_TAG, EventCallbackInfo::callbackData,
            EventCallbackInfo::new);

    public static EventCallbackInfo ofDefault(String name){
        return new EventCallbackInfo(name, 0,0, new CompoundTag());
    }
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void subscribeOnExit(){
        var listener = new CompoundTag();
        listener.putInt("on_exit", this.maxTick());
        this.callbackData().put("listener", listener);
    }

    public EventCallbackInfo addListener(String eventListenerId, int keyTick){
        var tick = Mth.clamp(keyTick, 0, this.maxTick());
        var listener = new CompoundTag();
        listener.putInt(eventListenerId, tick);
        this.callbackData().put("listener", listener);
        return this;
    }

    public boolean tryGetListener(AtomicReference<CompoundTag> ref){
        if (this.callbackData().get("listener") instanceof CompoundTag compoundTag){
            ref.set(compoundTag);
            return true;
        }
        return false;
    }

    /**
     * 仅服务端使用的方法，用于异步协程等并发场景下业务逻辑
     */
    public void invoke(String eventListenerId, Runnable runnable){
        var ref = new AtomicReference<CompoundTag>();
        if (!this.tryGetListener(ref)) return;
        if (!ref.get().contains(eventListenerId)) return;
        if (this.currentTick() != ref.get().getIntOr(eventListenerId, 0)) return;
        runnable.run();
    }

    /**
     * 仅服务端使用的方法，用于异步协程等并发场景下业务逻辑
     */
    public void invokeOnExit(Runnable runnable){
        var ref = new AtomicReference<CompoundTag>();
        if (!this.tryGetListener(ref)) return;
        if (!ref.get().contains("on_exit")) return;
        if (this.currentTick() < ref.get().getIntOr("on_exit", 0)) return;
        runnable.run();
    }

    public EventCallbackInfo newInstance(String name, int currentTick, int maxTick, CompoundTag callbackData){
        return new EventCallbackInfo(name, currentTick, maxTick, callbackData);
    }
    public EventCallbackInfo newCurrentTick(int currentTick){
        return new EventCallbackInfo(this.name(), currentTick, this.maxTick(), this.callbackData());
    }

}

