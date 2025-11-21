package com.c2h6s.etstlib.tool.hooks;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Collection;

public interface CriticalAttackModifierHook {
    boolean setCritical(IToolStackView tool, ModifierEntry entry, LivingEntity attacker, InteractionHand hand, Entity target, EquipmentSlot sourceSlot, boolean isFullyCharged, boolean isExtraAttack, boolean isCritical);
    default float getCriticalModifier(IToolStackView toolStackView,ModifierEntry entry, ToolAttackContext context,float originalModifier,float modifier){
        return modifier;
    }
    record Merger(Collection<CriticalAttackModifierHook> modules) implements CriticalAttackModifierHook {
        @Override
        public boolean setCritical(IToolStackView tool, ModifierEntry entry, LivingEntity attacker, InteractionHand hand, Entity target, EquipmentSlot sourceSlot, boolean isFullyCharged, boolean isExtraAttack,boolean isCritical) {
            for (CriticalAttackModifierHook module:this.modules){
                isCritical =module.setCritical(tool,entry,attacker,hand,target,sourceSlot,isFullyCharged,isExtraAttack,isCritical);
                if (isCritical) return isCritical;
            }
            return isCritical;
        }

        @Override
        public float getCriticalModifier(IToolStackView toolStackView, ModifierEntry entry, ToolAttackContext context, float originalModifier, float modifier) {
            for (CriticalAttackModifierHook module:this.modules){
                modifier =module.getCriticalModifier(toolStackView,entry,context,originalModifier,modifier);
            }
            return modifier;
        }
    }

}
