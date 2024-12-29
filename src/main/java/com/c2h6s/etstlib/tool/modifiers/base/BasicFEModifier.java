package com.c2h6s.etstlib.tool.modifiers.base;

import com.c2h6s.etstlib.register.EtSTLibHooks;
import com.c2h6s.etstlib.register.EtSTLibToolStat;
import com.c2h6s.etstlib.tool.hooks.EnergyStorage.CustomEnergyBarModifierHook;
import com.c2h6s.etstlib.tool.modifiers.capabilityProvider.FEStorageProvider;
import com.c2h6s.etstlib.util.MathUtil;
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

public abstract class BasicFEModifier extends EtSTBaseModifier implements ModifierRemovalHook, TooltipModifierHook, CustomEnergyBarModifierHook,ToolStatsModifierHook {

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this,ModifierHooks.REMOVE, ModifierHooks.TOOLTIP, EtSTLibHooks.ENERGY_BAR,ModifierHooks.TOOL_STATS);
    }

    @Nullable
    @Override
    public Component onRemoved(IToolStackView tool, Modifier modifier) {
        if (FEStorageProvider.getMaxEnergy(tool) <= 0) {
            tool.getPersistentData().remove(FEStorageProvider.LOCATION_ENERGY_STORAGE);
        }
        return null;
    }

    @Override
    public void addTooltip(IToolStackView tool, ModifierEntry modifierEntry, @Nullable Player player, List<Component> list, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        list.add(Component.translatable("tooltip.etstlib.energy_storage").append(":").append(" "+ MathUtil.getEnergyString(FEStorageProvider.getEnergy(tool))+"/"+MathUtil.getEnergyString(FEStorageProvider.getMaxEnergy(tool))).withStyle(Style.EMPTY.withColor(0xFF3000)));
        list.add(Component.translatable("tooltip.etstlib.max_transfer").append(":").append(" "+MathUtil.getEnergyString(FEStorageProvider.getMaxTransfer(tool))).withStyle(Style.EMPTY.withColor(0xFF3000)));
    }

    @Override
    public void addToolStats(IToolContext iToolContext, ModifierEntry modifierEntry, ModifierStatsBuilder modifierStatsBuilder) {
        EtSTLibToolStat.MAX_TRANSFER.add(modifierStatsBuilder,this.getMaxTransfer(modifierEntry));
        EtSTLibToolStat.MAX_ENERGY.add(modifierStatsBuilder,this.getCapacity(modifierEntry));
    }

    public abstract int getCapacity(ModifierEntry modifier);

    public abstract int getMaxTransfer(ModifierEntry modifier);

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
}
