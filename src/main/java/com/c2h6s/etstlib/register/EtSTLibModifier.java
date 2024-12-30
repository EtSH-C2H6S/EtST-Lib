package com.c2h6s.etstlib.register;

import com.c2h6s.etstlib.EtSTLib;
import com.c2h6s.etstlib.tool.modifiers.Combat.ArmorPiercing;
import com.c2h6s.etstlib.tool.modifiers.Combat.Critical;
import com.c2h6s.etstlib.tool.modifiers.Combat.MagicStrike;
import com.c2h6s.etstlib.tool.modifiers.Integration.AEIntegration.*;
import com.c2h6s.etstlib.tool.modifiers.Integration.PnCIntegration.*;
import com.c2h6s.etstlib.tool.modifiers.Integration.MekIntegration.*;
import com.c2h6s.etstlib.tool.modifiers.Common.*;
import com.c2h6s.etstlib.tool.modifiers.Defense.*;
import com.c2h6s.etstlib.tool.modifiers.Harvest.*;
import slimeknights.tconstruct.library.modifiers.util.ModifierDeferredRegister;
import slimeknights.tconstruct.library.modifiers.util.StaticModifier;

public class EtSTLibModifier {
    public static ModifierDeferredRegister MODIFIERS = ModifierDeferredRegister.create(EtSTLib.MODID);
    public static ModifierDeferredRegister PnC_MODIFIERS = ModifierDeferredRegister.create(EtSTLib.MODID);
    public static ModifierDeferredRegister Mek_MODIFIERS = ModifierDeferredRegister.create(EtSTLib.MODID);
    public static ModifierDeferredRegister AE_MODIFIERS = ModifierDeferredRegister.create(EtSTLib.MODID);

    //无集成词条
    public static final StaticModifier<AntiStunGlasses> anti_stun_glasses =MODIFIERS.register("anti_stun_glasses",AntiStunGlasses::new);
    public static final StaticModifier<EnergyShield> energy_shield =MODIFIERS.register("energy_shield",EnergyShield::new);
    public static final StaticModifier<EnergyLoaded> energy_loaded =MODIFIERS.register("energy_loaded",EnergyLoaded::new);
    public static final StaticModifier<AtomicDecompose> atomic_decompose =MODIFIERS.register("atomic_decompose",AtomicDecompose::new);
    public static final StaticModifier<Clearing> clearing =MODIFIERS.register("clearing",Clearing::new);
    public static final StaticModifier<MomentumAccelerate> momentum_accelerate =MODIFIERS.register("momentum_accelerate",MomentumAccelerate::new);
    public static final StaticModifier<MagicStrike> magic_strike =MODIFIERS.register("magic_strike",MagicStrike::new);
    public static final StaticModifier<Critical> critical =MODIFIERS.register("critical",Critical::new);
    public static final StaticModifier<ArmorPiercing> armor_piercing =MODIFIERS.register("armor_piercing",ArmorPiercing::new);


    //PnC集成词条
    public static final StaticModifier<AerialReinforced> aerial_reinforced =PnC_MODIFIERS.register("aerial_reinforced",AerialReinforced::new);


    //mek集成词条
    public static final StaticModifier<RadiationShielding> radiation_shielding = Mek_MODIFIERS.register("radiation_shielding",RadiationShielding::new);
    public static final StaticModifier<RadiationInflict> radiation_inflict = Mek_MODIFIERS.register("radiation_inflict",RadiationInflict::new);


    //AE集成词条
    public static final StaticModifier<AppliedFixing> applied_fixing = Mek_MODIFIERS.register("applied_fixing",AppliedFixing::new);
    public static final StaticModifier<EnergeticAttack> energetic_attack = Mek_MODIFIERS.register("energetic_attack",EnergeticAttack::new);
}
