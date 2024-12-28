package com.c2h6s.etstlib.tool.modifiers.base;

import com.c2h6s.etstlib.register.EtSTLibHooks;
import com.c2h6s.etstlib.tool.hooks.AirStorage.ICustomPressureBarModifierHook;
import com.c2h6s.etstlib.tool.modifiers.capabilityProvider.PnCIntegration.AirStorageProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ModifierRemovalHook;
import slimeknights.tconstruct.library.modifiers.hook.build.VolatileDataModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;

import java.util.List;

public class BasicPressurizableModifier extends EtSTBaseModifier implements VolatileDataModifierHook, ModifierRemovalHook, TooltipModifierHook , ICustomPressureBarModifierHook {

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.VOLATILE_DATA,ModifierHooks.REMOVE, ModifierHooks.TOOLTIP, EtSTLibHooks.PRESSURE_BAR);
    }

    @Nullable
    @Override
    public Component onRemoved(IToolStackView tool, Modifier modifier) {
        if (tool.getVolatileData().getInt(AirStorageProvider.LOCATION_MAX_PRESSURE) == 0) {
            tool.getPersistentData().remove(AirStorageProvider.LOCATION_AIR_STORAGE);
        }
        return null;
    }

    @Override
    public void addVolatileData(IToolContext iToolContext, ModifierEntry modifierEntry, ModDataNBT modDataNBT) {
        if (modDataNBT.contains(AirStorageProvider.LOCATION_BASE_VOLUME,3)){
            modDataNBT.putInt(AirStorageProvider.LOCATION_BASE_VOLUME,modDataNBT.getInt(AirStorageProvider.LOCATION_BASE_VOLUME)+this.getBaseVolume(modifierEntry));
        }else {
            modDataNBT.putInt(AirStorageProvider.LOCATION_BASE_VOLUME,this.getBaseVolume(modifierEntry));
        }
        if (modDataNBT.contains(AirStorageProvider.LOCATION_MAX_PRESSURE,3)){
            modDataNBT.putFloat(AirStorageProvider.LOCATION_MAX_PRESSURE,modDataNBT.getInt(AirStorageProvider.LOCATION_MAX_PRESSURE)+this.getMaxPressure(modifierEntry));
        }else {
            modDataNBT.putFloat(AirStorageProvider.LOCATION_MAX_PRESSURE,this.getMaxPressure(modifierEntry));
        }
    }



    @Override
    public void addTooltip(IToolStackView tool, ModifierEntry modifierEntry, @Nullable Player player, List<Component> list, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        list.add(Component.translatable("tooltip.etstlib.base_volume").append(":").append(" "+tool.getVolatileData().getInt(AirStorageProvider.LOCATION_BASE_VOLUME)+"mL"));
        list.add(Component.translatable("tooltip.etstlib.air").append(":").append(" "+tool.getPersistentData().getInt(AirStorageProvider.LOCATION_AIR_STORAGE)+"mL"));
        list.add(Component.translatable("tooltip.etstlib.max_pressure").append(":").append(" "+String.format("%.1f",(float)tool.getPersistentData().getInt(AirStorageProvider.LOCATION_AIR_STORAGE)/tool.getVolatileData().getInt(AirStorageProvider.LOCATION_BASE_VOLUME))+"/"+String.format("%.1f",tool.getVolatileData().getFloat(AirStorageProvider.LOCATION_MAX_PRESSURE))+"bar"));
    }

    public int getBaseVolume(ModifierEntry modifier) {
        return 3000*modifier.getLevel();
    }

    public float getMaxPressure(ModifierEntry modifierEntry) {
        return 10*modifierEntry.getLevel();
    }



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
