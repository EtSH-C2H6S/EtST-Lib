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
    @ModifyArg(method = "onHitEntity",at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"),index =0)
    private DamageSource modifyDamageSource(DamageSource source0){
        AbstractArrow arrow = (AbstractArrow) (Object) this;
        ModifierNBT modifiers = EntityModifierCapability.getOrEmpty(arrow);
        Entity target = arrowHit;
        if (!modifiers.isEmpty()&&target !=null) {
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
    @ModifyArg(method = "onHitEntity",at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"),index =1)
    private float modifyDamage(float pAmount){
        AbstractArrow arrow = (AbstractArrow) (Object) this;
        ModifierNBT modifiers = EntityModifierCapability.getOrEmpty(arrow);
        Entity target = arrowHit;
        float damage = pAmount;
        if (!modifiers.isEmpty()&&target !=null) {
            ModDataNBT nbt = PersistentDataCapability.getOrWarn(arrow);
            LivingEntity attacker = arrow.getOwner() instanceof LivingEntity living?living:null;
            for (ModifierEntry entry:modifiers.getModifiers()){
                damage = entry.getHook(EtSTLibHooks.ARROW_DAMAGE).getArrowDamage(nbt,entry,modifiers,arrow,attacker,target,pAmount,damage);
            }
            return damage;
        }
        return damage;
    }
    @Inject(method = "onHitEntity",at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/AbstractArrow;doPostHurtEffects(Lnet/minecraft/world/entity/LivingEntity;)V"))
    private void doAfterArrowHit(EntityHitResult pResult, CallbackInfo ci){
        AbstractArrow arrow = (AbstractArrow) (Object) this;
        ModifierNBT modifiers = EntityModifierCapability.getOrEmpty(arrow);
        Entity target = arrowHit;
        if (!modifiers.isEmpty()&&target instanceof LivingEntity living) {
            float damageDealt = living.getHealth()- entityHealth;
            ModDataNBT nbt = PersistentDataCapability.getOrWarn(arrow);
            LivingEntity attacker = arrow.getOwner() instanceof LivingEntity entity?entity:null;
            for (ModifierEntry entry:modifiers.getModifiers()){
                entry.getHook(EtSTLibHooks.ARROW_HIT).afterArrowHit(nbt,entry,modifiers,arrow,attacker,living,damageDealt);
            }
        }
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
