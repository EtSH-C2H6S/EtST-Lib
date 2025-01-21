package com.c2h6s.etstlib.mixin;

import com.c2h6s.etstlib.MixinTemp;
import com.c2h6s.etstlib.entity.specialDamageSources.LegacyDamageSource;
import com.c2h6s.etstlib.register.EtSTLibHooks;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.phys.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.capability.EntityModifierCapability;
import slimeknights.tconstruct.library.tools.capability.PersistentDataCapability;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;

import java.util.List;

import static com.c2h6s.etstlib.MixinTemp.*;

@Mixin(AbstractArrow.class)
public class AbstractArrowMixin {
    @Shadow
    private IntOpenHashSet piercingIgnoreEntityIds;
    @Shadow
    protected boolean inGround;
    @Inject(method = "onHitEntity",at = @At(value = "HEAD"))
    private void getEntity(EntityHitResult hitResult, CallbackInfo ci){
        MixinTemp.arrowHit = hitResult.getEntity();
    }
    @ModifyArg(method = "onHitEntity",at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"))
    private DamageSource modifyDamageSource(DamageSource source0){
        AbstractArrow arrow = (AbstractArrow) (Object) this;
        ModifierNBT modifiers = EntityModifierCapability.getOrEmpty(arrow);
        if (!modifiers.isEmpty()&&MixinTemp.arrowHit instanceof LivingEntity target) {
            ModDataNBT nbt = PersistentDataCapability.getOrWarn(arrow);
            LivingEntity attacker = arrow.getOwner() instanceof LivingEntity living?living:null;
            LegacyDamageSource damageSource = new LegacyDamageSource(source0);
            for (ModifierEntry entry:modifiers.getModifiers()){
                damageSource = entry.getHook(EtSTLibHooks.MODIFY_DAMAGE_SOURCE).modifyArrowDamageSource(modifiers,nbt,entry,arrow,attacker,target,damageSource);
            }
            return damageSource;
        }
        return source0;
    }
    @Inject(method = "tick",at = @At(value = "HEAD"))
    private void tick(CallbackInfo ci){
        AbstractArrow arrow = (AbstractArrow) (Object)this;
        ModifierNBT nbt =null;
        EntityModifierCapability.EntityModifiers cap= arrow.getCapability(EntityModifierCapability.CAPABILITY).orElse(null);
        ModDataNBT projectileData = PersistentDataCapability.getOrWarn(arrow);
        if (cap!=null&&!cap.getModifiers().isEmpty()){
            nbt = cap.getModifiers();
            for (ModifierEntry entry:nbt.getModifiers()){
                entry.getHook(EtSTLibHooks.PROJECTILE_TICK).onArrowTick(nbt,entry, arrow.level(), arrow, projectileData,hasBeenShot,leftOwner,inGround,piercingIgnoreEntityIds);
            }
        }
    }
}
