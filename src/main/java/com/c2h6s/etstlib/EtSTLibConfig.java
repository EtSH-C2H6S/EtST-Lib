package com.c2h6s.etstlib;

import net.minecraftforge.common.ForgeConfigSpec;

public class EtSTLibConfig {
    public static final ForgeConfigSpec.Builder common = new ForgeConfigSpec.Builder()
            .comment("一些常规的配置")
            .push("常规");

    public static final ForgeConfigSpec.BooleanValue ALLOW_TICKER_ON_PLAYER = common
            .comment("是否允许EntityTicker作用于玩家，由于可能导致问题所以默认false")
            .define("allow_entity_ticker_on_player",false);

    public static final ForgeConfigSpec COMMON_CONFIG = common.pop().build();

}
