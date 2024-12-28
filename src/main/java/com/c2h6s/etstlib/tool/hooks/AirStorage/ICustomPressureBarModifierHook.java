package com.c2h6s.etstlib.tool.hooks.AirStorage;

import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Collection;

public interface ICustomPressureBarModifierHook {
    boolean showPressureBar(IToolStackView tool, ModifierEntry entry);
    int getPressureBarWidth(IToolStackView tool, ModifierEntry entry);

    record AllMerger(Collection<ICustomPressureBarModifierHook> modules) implements ICustomPressureBarModifierHook {
        @Override
        public boolean showPressureBar(IToolStackView tool, ModifierEntry entry){
            boolean show =false;
            for (ICustomPressureBarModifierHook module:this.modules){
                show =module.showPressureBar(tool,entry);
                if (show) break;
            }
            return show;
        }

        @Override
        public int getPressureBarWidth(IToolStackView tool, ModifierEntry entry) {
            int width = 0;
            for (ICustomPressureBarModifierHook module:this.modules){
                width = module.getPressureBarWidth(tool,entry);
                if (width>0) break;
            }
            return width;
        }
    }

}
