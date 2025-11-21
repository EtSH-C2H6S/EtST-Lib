package com.c2h6s.etstlib.client;

import com.c2h6s.etstlib.EtSTLib;
import com.c2h6s.etstlib.client.gui.item.CustomBarDecoration;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterItemDecorationsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import slimeknights.tconstruct.library.tools.item.IModifiable;

@Mod.EventBusSubscriber(modid = EtSTLib.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientModEventHandler {
    @SubscribeEvent
    public static void registerItemDecoration(RegisterItemDecorationsEvent event){
        ForgeRegistries.ITEMS.getValues().stream().filter(item -> item instanceof IModifiable).forEach(item -> {
            event.register(item,new CustomBarDecoration());
        });
    }
}
