package com.c2h6s.etstlib.register;

import com.c2h6s.etstlib.EtSTLib;
import com.c2h6s.etstlib.tool.hooks.*;
import com.c2h6s.etstlib.tool.hooks.AirStorage.CustomPressureBarModifierHook;
import com.c2h6s.etstlib.tool.hooks.EnergyStorage.CustomEnergyBarModifierHook;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.module.ModuleHook;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class EtSTLibHooks {
    public static final ModuleHook<CustomEnergyBarModifierHook> ENERGY_BAR = ModifierHooks.register(EtSTLib.getResourceLocation("energy_bar"), CustomEnergyBarModifierHook.class, CustomEnergyBarModifierHook.AllMerger::new,new CustomEnergyBarModifierHook(){
        public boolean showEnergyBar(IToolStackView tool, ModifierEntry entry) {
            return false;
        }

        public int getEnergyBarWidth(IToolStackView tool, ModifierEntry entry) {
            return 0;
        }
    } );
    public static final ModuleHook<CustomPressureBarModifierHook> PRESSURE_BAR = ModifierHooks.register(EtSTLib.getResourceLocation("pressure_bar"), CustomPressureBarModifierHook.class, CustomPressureBarModifierHook.AllMerger::new,new CustomPressureBarModifierHook(){
        public boolean showPressureBar(IToolStackView tool, ModifierEntry entry) {
            return false;
        }

        public int getPressureBarWidth(IToolStackView tool, ModifierEntry entry) {
            return 0;
        }
    } );
    public static final ModuleHook<CorrectDropModifierHook> CORRECT_TOOL = ModifierHooks.register(EtSTLib.getResourceLocation("correct_tool"), CorrectDropModifierHook.class, CorrectDropModifierHook.AllMerger::new,(tool, entry, state, drop)->drop);
    public static final ModuleHook<EffectApplicableModifierHook> EFFECT_APPLICABLE = ModifierHooks.register(EtSTLib.getResourceLocation("effect_applicable"), EffectApplicableModifierHook.class, EffectApplicableModifierHook.AllMerger::new,(tool, entry, equipmentSlot,instance, applicable)->applicable);
    public static final ModuleHook<CriticalAttackModifierHook> CRITICAL_ATTACK = ModifierHooks.register(EtSTLib.getResourceLocation("critical_attack"), CriticalAttackModifierHook.class, CriticalAttackModifierHook.AllMerger::new,(tool,entry,attacker,hand,target,sourceSlot,isFullyCharged,isExtraAttack,isCritical)->isCritical);
    public static final ModuleHook<ModifyDamageSourceModifierHook> MODIFY_DAMAGE_SOURCE = ModifierHooks.register(EtSTLib.getResourceLocation("modify_damage_source"), ModifyDamageSourceModifierHook.class, ModifyDamageSourceModifierHook.AllMerger::new, new ModifyDamageSourceModifierHook() {
        @Override
        public DamageSource modifyDamageSource(IToolStackView tool, ModifierEntry entry, LivingEntity attacker, InteractionHand hand, Entity target, EquipmentSlot sourceSlot, boolean isFullyCharged, boolean isExtraAttack, boolean isCritical,DamageSource source) {
            return source;
        }
    });

    public static final ModuleHook<LeftClickModifierHook> LEFT_CLICK = ModifierHooks.register(EtSTLib.getResourceLocation("left_click"), LeftClickModifierHook.class, LeftClickModifierHook.AllMerger::new, new LeftClickModifierHook() {});
}
