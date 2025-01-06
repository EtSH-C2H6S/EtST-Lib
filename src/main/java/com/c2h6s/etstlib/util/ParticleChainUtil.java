package com.c2h6s.etstlib.util;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;

public class ParticleChainUtil {
    public static void summonParticleChain(Vec3 pos1, Vec3 pos2, ParticleOptions options,double spacing,int maxParts, ServerLevel serverLevel,int count,double xSpread,double ySpread,double zSpread,double velocity){
        Vec3 direction = pos2.subtract(pos1);
        double length = direction.length();
        for (double d =0;d<length;d+=spacing){
            if (d>=spacing*maxParts){
                break;
            }
            Vec3 position = pos1.add(direction.normalize().scale(d));
            serverLevel.sendParticles(options,position.x,position.y,position.z,count,xSpread,ySpread,zSpread,velocity);
        }
    }
}
