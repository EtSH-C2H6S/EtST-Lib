package com.c2h6s.etstlib.util;

import com.c2h6s.etstlib.register.EtSTLibModifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

public class EquipmentUtil {
    public static boolean isAntiStun(LivingEntity living){
        return living.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof IModifiable && ToolStack.from(living.getItemBySlot(EquipmentSlot.HEAD)).getModifierLevel(EtSTLibModifier.anti_stun_glasses.get())>0;
    }
}
