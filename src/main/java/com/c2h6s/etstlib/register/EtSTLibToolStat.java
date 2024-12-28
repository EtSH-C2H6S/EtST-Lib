package com.c2h6s.etstlib.register;

import com.c2h6s.etstlib.EtSTLib;
import slimeknights.tconstruct.library.tools.stat.FloatToolStat;
import slimeknights.tconstruct.library.tools.stat.ToolStatId;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

public class EtSTLibToolStat {
    private static ToolStatId name(String name) {
        return new ToolStatId(EtSTLib.MODID, name);
    }

    public static final FloatToolStat ENERGY_STORE = ToolStats.register(new FloatToolStat(name("energy_capacity"), -3135232, 0.0F, 0.0F, Float.MAX_VALUE));
    public static final FloatToolStat MAX_TRANSFER = ToolStats.register(new FloatToolStat(name("max_transfer"), -3135232, 0.0F, 0.0F, Float.MAX_VALUE));
}
