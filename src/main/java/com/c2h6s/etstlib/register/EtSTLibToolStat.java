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
    public static final FloatToolStat BASIC_AIR_CAPACITY = ToolStats.register(new FloatToolStat(name("basic_air_capacity"),
            0xD6D6D6, 0.0F, 0.0F, Integer.MAX_VALUE));
    public static final FloatToolStat MAX_PRESSURE = ToolStats.register(new FloatToolStat(name("max_pressure"),
            0xD6D6D6, 0.0F, 0.0F, 2048));
    public static final FloatToolStat RADIATION_PROTECT = ToolStats.register(new FloatToolStat(name("radiation_protect"),
            0xA9D699, 0.0F, 0.0F, 1));
    public static final FloatToolStat CHEMICAL_TANK_CAPACITY = ToolStats.register(new FloatToolStat(name("chemical_tank_cap"),
            0xA9D699, 0F, 0.0F, Float.MAX_VALUE));
    public static final FloatToolStat CHEMICAL_TANK_COUNT = ToolStats.register(new FloatToolStat(name("chemical_tank"),
            0xA9D699, 0F, 0.0F,100));
}
