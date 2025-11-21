package com.c2h6s.etstlib.mixin.TconMixin;

import com.c2h6s.etstlib.content.misc.EtSTLibToolAttackTweak;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;

@Mixin(value = ToolAttackContext.Builder.class,remap = false)
public class ToolAttackContextBuilderMixin {
    @Inject(at = @At("HEAD"),method = "build")
    public void storeContextBuilder(CallbackInfoReturnable<ToolAttackContext> cir){
        EtSTLibToolAttackTweak.buildCritical((ToolAttackContext.Builder)(Object) this);
    }

}
