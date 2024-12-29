package com.c2h6s.etstlib.mixin.AEMixin;

import appeng.blockentity.misc.ChargerRecipes;
import com.c2h6s.etstlib.register.EtSTLibModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

@Mixin(remap = false,value = ChargerRecipes.class)
public class ChargerRecipesMixin {
    @Inject(at = @At(value = "RETURN"),cancellable = true,method = "allowInsert")
    private static void allowToolInsert(Level level, ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (stack.getItem() instanceof IModifiable) {
            ToolStack tool = ToolStack.from(stack);
            if (tool.getModifierLevel(EtSTLibModifier.applied_fixing.get()) > 0 && tool.getDamage() > 0) {
                cir.setReturnValue(true);
            }
        }
    }

    @Inject(at = @At(value = "RETURN"),cancellable = true,method = "allowExtract")
    private static void allowToolExtract(Level level, ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (stack.getItem() instanceof IModifiable) {
            ToolStack tool = ToolStack.from(stack);
            if (tool.getDamage() == 0) {
                cir.setReturnValue(true);
            }
        }
    }
}
