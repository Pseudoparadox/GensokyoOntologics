package github.thelawf.gensokyoontology.api.util;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class Maybe<T> {
    @Nullable private T value;
    private Maybe(){}
    private Maybe(@org.jetbrains.annotations.Nullable T value){
        this.value = value;
    }
    public static <T> Maybe<T> empty(){
        return new Maybe<>();
    }
    public static <T> Maybe<T> ofNullable(@Nullable T value){
        return value == null ? new Maybe<>() : new Maybe<>(value);
    }
    public static <T> Maybe<T> from(Optional<T> optional){
        return optional.map(Maybe::new).orElseGet(Maybe::new);
    }
    public static <T> boolean tryCreate(Maybe<T> optional, Maybe<T> thisRef){
        if (!optional.isPresent()) return false;
        thisRef.set(optional.get());
        return true;
    }

    public void set(T value){
        this.value = value;
    }

    public @Nullable T get(){
        return this.value;
    }

    public boolean isPresent(){
        return this.value != null;
    }

    /**
     * @param <A> Any Type
     */
    public <A> Maybe<T> ifTypeMatches(Consumer<A> action, Function<T, A> type2Any){
        if (this.value == null) return this;
        action.accept(type2Any.apply(this.value));
        return this;
    }

    public Maybe<T> ifPresent(Consumer<T> action){
        if (this.value == null) return this;
        action.accept(this.value);
        return this;
    }

    public <T1> Maybe<T1> map(Function<T, T1> mapper){
        if (this.value == null) return Maybe.empty();
        return Maybe.ofNullable(mapper.apply(this.value));
    }

}
