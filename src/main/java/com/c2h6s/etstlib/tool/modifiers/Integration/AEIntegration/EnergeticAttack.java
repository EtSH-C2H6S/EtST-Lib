package com.c2h6s.etstlib.tool.modifiers.Integration.AEIntegration;

import appeng.core.AppEng;
import appeng.core.sync.packets.LightningPacket;
import com.c2h6s.etstlib.entity.specialDamageSources.AncientDamageSource;
import com.c2h6s.etstlib.tool.modifiers.base.EtSTBaseModifier;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;
import slimeknights.tconstruct.library.tools.nbt.NamespacedNBT;

public class EnergeticAttack extends EtSTBaseModifier {
    @Override
    public void afterMeleeHit (IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        Entity entity = context.getTarget();
        Level level  = context.getLevel();
        if (!level.isClientSide&&context.isFullyCharged()) {
            AncientDamageSource source = new AncientDamageSource(level.damageSources().magic().typeHolder(), context.getAttacker());
            source.setBypassInvulnerableTime();
            entity.hurt(source, 3 + modifier.getLevel());
            final AABB entityBoundingBox = entity.getBoundingBox();
            final float dx = (float) (entity.level().getRandom().nextFloat() * entity.getBbWidth() + entityBoundingBox.minX);
            final float dy = (float) (entity.level().getRandom().nextFloat() * entity.getBbHeight() + entityBoundingBox.minY);
            final float dz = (float) (entity.level().getRandom().nextFloat() * entity.getBbWidth() + entityBoundingBox.minZ);
            AppEng.instance().sendToAllNearExcept(null, dx, dy, dz, 32.0, entity.level(), new LightningPacket(dx, dy, dz));
        }

    }

    @Override
    public boolean onProjectileHitEntity(ModifierNBT modifiers, NamespacedNBT persistentData, ModifierEntry modifier, Projectile projectile, EntityHitResult hit, @Nullable LivingEntity attacker, @Nullable LivingEntity target) {
        if (target!=null&&projectile instanceof AbstractArrow arrow&&arrow.isCritArrow()){
            Level level  = target.level();
            if (!level.isClientSide) {
                target.invulnerableTime = 0;
                target.hurt(new DamageSource(level.damageSources().magic().typeHolder(), attacker), 3 + modifier.getLevel());
                target.invulnerableTime = 0;
                final AABB entityBoundingBox = target.getBoundingBox();
                final float dx = (float) (target.level().getRandom().nextFloat() * target.getBbWidth() + entityBoundingBox.minX);
                final float dy = (float) (target.level().getRandom().nextFloat() * target.getBbHeight() + entityBoundingBox.minY);
                final float dz = (float) (target.level().getRandom().nextFloat() * target.getBbWidth() + entityBoundingBox.minZ);
                AppEng.instance().sendToAllNearExcept(null, dx, dy, dz, 32.0, target.level(), new LightningPacket(dx, dy, dz));
            }
        }
        return false;
    }
}
