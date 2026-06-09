package com.github.fictology.gensokyoontology.data;

import net.minecraft.util.Mth;

import java.util.Random;

/**
 * 用于那些需要保底随机数的数据
 */
public record FakeRandomRoll(float probability, int rollCount, boolean jackpot) {
    public static FakeRandomRoll of(float probability){
        return new FakeRandomRoll(Mth.clamp(probability, 0F, 1F), 0, false);
    }

    /**
     * 是否触发保底机制
     */
    public boolean canMustTriggerJackpot(){
        return this.rollCount() >= 1F / this.probability();
    }

    public FakeRandomRoll roll(){
        var rand = new Random();
        var getJackpot = new FakeRandomRoll(this.probability(), this.rollCount(), true);
        var noJackpot = new FakeRandomRoll(this.probability(), this.rollCount() + 1, true);
        return rand.nextFloat() <= this.probability()
                ? getJackpot : this.canMustTriggerJackpot()
                ? getJackpot : noJackpot ;
    }
}
