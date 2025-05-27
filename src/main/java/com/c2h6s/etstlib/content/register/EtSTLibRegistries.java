package com.c2h6s.etstlib.content.register;

import com.c2h6s.etstlib.EtSTLib;
import com.c2h6s.etstlib.content.misc.entityTicker.EntityTicker;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.NewRegistryEvent;
import net.minecraftforge.registries.RegistryBuilder;

@Mod.EventBusSubscriber(modid = EtSTLib.MODID,bus = Mod.EventBusSubscriber.Bus.MOD)
public class EtSTLibRegistries {
    public static final ResourceKey<Registry<EntityTicker>> ENTITY_TICKER = ResourceKey.createRegistryKey(EtSTLib.getResourceLocation("entity_ticker"));
    public static IForgeRegistry<EntityTicker> ENTITY_TICKER_REGISTRY;

    @SubscribeEvent
    public static void onNewRegistry(NewRegistryEvent event){
        RegistryBuilder<EntityTicker> builder = new RegistryBuilder<>();
        builder.setName(ENTITY_TICKER.location()).setDefaultKey(EtSTLib.getResourceLocation("default"));
        event.create(builder,registry -> ENTITY_TICKER_REGISTRY = registry);
    }
}
