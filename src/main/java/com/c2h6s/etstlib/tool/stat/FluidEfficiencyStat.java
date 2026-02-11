package com.c2h6s.etstlib.tool.stat;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.data.predicate.IJsonPredicate;
import slimeknights.tconstruct.library.tools.stat.FloatToolStat;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStatId;

public class FluidEfficiencyStat extends FloatToolStat {
    public FluidEfficiencyStat(ToolStatId name, int color, float defaultValue, float minValue, float maxValue, IJsonPredicate<Item> items) {
        super(name, color, defaultValue, minValue, maxValue, items);
    }

    public FluidEfficiencyStat(ToolStatId name, int color, float defaultValue, float minValue, float maxValue, @Nullable TagKey<Item> tag) {
        super(name, color, defaultValue, minValue, maxValue, tag);
    }

    public FluidEfficiencyStat(ToolStatId name, int color, float defaultValue, float minValue, float maxValue) {
        super(name, color, defaultValue, minValue, maxValue);
    }

    @Override
    public EfficiencyFloatBuilder makeBuilder() {
        return new EfficiencyFloatBuilder(getDefaultValue());
    }

    @Override
    public void update(ModifierStatsBuilder builder, Float value) {
        builder.<EfficiencyFloatBuilder>updateStat(this, b -> {
            b.add += value;
            b.base = 0;
        });
    }

    @Override
    public void add(ModifierStatsBuilder builder, double value) {
        builder.<EfficiencyFloatBuilder>updateStat(this, b -> b.add += value);
    }

    @Override
    public void percent(ModifierStatsBuilder builder, double factor) {
        builder.<EfficiencyFloatBuilder>updateStat(this, b -> b.percent += factor);
    }

    @Override
    public void multiply(ModifierStatsBuilder builder, double factor) {
        builder.<EfficiencyFloatBuilder>updateStat(this, b -> b.multiply *= factor);
    }

    @Override
    public void multiplyAll(ModifierStatsBuilder builder, double factor) {
        builder.<EfficiencyFloatBuilder>updateStat(this, b -> b.multiply *= factor);
        builder.multiplier(this, factor);
    }

    @Override
    public Float build(ModifierStatsBuilder parent, Object builderObj) {
        EfficiencyFloatBuilder builder = (EfficiencyFloatBuilder)builderObj;
        var baseValue = builder.base+builder.add;
        var mulBase = baseValue-1;
        return (baseValue+(mulBase*builder.percent))*builder.multiply;
    }

    public static class EfficiencyFloatBuilder extends FloatBuilder{
        public EfficiencyFloatBuilder(float base) {
            super(base);
        }
        private float base;
        private float add = 0;
        private float percent = 0;
        private float multiply = 1;
    }
}
