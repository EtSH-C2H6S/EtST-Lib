package com.c2h6s.etstlib.tool.hooks;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Collection;

public interface CriticalAttackModifierHook {
    Boolean setCritical(IToolStackView tool, ModifierEntry entry, LivingEntity attacker, InteractionHand hand, Entity target, EquipmentSlot sourceSlot, boolean isFullyCharged, boolean isExtraAttack, Boolean isCritical);

    record FirstMerger(Collection<CriticalAttackModifierHook> modules) implements CriticalAttackModifierHook {
        @Override
        public Boolean setCritical(IToolStackView tool, ModifierEntry entry, LivingEntity attacker, InteractionHand hand, Entity target, EquipmentSlot sourceSlot, boolean isFullyCharged, boolean isExtraAttack,Boolean isCritical) {
            for (CriticalAttackModifierHook module:this.modules){
                Boolean IsCritical =module.setCritical(tool,entry,attacker,hand,target,sourceSlot,isFullyCharged,isExtraAttack,isCritical);
                if (IsCritical!=null) return IsCritical;
            }
            return isCritical;
        }
    }

}
