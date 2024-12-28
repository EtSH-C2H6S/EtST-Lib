package com.c2h6s.etstlib.tool.modifiers.base;

import com.c2h6s.etstlib.register.EtSTLibHooks;
import com.c2h6s.etstlib.register.EtSTLibToolStat;
import com.c2h6s.etstlib.tool.hooks.EnergyStorage.ICustomEnergyBarModifierHook;
import com.c2h6s.etstlib.tool.modifiers.capabilityProvider.FEStorageProvider;
import com.c2h6s.etstlib.util.MathUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ModifierRemovalHook;
import slimeknights.tconstruct.library.modifiers.hook.build.ToolStatsModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.build.VolatileDataModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;

import java.util.List;

public class BasicFEModifier extends EtSTBaseModifier implements VolatileDataModifierHook, ModifierRemovalHook, TooltipModifierHook, ICustomEnergyBarModifierHook , ToolStatsModifierHook {

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.VOLATILE_DATA,ModifierHooks.REMOVE, ModifierHooks.TOOLTIP, EtSTLibHooks.ENERGY_BAR,ModifierHooks.TOOL_STATS);
    }

    @Nullable
    @Override
    public Component onRemoved(IToolStackView tool, Modifier modifier) {
        if (tool.getVolatileData().getInt(FEStorageProvider.LOCATION_MAX_ENERGY) == 0) {
            tool.getPersistentData().remove(FEStorageProvider.LOCATION_ENERGY_STORAGE);
        }
        return null;
    }

    @Override
    public void addVolatileData(IToolContext iToolContext, ModifierEntry modifierEntry, ModDataNBT volatileData) {
        if (volatileData.contains(FEStorageProvider.LOCATION_MAX_ENERGY, 3)) {
            volatileData.putInt(FEStorageProvider.LOCATION_MAX_ENERGY, volatileData.getInt(FEStorageProvider.LOCATION_MAX_ENERGY) + this.getCapacity(iToolContext, modifierEntry, volatileData) * modifierEntry.getLevel());
        } else {
            volatileData.putInt(FEStorageProvider.LOCATION_MAX_ENERGY, this.getCapacity(iToolContext, modifierEntry, volatileData) * modifierEntry.getLevel());
        }
    }

    @Override
    public void addTooltip(IToolStackView tool, ModifierEntry modifierEntry, @Nullable Player player, List<Component> list, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        list.add(Component.translatable("tooltip.etstlib.energy_storage").append(":").append(" "+ MathUtil.getEnergyString(FEStorageProvider.getEnergy(tool))+"/"+MathUtil.getEnergyString(FEStorageProvider.getMaxEnergy(tool))));
        list.add(Component.translatable("tooltip.etstlib.max_transfer").append(":").append(" "+MathUtil.getEnergyString(FEStorageProvider.getMaxEnergy(tool))));
    }

    public int getCapacity(IToolContext context, ModifierEntry modifier, ModDataNBT volatileData) {
        return 10000;
    }

    @Override
    public boolean showEnergyBar(IToolStackView tool, ModifierEntry entry) {
        return FEStorageProvider.getEnergy(tool)>0;
    }

    @Override
    public int getEnergyBarWidth(IToolStackView tool, ModifierEntry entry) {
        float FE = FEStorageProvider.getEnergy(tool);
        float maxEnergy =FEStorageProvider.getMaxEnergy(tool);
        return (int)Math.min ( (FE *13/ maxEnergy)+1,13);
    }

    @Override
    public void addToolStats(IToolContext iToolContext, ModifierEntry modifierEntry, ModifierStatsBuilder modifierStatsBuilder) {
        EtSTLibToolStat.MAX_TRANSFER.add(modifierStatsBuilder,modifierEntry.getLevel()*1000);
    }
}
