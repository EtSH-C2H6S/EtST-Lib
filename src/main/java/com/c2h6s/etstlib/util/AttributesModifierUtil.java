package com.c2h6s.etstlib.util;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class AttributesModifierUtil {
    //一个简单的叠加attributeModifier的方法，当实体已经有这个modifier时将amount相加，没有时则直接添加
    public static void addTransientModifierOrAddUp(AttributeModifier modifier, Attribute attribute, LivingEntity living){
        AttributeInstance instance = living.getAttribute(attribute);
        if (instance!=null){
            AttributeModifier existingModifier = instance.getModifier(modifier.getId());
            if (existingModifier!=null){
                modifier = new AttributeModifier(modifier.getId(),modifier.getName(),modifier.getAmount()+existingModifier.getAmount(),modifier.getOperation());
                instance.removeModifier(modifier.getId());
            }
            instance.addTransientModifier(modifier);
        }
    }
}
