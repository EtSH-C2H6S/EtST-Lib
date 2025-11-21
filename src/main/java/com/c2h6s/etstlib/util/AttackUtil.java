package com.c2h6s.etstlib.util;

import com.c2h6s.etstlib.content.misc.EtSTLibToolAttackTweak;
import com.c2h6s.etstlib.entity.specialDamageSources.LegacyDamageSource;
import com.c2h6s.etstlib.register.EtSTLibHooks;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.definition.module.ToolHooks;
import slimeknights.tconstruct.library.tools.definition.module.weapon.MeleeHitToolHook;
import slimeknights.tconstruct.library.tools.helper.ModifierLootingHandler;
import slimeknights.tconstruct.library.tools.helper.ToolAttackUtil;
import slimeknights.tconstruct.library.tools.helper.ToolDamageUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.function.DoubleSupplier;

import static net.minecraft.world.entity.LivingEntity.DATA_HEALTH_ID;
import static slimeknights.tconstruct.library.tools.helper.ToolAttackUtil.*;

public class AttackUtil {

    private static final float DEGREE_TO_RADIANS = (float)Math.PI / 180F;
    private static final AttributeModifier ANTI_KNOCKBACK_MODIFIER = new AttributeModifier(TConstruct.MOD_ID + ".anti_knockback", 1f, AttributeModifier.Operation.ADDITION);

    public static boolean attackEntity(IToolStackView tool, LivingEntity attacker, Entity target,float damageOffset,float damageModifier,boolean noToolDamage){
        if (!ToolAttackUtil.canPerformAttack(tool)) return false;
        EtSTLibToolAttackTweak.onStart(tool);
        return performAttack(tool,ToolAttackContext.attacker(attacker).target(target).defaultCooldown().applyAttributes().build(),damageOffset,damageModifier,noToolDamage);
    }

