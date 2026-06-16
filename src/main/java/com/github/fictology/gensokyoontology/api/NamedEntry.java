package com.github.fictology.gensokyoontology.api;

import com.github.fictology.gensokyoontology.data.Incident;
import com.mojang.datafixers.util.Pair;

public class NamedEntry<A, B> {
    public Pair<String, A> aPair;
    public Pair<String, B> bPair;

    public NamedEntry(Pair<String, A> aPair, Pair<String, B> bPair) {
        this.aPair = aPair;
        this.bPair = bPair;
    }

    public static <A, B> NamedEntry<A, B> of(String nameOfA, A a, String nameOfB, B b){
        return new NamedEntry<>(Pair.of(nameOfA, a), Pair.of(nameOfB, b));
    }

    public Object get(String name){
        return aPair.getFirst().equals(name) ? aPair.getSecond() : bPair.getFirst().equals(name) ? bPair.getSecond() : null;
    }

    public void set(A a, B b){
        this.aPair = Pair.of(this.aPair.getFirst(), a);
        this.bPair = Pair.of(this.bPair.getFirst(), b);
    }

    @SuppressWarnings("unchecked")
    public <C> void set(String name, C value){
        if (this.aPair.getFirst().equals(name)){
            try {
                this.aPair = (Pair<String, A>) Pair.of(name, value);
            }catch (Exception ignored){}
        }
        if (this.bPair.getFirst().equals(name)){
            try {
                this.bPair = (Pair<String, B>) Pair.of(name, value);
            }catch (Exception ignored){}
        }
    }

    public void rename(String oldName, String newName){
        if (this.aPair.getFirst().equals(oldName)) {
            this.aPair = Pair.of(newName, this.aPair.getSecond());
        }
        if (this.bPair.getFirst().equals(oldName)) {
            this.bPair = Pair.of(newName, this.bPair.getSecond());
        }
    }

    public static final NamedEntry<Incident, Boolean> SCARLET_MIST = of("incident", Incident.SCARLET_MIST, "isTriggered", true);
    public static final NamedEntry<Incident, Boolean> REALITY = of("incident", Incident.REALITY, "isTriggered", false);
}
