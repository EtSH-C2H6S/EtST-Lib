package com.c2h6s.etstlib.tool.modifiers.base;

import com.c2h6s.etstlib.register.EtSTLibHooks;
import com.c2h6s.etstlib.register.EtSTLibToolStat;
import com.c2h6s.etstlib.tool.hooks.CustomBarDisplayModifierHook;
import com.c2h6s.etstlib.tool.modifiers.capabilityProvider.FEStorageProvider;
import com.c2h6s.etstlib.util.MathUtil;
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

import com.c2h6s.etstlib.tool.modifiers.capabilityProvider.FEStorageProvider;

import java.util.ArrayList;
import java.util.List;

public abstract class BasicFEModifier extends EtSTBaseModifier implements ModifierRemovalHook, TooltipModifierHook,ToolStatsModifierHook, CustomBarDisplayModifierHook, ValidateModifierHook {

    @Override
    public int getPriority() {
        return 25;
    }

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this,ModifierHooks.REMOVE, ModifierHooks.TOOLTIP,ModifierHooks.TOOL_STATS,EtSTLibHooks.CUSTOM_BAR);
    }

    @Nullable
    @Override
    public Component validate(IToolStackView tool, ModifierEntry modifierEntry) {
        if (FEStorageProvider.getEnergy(tool)>FEStorageProvider.getMaxEnergy(tool)){
            FEStorageProvider.setEnergy(tool,FEStorageProvider.getMaxEnergy(tool));
        }
        return null;
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
        List<Component> ls = new ArrayList<>(List.of());
        ls.add(Component.translatable("tooltip.etstlib.energy_storage").append(":").append(" "+ MathUtil.getEnergyString(FEStorageProvider.getEnergy(tool))+"/"+MathUtil.getEnergyString(FEStorageProvider.getMaxEnergy(tool))).withStyle(Style.EMPTY.withColor(0xFF3000)));
        ls.add(Component.translatable("tooltip.etstlib.max_transfer").append(":").append(" "+MathUtil.getEnergyString(FEStorageProvider.getMaxTransfer(tool))).withStyle(Style.EMPTY.withColor(0xFF3000)));
        for (Component component:ls){
            if (!list.contains(component)&&player!=null){
                list.add(component);
            }
        }
    }

    @Override
    public void addToolStats(IToolContext iToolContext, ModifierEntry modifierEntry, ModifierStatsBuilder modifierStatsBuilder) {
        EtSTLibToolStat.MAX_TRANSFER.add(modifierStatsBuilder,this.getMaxTransfer(modifierEntry));
        EtSTLibToolStat.MAX_ENERGY.add(modifierStatsBuilder,this.getCapacity(modifierEntry));
    }

    public abstract int getCapacity(ModifierEntry modifier);

    public abstract int getMaxTransfer(ModifierEntry modifier);

    @Override
    public boolean showBar(IToolStackView tool, ModifierEntry entry, int barsHadBeenShown) {
        return FEStorageProvider.getEnergy(tool)>0;
    }

    @Override
    public int getBarRGB(IToolStackView tool, ModifierEntry entry, int barsHadBeenShown) {
        return 0xFFFF6500;
    }

    @Override
    public Vec2 getBarXYSize(IToolStackView tool, ModifierEntry entry, int barsHadBeenShown) {
        int FE = FEStorageProvider.getEnergy(tool);
        int maxStorage = FEStorageProvider.getMaxEnergy(tool);
        if (maxStorage>0) {
            return new Vec2(Math.min(13, 13 * FE / maxStorage), 1);
        }
        return new Vec2(0,0);
    }

    @Override
    public String barId(IToolStackView tool, ModifierEntry entry, int barsHadBeenShown) {
        return "etstlib:fe_bar";
    }
}
