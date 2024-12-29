package com.c2h6s.etstlib.register;

import com.c2h6s.etstlib.EtSTLib;
import com.c2h6s.etstlib.tool.modifiers.Combat.AEIntegration.AppliedFixing;
import com.c2h6s.etstlib.tool.modifiers.Common.EnergyLoaded;
import com.c2h6s.etstlib.tool.modifiers.Defense.AntiStunGlasses;
import com.c2h6s.etstlib.tool.modifiers.Defense.EnergyShield;
import com.c2h6s.etstlib.tool.modifiers.Combat.MekIntegration.RadiationShielding;
import com.c2h6s.etstlib.tool.modifiers.Harvest.AtomicDecompose;
import slimeknights.tconstruct.library.modifiers.util.ModifierDeferredRegister;
import slimeknights.tconstruct.library.modifiers.util.StaticModifier;

public class EtSTLibModifier {
    public static ModifierDeferredRegister MODIFIERS = ModifierDeferredRegister.create(EtSTLib.MODID);
    public static ModifierDeferredRegister PnC_MODIFIERS = ModifierDeferredRegister.create(EtSTLib.MODID);
    public static ModifierDeferredRegister Mek_MODIFIERS = ModifierDeferredRegister.create(EtSTLib.MODID);
    public static ModifierDeferredRegister AE_MODIFIERS = ModifierDeferredRegister.create(EtSTLib.MODID);


    public static final StaticModifier<AntiStunGlasses> anti_stun_glasses =MODIFIERS.register("anti_stun_glasses",AntiStunGlasses::new);
    public static final StaticModifier<EnergyShield> energy_shield =MODIFIERS.register("energy_shield",EnergyShield::new);
    public static final StaticModifier<EnergyLoaded> energy_loaded =MODIFIERS.register("energy_loaded",EnergyLoaded::new);
    public static final StaticModifier<AtomicDecompose> atomic_decompose =MODIFIERS.register("atomic_decompose",AtomicDecompose::new);






    public static final StaticModifier<RadiationShielding> radiation_shielding = Mek_MODIFIERS.register("radiation_shielding",RadiationShielding::new);



    public static final StaticModifier<AppliedFixing> applied_fixing = Mek_MODIFIERS.register("applied_fixing",AppliedFixing::new);
}
