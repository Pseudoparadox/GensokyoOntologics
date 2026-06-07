package com.github.fictology.gensokyoontology.util.script;

@FunctionalInterface
public interface CastHelper<T extends S, S> {
    T cast(S superObj);
}
