package com.c2h6s.etstlib.tool.modifiers.base;

import com.c2h6s.etstlib.register.EtSTLibHooks;
import com.c2h6s.etstlib.register.EtSTLibToolStat;
import com.c2h6s.etstlib.tool.hooks.AirStorage.CustomPressureBarModifierHook;
import com.c2h6s.etstlib.tool.modifiers.capabilityProvider.PnCIntegration.AirStorageProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ModifierRemovalHook;
import slimeknights.tconstruct.library.modifiers.hook.build.ToolStatsModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;

import java.util.List;

public abstract class BasicPressurizableModifier extends EtSTBaseModifier implements ModifierRemovalHook, TooltipModifierHook , CustomPressureBarModifierHook, ToolStatsModifierHook {

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this,ModifierHooks.REMOVE, ModifierHooks.TOOLTIP, EtSTLibHooks.PRESSURE_BAR,ModifierHooks.TOOL_STATS);
    }

    @Nullable
    @Override
    public Component onRemoved(IToolStackView tool, Modifier modifier) {
        if (AirStorageProvider.getMaxPressure(tool)<=0) {
            tool.getPersistentData().remove(AirStorageProvider.LOCATION_AIR_STORAGE);
        }
        return null;
    }




    @Override
    public void addTooltip(IToolStackView tool, ModifierEntry modifierEntry, @Nullable Player player, List<Component> list, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        list.add(Component.translatable("tooltip.etstlib.base_volume").append(":").append(" "+AirStorageProvider.getBaseVolume(tool)+"mL").withStyle(Style.EMPTY.withColor(0xBABABA)));
        list.add(Component.translatable("tooltip.etstlib.air").append(":").append(" "+AirStorageProvider.getAir(tool)+"mL").withStyle(Style.EMPTY.withColor(0xBABABA)));
        list.add(Component.translatable("tooltip.etstlib.max_pressure").append(":").append(" "+String.format("%.1f",(float)AirStorageProvider.getAir(tool)/AirStorageProvider.getBaseVolume(tool))+"/"+String.format("%.1f",AirStorageProvider.getMaxPressure(tool))+"bar").withStyle(Style.EMPTY.withColor(0xBABABA)));
    }

    @Override
    public void addToolStats(IToolContext iToolContext, ModifierEntry modifierEntry, ModifierStatsBuilder modifierStatsBuilder) {
        EtSTLibToolStat.MAX_PRESSURE.add(modifierStatsBuilder,this.getMaxPressure(modifierEntry));
        EtSTLibToolStat.BASIC_AIR_CAPACITY.add(modifierStatsBuilder,this.getBaseVolume(modifierEntry));
    }

    public abstract int getBaseVolume(ModifierEntry modifier) ;

    public abstract float getMaxPressure(ModifierEntry modifierEntry) ;



    @Override
    public boolean showPressureBar(IToolStackView tool, ModifierEntry entry) {
        return AirStorageProvider.getAir(tool)>0;
    }

    @Override
    public int getPressureBarWidth(IToolStackView tool, ModifierEntry entry) {
        float Pressure =AirStorageProvider.getPressure(tool);
        float maxPressure =AirStorageProvider.getMaxPressure(tool);
        return (int)Math.min ((Pressure*13/maxPressure)+1,13);
    }
}
