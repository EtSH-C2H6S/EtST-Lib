package com.c2h6s.etstlib.register;

import com.c2h6s.etstlib.EtSTLib;
import com.c2h6s.etstlib.tool.modifiers.base.BasicFEModifier;
import com.c2h6s.etstlib.tool.modifiers.base.BasicPressurizableModifier;
import slimeknights.tconstruct.library.modifiers.util.ModifierDeferredRegister;
import slimeknights.tconstruct.library.modifiers.util.StaticModifier;

public class EtSTLibModifier {
    public static ModifierDeferredRegister MODIFIERS = ModifierDeferredRegister.create(EtSTLib.MODID);
    public static ModifierDeferredRegister PnC_MODIFIERS = ModifierDeferredRegister.create(EtSTLib.MODID);
    public static ModifierDeferredRegister Mek_MODIFIERS = ModifierDeferredRegister.create(EtSTLib.MODID);

    
    public static final StaticModifier<BasicFEModifier> FE_basic =MODIFIERS.register("forge_energy_basic",BasicFEModifier::new);


    public static final StaticModifier<BasicPressurizableModifier> pressurizable_basic= PnC_MODIFIERS.register("pressurizable_basic", BasicPressurizableModifier::new);
}
