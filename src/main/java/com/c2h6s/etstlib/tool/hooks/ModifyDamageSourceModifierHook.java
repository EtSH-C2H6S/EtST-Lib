package com.c2h6s.etstlib.tool.hooks;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;
import slimeknights.tconstruct.library.tools.nbt.NamespacedNBT;

import javax.annotation.Nullable;
import java.util.Collection;

public interface ModifyDamageSourceModifierHook {
    default DamageSource modifyDamageSource(IToolStackView tool, ModifierEntry entry, LivingEntity attacker, InteractionHand hand, Entity target, EquipmentSlot sourceSlot, boolean isFullyCharged, boolean isExtraAttack, boolean isCritical){
        return null;
    }
    default DamageSource modifyArrowDamageSource(ModifierNBT modifiers, NamespacedNBT persistentData, ModifierEntry modifier, AbstractArrow arrow, @Nullable LivingEntity attacker, @Nullable LivingEntity target){
        return null;
    }
    record AllMerger(Collection<ModifyDamageSourceModifierHook> modules) implements ModifyDamageSourceModifierHook {
        @Override
        public DamageSource modifyDamageSource(IToolStackView tool, ModifierEntry entry, LivingEntity attacker, InteractionHand hand, Entity target, EquipmentSlot sourceSlot, boolean isFullyCharged, boolean isExtraAttack,boolean isCritical) {
            DamageSource source = null;
            for (ModifyDamageSourceModifierHook module:this.modules){
                source =module.modifyDamageSource(tool, entry,attacker,hand,target,sourceSlot,isFullyCharged,isExtraAttack,isCritical);
                if (source!=null) break;
            }
            return source;
        }
        @Override
        public DamageSource modifyArrowDamageSource(ModifierNBT modifiers, NamespacedNBT persistentData, ModifierEntry modifier, @NotNull AbstractArrow arrow, @Nullable LivingEntity attacker, @Nullable LivingEntity target){
            DamageSource source = null;
            for (ModifyDamageSourceModifierHook module:this.modules){
                source =module.modifyArrowDamageSource(modifiers, persistentData,modifier,arrow,attacker,target);
                if (source!=null) break;
            }
            return source;
        }
    }
}
