package com.c2h6s.etstlib.util;

import net.minecraft.world.entity.EquipmentSlot;
import slimeknights.tconstruct.library.modifiers.ModifierId;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class UUIDUtil {
    /**
     * 用于生成UUID,避免手动生成和直接random,而是根据字符串去固定UUID
     * @param slot 盔甲槽位
     * @param modifierId 词条id
     * @return 不同且唯一的UUID
     */
    public static UUID UUIDFromSlot(EquipmentSlot slot, ModifierId modifierId){
        return UUID.nameUUIDFromBytes((slot.getName() +modifierId.toString()).getBytes(StandardCharsets.UTF_8));
    }
}