    //修改过的近战攻击过程，保留全部攻击过程的同时允许不损坏工具和施加全局伤害修正。
    public static boolean performAttack(IToolStackView tool, ToolAttackContext context,float damageOffset,float damageModifier,boolean noToolDamage) {
        EtSTLibToolAttackTweak.processContext(tool,context);
        float baseDamage = context.getBaseDamage();
        float damage = baseDamage;
        List<ModifierEntry> modifiers = tool.getModifierList();
        for (ModifierEntry entry : modifiers) {
            damage = entry.getHook(ModifierHooks.MELEE_DAMAGE).getMeleeDamage(tool, entry, context, baseDamage, damage);
        }
        damage+=damageOffset;
        if (damage <= 0) {
            EtSTLibToolAttackTweak.onEnd();
            return false;
        }
        boolean isMagic = damage > baseDamage;

        float criticalModifier = context.getCriticalModifier();
        if (criticalModifier != 1) {
            damage += baseDamage * (criticalModifier - 1);
        }

        float cooldown = context.getCooldown();
        if (cooldown < 1) {
            damage *= (0.2f + cooldown * cooldown * 0.8f);
        }

        float oldHealth = 0.0F;
        LivingEntity targetLiving = context.getLivingTarget();
        if (targetLiving != null) {
            oldHealth = targetLiving.getHealth();
        }

        float baseKnockback = context.getBaseKnockback();
        float knockback = baseKnockback;
        for (ModifierEntry entry : modifiers) {
            knockback = entry.getHook(ModifierHooks.MELEE_HIT).beforeMeleeHit(tool, entry, context, damage, baseKnockback, knockback);
        }

        LivingEntity attackerLiving = context.getAttacker();
        EquipmentSlot sourceSlot = context.getSlotType();
        ModifierLootingHandler.setLootingSlot(attackerLiving, sourceSlot);

        AttributeInstance knockbackModifier = null;
        if (knockback < 0.4f) {
            knockbackModifier = ToolAttackUtil.disableKnockback(targetLiving);
        } else if (targetLiving != null) {
            knockback -= 0.4f;
        }
        damage*=damageModifier;
        boolean didHit;
        Projectile projectile = context.getProjectile();
        Entity targetEntity = context.getTarget();
        boolean isExtraAttack = context.isExtraAttack();
        EtSTLibToolAttackTweak.setCachedDamage(damage);
        if (isExtraAttack) {
            didHit = targetEntity.hurt(context.makeDamageSource(), damage);
        } else {
            didHit = MeleeHitToolHook.dealDamage(tool, context, damage);
        }

        ModifierLootingHandler.setLootingSlot(attackerLiving, EquipmentSlot.MAINHAND);
        if (knockbackModifier != null) {
            enableKnockback(knockbackModifier);
        }

        Level level = context.getLevel();
        if (!didHit) {
            if (!isExtraAttack) {
                level.playSound(null, attackerLiving.getX(), attackerLiving.getY(), attackerLiving.getZ(), SoundEvents.PLAYER_ATTACK_NODAMAGE, attackerLiving.getSoundSource(), 1.0F, 1.0F);
            }
            for (ModifierEntry entry : modifiers) {
                entry.getHook(ModifierHooks.MELEE_HIT).failedMeleeHit(tool, entry, context, damage);
            }
            EtSTLibToolAttackTweak.onEnd();
            return false;
        }

        float damageDealt = damage;
        if (targetLiving != null) {
            damageDealt = oldHealth - targetLiving.getHealth();
        }

        if (knockback > 0) {
            if (targetLiving != null) {
                targetLiving.knockback(knockback, Mth.sin(attackerLiving.getYRot() * DEGREE_TO_RADIANS), -Mth.cos(attackerLiving.getYRot() * DEGREE_TO_RADIANS));
            } else {
                targetEntity.push(-Mth.sin(attackerLiving.getYRot() * DEGREE_TO_RADIANS) * knockback, 0.1d, Mth.cos(attackerLiving.getYRot() * DEGREE_TO_RADIANS) * knockback);
            }
            attackerLiving.setDeltaMovement(attackerLiving.getDeltaMovement().multiply(0.6D, 1.0D, 0.6D));
            attackerLiving.setSprinting(false);
        }

        if (targetEntity.hurtMarked && targetEntity instanceof ServerPlayer serverPlayer) {
            serverPlayer.connection.send(new ClientboundSetEntityMotionPacket(targetEntity));
            targetEntity.hurtMarked = false;
        }

        Player attackerPlayer = context.getPlayerAttacker();
        if (attackerPlayer != null) {
            if (criticalModifier > 1) {
                attackerPlayer.crit(targetEntity);
            }
            if (isMagic) {
                attackerPlayer.magicCrit(targetEntity);
            }
            level.playSound(null, attackerLiving.getX(), attackerLiving.getY(), attackerLiving.getZ(), context.getSound(), attackerLiving.getSoundSource(), 1.0F, 1.0F);
        }
        if (damageDealt > 2.0F && level instanceof ServerLevel server) {
            int particleCount = (int)(damageDealt * 0.5f);
            server.sendParticles(ParticleTypes.DAMAGE_INDICATOR, targetEntity.getX(), targetEntity.getY(0.5), targetEntity.getZ(), particleCount, 0.1, 0, 0.1, 0.2);
        }

        attackerLiving.setLastHurtMob(targetEntity);
        if (targetLiving != null) {
            EnchantmentHelper.doPostHurtEffects(targetLiving, attackerLiving);
        }

        for (ModifierEntry entry : modifiers) {
            entry.getHook(ModifierHooks.MELEE_HIT).afterMeleeHit(tool, entry, context, damageDealt);
        }

        float speed = tool.getStats().get(ToolStats.ATTACK_SPEED);
        int time = Math.round(20f / speed);
        if (time < targetEntity.invulnerableTime) {
            targetEntity.invulnerableTime = (targetEntity.invulnerableTime + time) / 2;
        }

        if (attackerPlayer != null) {
            if (targetLiving != null) {
                if (!level.isClientSide && !isExtraAttack) {
                    ItemStack held = attackerLiving.getItemBySlot(sourceSlot);
                    if (!held.isEmpty()) {
                        held.hurtEnemy(targetLiving, attackerPlayer);
                    }
                }
                attackerPlayer.awardStat(Stats.DAMAGE_DEALT, Math.round(damageDealt * 10.0F));
            }
            if (projectile == null) {
                attackerPlayer.causeFoodExhaustion(0.1F);
            }
            if (!isExtraAttack && projectile == null) {
                attackerPlayer.awardStat(Stats.ITEM_USED.get(tool.getItem()));
            }
        }

        if (!tool.hasTag(TinkerTags.Items.UNARMED)&&!noToolDamage) {
            int durabilityLost = targetLiving != null ? 1 : 0;
            if (!tool.hasTag(TinkerTags.Items.MELEE_PRIMARY)) {
                durabilityLost *= 2;
            }
            if (projectile != null) {
                ToolDamageUtil.damage(tool, durabilityLost, attackerLiving, attackerLiving.getItemBySlot(sourceSlot));
            } else {
                ToolDamageUtil.damageAnimated(tool, durabilityLost, attackerLiving, sourceSlot);
            }
        }
        EtSTLibToolAttackTweak.onEnd();
        return true;
    }

