package com.c2h6s.etstlib.tool.modifiers.Common;

import com.c2h6s.etstlib.EtSTLib;
import com.c2h6s.etstlib.tool.modifiers.base.EtSTBaseModifier;
import com.c2h6s.etstlib.util.EquipmentUtil;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerEvent;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.behavior.AttributesModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.build.ToolStatsModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.mining.BreakSpeedModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import java.util.UUID;
import java.util.function.BiConsumer;

public class MomentumAccelerate extends EtSTBaseModifier implements ToolStatsModifierHook , AttributesModifierHook, BreakSpeedModifierHook {
    public static final ResourceLocation LOCATION_ACCEL = EtSTLib.getResourceLocation("momentum_accelerate");

    @Override
    public void modifierOnInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity holder, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        if (tool.getPersistentData().getFloat(LOCATION_ACCEL)>0&&!world.isClientSide&&world.getGameTime()%10==0){
            tool.getPersistentData().putFloat(LOCATION_ACCEL,tool.getPersistentData().getFloat(LOCATION_ACCEL)-0.01f*modifier.getLevel());
        }
    }

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.TOOL_STATS,ModifierHooks.ATTRIBUTES,ModifierHooks.BREAK_SPEED);
    }

    @Override
    public Component onModifierRemoved(IToolStackView tool, Modifier modifier) {
        tool.getPersistentData().remove(LOCATION_ACCEL);
        return null;
    }

    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        if (tool.getPersistentData().getFloat(LOCATION_ACCEL)<modifier.getLevel()){
            tool.getPersistentData().putFloat(LOCATION_ACCEL,tool.getPersistentData().getFloat(LOCATION_ACCEL)+0.05f*modifier.getLevel());
        }
    }

    @Override
    public void addToolStats(IToolContext iToolContext, ModifierEntry modifierEntry, ModifierStatsBuilder modifierStatsBuilder) {
        ToolStats.ATTACK_SPEED.multiply(modifierStatsBuilder,0.8);
        ToolStats.MINING_SPEED.multiply(modifierStatsBuilder,0.8);
    }

    @Override
    public void addAttributes(IToolStackView tool, ModifierEntry modifierEntry, EquipmentSlot equipmentSlot, BiConsumer<Attribute, AttributeModifier> biConsumer) {
        if (tool.getPersistentData().getFloat(LOCATION_ACCEL)>0&& EquipmentUtil.HAND.contains(equipmentSlot)) {
            biConsumer.accept(Attributes.ATTACK_SPEED, new AttributeModifier(UUID.fromString("c7b04ef1-4130-fe3d-cb56-fbe7ea4be5fa"), Attributes.ATTACK_SPEED.getDescriptionId(), tool.getPersistentData().getFloat(LOCATION_ACCEL), AttributeModifier.Operation.MULTIPLY_BASE));
        }
    }

    @Override
    public void onBreakSpeed(IToolStackView tool, ModifierEntry modifierEntry, PlayerEvent.BreakSpeed event, Direction direction, boolean b, float v) {
        if (tool.getPersistentData().getFloat(LOCATION_ACCEL)>0) {
            event.setNewSpeed(event.getNewSpeed()*(1+tool.getPersistentData().getFloat(LOCATION_ACCEL)));
        }
    }
}
