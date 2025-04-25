package com.c2h6s.etstlib.tool.modifiers.Common;

import com.c2h6s.etstlib.EtSTLib;
import com.c2h6s.etstlib.content.misc.vibration.VibrationContext;
import com.c2h6s.etstlib.register.EtSTLibHooks;
import com.c2h6s.etstlib.tool.hooks.VibrationListeningModifierHook;
import com.c2h6s.etstlib.tool.modifiers.base.EtSTBaseModifier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.List;
import java.util.UUID;

public class Resonating extends EtSTBaseModifier implements VibrationListeningModifierHook {
    public static final UUID RESONATING_UUID = UUID.fromString("a9869566-5540-d75f-5d12-b5b0393fc87e");
    public static final String KEY_FREQ = "resonating";
    public static final ResourceLocation KEY = EtSTLib.getResourceLocation(KEY_FREQ);
    public static final List<MobEffect> effectList = List.of(
            MobEffects.DAMAGE_BOOST,
            MobEffects.DIG_SPEED,
            MobEffects.ABSORPTION,
            MobEffects.LUCK,
            MobEffects.DAMAGE_RESISTANCE,
            MobEffects.SATURATION,
            MobEffects.HEAL
    );

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, EtSTLibHooks.VIBRATION_LISTENING, ModifierHooks.EQUIPMENT_CHANGE);
    }

    @Override
    public void onAcceptorTick(IToolStackView tool, ModifierEntry modifier, Player player, ServerLevel level, EquipmentSlot slot, int acceptorLevel) {
        if (tool.getPersistentData().getInt(KEY) > 0) {
            tool.getPersistentData().putInt(KEY, tool.getPersistentData().getInt(KEY) - 1);
        }
        if (level.getGameTime() % 40 == 0 && player.getPersistentData().getInt(KEY_FREQ) > 0) {
            int bonus = player.getPersistentData().getInt(KEY_FREQ) / 9;
            player.getPersistentData().putInt(KEY_FREQ, 0);
            bonus = Math.min(bonus, 6);
            for (int i = 0; i < bonus; i++) {
                int effectLevel = acceptorLevel-1;
                MobEffect effect = effectList.get(i);
                if (effect==MobEffects.DAMAGE_RESISTANCE||effect==MobEffects.HEAL) effectLevel = Math.min(2,effectLevel);
                player.addEffect(new MobEffectInstance(effect,60,effectLevel,false,false));
            }
        }
    }

    @Override
    public UUID getAcceptorUUID(IToolStackView tool, ModifierEntry modifier, Player player, Level level, EquipmentSlot slot) {
        return RESONATING_UUID;
    }

    @Override
    public int listenRange(IToolStackView tool, ModifierEntry modifier, Player player, Level level, EquipmentSlot slot, int range) {
        return 16;
    }

    @Override
    public boolean canReceiveVibration(IToolStackView tool, ModifierEntry modifier, Player player, ServerLevel level, EquipmentSlot slot, VibrationContext context) {
        return tool.getPersistentData().getInt(KEY)<=0;
    }

    @Override
    public void onReceivingVibration(IToolStackView tool, ModifierEntry modifier, Player player, ServerLevel level, EquipmentSlot slot, VibrationContext context) {
        CompoundTag nbt = player.getPersistentData();
        nbt.putInt(KEY_FREQ, nbt.getInt(KEY_FREQ)+context.frequency);
        tool.getPersistentData().putInt(KEY,40);
    }
}
