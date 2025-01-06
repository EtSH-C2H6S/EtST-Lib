package com.c2h6s.etstlib.tool.modifiers.Combat.Defense;

import com.c2h6s.etstlib.tool.modifiers.base.BasicFEModifier;
import com.c2h6s.etstlib.tool.modifiers.capabilityProvider.FEStorageProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.DamageBlockModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.armor.ModifyDamageModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class EnergyShield extends BasicFEModifier implements DamageBlockModifierHook , ModifyDamageModifierHook {
    @Override
    public int getCapacity(ModifierEntry modifier) {
        return 100000*modifier.getLevel();
    }

    @Override
    public int getMaxTransfer(ModifierEntry modifier) {
        return 10000*modifier.getLevel();
    }

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.DAMAGE_BLOCK,ModifierHooks.MODIFY_DAMAGE);
    }

    @Override
    public boolean isDamageBlocked(IToolStackView tool, ModifierEntry entry, EquipmentContext context, EquipmentSlot equipmentSlot, DamageSource damageSource, float amount) {
        if (context.getEntity().invulnerableTime>0){
            return true;
        }
        int needed = (int) (amount*10000);
        int maxCancle= FEStorageProvider.extractEnergy(tool,needed,true,true);
        if (maxCancle>=needed){
            FEStorageProvider.extractEnergy(tool,needed,false,true);
            context.getEntity().invulnerableTime+=10;
            return true;
        }
        return false;
    }

    @Override
    public float modifyDamageTaken(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, DamageSource source, float amount, boolean isDirectDamage) {
        amount = amount- (float) FEStorageProvider.extractEnergy(tool, (int) (amount * 20000), false, true) /20000;
        return amount;
    }
}
