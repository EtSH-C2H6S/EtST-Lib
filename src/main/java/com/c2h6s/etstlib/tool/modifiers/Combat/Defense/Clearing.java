package com.c2h6s.etstlib.tool.modifiers.Combat.Defense;

import com.c2h6s.etstlib.tool.modifiers.base.EtSTBaseModifier;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.modules.armor.EffectImmunityModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class Clearing extends EtSTBaseModifier {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addModule(new EffectImmunityModule(MobEffects.POISON));
        hookBuilder.addModule(new EffectImmunityModule(MobEffects.WITHER));
    }

    @Override
    public void modifierOnInventoryTick(IToolStackView iToolStackView, ModifierEntry modifierEntry, Level level, LivingEntity livingEntity, int i, boolean b, boolean b1, ItemStack itemStack) {
        MobEffectInstance instance = livingEntity.getEffect(MobEffects.POISON);
        if (instance!=null){
            livingEntity.removeEffect(MobEffects.POISON);
        }
        instance = livingEntity.getEffect(MobEffects.WITHER);
        if (instance!=null){
            livingEntity.removeEffect(MobEffects.WITHER);
        }
    }
}
