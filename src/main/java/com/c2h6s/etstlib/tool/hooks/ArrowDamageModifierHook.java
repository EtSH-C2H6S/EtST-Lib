package com.c2h6s.etstlib.tool.hooks;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;

import java.util.Collection;
import java.util.Iterator;

public interface ArrowDamageModifierHook {
    /**
     *
     * @param persistentData
     * @param entry
     * @param modifiers 词条实例
     * @param arrow 箭矢实体
     * @param attacker 攻击者
     * @param target 被攻击者
     * @param baseDamage 基础伤害
     * @param damage 总伤害
     * @return 箭矢的伤害
     */
    float getArrowDamage(ModDataNBT persistentData, ModifierEntry entry, ModifierNBT modifiers, AbstractArrow arrow, @Nullable LivingEntity attacker, @NotNull Entity target, float baseDamage, float damage);

    record AllMerger(Collection<ArrowDamageModifierHook> modules) implements ArrowDamageModifierHook {
        public AllMerger(Collection<ArrowDamageModifierHook> modules) {
            this.modules = modules;
        }

        public float getArrowDamage(ModDataNBT persistentData, ModifierEntry entry, ModifierNBT modifiers, AbstractArrow arrow, @Nullable LivingEntity attacker,@NotNull Entity target, float baseDamage, float damage) {
            ArrowDamageModifierHook module;
            for(Iterator<ArrowDamageModifierHook> var6 = this.modules.iterator(); var6.hasNext(); damage = module.getArrowDamage(persistentData, entry,modifiers, arrow,attacker,target, baseDamage, damage)) {
                module = var6.next();
            }
            return damage;
        }

        public Collection<ArrowDamageModifierHook> modules() {
            return this.modules;
        }
    }
}
