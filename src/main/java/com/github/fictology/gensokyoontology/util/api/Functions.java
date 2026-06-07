package com.github.fictology.gensokyoontology.util.api;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

/**
 *
 * F -> Function 函数<br>
 * P -> Parameter 参数<br>
 * R -> Return 返回值
 */
public interface Functions {

    @FunctionalInterface
    interface F<P, R> {
        R invoke(P p);
    }

    @FunctionalInterface
    interface F2<P1, P2, R> {
        R invoke(P1 p1, P2 p2);
    }

    @FunctionalInterface
    interface F3<P1, P2, P3, R> {
        R invoke(P1 p1, P2 p2, P3 p3);
    }

    @FunctionalInterface
    interface F4<P1, P2, P3, P4, R> {
        R invoke(P1 p1, P2 p2, P3 p3, P4 p4);
    }

    static <P,R> R setLocalInLambda(AtomicReference<R> ref, F<P, R> func, P param){
        ref.set(func.invoke(param));
        return ref.get();
    }
    static <C, P, R> R setLocalInLambda(AtomicReference<R> ref, C condition, P param,
                                                         Predicate<C> predicate, F2<Predicate<C>, P, R> func){
        ref.set(func.invoke(predicate, param));
        return ref.get();
    }
}
