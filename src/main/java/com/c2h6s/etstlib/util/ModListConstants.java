package com.c2h6s.etstlib.util;

import net.minecraftforge.fml.ModList;

public class ModListConstants {
    public static boolean MekLoaded = ModList.get().isLoaded("mekanism");
    public static boolean COFHLoaded = ModList.get().isLoaded("cofh_core");
    public static boolean BOTLoaded = ModList.get().isLoaded("botania");
    public static boolean PowahLoaded = ModList.get().isLoaded("powah");
    public static boolean AdAstraLoaded = ModList.get().isLoaded("ad_astra");
    public static boolean AE2Loaded = ModList.get().isLoaded("ae2");
    public static boolean IELoaded=ModList.get().isLoaded("immersiveengineering");
    public static boolean CYCLoaded=ModList.get().isLoaded("cyclic");
    public static boolean MBOTLoaded = ModList.get().isLoaded("mythicbotany");
    public static boolean PnCLoaded = ModList.get().isLoaded("pneumaticcraft");
}
