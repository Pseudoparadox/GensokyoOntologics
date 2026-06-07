package com.github.fictology.gensokyoontology.util.script;

import com.mojang.serialization.MapCodec;

public interface IExpressionType {

    MapCodec<? extends IExpressionType> type();

    ExpressionType<?> getExp();
}
