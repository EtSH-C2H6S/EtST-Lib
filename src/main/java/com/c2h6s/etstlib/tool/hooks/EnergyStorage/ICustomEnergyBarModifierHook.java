package com.c2h6s.etstlib.tool.hooks.EnergyStorage;

import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Collection;

public interface ICustomEnergyBarModifierHook {
    boolean showEnergyBar(IToolStackView tool, ModifierEntry entry);
    int getEnergyBarWidth(IToolStackView tool, ModifierEntry entry);

    record AllMerger(Collection<ICustomEnergyBarModifierHook> modules) implements ICustomEnergyBarModifierHook {
        @Override
        public boolean showEnergyBar(IToolStackView tool, ModifierEntry entry){
            boolean show =false;
            for (ICustomEnergyBarModifierHook module:this.modules){
                show =module.showEnergyBar(tool,entry);
                if (show) break;
            }
            return show;
        }

        @Override
        public int getEnergyBarWidth(IToolStackView tool, ModifierEntry entry) {
            int width = 0;
            for (ICustomEnergyBarModifierHook module:this.modules){
                width = module.getEnergyBarWidth(tool,entry);
                if (width>0) break;
            }
            return width;
        }
    }

}
