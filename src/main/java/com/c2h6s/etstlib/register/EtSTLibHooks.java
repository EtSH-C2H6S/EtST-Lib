package com.c2h6s.etstlib.register;

import com.c2h6s.etstlib.EtSTLib;
import com.c2h6s.etstlib.tool.hooks.AirStorage.CustomPressureBarModifierHook;
import com.c2h6s.etstlib.tool.hooks.EnergyStorage.CustomEnergyBarModifierHook;
import com.c2h6s.etstlib.tool.hooks.CorrectDropModifierHook;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.module.ModuleHook;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class EtSTLibHooks {
    public static final ModuleHook<CustomEnergyBarModifierHook> ENERGY_BAR = ModifierHooks.register(EtSTLib.getResourceLocation("energy_bar"), CustomEnergyBarModifierHook.class, CustomEnergyBarModifierHook.AllMerger::new,new CustomEnergyBarModifierHook(){
        public boolean showEnergyBar(IToolStackView tool, ModifierEntry entry) {
            return false;
        }

        public int getEnergyBarWidth(IToolStackView tool, ModifierEntry entry) {
            return 0;
        }
    } );
    public static final ModuleHook<CustomPressureBarModifierHook> PRESSURE_BAR = ModifierHooks.register(EtSTLib.getResourceLocation("pressure_bar"), CustomPressureBarModifierHook.class, CustomPressureBarModifierHook.AllMerger::new,new CustomPressureBarModifierHook(){
        public boolean showPressureBar(IToolStackView tool, ModifierEntry entry) {
            return false;
        }

        public int getPressureBarWidth(IToolStackView tool, ModifierEntry entry) {
            return 0;
        }
    } );
    public static final ModuleHook<CorrectDropModifierHook> CORRECT_TOOL = ModifierHooks.register(EtSTLib.getResourceLocation("correct_tool"), CorrectDropModifierHook.class, CorrectDropModifierHook.AllMerger::new,(tool, entry, state, drop)->drop);
}
