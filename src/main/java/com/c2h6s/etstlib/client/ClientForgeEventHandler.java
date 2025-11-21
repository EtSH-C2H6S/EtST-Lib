package com.c2h6s.etstlib.client;

import com.c2h6s.etstlib.EtSTLib;
import com.c2h6s.etstlib.mixinUtil.ITinkerStationScreenMixin;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EtSTLib.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientForgeEventHandler {
    @SubscribeEvent
    public static void postScreenInit(ScreenEvent.Init.Post event){
        if (event.getScreen() instanceof ITinkerStationScreenMixin screen){
            screen.etstlib$postInit();
        }
    }
}
