package com.c2h6s.etstlib.tool.hooks;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;

import java.util.Collection;

public interface ArrowHitModifierHook {
    default void afterArrowHit(ModDataNBT persistentData, ModifierEntry entry, ModifierNBT modifiers, AbstractArrow arrow, @Nullable LivingEntity attacker, @NotNull LivingEntity target, float damageDealt) {

    }

    record AllMerger(Collection<ArrowHitModifierHook> modules) implements ArrowHitModifierHook {
        public AllMerger(Collection<ArrowHitModifierHook> modules) {
            this.modules = modules;
        }

        public void afterArrowHit(ModDataNBT persistentData, ModifierEntry entry, ModifierNBT modifiers, AbstractArrow arrow,@Nullable LivingEntity attacker,@NotNull LivingEntity target, float damageDealt) {
            for(ArrowHitModifierHook module:this.modules) {
                module.afterArrowHit(persistentData, entry,modifiers, arrow,attacker,target, damageDealt);
            }
        }

        public Collection<ArrowHitModifierHook> modules() {
            return this.modules;
        }
    }
}
