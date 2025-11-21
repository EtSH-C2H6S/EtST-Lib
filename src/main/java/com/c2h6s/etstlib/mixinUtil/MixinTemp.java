package com.c2h6s.etstlib.mixinUtil;

import net.minecraft.world.entity.Entity;
import slimeknights.tconstruct.library.materials.stats.MaterialStatsId;

public class MixinTemp {
    public static boolean isProcessingDamageSource;
    public static float damageBeforeArmorAbs;

    public static Entity arrowHit ;
    public static float entityHealth ;
    public static boolean hasBeenShot;
    public static boolean leftOwner;
    public static boolean onGround;

    public static MaterialStatsId statType;
}
