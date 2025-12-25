package com.c2h6s.etstlib.tool.modifiers.Integration.AEIntegration;

import com.c2h6s.etstlib.entity.specialDamageSources.LegacyDamageSource;
import com.c2h6s.etstlib.tool.modifiers.base.EtSTBaseModifier;
import com.c2h6s.etstlib.util.CommonConstants;
import com.c2h6s.etstlib.util.integrations.AEIntegrationUtils;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;


import static com.c2h6s.etstlib.util.ModListConstants.AE2Loaded;

public class EnergeticAttack extends EtSTBaseModifier {
    @Override
    public void postMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt,float f) {
        Entity entity = context.getTarget();
        Level level  = context.getLevel();
        if (!level.isClientSide&&context.isFullyCharged()) {
            int it = entity.invulnerableTime;
            entity.hurt(LegacyDamageSource.indirectMagic(context.getAttacker()).setBypassInvulnerableTime(), 2 + modifier.getLevel());
            entity.invulnerableTime = it;

            if (AE2Loaded) {
                final AABB entityBoundingBox = entity.getBoundingBox();
                final float dx = (float) (entity.level().getRandom().nextFloat() * entity.getBbWidth() + entityBoundingBox.minX);
                final float dy = (float) (entity.level().getRandom().nextFloat() * entity.getBbHeight() + entityBoundingBox.minY);
                final float dz = (float) (entity.level().getRandom().nextFloat() * entity.getBbWidth() + entityBoundingBox.minZ);
                AEIntegrationUtils.spawnLightningParticle(entity.level(),dx,dy,dz);
            }
        }

    }

    @Override
    public void afterArrowHit(ModDataNBT persistentData, ModifierEntry entry, ModifierNBT modifiers, AbstractArrow arrow, @org.jetbrains.annotations.Nullable LivingEntity attacker, @NotNull LivingEntity target, float damageDealt) {
        if (arrow!=null&&arrow.getTags().contains(CommonConstants.KEY_CRITARROW)&&attacker==null){
            Level level  = target.level();
            if (!level.isClientSide) {
                int it = target.invulnerableTime;
                target.invulnerableTime = 0;
                target.hurt(new DamageSource(level.damageSources().magic().typeHolder(), attacker), 2 + entry.getLevel());
                target.invulnerableTime = it;
                if (AE2Loaded) {
                    final AABB entityBoundingBox = target.getBoundingBox();
                    final float dx = (float) (target.level().getRandom().nextFloat() * target.getBbWidth() + entityBoundingBox.minX);
                    final float dy = (float) (target.level().getRandom().nextFloat() * target.getBbHeight() + entityBoundingBox.minY);
                    final float dz = (float) (target.level().getRandom().nextFloat() * target.getBbWidth() + entityBoundingBox.minZ);
                    AEIntegrationUtils.spawnLightningParticle(target.level(),dx,dy,dz);
                }
            }
        }
    }

    @Override
    public boolean onProjectileHitEntity(ModifierNBT modifiers, ModDataNBT persistentData, ModifierEntry modifier, Projectile projectile, EntityHitResult hit, @Nullable LivingEntity attacker, @Nullable LivingEntity target, boolean notBlocked) {
        if (attacker!=null&&target!=null&&!projectile.level().isClientSide){
            if (projectile instanceof AbstractArrow arrow&&!arrow.getTags().contains(CommonConstants.KEY_CRITARROW)) return super.onProjectileHitEntity(modifiers, persistentData, modifier, projectile, hit, attacker, target, notBlocked);
            int it = target.invulnerableTime;
            target.invulnerableTime = 0;
            target.hurt(new DamageSource(projectile.damageSources().indirectMagic(attacker,projectile).typeHolder(), attacker), 2 + modifier.getLevel());
            target.invulnerableTime = it;
            if (AE2Loaded) {
                final AABB entityBoundingBox = target.getBoundingBox();
                final float dx = (float) (target.level().getRandom().nextFloat() * target.getBbWidth() + entityBoundingBox.minX);
                final float dy = (float) (target.level().getRandom().nextFloat() * target.getBbHeight() + entityBoundingBox.minY);
                final float dz = (float) (target.level().getRandom().nextFloat() * target.getBbWidth() + entityBoundingBox.minZ);
                AEIntegrationUtils.spawnLightningParticle(target.level(),dx,dy,dz);
            }
        }
        return super.onProjectileHitEntity(modifiers, persistentData, modifier, projectile, hit, attacker, target, notBlocked);
    }
}
