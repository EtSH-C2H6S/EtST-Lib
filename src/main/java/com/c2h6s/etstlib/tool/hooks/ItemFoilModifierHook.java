package com.c2h6s.etstlib.tool.hooks;

import net.minecraft.world.level.block.state.BlockState;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Collection;

public interface ItemFoilModifierHook {
    boolean hasFoil(IToolStackView tool, ModifierEntry entry);
    record FirstMerger(Collection<ItemFoilModifierHook> modules) implements ItemFoilModifierHook {
        @Override
        public boolean hasFoil(IToolStackView tool, ModifierEntry entry) {
            for (ItemFoilModifierHook module:this.modules){
                if (module.hasFoil(tool,entry)){
                    return true;
                }
            }
            return false;
        }
    }
}
