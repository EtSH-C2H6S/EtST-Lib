package com.c2h6s.etstlib.mixin.TconMixin;

import com.c2h6s.etstlib.entity.specialDamageSources.LegacyDamageSource;
import com.c2h6s.etstlib.mixinUtil.MixinTemp;
import com.c2h6s.etstlib.register.EtSTLibHooks;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import javax.annotation.Nonnull;

@Mixin(value = ToolAttackContext.class,remap = false)
public abstract class ToolAttackContextMixin {
    @Shadow @Nonnull public abstract LivingEntity getAttacker();

    @Inject(at = @At("RETURN"),method = "makeDamageSource",cancellable = true)
    public void modifyDamageSource(CallbackInfoReturnable<DamageSource> cir){
        if (MixinTemp.isProcessingDamageSource) {
            IToolStackView tool = MixinTemp.attackUtilTemp.tool;
            LegacyDamageSource damageSource;
            if (getAttacker() instanceof Player player){
                damageSource = LegacyDamageSource.playerAttack(player);
            }
            else damageSource = LegacyDamageSource.mobAttack(getAttacker());
            for (ModifierEntry entry : tool.getModifierList()) {
                damageSource = entry.getHook(EtSTLibHooks.MODIFY_DAMAGE_SOURCE).modifyDamageSource(tool, entry, MixinTemp.attackUtilTemp.attacker, MixinTemp.attackUtilTemp.hand, MixinTemp.attackUtilTemp.target, MixinTemp.attackUtilTemp.sourceSlot, MixinTemp.attackUtilTemp.isFullyCharged, MixinTemp.attackUtilTemp.isExtraAttack, false,damageSource);
            }
            MixinTemp.isProcessingDamageSource=false;
            cir.setReturnValue(damageSource);
        }
    }
}
