package com.c2h6s.etstlib.register;

import com.c2h6s.etstlib.EtSTLib;
import com.c2h6s.etstlib.content.misc.vibration.VibrationContext;
import com.c2h6s.etstlib.tool.hooks.*;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.module.ModuleHook;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;

import java.util.UUID;

public class EtSTLibHooks {
    public static final ModuleHook<CorrectDropModifierHook> CORRECT_TOOL = ModifierHooks.register(EtSTLib.getResourceLocation("correct_tool"), CorrectDropModifierHook.class, CorrectDropModifierHook.AllMerger::new, (tool, entry, state, drop) -> false);
    public static final ModuleHook<EffectApplicableModifierHook> EFFECT_APPLICABLE = ModifierHooks.register(EtSTLib.getResourceLocation("effect_applicable"), EffectApplicableModifierHook.class, EffectApplicableModifierHook.AllMerger::new,(tool, entry, equipmentSlot, instance, applicable)->applicable);
    public static final ModuleHook<CriticalAttackModifierHook> CRITICAL_ATTACK = ModifierHooks.register(EtSTLib.getResourceLocation("critical_attack"), CriticalAttackModifierHook.class, CriticalAttackModifierHook.FirstMerger::new,(tool, entry, attacker, hand, target, sourceSlot, isFullyCharged, isExtraAttack, isCritical)->isCritical);
    public static final ModuleHook<OnDeathModifierHook> ON_DEATH = ModifierHooks.register(EtSTLib.getResourceLocation("on_death"), OnDeathModifierHook.class, OnDeathModifierHook.AllMerger::new, (tool, modifier, context, slotType, source, victim, isAliveSource) -> {});
    public static final ModuleHook<ModifyDamageSourceModifierHook> MODIFY_DAMAGE_SOURCE = ModifierHooks.register(EtSTLib.getResourceLocation("modify_damage_source"), ModifyDamageSourceModifierHook.class, ModifyDamageSourceModifierHook.AllMerger::new, new ModifyDamageSourceModifierHook() {});
    public static final ModuleHook<LeftClickModifierHook> LEFT_CLICK = ModifierHooks.register(EtSTLib.getResourceLocation("left_click"), LeftClickModifierHook.class, LeftClickModifierHook.AllMerger::new, new LeftClickModifierHook() {});
    public static final ModuleHook<CustomBarDisplayModifierHook> CUSTOM_BAR = ModifierHooks.register(EtSTLib.getResourceLocation("custom_bar"), CustomBarDisplayModifierHook.class, CustomBarDisplayModifierHook.FirstMerger::new,new CustomBarDisplayModifierHook(){
        @Override
        public String barId(IToolStackView tool, ModifierEntry entry, int barsHadBeenShown) {
            return null;
        }

        @Override
        public boolean showBar(IToolStackView tool, ModifierEntry entry, int barsHadBeenShown) {
            return false;
        }

        @Override
        public Vec2 getBarXYSize(IToolStackView tool, ModifierEntry entry, int barsHadBeenShown) {
            return null;
        }

        @Override
        public Vec2 getBarXYPos(IToolStackView tool, ModifierEntry entry, int barsHadBeenShown) {
            return null;
        }

        @Override
        public int getBarRGB(IToolStackView tool, ModifierEntry entry, int barsHadBeenShown) {
            return 0;
        }
    });
    public static final ModuleHook<ProjectileTickModifierHook> PROJECTILE_TICK = ModifierHooks.register(EtSTLib.getResourceLocation("projectile_tick"), ProjectileTickModifierHook.class, ProjectileTickModifierHook.AllMerger::new, new ProjectileTickModifierHook() {
        @Override
        public void onProjectileTick(ModifierNBT modifiers, ModifierEntry entry, Level level, @NotNull Projectile projectile, ModDataNBT persistentData, boolean hasBeenShot, boolean leftOwner) {
        }
        @Override
        public void onArrowTick(ModifierNBT modifiers, ModifierEntry entry, Level level, @NotNull AbstractArrow arrow, ModDataNBT persistentData, boolean hasBeenShot, boolean leftOwner,boolean inGround, @Nullable IntOpenHashSet piercingIgnoreEntityIds) {
        }
    });
    public static final ModuleHook<OnHoldingPreventDeathHook> PREVENT_DEATH = ModifierHooks.register(EtSTLib.getResourceLocation("holding_death"), OnHoldingPreventDeathHook.class, OnHoldingPreventDeathHook.FirstMerger::new, new OnHoldingPreventDeathHook() {
        @Override
        public float onHoldingPreventDeath(LivingEntity livingEntity, IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, DamageSource source) {
            return 0;
        }

        @Override
        public boolean canIgnorePassInvul() {
            return false;
        }
    });
    public static final ModuleHook<ArrowDamageModifierHook> ARROW_DAMAGE = ModifierHooks.register(EtSTLib.getResourceLocation("arrow_damage"), ArrowDamageModifierHook.class, ArrowDamageModifierHook.AllMerger::new, (modDataNBT,modifierEntry,modifierEntries,arrow,living,entity,baseDamage,damage) -> damage);
    public static final ModuleHook<ArrowHitModifierHook> ARROW_HIT = ModifierHooks.register(EtSTLib.getResourceLocation("arrow_hit"), ArrowHitModifierHook.class, ArrowHitModifierHook.AllMerger::new, new ArrowHitModifierHook() {
        @Override
        public void afterArrowHit(ModDataNBT persistentData, ModifierEntry entry, ModifierNBT modifiers, AbstractArrow arrow, @Nullable LivingEntity attacker, @NotNull LivingEntity target, float damageDealt) {
        }
    });
    public static final ModuleHook<VibrationListeningModifierHook> VIBRATION_LISTENING = ModifierHooks.register(EtSTLib.getResourceLocation("vibration_listening"), VibrationListeningModifierHook.class, VibrationListeningModifierHook.AllMerger::new, new VibrationListeningModifierHook() {
        @Override
        public UUID getAcceptorUUID(IToolStackView tool, ModifierEntry modifier, Player player, Level level, EquipmentSlot slot) {
            return null;
        }

        @Override
        public int listenRange(IToolStackView tool, ModifierEntry modifier, Player player, Level level, EquipmentSlot slot, int range) {
            return 0;
        }

        @Override
        public boolean canReceiveVibration(IToolStackView tool, ModifierEntry modifier, Player player, ServerLevel level, EquipmentSlot slot, VibrationContext context) {
            return false;
        }

        @Override
        public void onReceivingVibration(IToolStackView tool, ModifierEntry modifier, Player player, ServerLevel level, EquipmentSlot slot, VibrationContext context) {

        }
    });
}
