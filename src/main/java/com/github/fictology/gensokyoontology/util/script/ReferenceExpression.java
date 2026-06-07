package com.github.fictology.gensokyoontology.util.script;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class ReferenceExpression extends ExpressionType<IExpressionType> implements IExpressionType{

    public final ParamExpression variableRef;
    public final Optional<UUID> entityUuid;
    public final Optional<BlockPos> posOptional;
    public final ResourceKey<Level> level;

    public static final ReferenceExpression EMPTY = new ReferenceExpression(
            ParamExpression.NULL, Optional.empty(), Optional.empty(), Level.OVERWORLD);

    public ReferenceExpression(ParamExpression variableRef, Optional<UUID> entityRef, Optional<BlockPos> posOptional, ResourceKey<Level> level) {
        this.variableRef = variableRef;
        this.entityUuid = entityRef;
        this.posOptional = posOptional;
        this.level = level;
    }

    public ParamExpression getVariableRef() {
        return this.variableRef;
    }

    public Optional<UUID> getEntityUuid() {
        return this.entityUuid;
    }

    public Optional<BlockPos> getPosOptional() {
        return this.posOptional;
    }

    public ResourceKey<Level> getLevel() {
        return this.level;
    }

    @Override
    public ExpressionType<IExpressionType> get() {
        return this;
    }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, ReferenceExpression> streamCodec() {
        return StreamCodec.composite(
                ByteBufCodecs.fromCodec(ParamExpression.NULL.type().codec()), type -> this.getVariableRef(),
                ByteBufCodecs.optional(UUIDUtil.STREAM_CODEC), type -> this.getEntityUuid(),
                ByteBufCodecs.optional(BlockPos.STREAM_CODEC), type -> this.getPosOptional(),
                ByteBufCodecs.fromCodec(Level.RESOURCE_KEY_CODEC), type -> this.getLevel(),
                ReferenceExpression::new);
    }


    public void pushRefToMap(Map<String, Object> varMap){
        varMap.put(this.variableRef.paramName, this.variableRef.getValue());
    }

    @Override
    public MapCodec<ReferenceExpression> type() {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
                ParamExpression.NULL.type().codec().fieldOf("paramRef").forGetter(type -> this.getVariableRef()),
                UUIDUtil.CODEC.optionalFieldOf("entity_uuid").forGetter(type -> this.getEntityUuid()),
                BlockPos.CODEC.optionalFieldOf("pos").forGetter(type -> this.getPosOptional()),
                Level.RESOURCE_KEY_CODEC.fieldOf("world").forGetter(type -> this.getLevel())
        ).apply(instance, ReferenceExpression::new));
    }


    @Override
    public ExpressionType<?> getExp() {
        return this;
    }
}
