package com.c2h6s.etstlib.util;

import com.c2h6s.etstlib.EtSTLib;
import net.minecraft.tags.TagKey;
import slimeknights.tconstruct.library.materials.definition.IMaterial;
import slimeknights.tconstruct.library.materials.definition.MaterialManager;

public class EtSTLibTags {
    //远古工具材料的黑名单，仅在白名单为空时使用。带有此tag的材料无法被RandomMaterial$Randomized随机到。
    //BlackList for materials that can be randomized by Ancient Tools.Will not count when WHITELISTED_RANDOM_MATERIAL is not Empty.
    public static final TagKey<IMaterial> BLACKLISTED_RANDOM_MATERIAL = MaterialManager.getTag(EtSTLib.getResourceLocation("randomize_blacklisted"));
    //远古工具材料的白名单，不为空时使黑名单失效。仅带有此tag的材料能被RandomMaterial$Randomized随机到。
    //WhiteList for materials that can be randomized by Ancient Tools.
    public static final TagKey<IMaterial> WHITELISTED_RANDOM_MATERIAL = MaterialManager.getTag(EtSTLib.getResourceLocation("randomize_whitelisted"));
}