    @Deprecated(forRemoval = true)
    public static boolean attackEntity(IToolStackView tool, LivingEntity attackerLiving, InteractionHand hand,
                                       Entity targetEntity, DoubleSupplier cooldownFunction, boolean isExtraAttack, EquipmentSlot sourceSlot,boolean setDamage,float damageSet,boolean noToolDamage) {
        if (tool.isBroken() || !tool.hasTag(TinkerTags.Items.MELEE)) {
            return false;
        }
        Level level = attackerLiving.level();
        if (level.isClientSide || !targetEntity.isAttackable() || targetEntity.skipAttackInteraction(attackerLiving)) {
            return true;
        }
        LivingEntity targetLiving = getLivingEntity(targetEntity);
        Player attackerPlayer = null;
        if (attackerLiving instanceof Player player) {
            attackerPlayer = player;
        }

        float damage = getAttributeAttackDamage(tool, attackerLiving, sourceSlot);
        if (setDamage) damage = damageSet;

        float cooldown = (float)cooldownFunction.getAsDouble();
        boolean fullyCharged = cooldown > 0.9f;

        boolean isCritical = !isExtraAttack && fullyCharged && attackerLiving.fallDistance > 0.0F && !attackerLiving.onGround() && !attackerLiving.onClimbable()
                && !attackerLiving.isInWater() && !attackerLiving.hasEffect(MobEffects.BLINDNESS)
                && !attackerLiving.isPassenger() && targetLiving != null && !attackerLiving.isSprinting();

        boolean setCritical;
        for (ModifierEntry entry:tool.getModifierList()){
            setCritical = entry.getHook(EtSTLibHooks.CRITICAL_ATTACK).setCritical(tool,entry,attackerLiving,hand,targetEntity,sourceSlot,fullyCharged,isExtraAttack,isCritical);
            isCritical = setCritical;
            break;
        }

        ToolAttackContext context = new ToolAttackContext(attackerLiving, attackerPlayer, hand, sourceSlot, targetEntity, targetLiving, isCritical, cooldown, isExtraAttack);

        float baseDamage = damage;
        List<ModifierEntry> modifiers = tool.getModifierList();
        for (ModifierEntry entry : modifiers) {
            damage = entry.getHook(ModifierHooks.MELEE_DAMAGE).getMeleeDamage(tool, entry, context, baseDamage, damage);
        }

        if (damage <= 0) {
            return !isExtraAttack;
        }

        float knockback = (float)attackerLiving.getAttributeValue(Attributes.ATTACK_KNOCKBACK) / 2f;
        if (targetLiving != null) {
            knockback += 0.4f;
        }
        SoundEvent sound;
        if (attackerLiving.isSprinting() && fullyCharged) {
            sound = SoundEvents.PLAYER_ATTACK_KNOCKBACK;
            knockback += 0.5f;
        } else if (fullyCharged) {
            sound = SoundEvents.PLAYER_ATTACK_STRONG;
        } else {
            sound = SoundEvents.PLAYER_ATTACK_WEAK;
        }

        if (!isExtraAttack) {
            float criticalModifier = isCritical ? 1.5f : 1.0f;
            if (attackerPlayer != null) {
                CriticalHitEvent hitResult = ForgeHooks.getCriticalHit(attackerPlayer, targetEntity, isCritical, isCritical ? 1.5F : 1.0F);
                isCritical = hitResult != null;
                if (isCritical) {
                    criticalModifier = hitResult.getDamageModifier();
                }
            }
            if (isCritical) {
                damage *= criticalModifier;
            }
        }
        boolean isMagic = damage > baseDamage;
        if (cooldown < 1) {
            damage *= (0.2f + cooldown * cooldown * 0.8f);
        }

        float oldHealth = 0.0F;
        if (targetLiving != null) {
            oldHealth = targetLiving.getHealth();
        }

        float baseKnockback = knockback;
        for (ModifierEntry entry : modifiers) {
            knockback = entry.getHook(ModifierHooks.MELEE_HIT).beforeMeleeHit(tool, entry, context, damage, baseKnockback, knockback);
        }

        ModifierLootingHandler.setLootingSlot(attackerLiving, sourceSlot);

        Optional<AttributeInstance> knockbackModifier = getKnockbackAttribute(targetLiving);
        boolean canceledKnockback = false;
        if (knockback < 0.4f) {
            canceledKnockback = true;
            knockbackModifier.ifPresent(AttackUtil::disableKnockback);
        } else if (targetLiving != null) {
            knockback -= 0.4f;
        }

        LegacyDamageSource source =LegacyDamageSource.any(attackerLiving instanceof Player player?attackerLiving.damageSources().playerAttack(player):attackerLiving.damageSources().mobAttack(attackerLiving));
        for (ModifierEntry entry:tool.getModifierList()){
            source=entry.getHook(EtSTLibHooks.MODIFY_DAMAGE_SOURCE).modifyDamageSource(tool,entry,attackerLiving,hand,targetEntity,sourceSlot,fullyCharged,isExtraAttack,isCritical,source);
        }

        boolean didHit;
        if (isExtraAttack) {
            didHit = dealDefaultDamage(attackerLiving, targetEntity, damage,source);
        } else {
            didHit = dealModifiedDamage(tool, context, damage,source);
        }

        ModifierLootingHandler.setLootingSlot(attackerLiving, EquipmentSlot.MAINHAND);

        if (canceledKnockback) {
            knockbackModifier.ifPresent(AttackUtil::enableKnockback);
        }

        if (!didHit) {
            if (!isExtraAttack) {
                level.playSound(null, attackerLiving.getX(), attackerLiving.getY(), attackerLiving.getZ(), SoundEvents.PLAYER_ATTACK_NODAMAGE, attackerLiving.getSoundSource(), 1.0F, 1.0F);
            }
            for (ModifierEntry entry : modifiers) {
                entry.getHook(ModifierHooks.MELEE_HIT).failedMeleeHit(tool, entry, context, damage);
            }

            return !isExtraAttack;
        }

        float damageDealt = damage;
        if (targetLiving != null) {
            damageDealt = oldHealth - targetLiving.getHealth();
        }

        if (knockback > 0) {
            if (targetLiving != null) {
                targetLiving.knockback(knockback, Mth.sin(attackerLiving.getYRot() * DEGREE_TO_RADIANS), -Mth.cos(attackerLiving.getYRot() * DEGREE_TO_RADIANS));
            } else {
                targetEntity.push(-Mth.sin(attackerLiving.getYRot() * DEGREE_TO_RADIANS) * knockback, 0.1d, Mth.cos(attackerLiving.getYRot() * DEGREE_TO_RADIANS) * knockback);
            }
            attackerLiving.setDeltaMovement(attackerLiving.getDeltaMovement().multiply(0.6D, 1.0D, 0.6D));
            attackerLiving.setSprinting(false);
        }

        if (targetEntity.hurtMarked && targetEntity instanceof ServerPlayer serverPlayer) {
            serverPlayer.connection.send(new ClientboundSetEntityMotionPacket(targetEntity));
            targetEntity.hurtMarked = false;
        }

        if (attackerPlayer != null) {
            if (isCritical) {
                sound = SoundEvents.PLAYER_ATTACK_CRIT;
                attackerPlayer.crit(targetEntity);
            }
            if (isMagic) {
                attackerPlayer.magicCrit(targetEntity);
            }
            level.playSound(null, attackerLiving.getX(), attackerLiving.getY(), attackerLiving.getZ(), sound, attackerLiving.getSoundSource(), 1.0F, 1.0F);
        }
        if (damageDealt > 2.0F && level instanceof ServerLevel server) {
            int particleCount = (int)(damageDealt * 0.5f);
            server.sendParticles(ParticleTypes.DAMAGE_INDICATOR, targetEntity.getX(), targetEntity.getY(0.5), targetEntity.getZ(), particleCount, 0.1, 0, 0.1, 0.2);
        }

        attackerLiving.setLastHurtMob(targetEntity);
        if (targetLiving != null) {
            EnchantmentHelper.doPostHurtEffects(targetLiving, attackerLiving);
        }

        for (ModifierEntry entry : modifiers) {
            entry.getHook(ModifierHooks.MELEE_HIT).afterMeleeHit(tool, entry, context, damageDealt);
        }

        float speed = tool.getStats().get(ToolStats.ATTACK_SPEED);
        int time = Math.round(20f / speed);
        if (time < targetEntity.invulnerableTime) {
            targetEntity.invulnerableTime = (targetEntity.invulnerableTime + time) / 2;
        }

        if (attackerPlayer != null) {
            if (targetLiving != null) {
                if (!isExtraAttack) {
                    ItemStack held = attackerLiving.getItemBySlot(sourceSlot);
                    if (!held.isEmpty()) {
                        held.hurtEnemy(targetLiving, attackerPlayer);
                    }
                }
                attackerPlayer.awardStat(Stats.DAMAGE_DEALT, Math.round(damageDealt * 10.0F));
            }
            attackerPlayer.causeFoodExhaustion(0.1F);

            if (!isExtraAttack) {
                attackerPlayer.awardStat(Stats.ITEM_USED.get(tool.getItem()));
            }
        }

        if (!tool.hasTag(TinkerTags.Items.UNARMED)&&!noToolDamage) {
            int durabilityLost = targetLiving != null ? 1 : 0;
            if (!tool.hasTag(TinkerTags.Items.MELEE_PRIMARY)) {
                durabilityLost *= 2;
            }
            ToolDamageUtil.damageAnimated(tool, durabilityLost, attackerLiving);
        }

        return true;
    }

