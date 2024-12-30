package com.c2h6s.etstlib;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class MixinTemp {
    public static boolean isProcessingDamageSource;
    public static float damageBeforeArmorAbs;
    public static class attackUtilTemp{
        public static IToolStackView tool;
        public static LivingEntity attacker;
        public static InteractionHand hand;
        public static Entity target;
        public static EquipmentSlot sourceSlot;
        public static boolean isFullyCharged;
        public static boolean isExtraAttack;
        public static boolean isCritical;
        public static DamageSource damageSource;
    }
}
