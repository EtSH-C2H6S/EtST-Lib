package com.c2h6s.etstlib.tool.hooks;

import net.minecraft.world.level.block.state.BlockState;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Collection;

public interface CorrectDropModifierHook {
    //Used to determine if tool is suitable for dropping item when breaking a block.
    Boolean isCorrectToolForDrop(IToolStackView tool, ModifierEntry entry, BlockState state, Boolean drop);
    record FirstMerger(Collection<CorrectDropModifierHook> modules) implements CorrectDropModifierHook {
        @Override
        public Boolean isCorrectToolForDrop(IToolStackView tool, ModifierEntry entry, BlockState state,Boolean drop) {
            for (CorrectDropModifierHook module:this.modules){
                Boolean isDrop =module.isCorrectToolForDrop(tool,entry,state,drop);
                if (isDrop!=null){
                    return isDrop;
                }
            }
            return drop;
        }
    }
}
