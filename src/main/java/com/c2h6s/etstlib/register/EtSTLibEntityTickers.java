package com.c2h6s.etstlib.register;

import com.c2h6s.etstlib.EtSTLib;
import com.c2h6s.etstlib.content.misc.entityTicker.EntityTicker;
import com.c2h6s.etstlib.content.misc.entityTicker.tickers.Freezing;
import com.c2h6s.etstlib.content.register.EtSTLibRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class EtSTLibEntityTickers {
    public static final DeferredRegister<EntityTicker> ENTITY_TICKERS = DeferredRegister.create(EtSTLibRegistries.ENTITY_TICKER, EtSTLib.MODID);

    public static final RegistryObject<Freezing> FREEZING = ENTITY_TICKERS.register("freezing",Freezing::new);
}
