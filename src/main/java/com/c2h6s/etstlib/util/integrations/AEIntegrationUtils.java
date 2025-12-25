package com.c2h6s.etstlib.util.integrations;

import appeng.core.AppEng;
import appeng.core.sync.packets.LightningPacket;
import net.minecraft.world.level.Level;

public class AEIntegrationUtils {
    public static void spawnLightningParticle(Level level,double dx,double dy,double dz){
        AppEng.instance().sendToAllNearExcept(null, dx, dy, dz, 32.0, level, new LightningPacket(dx, dy, dz));
    }
}
