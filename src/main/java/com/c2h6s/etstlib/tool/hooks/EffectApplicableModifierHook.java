package com.c2h6s.etstlib.tool.hooks;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Collection;

public interface EffectApplicableModifierHook {
    Boolean isApplicable(IToolStackView tool, ModifierEntry entry, EquipmentSlot slot, MobEffectInstance instance, Boolean notApplicable);
    record FirstMerger(Collection<EffectApplicableModifierHook> modules) implements EffectApplicableModifierHook {
        @Override
        public Boolean isApplicable(IToolStackView tool, ModifierEntry entry, EquipmentSlot slot, MobEffectInstance instance, Boolean notApplicable) {
            for (EffectApplicableModifierHook module:this.modules){
                Boolean NotApplicable =module.isApplicable(tool,entry,slot,instance,notApplicable);
                if (NotApplicable!=null) return NotApplicable;
            }
            return notApplicable;
        }
    }
}
