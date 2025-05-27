package com.c2h6s.etstlib.entity.specialDamageSources.interfaces;

import net.minecraft.world.entity.Entity;

public interface IConditionalHurt {
    Boolean canHurtEntity(Entity entity);
}
