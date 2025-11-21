package com.c2h6s.etstlib.tool.modifiers.Integration.MekIntegration;

import com.c2h6s.etstlib.tool.modifiers.base.EtSTBaseModifier;
import com.c2h6s.etstlib.util.CommonConstants;
import mekanism.api.radiation.IRadiationManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;

import javax.annotation.Nullable;

public class RadiationInflict extends EtSTBaseModifier {
    @Override
    public void postMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt,float f) {
        if (!context.getLevel().isClientSide&&context.isFullyCharged()) {
            Entity entity = context.getTarget();
            if (entity instanceof LivingEntity living) {
                IRadiationManager.INSTANCE.radiate(living, modifier.getLevel()*0.1);
            }
        }
    }

    @Override
    public void afterArrowHit(ModDataNBT persistentData, ModifierEntry entry, ModifierNBT modifiers, AbstractArrow arrow, @org.jetbrains.annotations.Nullable LivingEntity attacker, @NotNull LivingEntity target, float damageDealt) {
        if (!arrow.level().isClientSide&&arrow.getTags().contains(CommonConstants.KEY_CRITARROW)){
            IRadiationManager.INSTANCE.radiate(target, entry.getLevel()*0.1);
        }
    }

    @Override
    public boolean onProjectileHitEntity(ModifierNBT modifiers, ModDataNBT persistentData, ModifierEntry modifier, Projectile projectile, EntityHitResult hit, @javax.annotation.Nullable LivingEntity attacker, @Nullable LivingEntity target) {
        if (projectile instanceof AbstractArrow arrow &&!arrow.level().isClientSide&&arrow.isCritArrow()&&target!=null){
            IRadiationManager.INSTANCE.radiate(target, modifier.getLevel()*0.1);
        }
        return false;
    }
}
