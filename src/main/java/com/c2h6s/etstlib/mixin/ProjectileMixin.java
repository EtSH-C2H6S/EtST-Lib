package com.c2h6s.etstlib.mixin;

import com.c2h6s.etstlib.MixinTemp;
import com.c2h6s.etstlib.register.EtSTLibHooks;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.capability.EntityModifierCapability;
import slimeknights.tconstruct.library.tools.capability.PersistentDataCapability;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;
import slimeknights.tconstruct.library.tools.nbt.NamespacedNBT;

@Mixin(Projectile.class)
public class ProjectileMixin {
    @Shadow private boolean hasBeenShot;
    @Shadow private boolean leftOwner;
    @Inject(method = "tick",at = @At(value = "HEAD"))
    private void tick(CallbackInfo ci){
        Projectile projectile = (Projectile) (Object)this;
        if (projectile instanceof AbstractArrow arrow){
            MixinTemp.leftOwner = leftOwner;
            MixinTemp.hasBeenShot = hasBeenShot;
            return;
        }
        ModifierNBT nbt =null;
        EntityModifierCapability.EntityModifiers cap= projectile.getCapability(EntityModifierCapability.CAPABILITY).orElse(null);
        NamespacedNBT projectileData = PersistentDataCapability.getOrWarn(projectile);
        if (cap!=null&&!cap.getModifiers().isEmpty()){
            nbt = cap.getModifiers();
            for (ModifierEntry entry:nbt.getModifiers()){
                entry.getHook(EtSTLibHooks.PROJECTILE_TICK).onProjectileTick(nbt,entry,projectile.level(),projectile, projectileData,hasBeenShot,leftOwner);
            }
        }
    }
}
