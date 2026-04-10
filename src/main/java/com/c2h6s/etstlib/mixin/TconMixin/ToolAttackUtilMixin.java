package com.c2h6s.etstlib.mixin.TconMixin;

import com.c2h6s.etstlib.content.misc.EtSTLibToolAttackTweak;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.helper.ToolAttackUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

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

    @ModifyArg(method = "performAttack",at = @At(value = "INVOKE", target = "Lslimeknights/tconstruct/library/modifiers/hook/combat/MeleeHitModifierHook;beforeMeleeHit(Lslimeknights/tconstruct/library/tools/nbt/IToolStackView;Lslimeknights/tconstruct/library/modifiers/ModifierEntry;Lslimeknights/tconstruct/library/tools/context/ToolAttackContext;FFF)F"),index = 3)
    private static float cacheDamage(float damage){
        EtSTLibToolAttackTweak.setCachedDamage(damage);
        return damage;
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
