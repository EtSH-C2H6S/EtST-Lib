package com.c2h6s.etstlib.event.eventHandler;

import com.c2h6s.etstlib.EtSTLib;
import com.c2h6s.etstlib.register.EtSTLibEffects;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EtSTLib.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LivingEvents {
    @SubscribeEvent
    public static void onLivingHeal(LivingHealEvent event){
        var livingEntity = event.getEntity();
        var amount = event.getAmount();
        if (livingEntity.hasEffect(EtSTLibEffects.FATAL_TRAUMA.get())) event.setCanceled(true);
    }
}
