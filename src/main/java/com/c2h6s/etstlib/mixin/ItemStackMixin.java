package com.c2h6s.etstlib.mixin;

import com.c2h6s.etstlib.register.EtSTLibHooks;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    @Inject(at = @At(value = "RETURN"),method = "isCorrectToolForDrops",cancellable = true)
    public void allDrop(BlockState state, CallbackInfoReturnable<Boolean> cir){
        ItemStack stack = (ItemStack) (Object) this;
        if (stack.getItem() instanceof IModifiable){
            ToolStack tool = ToolStack.from(stack);
            boolean drop = cir.getReturnValueZ();
            for (ModifierEntry entry:tool.getModifierList()){
                drop = entry.getHook(EtSTLibHooks.CORRECT_TOOL).isCorrectToolForDrop(tool,entry,state,drop);
                if (drop){
                    break;
                }
            }
            cir.setReturnValue(drop);
        }
    }
}
