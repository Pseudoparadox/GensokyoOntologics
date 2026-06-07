package com.github.fictology.gensokyoontology.util;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class NullableObject<T> {
    private final T t;
    NullableObject(T t){
        this.t = t;
    }

    public static <T> NullableObject<T> of(T value){
        return new NullableObject<>(value);
    }

    public static <T> NullableObject<T> empty(T value){
        return new NullableObject<>(value);
    }

    public Optional<T> asOptional(){
        return Optional.of(t);
    }

    public boolean isNull() {
        return this.t == null;
    }

    public boolean notNull() {
        return this.t != null;
    }

    public void ifPresent(Consumer<T> consumer){
        if (notNull()) {
            consumer.accept(t);
        }
    }

    public <R> R mapIfPresent(Function<T, R> function, R r){
        return function.apply(t);
    }
}
