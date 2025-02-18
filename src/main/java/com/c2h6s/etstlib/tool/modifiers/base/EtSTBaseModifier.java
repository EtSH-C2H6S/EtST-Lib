package com.c2h6s.etstlib.tool.modifiers.base;

import com.c2h6s.etstlib.register.EtSTLibHooks;
import com.c2h6s.etstlib.tool.hooks.LeftClickModifierHook;
import com.c2h6s.etstlib.tool.hooks.ModifyDamageSourceModifierHook;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.behavior.ToolDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.build.ModifierRemovalHook;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.ranged.ProjectileHitModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.ranged.ProjectileLaunchModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;

import javax.annotation.Nullable;

public class EtSTBaseModifier extends Modifier implements MeleeHitModifierHook, MeleeDamageModifierHook, ProjectileHitModifierHook, ProjectileLaunchModifierHook, ToolDamageModifierHook, InventoryTickModifierHook , ModifierRemovalHook , ModifyDamageSourceModifierHook , LeftClickModifierHook {
    public boolean isNoLevels(){
        return false;
    }
    public @NotNull Component getDisplayName(int level) {
        return this.isNoLevels() ? super.getDisplayName() : super.getDisplayName(level);
    }

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.MELEE_DAMAGE,ModifierHooks.MELEE_HIT,ModifierHooks.PROJECTILE_LAUNCH,ModifierHooks.PROJECTILE_HIT,ModifierHooks.TOOL_DAMAGE,ModifierHooks.INVENTORY_TICK,EtSTLibHooks.MODIFY_DAMAGE_SOURCE,EtSTLibHooks.LEFT_CLICK);
    }

    @Override
    public float getMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage) {
        return this.onGetMeleeDamage(tool,modifier,context,baseDamage,damage);
    }
    @Override
    public void onProjectileLaunch(IToolStackView tool, ModifierEntry modifier, LivingEntity shooter, Projectile projectile, @Nullable AbstractArrow arrow, ModDataNBT persistentData, boolean primary) {
        this.modifierProjectileLaunch(tool,modifier,shooter,projectile,arrow,persistentData,primary);
    }
    @Override
    public int onDamageTool(IToolStackView tool, ModifierEntry modifier, int amount, @Nullable LivingEntity holder) {
        return this.modifierDamageTool(tool,modifier,amount,holder);
    }
    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity holder, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack stack){
        this.modifierOnInventoryTick(tool,modifier,world,holder,itemSlot,isSelected,isCorrectSlot,stack);
    }
    @org.jetbrains.annotations.Nullable
    @Override
    public Component onRemoved(IToolStackView tool, Modifier modifier) {
        return this.onModifierRemoved(tool,modifier);
    }

    public float onGetMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage) {
        return damage;
    }
    public void modifierProjectileLaunch(IToolStackView tool, ModifierEntry modifier, LivingEntity shooter, Projectile projectile, @Nullable AbstractArrow arrow, ModDataNBT persistentData, boolean primary) {

    }
    public int modifierDamageTool(IToolStackView tool, ModifierEntry modifier, int amount, @Nullable LivingEntity holder) {
        return amount;
    }
    public void modifierOnInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity holder, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack stack){

    }
    public Component onModifierRemoved(IToolStackView tool, Modifier modifier) {
        return null;
    }



}
