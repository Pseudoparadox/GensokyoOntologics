package com.github.fictology.gensokyoontology.api;

import com.mojang.datafixers.util.Pair;

public class FloatPair extends NamedEntry<Float, Float>{
    private FloatPair(Pair<String, Float> stringFloatPair, Pair<String, Float> stringFloatPair2) {
        super(stringFloatPair, stringFloatPair2);
    }

    public static FloatPair of(String name1, float value1, String name2, float value2){
        return new FloatPair(Pair.of(name1, value1), Pair.of(name2, value2));
    }

    public float getValue(String name) {
        return aPair.getFirst().equals(name) ? aPair.getSecond() : bPair.getFirst().equals(name) ? bPair.getSecond() : 0;
    }
}
