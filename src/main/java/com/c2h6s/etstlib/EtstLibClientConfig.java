package com.c2h6s.etstlib;

import net.minecraftforge.common.ForgeConfigSpec;

public class EtstLibClientConfig {
    public static final ForgeConfigSpec.Builder ToolBuilder = new ForgeConfigSpec.Builder()
            .comment("工匠砧显示的配置")
            .push("工匠砧显示");

    public static final ForgeConfigSpec.BooleanValue shouldRendererArmorStand = ToolBuilder.comment("是否隐藏工匠砧界面的盔甲架")
            .define("RendererArmorStand",true);

    public static final ForgeConfigSpec ClientConfig = ToolBuilder.pop().build();
}
