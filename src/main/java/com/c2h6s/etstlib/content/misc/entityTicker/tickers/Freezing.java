package com.c2h6s.etstlib.content.misc.entityTicker.tickers;

import com.c2h6s.etstlib.content.misc.entityTicker.EntityTicker;
import net.minecraft.world.entity.Entity;

public class Freezing extends EntityTicker {
    @Override
    public boolean tick(int duration, int level, Entity entity) {
        return false;
    }
}
