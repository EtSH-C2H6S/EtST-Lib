package com.c2h6s.etstlib;

import net.minecraftforge.common.ForgeConfigSpec;

public class EtstLibClientConfig {
    public static final ForgeConfigSpec.Builder ToolBuilder = new ForgeConfigSpec.Builder()
            .comment("工匠砧显示的配置")
            .push("工匠砧显示");

    public static final ForgeConfigSpec.BooleanValue notRenderArmorStand = ToolBuilder.comment("是否隐藏工匠砧界面的盔甲架，默认false")
            .define("DisableRenderArmorStand",false);
    public static final ForgeConfigSpec.BooleanValue toggleScrollingBar = ToolBuilder.comment("是否在工具太多时为工匠站添加滚动条，默认false")
            .define("enableScrollingBar",false);
    public static final ForgeConfigSpec.IntValue scrollingRollsThreshold = ToolBuilder.comment("在工具有多少行后才添加滚动条，默认8")
            .defineInRange("ScrollingRollsThreshold",8,0,Integer.MAX_VALUE);

    public static final ForgeConfigSpec ClientConfig = ToolBuilder.pop().build();
}