    private static Optional<AttributeInstance> getKnockbackAttribute(@Nullable LivingEntity living) {
        return Optional.ofNullable(living)
                .map(e -> e.getAttribute(Attributes.KNOCKBACK_RESISTANCE))
                .filter(attribute -> !attribute.hasModifier(ANTI_KNOCKBACK_MODIFIER));
    }


    private static void disableKnockback(AttributeInstance instance) {
        instance.addTransientModifier(ANTI_KNOCKBACK_MODIFIER);
    }


    private static void enableKnockback(AttributeInstance instance) {
        instance.removeModifier(ANTI_KNOCKBACK_MODIFIER);
    }

    public static boolean dealDefaultDamage(LivingEntity attacker, Entity target, float damage,LegacyDamageSource source) {
        return target.hurt(source, damage);
    }
    public static boolean dealModifiedDamage(IToolStackView tool, ToolAttackContext context, float damage,LegacyDamageSource source) {
        boolean hit = dealDefaultDamage(context.getAttacker(), context.getTarget(), damage,source);
        if (hit) {
            tool.getHook(ToolHooks.MELEE_HIT).afterMeleeHit(tool, context, damage);
        }
        return hit;
    }

    public static boolean hurtEntity(LivingEntity living, float amount, DamageSource source){
        if (living instanceof Player) return false;
        else if (living.level().isClientSide) return false;
        else if (living.isDeadOrDying()) return false;
        else {
            if (living.isSleeping() && !living.level().isClientSide) {
                living.stopSleeping();
            }
            living.setNoActionTime(0);
            boolean flag = false;
            living.walkAnimation.setSpeed(1.5F);
            living.lastHurt = amount;
            living.invulnerableTime = 20;
            actualHurtEntity(living,amount, source);
            living.hurtDuration = 10;
            living.hurtTime = living.hurtDuration;

            Entity entity1 = source.getEntity();
            if (entity1 != null) {
                if (entity1 instanceof LivingEntity living1) {
                    if (!source.is(DamageTypeTags.NO_ANGER)) {
                        living.setLastHurtByMob(living1);
                    }
                }
                if (entity1 instanceof Player player1) {
                    living.setLastHurtByPlayer(player1);
                }
            }

            living.level().broadcastDamageEvent(living, source);
            if (living.isDeadOrDying()) {
                living.die(source);
            }

            living.lastDamageSource = source;
            living.lastDamageStamp = living.level().getGameTime();

            if (entity1 instanceof ServerPlayer) {
                CriteriaTriggers.PLAYER_HURT_ENTITY.trigger((ServerPlayer)entity1, entity1, source, amount, amount, flag);
            }

            return true;
        }
    }
    public static void actualHurtEntity(LivingEntity living,float amount,DamageSource source) {
        if (amount <= 0) return;
        living.getCombatTracker().recordDamage(source, amount);
        setHealth(living,getHealth(living)-amount);
        living.setAbsorptionAmount(living.getAbsorptionAmount() - amount);
        living.gameEvent(GameEvent.ENTITY_DAMAGE);
    }
    public static float getHealth(LivingEntity living) {
        return living.entityData.get(DATA_HEALTH_ID);
    }

    public static void setHealth(LivingEntity living,float pHealth) {
        living.entityData.set(DATA_HEALTH_ID, Mth.clamp(pHealth, 0.0F, living.getMaxHealth()));
    }

}
