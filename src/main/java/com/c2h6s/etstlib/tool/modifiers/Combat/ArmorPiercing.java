package com.c2h6s.etstlib.tool.modifiers.Combat;

import com.c2h6s.etstlib.entity.specialDamageSources.PercentageBypassArmorSource;
import com.c2h6s.etstlib.tool.modifiers.base.EtSTBaseModifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;
import slimeknights.tconstruct.library.tools.nbt.NamespacedNBT;

public class ArmorPiercing extends EtSTBaseModifier {
    @Override
    public DamageSource modifyDamageSource(IToolStackView tool, ModifierEntry entry, LivingEntity attacker, InteractionHand hand, Entity target, EquipmentSlot sourceSlot, boolean isFullyCharged, boolean isExtraAttack, boolean isCritical) {
        return PercentageBypassArmorSource.mobAttack(attacker,entry.getLevel()*0.25f);
    }

    @Override
    public DamageSource modifyArrowDamageSource(ModifierNBT modifiers, NamespacedNBT persistentData, ModifierEntry modifier, AbstractArrow arrow, @Nullable LivingEntity attacker, @Nullable LivingEntity target) {
        return PercentageBypassArmorSource.projectileHit(arrow,modifier.getLevel()*0.25f);
    }
}
