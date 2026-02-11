package com.c2h6s.etstlib.register;

import com.c2h6s.etstlib.EtSTLib;
import com.c2h6s.etstlib.tool.stat.FluidEfficiencyStat;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.tools.stat.FloatToolStat;
import slimeknights.tconstruct.library.tools.stat.IToolStat;
import slimeknights.tconstruct.library.tools.stat.ToolStatId;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import java.util.ArrayList;
import java.util.List;

public class EtSTLibToolStat {
    private static ToolStatId name(String name) {
        return new ToolStatId(EtSTLib.MODID, name);
    }
    private static final List<IToolStat<?>> ETSTLIB_STATS = new ArrayList<>();
    private static FloatToolStat regFloatStat(FloatToolStat stat){
        ETSTLIB_STATS.add(stat);
        return stat;
    }
    public static final FloatToolStat BASIC_AIR_CAPACITY = regFloatStat(new FloatToolStat(name("basic_air_capacity"),
            0xD6D6D6, 0.0F, 0.0F, Integer.MAX_VALUE));
    public static final FloatToolStat MAX_PRESSURE = regFloatStat(new FloatToolStat(name("max_pressure"),
            0xD6D6D6, 0.0F, 0.0F, 2048));
    public static final FloatToolStat RADIATION_PROTECT = regFloatStat(new FloatToolStat(name("radiation_protect"),
            0xA9D699, 0.0F, 0.0F, 1));
    public static final FloatToolStat CHEMICAL_TANK_CAPACITY = regFloatStat(new FloatToolStat(name("chemical_tank_cap"),
            0xA9D699, 0F, 0.0F, Float.MAX_VALUE));
    public static final FloatToolStat CHEMICAL_TANK_COUNT = regFloatStat(new FloatToolStat(name("chemical_tank"),
            0xA9D699, 0F, 0.0F,100));
    public static final FloatToolStat FLUID_EFFICIENCY = regFloatStat(new FloatToolStat(name("fluid_efficiency"),
            0xC8FF5D, 0.0F, Integer.MIN_VALUE, 1.0F));
    public static final FloatToolStat SCALE = regFloatStat(new FloatToolStat(name("scale"),
            0x4BFFAB, 1.0F, 0.0F, 16.0F));
    public static final FloatToolStat RANGE = regFloatStat(new FloatToolStat(name("range"),
            0xA584FF, 12.0F, 0.0F, 128.0F));
    public static final FloatToolStat POWER_MULTIPLIER = regFloatStat(new FloatToolStat(name("power_multiplier"),
            0xFF888A, 1.0F, 0.0F, Integer.MAX_VALUE));
    public static final FloatToolStat PIERCE = regFloatStat(new FloatToolStat(name("pierce"),
            0x7C6CFF,0,0,2048));

    public static void initStats(){
        ETSTLIB_STATS.forEach(ToolStats::register);
    }
}
