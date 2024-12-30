package com.c2h6s.etstlib.mixin.TconMixin;

import com.c2h6s.etstlib.MixinTemp;
import com.c2h6s.etstlib.register.EtSTLibHooks;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.helper.ToolAttackUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.function.DoubleSupplier;

import static com.c2h6s.etstlib.MixinTemp.attackUtilTemp;

@Mixin(remap = false,value = ToolAttackUtil.class)
public class ToolAttackUtilMixin {
    @Inject(at = @At(value = "HEAD"),method = "attackEntity(Lslimeknights/tconstruct/library/tools/nbt/IToolStackView;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/entity/Entity;Ljava/util/function/DoubleSupplier;ZLnet/minecraft/world/entity/EquipmentSlot;)Z")
    private static void storingVariable(IToolStackView tool, LivingEntity attackerLiving, InteractionHand hand, Entity targetEntity, DoubleSupplier cooldownFunction, boolean isExtraAttack, EquipmentSlot sourceSlot, CallbackInfoReturnable<Boolean> cir){
        attackUtilTemp.tool =tool;
        attackUtilTemp.attacker =attackerLiving;
        attackUtilTemp.hand =hand;
        attackUtilTemp.target = targetEntity;
        attackUtilTemp.isFullyCharged = cooldownFunction.getAsDouble()>0.9;
        attackUtilTemp.isExtraAttack = isExtraAttack;
        attackUtilTemp.sourceSlot =sourceSlot;
    }

    @ModifyArg(method = "attackEntity(Lslimeknights/tconstruct/library/tools/nbt/IToolStackView;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/entity/Entity;Ljava/util/function/DoubleSupplier;ZLnet/minecraft/world/entity/EquipmentSlot;)Z",at = @At(value = "INVOKE", target = "Lslimeknights/tconstruct/library/tools/context/ToolAttackContext;<init>(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/entity/EquipmentSlot;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/entity/LivingEntity;ZFZ)V"),index = 6)
    private static boolean setCritical(boolean isCritical){
        if (!attackUtilTemp.isExtraAttack) {
            IToolStackView tool = attackUtilTemp.tool;
            for (ModifierEntry entry : tool.getModifierList()) {
                isCritical = entry.getHook(EtSTLibHooks.CRITICAL_ATTACK).setCritical(tool,entry,attackUtilTemp.attacker,attackUtilTemp.hand,attackUtilTemp.target,attackUtilTemp.sourceSlot,attackUtilTemp.isFullyCharged,attackUtilTemp.isExtraAttack,isCritical);
                if (isCritical) break;
            }
            attackUtilTemp.isCritical = isCritical;
        }
        return isCritical;
    }

    @ModifyArg(method = "attackEntity(Lslimeknights/tconstruct/library/tools/nbt/IToolStackView;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/entity/Entity;Ljava/util/function/DoubleSupplier;ZLnet/minecraft/world/entity/EquipmentSlot;)Z",at = @At(value = "INVOKE", target = "Lnet/minecraftforge/common/ForgeHooks;getCriticalHit(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/entity/Entity;ZF)Lnet/minecraftforge/event/entity/player/CriticalHitEvent;"),index = 2)
    private static boolean setEventCritical(boolean vanillaCritical){
        if (attackUtilTemp.isCritical){
            return true;
        }
        return vanillaCritical;
    }
    @ModifyArg(method = "attackEntity(Lslimeknights/tconstruct/library/tools/nbt/IToolStackView;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/entity/Entity;Ljava/util/function/DoubleSupplier;ZLnet/minecraft/world/entity/EquipmentSlot;)Z",at = @At(value = "INVOKE", target = "Lnet/minecraftforge/common/ForgeHooks;getCriticalHit(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/entity/Entity;ZF)Lnet/minecraftforge/event/entity/player/CriticalHitEvent;"),index = 3)
    private static float setEventCritical(float damageModifier){
        if (attackUtilTemp.isCritical){
            return 1.5f;
        }
        return damageModifier;
    }

    @Inject(at = @At(value = "INVOKE", target = "Lslimeknights/tconstruct/library/tools/helper/ToolAttackUtil;getKnockbackAttribute(Lnet/minecraft/world/entity/LivingEntity;)Ljava/util/Optional;"),method = "attackEntity(Lslimeknights/tconstruct/library/tools/nbt/IToolStackView;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/entity/Entity;Ljava/util/function/DoubleSupplier;ZLnet/minecraft/world/entity/EquipmentSlot;)Z")
    private static void updateStatus(IToolStackView tool, LivingEntity attackerLiving, InteractionHand hand, Entity targetEntity, DoubleSupplier cooldownFunction, boolean isExtraAttack, EquipmentSlot sourceSlot, CallbackInfoReturnable<Boolean> cir){
        MixinTemp.isProcessingDamageSource=true;
        attackUtilTemp.isCritical=false;
    }

    @Inject(at = @At(value = "HEAD"),method = "dealDefaultDamage")
    private static void getDamageSource(LivingEntity attacker, Entity target, float damage, CallbackInfoReturnable<Boolean> cir){
        if (MixinTemp.isProcessingDamageSource){
            IToolStackView tool = attackUtilTemp.tool;
            DamageSource damageSource =null;
            for (ModifierEntry entry : tool.getModifierList()) {
                damageSource = entry.getHook(EtSTLibHooks.MODIFY_DAMAGE_SOURCE).modifyDamageSource(tool,entry,attackUtilTemp.attacker,attackUtilTemp.hand,attackUtilTemp.target,attackUtilTemp.sourceSlot,attackUtilTemp.isFullyCharged,attackUtilTemp.isExtraAttack,false);
                if (damageSource!=null){
                    attackUtilTemp.damageSource =damageSource;
                    break;
                }
            }
            if (damageSource==null) MixinTemp.isProcessingDamageSource =false;
        }
    }

    @ModifyArg(method = "dealDefaultDamage",at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;m_6469_(Lnet/minecraft/world/damagesource/DamageSource;F)Z"))
    private static DamageSource modifyDamageSource(DamageSource par1){
        if (MixinTemp.isProcessingDamageSource&&attackUtilTemp.damageSource!=null){
            return attackUtilTemp.damageSource;
        }
        return par1;
    }

}
