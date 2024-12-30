package com.c2h6s.etstlib.tool.hooks;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Collection;

public interface EffectApplicableModifierHook {
    boolean isApplicable(IToolStackView tool, ModifierEntry entry, EquipmentSlot slot, MobEffectInstance instance, boolean notApplicable);
    record AllMerger(Collection<EffectApplicableModifierHook> modules) implements EffectApplicableModifierHook {
        @Override
        public boolean isApplicable(IToolStackView tool, ModifierEntry entry, EquipmentSlot slot, MobEffectInstance instance, boolean notApplicable) {
            for (EffectApplicableModifierHook module:this.modules){
                notApplicable =module.isApplicable(tool,entry,slot,instance,notApplicable);
                if (notApplicable) break;
            }
            return notApplicable;
        }
    }
}
