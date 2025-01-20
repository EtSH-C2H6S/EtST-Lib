package com.c2h6s.etstlib.register;

import com.c2h6s.etstlib.EtSTLib;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.tools.stat.FloatToolStat;
import slimeknights.tconstruct.library.tools.stat.ToolStatId;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

public class EtSTLibToolStat {
    private static ToolStatId name(String name) {
        return new ToolStatId(EtSTLib.MODID, name);
    }
    public static final FloatToolStat MAX_ENERGY = ToolStats.register(new FloatToolStat(name("max_energy"), -3135232, 0.0F, 0.0F, Float.MAX_VALUE));
    public static final FloatToolStat MAX_TRANSFER = ToolStats.register(new FloatToolStat(name("max_transfer"), -3135232, 0.0F, 0.0F, Float.MAX_VALUE));

    public static final FloatToolStat BASIC_AIR_CAPACITY = ToolStats.register(new FloatToolStat(name("basic_air_capacity"), -3135232, 0.0F, 0.0F, Float.MAX_VALUE));
    public static final FloatToolStat MAX_PRESSURE = ToolStats.register(new FloatToolStat(name("max_pressure"), -3135232, 0.0F, 0.0F, 25));

    public static final FloatToolStat RADIATION_PROTECT = ToolStats.register(new FloatToolStat(name("radiation_protect"), -3135232, 0.0F, 0.0F, 1));
}
