package com.c2h6s.etstlib.register;

import com.c2h6s.etstlib.EtSTLib;
import com.c2h6s.etstlib.tool.hooks.AirStorage.ICustomPressureBarModifierHook;
import com.c2h6s.etstlib.tool.hooks.EnergyStorage.ICustomEnergyBarModifierHook;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.module.ModuleHook;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class EtSTLibHooks {
    public static final ModuleHook<ICustomEnergyBarModifierHook> ENERGY_BAR = ModifierHooks.register(EtSTLib.getResourceLocation("energy_bar"), ICustomEnergyBarModifierHook.class, ICustomEnergyBarModifierHook.AllMerger::new,new ICustomEnergyBarModifierHook(){
        public boolean showEnergyBar(IToolStackView tool, ModifierEntry entry) {
            return false;
        }

        public int getEnergyBarWidth(IToolStackView tool, ModifierEntry entry) {
            return 0;
        }
    } );
    public static final ModuleHook<ICustomPressureBarModifierHook> PRESSURE_BAR = ModifierHooks.register(EtSTLib.getResourceLocation("pressure_bar"), ICustomPressureBarModifierHook.class, ICustomPressureBarModifierHook.AllMerger::new,new ICustomPressureBarModifierHook(){
        public boolean showPressureBar(IToolStackView tool, ModifierEntry entry) {
            return false;
        }

        public int getPressureBarWidth(IToolStackView tool, ModifierEntry entry) {
            return 0;
        }
    } );
}
