package com.github.fictology.gensokyoontology.util.api;

public interface Actions {

    @FunctionalInterface
    interface Act3<A1, A2, A3> {
        void invoke(A1 a1, A2 a2, A3 a3);
    }

    @FunctionalInterface
    interface Act4<A1, A2, A3, A4> {
        void invoke(A1 a1, A2 a2, A3 a3, A4 a4);
    }

}
