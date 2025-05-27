package com.c2h6s.etstlib.event.eventHandler;

import com.c2h6s.etstlib.EtSTLib;
import com.c2h6s.etstlib.content.misc.entityTicker.EntityTickerManager;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EtSTLib.MODID,bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ServerEvent {
    @SubscribeEvent
    public static void onServerStopping(ServerStoppingEvent event){
        EntityTickerManager.saveAll();
    }
}
