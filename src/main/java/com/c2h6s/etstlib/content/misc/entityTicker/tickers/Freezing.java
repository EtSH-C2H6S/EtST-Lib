package com.c2h6s.etstlib.content.misc.entityTicker.tickers;

import com.c2h6s.etstlib.content.misc.entityTicker.EntityTicker;
import net.minecraft.world.entity.Entity;

//简单粗暴冻住一个实体的Ticker
public class Freezing extends EntityTicker {
    @Override
    public boolean tick(int duration, int level, Entity entity) {
        return false;
    }
}
