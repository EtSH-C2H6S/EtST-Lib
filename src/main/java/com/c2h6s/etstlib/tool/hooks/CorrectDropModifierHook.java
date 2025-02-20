package com.c2h6s.etstlib.tool.hooks;

import net.minecraft.world.level.block.state.BlockState;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Collection;

public interface CorrectDropModifierHook {
    //Used to force the tool to drop loot from a certain block.
    boolean isCorrectToolForDrop(IToolStackView tool, ModifierEntry entry, BlockState state, boolean drop);
    record AllMerger(Collection<CorrectDropModifierHook> modules) implements CorrectDropModifierHook {
        @Override
        public boolean isCorrectToolForDrop(IToolStackView tool, ModifierEntry entry, BlockState state,boolean drop) {
            for (CorrectDropModifierHook module:this.modules){
                boolean isDrop =module.isCorrectToolForDrop(tool,entry,state,drop);
                if (isDrop){
                    return true;
                }
            }
            return false;
        }
    }
}
