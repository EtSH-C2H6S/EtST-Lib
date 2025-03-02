package com.c2h6s.etstlib.tool.modifiers.base;

import com.c2h6s.etstlib.register.EtSTLibHooks;
import com.c2h6s.etstlib.register.EtSTLibToolStat;
import com.c2h6s.etstlib.tool.hooks.CustomBarDisplayModifierHook;
import com.c2h6s.etstlib.tool.modifiers.capabilityProvider.PnCIntegration.AirStorageProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.phys.Vec2;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ModifierRemovalHook;
import slimeknights.tconstruct.library.modifiers.hook.build.ToolStatsModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.build.ValidateModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;

import static com.c2h6s.etstlib.tool.modifiers.capabilityProvider.PnCIntegration.AirStorageProvider.*;

import java.util.ArrayList;
import java.util.List;

public abstract class BasicPressurizableModifier extends EtSTBaseModifier implements ModifierRemovalHook, TooltipModifierHook , ToolStatsModifierHook, CustomBarDisplayModifierHook, ValidateModifierHook {
    @Override
    public int getPriority() {
        return 20;
    }

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this,ModifierHooks.REMOVE, ModifierHooks.TOOLTIP,ModifierHooks.TOOL_STATS,EtSTLibHooks.CUSTOM_BAR,ModifierHooks.VALIDATE);
    }

    @Nullable
    @Override
    public Component onRemoved(IToolStackView tool, Modifier modifier) {
        if (AirStorageProvider.getMaxPressure(tool)<=0) {
            tool.getPersistentData().remove(AirStorageProvider.LOCATION_AIR_STORAGE);
        }
        return null;
    }

    @Nullable
    @Override
    public Component validate(IToolStackView tool, ModifierEntry modifierEntry) {
        if (getPressure(tool)>AirStorageProvider.getMaxPressure(tool)){
            AirStorageProvider.setAir(tool,getMaxVolume(tool));
        }
        return null;
    }
    @Override
    public void addTooltip(IToolStackView tool, ModifierEntry modifierEntry, @Nullable Player player, List<Component> list, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        List<Component> ls = new ArrayList<>(List.of());
        ls.add(Component.translatable("tooltip.etstlib.base_volume").append(":").append(" "+AirStorageProvider.getBaseVolume(tool)+"mL").withStyle(Style.EMPTY.withColor(0xBABABA)));
        ls.add(Component.translatable("tooltip.etstlib.air").append(":").append(" "+AirStorageProvider.getAir(tool)+"mL").withStyle(Style.EMPTY.withColor(0xBABABA)));
        ls.add(Component.translatable("tooltip.etstlib.max_pressure").append(":").append(" "+String.format("%.1f",(float)AirStorageProvider.getAir(tool)/AirStorageProvider.getBaseVolume(tool))+"/"+String.format("%.1f",AirStorageProvider.getMaxPressure(tool))+"bar").withStyle(Style.EMPTY.withColor(0xBABABA)));
        for (Component component:ls){
            if (!list.contains(component)&&player!=null){
                list.add(component);
            }
        }
    }

    @Override
    public void addToolStats(IToolContext iToolContext, ModifierEntry modifierEntry, ModifierStatsBuilder modifierStatsBuilder) {
        EtSTLibToolStat.MAX_PRESSURE.add(modifierStatsBuilder,this.getMaxPressure(modifierEntry));
        EtSTLibToolStat.BASIC_AIR_CAPACITY.add(modifierStatsBuilder,this.getBaseVolume(modifierEntry));
    }

    public abstract int getBaseVolume(ModifierEntry modifier) ;

    public abstract float getMaxPressure(ModifierEntry modifierEntry) ;

    @Override
    public boolean showBar(IToolStackView tool, ModifierEntry entry, int barsHadBeenShown) {
        return AirStorageProvider.getPressure(tool)>0;
    }

    @Override
    public int getBarRGB(IToolStackView tool, ModifierEntry entry, int barsHadBeenShown) {
        return 0xffffffff;
    }

    @Override
    public Vec2 getBarXYSize(IToolStackView tool, ModifierEntry entry, int barsHadBeenShown) {
        float Pressure = AirStorageProvider.getPressure(tool);
        float maxPressure = AirStorageProvider.getMaxPressure(tool);
        if (maxPressure>0) {
            return new Vec2(Math.min(13, 13 * Pressure / maxPressure), 1);
        }
        return new Vec2(0,0);
    }

    @Override
    public String barId(IToolStackView tool, ModifierEntry entry, int barsHadBeenShown) {
        return "etstlib:pressure_bar";
    }
}
