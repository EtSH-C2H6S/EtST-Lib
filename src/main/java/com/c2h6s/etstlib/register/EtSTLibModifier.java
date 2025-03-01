package com.c2h6s.etstlib.register;

import com.c2h6s.etstlib.EtSTLib;
import com.c2h6s.etstlib.tool.modifiers.Combat.Ranged.*;
import com.c2h6s.etstlib.tool.modifiers.Combat.*;
import com.c2h6s.etstlib.tool.modifiers.Integration.AEIntegration.*;
import com.c2h6s.etstlib.tool.modifiers.Integration.BOTIntegration.*;
import com.c2h6s.etstlib.tool.modifiers.Integration.PnCIntegration.*;
import com.c2h6s.etstlib.tool.modifiers.Integration.MekIntegration.*;
import com.c2h6s.etstlib.tool.modifiers.Common.*;
import com.c2h6s.etstlib.tool.modifiers.Combat.Defense.*;
import com.c2h6s.etstlib.tool.modifiers.Harvest.*;
import com.c2h6s.etstlib.tool.modifiers.Test;
import slimeknights.tconstruct.library.modifiers.util.ModifierDeferredRegister;
import slimeknights.tconstruct.library.modifiers.util.StaticModifier;

public class EtSTLibModifier {
    public static ModifierDeferredRegister MODIFIERS = ModifierDeferredRegister.create(EtSTLib.MODID);

    //示例属性
    public static final StaticModifier<MagicStrike> magic_strike =MODIFIERS.register("magic_strike",MagicStrike::new);
    public static final StaticModifier<Critical> critical =MODIFIERS.register("critical",Critical::new);
    public static final StaticModifier<ArmorPiercing> armor_piercing =MODIFIERS.register("armor_piercing",ArmorPiercing::new);
    public static final StaticModifier<PhotosynthesisGuide> photosynthesis_guide =MODIFIERS.register("photosynthesis_guide",PhotosynthesisGuide::new);
    public static final StaticModifier<EnergyLoaded> energy_loaded =MODIFIERS.register("energy_loaded",EnergyLoaded::new);
    public static final StaticModifier<AtomicDecompose> atomic_decompose =MODIFIERS.register("atomic_decompose",AtomicDecompose::new);

    //无集成属性
    public static final StaticModifier<AntiStunGlasses> anti_stun_glasses =MODIFIERS.register("anti_stun_glasses",AntiStunGlasses::new);
    public static final StaticModifier<EnergyShield> energy_shield =MODIFIERS.register("energy_shield",EnergyShield::new);
    public static final StaticModifier<Clearing> clearing =MODIFIERS.register("clearing",Clearing::new);
    public static final StaticModifier<MomentumAccelerate> momentum_accelerate =MODIFIERS.register("momentum_accelerate",MomentumAccelerate::new);
    public static final StaticModifier<WarpAttack> warp_attack =MODIFIERS.register("warp_attack",WarpAttack::new);
    public static final StaticModifier<Anisotropy> ANISOTROPY = MODIFIERS.register("anisotropy", Anisotropy::new);
    public static final StaticModifier<CrystalArmor> CRYSTAL_ARMOR = MODIFIERS.register("crystal_armor", CrystalArmor::new);
    public static final StaticModifier<RealityBreaker> reality_breaker =MODIFIERS.register("reality_breaker",RealityBreaker::new);
    public static final StaticModifier<Glowing> glowing =MODIFIERS.register("glowing",Glowing::new);


    //PnC集成属性
    public static class EtSTLibModifierPnC{
        public static ModifierDeferredRegister PnC_MODIFIERS = ModifierDeferredRegister.create(EtSTLib.MODID);
        //示例词条
        public static final StaticModifier<AerialReinforced> aerial_reinforced =PnC_MODIFIERS.register("aerial_reinforced",AerialReinforced::new);
    }



    //mek集成属性
    public static class EtSTLibModifierMek {
        public static ModifierDeferredRegister Mek_MODIFIERS = ModifierDeferredRegister.create(EtSTLib.MODID);
        public static final StaticModifier<RadiationShielding> radiation_shielding = Mek_MODIFIERS.register("radiation_shielding", RadiationShielding::new);
        public static final StaticModifier<RadiationInflict> radiation_inflict = Mek_MODIFIERS.register("radiation_inflict", RadiationInflict::new);
    }

    //AE集成属性
    public static class EtSTLibModifierAE {
        public static ModifierDeferredRegister AE_MODIFIERS = ModifierDeferredRegister.create(EtSTLib.MODID);
        public static final StaticModifier<AppliedFixing> applied_fixing = AE_MODIFIERS.register("applied_fixing", AppliedFixing::new);
        public static final StaticModifier<EnergeticAttack> energetic_attack = AE_MODIFIERS.register("energetic_attack", EnergeticAttack::new);
    }

    //植魔集成属性
    public static class EtSTLibModifierBOT {

        public static ModifierDeferredRegister BOT_MODIFIERS = ModifierDeferredRegister.create(EtSTLib.MODID);
        public static final StaticModifier<ManaRepair> mana_repair = BOT_MODIFIERS.register("mana_repair", ManaRepair::new);
        public static final StaticModifier<TerraBeam> terra_beam = BOT_MODIFIERS.register("terra_beam", TerraBeam::new);
    }

    //public static final StaticModifier<Test> test = MODIFIERS.register("test", Test::new);
}
