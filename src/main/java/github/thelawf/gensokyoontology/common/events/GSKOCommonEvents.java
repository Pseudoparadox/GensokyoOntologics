package github.thelawf.gensokyoontology.common.events;

import github.thelawf.gensokyoontology.GensokyoOntology;
import github.thelawf.gensokyoontology.data.expression.ExpressionType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.RegistryBuilder;

import static github.thelawf.gensokyoontology.core.init.Expressions.EXP_KEY;


@Mod.EventBusSubscriber(modid = GensokyoOntology.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class GSKOCommonEvents {
    // @SubscribeEvent
    public static void onRegisterNewEntry(RegistryEvent.NewRegistry event){
        new RegistryBuilder<ExpressionType>().setName(EXP_KEY.getRegistryName()).setType(ExpressionType.class).setMaxID(256).create();
    }

    public static void onExpressionRegister(final FMLCommonSetupEvent event){

    }
}
