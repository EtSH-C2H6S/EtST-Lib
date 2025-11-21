package com.c2h6s.etstlib.mixin.TconMixin;

import com.c2h6s.etstlib.content.misc.EtSTLibToolAttackTweak;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.helper.ToolAttackUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.List;

@Mixin(remap = false,value = ToolAttackUtil.class)
public class ToolAttackUtilMixin {
    @Inject(at = @At(value = "HEAD"),method = "attackEntity(Lslimeknights/tconstruct/library/tools/nbt/IToolStackView;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/entity/Entity;)Z")
    private static void initAttack(IToolStackView tool, Player attacker, Entity target, CallbackInfoReturnable<Boolean> cir){
        EtSTLibToolAttackTweak.onStart(tool);
    }

    @Inject(at = @At("HEAD"),method = "performAttack")
    private static void processContext(IToolStackView tool, ToolAttackContext context, CallbackInfoReturnable<Boolean> cir){
        EtSTLibToolAttackTweak.processContext(tool,context);
    }

    @Inject(at = @At(value = "RETURN"),method = "performAttack")
    private static void updateStatus(IToolStackView tool, ToolAttackContext context, CallbackInfoReturnable<Boolean> cir){
        EtSTLibToolAttackTweak.onEnd();
    }

    @Inject(method = "performAttack",at = @At(value = "INVOKE", target = "Lslimeknights/tconstruct/library/tools/context/ToolAttackContext;isExtraAttack()Z"),locals = LocalCapture.CAPTURE_FAILHARD)
    private static void cacheDamage(IToolStackView tool, ToolAttackContext context, CallbackInfoReturnable<Boolean> cir, float baseDamage, float damage, List modifiers, boolean isMagic, float criticalModifier, float cooldown, float oldHealth, LivingEntity targetLiving, float baseKnockback, float knockback, LivingEntity attackerLiving, EquipmentSlot sourceSlot, AttributeInstance knockbackModifier, Projectile projectile, Entity targetEntity){
        EtSTLibToolAttackTweak.setCachedDamage(damage);
    }

    @Inject(at = @At(value = "HEAD"),method = "getCriticalModifier")
    private static void storeVariable(LivingEntity attacker, Player attackerPlayer, Entity target, LivingEntity livingTarget, boolean fullyCharged, CallbackInfoReturnable<Float> cir){
        EtSTLibToolAttackTweak.initCritical(attacker,target,fullyCharged);
    }

    @ModifyVariable(at = @At("STORE"),method = "getCriticalModifier",ordinal = 1)
    private static boolean processCritical(boolean isCritical){
        return EtSTLibToolAttackTweak.processCritical(isCritical);
    }

}
