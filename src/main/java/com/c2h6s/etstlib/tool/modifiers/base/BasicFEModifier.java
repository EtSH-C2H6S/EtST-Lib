package com.c2h6s.etstlib.tool.modifiers.base;

import com.c2h6s.etstlib.register.EtSTLibHooks;
import com.c2h6s.etstlib.tool.hooks.CustomBarDisplayModifierHook;
import com.c2h6s.etstlib.util.MathUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.phys.Vec2;
import org.jetbrains.annotations.NotNull;
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
import slimeknights.tconstruct.library.tools.capability.ToolEnergyCapability;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;

import java.util.List;

import static slimeknights.tconstruct.library.tools.capability.ToolEnergyCapability.*;

public abstract class BasicFEModifier extends EtSTBaseModifier implements ModifierRemovalHook, TooltipModifierHook,ToolStatsModifierHook, CustomBarDisplayModifierHook, ValidateModifierHook {

    @Override
    public int getPriority() {
        return 25;
    }

    @Override
    protected void registerHooks(ModuleHookMap.@NotNull Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this,ModifierHooks.REMOVE, ModifierHooks.TOOLTIP,ModifierHooks.TOOL_STATS,EtSTLibHooks.CUSTOM_BAR);
    }

    @Nullable
    @Override
    public Component validate(IToolStackView tool, ModifierEntry modifierEntry) {
        checkEnergy(tool);
        return null;
    }

    @Nullable
    @Override
    public Component onRemoved(@NotNull IToolStackView tool, @NotNull Modifier modifier) {
        if (getMaxEnergy(tool) <= 0) {
            tool.getPersistentData().remove(ENERGY_KEY);
        }
        return null;
    }

    @Override
    public void addTooltip(IToolStackView tool, ModifierEntry modifierEntry, @Nullable Player player, List<Component> list, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        Component component = Component.translatable("tooltip.etstlib.energy_storage").append(":").append(" "+ MathUtil.getEnergyString(getEnergy(tool))+"/"+MathUtil.getEnergyString(getMaxEnergy(tool))).withStyle(Style.EMPTY.withColor(0xFF3000));
        list.add(component);
    }

    @Override
    public void addToolStats(IToolContext iToolContext, ModifierEntry modifierEntry, ModifierStatsBuilder modifierStatsBuilder) {
        ToolEnergyCapability.MAX_STAT.add(modifierStatsBuilder,getCapacity(modifierEntry));
    }

    public abstract int getCapacity(ModifierEntry modifier);

    @Override
    public boolean showBar(IToolStackView tool, ModifierEntry entry, int barsHadBeenShown) {
        return getEnergy(tool)>0;
    }

    @Override
    public int getBarRGB(IToolStackView tool, ModifierEntry entry, int barsHadBeenShown) {
        return 0xFFFF6500;
    }

    @Override
    public Vec2 getBarXYSize(IToolStackView tool, ModifierEntry entry, int barsHadBeenShown) {
        int FE = getEnergy(tool);
        int maxStorage = getMaxEnergy(tool);
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
