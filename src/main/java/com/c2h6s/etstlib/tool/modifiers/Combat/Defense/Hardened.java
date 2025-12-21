package com.c2h6s.etstlib.tool.modifiers.Combat.Defense;

import com.c2h6s.etstlib.register.EtSTLibHooks;
import com.c2h6s.etstlib.register.EtSTLibModifier;
import com.c2h6s.etstlib.tool.hooks.IndividualProtectionModifierHook;
import com.c2h6s.etstlib.tool.modifiers.base.EtSTBaseModifier;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.ModifyDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.build.ToolStatsModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

public class Hardened extends EtSTBaseModifier implements ToolStatsModifierHook , IndividualProtectionModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, EtSTLibHooks.INDIVIDUAL_PROTECTION, ModifierHooks.TOOL_STATS);
        hookBuilder.addModule(EtSTLibModifier.indiProtectionModule);
    }

    @Override
    public void addToolStats(IToolContext context, ModifierEntry modifierEntry, ModifierStatsBuilder builder) {
        ToolStats.DURABILITY.percent(builder,0.05*modifierEntry.getLevel());
    }

    @Override
    public float getIndividualProtectionModifier(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, DamageSource source, float modifierValue) {
        if (source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) return modifierValue;
        return modifierValue + 1.25f*modifier.getLevel();
    }

    @Override
    public float getProtectionModifierForDisplay(IToolStackView tool, ModifierEntry modifier, Player player, float value) {
        return value + 1.25f*modifier.getLevel();
    }

    @Override
    public Component getTranslationName(IToolStackView tool, ModifierEntry modifier, Player player) {
        return getDisplayName();
    }
}
