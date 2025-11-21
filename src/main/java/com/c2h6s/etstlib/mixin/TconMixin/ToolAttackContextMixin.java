package com.c2h6s.etstlib.mixin.TconMixin;

import com.c2h6s.etstlib.entity.specialDamageSources.LegacyDamageSource;
import com.c2h6s.etstlib.mixinUtil.IToolAttackContextMixin;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;

import javax.annotation.Nonnull;

@Mixin(value = ToolAttackContext.class,remap = false)
public abstract class ToolAttackContextMixin implements IToolAttackContextMixin {
    @Shadow @Nonnull public abstract LivingEntity getAttacker();

    @Mutable
    @Shadow @Final private float criticalModifier;

    @Unique
    private LegacyDamageSource etstlib$sourceReplacement = null;

    @Inject(at = @At("RETURN"),method = "makeDamageSource",cancellable = true)
    public void modifyDamageSource(CallbackInfoReturnable<DamageSource> cir){
        if (etstlib$sourceReplacement !=null) {
            cir.setReturnValue(etstlib$sourceReplacement);
        }
    }

    @Override
    @Unique
    public void etstlib$setCriticalModifier(float criticalModifier) {
        this.criticalModifier = criticalModifier;
    }
    @Unique
    @Override
    public void etstlib$setLegacySource(LegacyDamageSource source) {
        this.etstlib$sourceReplacement = source;
    }
}
