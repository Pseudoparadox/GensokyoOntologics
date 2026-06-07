package com.github.fictology.gensokyoontology.util.script;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;

import java.util.stream.Stream;

public class ScriptOps implements DynamicOps<ExpressionType<? extends IExpressionType>> {

    @Override
    public ExpressionType<? extends IExpressionType> empty() {
        return null;
    }

    @Override
    public <U> U convertTo(DynamicOps<U> outOps, ExpressionType<? extends IExpressionType> input) {
        return null;
    }

    @Override
    public DataResult<Number> getNumberValue(ExpressionType<? extends IExpressionType> input) {
        return null;
    }

    @Override
    public ExpressionType<? extends IExpressionType> createNumeric(Number i) {
        return null;
    }

    @Override
    public DataResult<String> getStringValue(ExpressionType<? extends IExpressionType> input) {
        return null;
    }

    @Override
    public ExpressionType<? extends IExpressionType> createString(String value) {
        return null;
    }

    @Override
    public DataResult<ExpressionType<? extends IExpressionType>> mergeToList(ExpressionType<? extends IExpressionType> list, ExpressionType<? extends IExpressionType> value) {
        return null;
    }

    @Override
    public DataResult<ExpressionType<? extends IExpressionType>> mergeToMap(ExpressionType<? extends IExpressionType> map, ExpressionType<? extends IExpressionType> key, ExpressionType<? extends IExpressionType> value) {
        return null;
    }

    @Override
    public DataResult<Stream<Pair<ExpressionType<? extends IExpressionType>, ExpressionType<? extends IExpressionType>>>> getMapValues(ExpressionType<? extends IExpressionType> input) {
        return null;
    }

    @Override
    public ExpressionType<? extends IExpressionType> createMap(Stream<Pair<ExpressionType<? extends IExpressionType>, ExpressionType<? extends IExpressionType>>> map) {
        return null;
    }

    @Override
    public DataResult<Stream<ExpressionType<? extends IExpressionType>>> getStream(ExpressionType<? extends IExpressionType> input) {
        return null;
    }

    @Override
    public ExpressionType<? extends IExpressionType> createList(Stream<ExpressionType<? extends IExpressionType>> input) {
        return null;
    }

    @Override
    public ExpressionType<? extends IExpressionType> remove(ExpressionType<? extends IExpressionType> input, String key) {
        return null;
    }
}
