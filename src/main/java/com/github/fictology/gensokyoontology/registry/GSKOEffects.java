//package com.github.fictology.gensokyoontology.registry;
//
//import com.github.fictology.gensokyoontology.GensokyoOntology;
//import net.minecraft.core.Holder;
//import net.minecraft.core.registries.Registries;
//import net.minecraft.world.effect.MobEffect;
//import net.minecraft.world.effect.MobEffectCategory;
//import net.neoforged.neoforge.registries.DeferredRegister;
//
//import java.util.function.Supplier;
//
//public class GSKOEffects {
//    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, GensokyoOntology.MODID);
//    public static final Holder<MobEffect> HAKUREI_BLESS = EFFECTS.register("hakurei_bless",
//            () -> new HakureiBlessEffect(MobEffectCategory.BENEFICIAL, 0xEF0000));
//
//    public static final Holder<MobEffect> IMPRISON = EFFECTS.register("imprison",
//            () -> new ImprisonEffect(MobEffectCategory.HARMFUL, 0xDEAD00));
//    public static final Holder<MobEffect> VERTIGO = EFFECTS.register("vertigo",
//            () -> new ImprisonEffect(MobEffectCategory.HARMFUL, 0xD122ED));
//    public static final Holder<MobEffect> SILENCE = EFFECTS.register("silence",
//            () -> new SilenceEffect(MobEffectCategory.HARMFUL, 0x511ECE));
//
//    public static Holder<MobEffect> get(Supplier<MobEffect> sup){
//        return Holder.direct(sup.get());
//    }
//}
