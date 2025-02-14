package com.c2h6s.etstlib.util;

import com.c2h6s.etstlib.register.EtSTLibModifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.List;

public class EquipmentUtil {
    public static final List<EquipmentSlot> ARMOR = List.of(EquipmentSlot.HEAD,EquipmentSlot.CHEST,EquipmentSlot.LEGS,EquipmentSlot.FEET);
    public static final List<EquipmentSlot> HAND = List.of(EquipmentSlot.MAINHAND,EquipmentSlot.OFFHAND);
    public static final List<EquipmentSlot> ALL = List.of(EquipmentSlot.HEAD,EquipmentSlot.CHEST,EquipmentSlot.LEGS,EquipmentSlot.FEET,EquipmentSlot.MAINHAND,EquipmentSlot.OFFHAND);
    public static boolean isAntiStun(LivingEntity living){
        return living.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof IModifiable && ToolStack.from(living.getItemBySlot(EquipmentSlot.HEAD)).getModifierLevel(EtSTLibModifier.anti_stun_glasses.get())>0;
    }
}
