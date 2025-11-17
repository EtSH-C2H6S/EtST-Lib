package com.c2h6s.etstlib;

import net.minecraftforge.common.ForgeConfigSpec;

public class EtSTLibConfig {
    public static final ForgeConfigSpec.Builder common = new ForgeConfigSpec.Builder()
            .comment("一些常规的配置")
            .push("常规");

    public static final ForgeConfigSpec.BooleanValue ALLOW_TICKER_ON_PLAYER = common
            .comment("是否允许EntityTicker作用于玩家，由于可能导致问题所以默认false")
            .define("allow_entity_ticker_on_player",false);
    public static final ForgeConfigSpec.BooleanValue ALLOW_PROJECTILE_TICK_INGROUND = common.comment("是否允许弹射物在命中方块后依然通过ProjectileTickModifierHook遍历全词条，默认true")
            .define("allow_projectile_tick_inground",false);
    public static final ForgeConfigSpec.BooleanValue ALLOW_TOOL_UUID_ON_COMMON_TOOL = common
            .comment("是否允许一般的工具在被合成时拥有UUID，暂时默认是，后续版本可能会改为否")
            .define("allow_common_tool_having_uuid",true);
    public static final ForgeConfigSpec.BooleanValue ALLOW_TOOL_UUID_ON_STACKABLE_TOOL = common
            .comment("是否允许一般的可堆叠工具在被合成时拥有UUID，默认否（仅在allow_common_tool_having_uuid=true时有效）")
            .define("allow_stackable_tool_having_uuid",false);

    public static final ForgeConfigSpec COMMON_CONFIG = common.pop().build();

}
