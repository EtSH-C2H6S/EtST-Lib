package com.c2h6s.etstlib.tool.modifiers.Combat;

import com.c2h6s.etstlib.EtSTLib;
import com.c2h6s.etstlib.register.EtSTLibHooks;
import com.c2h6s.etstlib.tool.hooks.ItemFoilModifierHook;
import com.c2h6s.etstlib.tool.modifiers.base.EtSTBaseModifier;
import net.minecraft.ChatFormatting;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.mining.BlockBreakModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.context.ToolHarvestContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;

import java.util.Arrays;
import java.util.List;

public class StaticDischarge extends EtSTBaseModifier implements ItemFoilModifierHook, BlockBreakModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, EtSTLibHooks.ITEM_FOIL, ModifierHooks.BLOCK_BREAK);
    }
    public static int CFG_CHARGE_REQUIRED = 3;
    public static float CFG_CHARGE_ATTACK_MUL = 2;
    public static int CFG_CHARGE_ATTACK_EFFECT_LEVEL = 2;
    public static int CFG_CHARGE_ATTACK_EFFECT_TICKS = 50;
    public static final ResourceLocation KEY_STATIC_ELECTRICITY = EtSTLib.getResourceLocation("static_electricity");

    @Override
    public boolean hasFoil(IToolStackView tool, ModifierEntry entry) {
        return isStaticReady(tool);
    }
    public static boolean isStaticReady(IToolStackView tool){
        return tool.getPersistentData().getInt(KEY_STATIC_ELECTRICITY)>=CFG_CHARGE_REQUIRED;
    }
    public static void releaseStatic(IToolStackView tool,ModifierEntry entry,@NotNull LivingEntity user){
        if (user.level().isClientSide) return;
        tool.getPersistentData().remove(KEY_STATIC_ELECTRICITY);
        user.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, CFG_CHARGE_ATTACK_EFFECT_TICKS *entry.getLevel(),
                CFG_CHARGE_ATTACK_EFFECT_LEVEL -1,false,false));
        user.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, CFG_CHARGE_ATTACK_EFFECT_TICKS *entry.getLevel(),
                CFG_CHARGE_ATTACK_EFFECT_LEVEL -1,false,false));
        user.level().playSound(null,user.getX(),user.getY(),user.getZ(),
                SoundEvents.FIREWORK_ROCKET_BLAST, SoundSource.PLAYERS,1f,1.5f);
    }
    public static void gainStatic(IToolStackView tool,LivingEntity holder){
        var charge = tool.getPersistentData().getInt(KEY_STATIC_ELECTRICITY);
        if (charge<CFG_CHARGE_REQUIRED){
            charge++;
            if (charge==CFG_CHARGE_REQUIRED) holder.level().playSound(null,holder.getX(),holder.getY(),holder.getZ(),
                    SoundEvents.PUFFER_FISH_BLOW_UP, SoundSource.PLAYERS,1f,1.25f);
            tool.getPersistentData().putInt(KEY_STATIC_ELECTRICITY,charge);
        }
    }

    @Override
    public void modifierOnInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity holder, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        if (!world.isClientSide&&holder.getDeltaMovement().length()>0.1&&world.getGameTime()%40==0){
            gainStatic(tool,holder);
        }
    }

    @Override
    public void modifierProjectileLaunch(IToolStackView tool, ModifierEntry modifier, LivingEntity shooter, Projectile projectile, @Nullable AbstractArrow arrow, ModDataNBT persistentData, boolean primary) {
        if (!isStaticReady(tool)) {
            gainStatic(tool,shooter);
        } else if (primary){
            persistentData.putInt(KEY_STATIC_ELECTRICITY,1);
            releaseStatic(tool,modifier,shooter);
        }
    }

    @Override
    public float onGetMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage) {
        if (isStaticReady(tool)){
            releaseStatic(tool,modifier,context.getAttacker());
            damage*=CFG_CHARGE_ATTACK_MUL;
        } else gainStatic(tool,context.getAttacker());
        return damage;
    }

    @Override
    public void afterBlockBreak(IToolStackView tool, ModifierEntry modifier, ToolHarvestContext context) {
        if (context.getWorld().isClientSide) return;
        if (isStaticReady(tool)) releaseStatic(tool,modifier,context.getLiving());
        else gainStatic(tool,context.getLiving());
    }

    @Override
    public List<Component> getDescriptionList() {
        if (descriptionList == null) {
            descriptionList = Arrays.asList(
                    Component.translatable(getTranslationKey() + ".flavor").withStyle(ChatFormatting.ITALIC),
                    Component.translatable(getTranslationKey() + ".description",CFG_CHARGE_REQUIRED,CFG_CHARGE_ATTACK_MUL,
                            CFG_CHARGE_ATTACK_EFFECT_TICKS,CFG_CHARGE_ATTACK_EFFECT_LEVEL).withStyle(ChatFormatting.GRAY));
        }
        return descriptionList;
    }

    @Override
    public Component getDisplayName(IToolStackView tool, ModifierEntry entry, @Nullable RegistryAccess access) {
        return super.getDisplayName(tool, entry, access).copy().append(" ["+tool.getPersistentData().getInt(KEY_STATIC_ELECTRICITY)+"]");
    }
}
