package com.github.fictology.gensokyoontology.util;

import com.github.fictology.gensokyoontology.util.api.Vector3f;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.blaze3d.platform.ClipboardManager;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;

import java.util.*;

public class AnimeHelper {
    public static final Map<String, AnimationChannel.Target> TARGET_MAP = Map.of(
            "rotation", AnimationChannel.Targets.ROTATION,
            "position", AnimationChannel.Targets.POSITION,
            "scale", AnimationChannel.Targets.SCALE);

    public static final Map<String, AnimationChannel.Interpolation> INTERPOLATION_MAP = Map.of(
            "linear", AnimationChannel.Interpolations.LINEAR,
            "catmullrom", AnimationChannel.Interpolations.CATMULLROM
    );

    public static final Codec<Keyframe> KEYFRAME_CODEC = RecordCodecBuilder.create(keyframeInstance -> keyframeInstance.group(
            Codec.FLOAT.fieldOf("timestamp").forGetter(Keyframe::timestamp),
            Vector3f.CODEC.fieldOf("target").forGetter(keyframe -> new org.joml.Vector3f(keyframe.postTarget().x(), keyframe.postTarget().y(), keyframe.postTarget().z())),
            Codec.STRING.fieldOf("interpolation").forGetter(keyframe ->
                    GSKOUtil.getKeyByValueOrDefault(INTERPOLATION_MAP, "", keyframe.interpolation()))
    ).apply(keyframeInstance, (f, v3f, str) -> new KeyFrameWrapper(f, v3f, str).wrap()));

    public static final Codec<AnimationChannel> CHANNEL_CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Codec.STRING.fieldOf("operationName").forGetter(channel ->
                            TARGET_MAP.entrySet().stream().filter(entry ->
                                    Objects.equals(entry.getValue(), channel.target())).map(
                                            Map.Entry::getKey).findFirst().orElse("")),
            Codec.list(KEYFRAME_CODEC).fieldOf("keyframe").forGetter(channel -> Arrays.asList(channel.keyframes()))
    ).apply(inst, (s, keyframes) -> new ChannelWrapper(s, keyframes).wrap()));

    public static final Codec<AnimationDefinition> ANIMATION_CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Codec.FLOAT.fieldOf("lengthSeconds").forGetter(AnimationDefinition::lengthInSeconds),
            Codec.BOOL.fieldOf("isLooping").forGetter(AnimationDefinition::looping),
            Codec.unboundedMap(Codec.STRING, Codec.list(CHANNEL_CODEC)).fieldOf("boneAnimations").forGetter(AnimationDefinition::boneAnimations)
    ).apply(inst, AnimationDefinition::new));

    public static void asJsonAndCopyToClipboard(AnimationDefinition animation){
        var clipboard = new ClipboardManager();
        clipboard.setClipboard(Minecraft.getInstance().getWindow(),
                ANIMATION_CODEC.encodeStart(JsonOps.INSTANCE, animation).result().map(JsonElement::toString)
                        .orElseThrow());
    }

    public static AnimationDefinition createAnimeByJson(String json){
        return ANIMATION_CODEC.decode(JsonOps.INSTANCE, JsonParser.parseString(json)).result().orElseThrow().getFirst();
    }

    public record KeyFrameWrapper(float timestamp, org.joml.Vector3f target, String interpolationName){
        public Keyframe wrap(){
            return new Keyframe(timestamp, target, INTERPOLATION_MAP.get(interpolationName));
        }
    }


    public static class ChannelWrapper {

        private final AnimationChannel.Target target;
        private final List<Keyframe> keyframes = new ArrayList<>();


        public ChannelWrapper(String operationName, List<Keyframe> keyframes) {
            this.target = TARGET_MAP.get(operationName);
            this.keyframes.addAll(keyframes);
        }

        public AnimationChannel wrap(){
            return new AnimationChannel(this.target, keyframes.toArray(Keyframe[]::new));
        }
    }
}
