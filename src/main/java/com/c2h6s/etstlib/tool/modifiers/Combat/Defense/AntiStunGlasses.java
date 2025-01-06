package com.c2h6s.etstlib.tool.modifiers.Combat.Defense;

import com.c2h6s.etstlib.tool.modifiers.base.EtSTBaseModifier;
import com.c2h6s.etstlib.util.EquipmentUtil;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class AntiStunGlasses extends EtSTBaseModifier {

    @Override
    public boolean onEffectApplicable(IToolStackView tool, ModifierEntry entry, EquipmentSlot slot, MobEffectInstance instance, boolean notApplicable) {
        return EquipmentUtil.ARMOR.contains(slot) && instance.getEffect() == MobEffects.CONFUSION;
    }

    @Override
    public void modifierOnInventoryTick(IToolStackView iToolStackView, ModifierEntry modifierEntry, Level level, LivingEntity livingEntity, int i, boolean b, boolean b1, ItemStack itemStack) {
        MobEffectInstance instance = livingEntity.getEffect(MobEffects.CONFUSION);
        if (instance!=null){
            livingEntity.removeEffect(MobEffects.CONFUSION);
        }
    }
}
