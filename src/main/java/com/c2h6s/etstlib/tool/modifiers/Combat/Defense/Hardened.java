package com.c2h6s.etstlib.tool.modifiers.Combat.Defense;

import com.c2h6s.etstlib.tool.modifiers.base.EtSTBaseModifier;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.hook.armor.ModifyDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.build.ToolStatsModifierHook;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

public class Hardened extends EtSTBaseModifier implements ToolStatsModifierHook , ModifyDamageModifierHook {
    @Override
    public void addToolStats(IToolContext context, ModifierEntry modifierEntry, ModifierStatsBuilder builder) {
        ToolStats.DURABILITY.percent(builder,0.1*modifierEntry.getLevel());
    }


    @Override
    public float modifyDamageTaken(IToolStackView iToolStackView, ModifierEntry modifierEntry, EquipmentContext equipmentContext, EquipmentSlot equipmentSlot, DamageSource damageSource, float damage, boolean b) {
        if (damageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) return damage;
        return damage*(1-0.1f*modifierEntry.getLevel());
    }
}
