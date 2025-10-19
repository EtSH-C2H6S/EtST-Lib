package com.c2h6s.etstlib.mixin.TconMixin;

import com.c2h6s.etstlib.mixinUtil.MixinTemp;
import com.c2h6s.etstlib.entity.specialDamageSources.LegacyDamageSource;
import com.c2h6s.etstlib.register.EtSTLibHooks;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.helper.ToolAttackUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.function.DoubleSupplier;

import static com.c2h6s.etstlib.mixinUtil.MixinTemp.attackUtilTemp;
import static com.c2h6s.etstlib.mixinUtil.MixinTemp.attackUtilTemp.isCritical;

@Mixin(remap = false,value = ToolAttackUtil.class)
public class ToolAttackUtilMixin {
    @Inject(at = @At(value = "HEAD"),method = "performAttack")
    private static void storingVariable(IToolStackView tool, ToolAttackContext context, CallbackInfoReturnable<Boolean> cir){
        attackUtilTemp.tool =tool;
        attackUtilTemp.attacker =context.getAttacker();
        attackUtilTemp.hand =context.getHand();
        attackUtilTemp.target = context.getTarget();
        attackUtilTemp.isFullyCharged = context.isFullyCharged();
        attackUtilTemp.isExtraAttack = context.isExtraAttack();
        attackUtilTemp.sourceSlot =context.getSlotType();
    }

    @ModifyVariable(method = "getCriticalModifier",at = @At(value = "STORE"),ordinal = 1)
    private static boolean setCritical(boolean value){
        if (!attackUtilTemp.isExtraAttack) {
            IToolStackView tool = attackUtilTemp.tool;
            for (ModifierEntry entry : tool.getModifierList()) {
                isCritical = entry.getHook(EtSTLibHooks.CRITICAL_ATTACK).setCritical(tool,entry,attackUtilTemp.attacker,attackUtilTemp.hand,attackUtilTemp.target,attackUtilTemp.sourceSlot,attackUtilTemp.isFullyCharged,attackUtilTemp.isExtraAttack,isCritical);
                if (isCritical) return true;
            }
        }
        return value;
    }


    @Inject(at = @At(value = "INVOKE", target = "Lslimeknights/tconstruct/library/tools/context/ToolAttackContext;getProjectile()Lnet/minecraft/world/entity/projectile/Projectile;"),method = "performAttack")
    private static void updateStatus(IToolStackView tool, ToolAttackContext context, CallbackInfoReturnable<Boolean> cir){
        MixinTemp.isProcessingDamageSource=true;
        isCritical=false;
    }
}
