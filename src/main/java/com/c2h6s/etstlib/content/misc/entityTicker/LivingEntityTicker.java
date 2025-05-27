package com.c2h6s.etstlib.content.misc.entityTicker;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

//只给活实体用的Ticker，不确定有啥用
public abstract class LivingEntityTicker extends EntityTicker {
    @Override
    public boolean tick(int duration, int level, Entity entity) {
        if (entity instanceof LivingEntity living) return this.livingTick(duration,level,living);
        return true;
    }

    public boolean livingTick(int duration, int level, LivingEntity entity){
        return true;
    }
}
