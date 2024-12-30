package com.c2h6s.etstlib.tool.modifiers.Integration.PnCIntegration;

import com.c2h6s.etstlib.tool.modifiers.base.BasicPressurizableModifier;
import com.c2h6s.etstlib.tool.modifiers.capabilityProvider.PnCIntegration.AirStorageProvider;
import com.c2h6s.etstlib.util.EquipmentUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.event.entity.player.PlayerEvent;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.behavior.AttributesModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.mining.BreakSpeedModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.ranged.ProjectileLaunchModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.NamespacedNBT;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;

public class AerialReinforced extends BasicPressurizableModifier implements BreakSpeedModifierHook, ProjectileLaunchModifierHook, AttributesModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder builder) {
        super.registerHooks(builder);
        builder.addHook(this, ModifierHooks.TOOL_DAMAGE,ModifierHooks.BREAK_SPEED,ModifierHooks.MELEE_DAMAGE,ModifierHooks.PROJECTILE_LAUNCH,ModifierHooks.ATTRIBUTES);
    }

    @Override
    public int getBaseVolume(ModifierEntry entry) {
        return 5000*entry.getLevel();
    }

    @Override
    public float getMaxPressure(ModifierEntry entry) {
        return 10*entry.getLevel();
    }

    @Override
    public int onDamageTool(IToolStackView tool, ModifierEntry modifier, int amount, @Nullable LivingEntity livingEntity) {
        if (100*amount< AirStorageProvider.getAir(tool)){
            AirStorageProvider.addAir(tool,-100*amount);
            return 0;
        }
        else if (AirStorageProvider.getAir(tool)>100){
            int minus = AirStorageProvider.getAir(tool)/100;
            AirStorageProvider.addAir(tool,-100*minus);
            return amount-minus;
        }
        return amount;
    }

    @Override
    public void onBreakSpeed(IToolStackView tool, ModifierEntry modifier, PlayerEvent.BreakSpeed event, Direction direction, boolean isEffective, float miningSpeedModifier) {
        Optional<BlockPos> pos = event.getPosition();
        if (!isEffective || pos.isEmpty()) {
            return;
        }
        event.setNewSpeed(event.getNewSpeed()*(1+getBonus(tool,modifier)));
    }

    public float getBonus(IToolStackView tool, ModifierEntry modifier){
        return AirStorageProvider.getPressure(tool)>0? (AirStorageProvider.getPressure(tool))/40:0;
    }


    @Override
    public float getMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage) {
        float multiplier =1+getBonus(tool,modifier);
        if (AirStorageProvider.getAir(tool)>100){
            AirStorageProvider.addAir(tool,-(int) (100*multiplier));
            return damage*multiplier;
        }
        return damage;
    }

    @Override
    public void addTooltip(IToolStackView tool, ModifierEntry modifierEntry, @Nullable Player player, List<Component> list, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        super.addTooltip(tool, modifierEntry, player, list, tooltipKey, tooltipFlag);
        list.add(Component.translatable("tooltip.etstlib.pressure_bonus").append(" +").append(String.format("%.1f",getBonus(tool,modifierEntry)*100)).append("%").withStyle(ChatFormatting.GREEN));
    }

    @Override
    public void onProjectileLaunch(IToolStackView tool, ModifierEntry modifier, LivingEntity livingEntity, Projectile projectile, @Nullable AbstractArrow abstractArrow, NamespacedNBT namespacedNBT, boolean b) {
        float multiplier =1+getBonus(tool,modifier);
        if (AirStorageProvider.getAir(tool)>100&&projectile instanceof AbstractArrow arrow){
            AirStorageProvider.addAir(tool,-(int) (100*multiplier));
            arrow.setBaseDamage(arrow.getBaseDamage()*multiplier);
        }
    }

    @Override
    public void addAttributes(IToolStackView tool, ModifierEntry modifier, EquipmentSlot equipmentSlot, BiConsumer<Attribute, AttributeModifier> biConsumer) {
        if (getBonus(tool,modifier)>0) {
            if (EquipmentUtil.ARMOR.contains(equipmentSlot)) {
                biConsumer.accept(Attributes.ARMOR, new AttributeModifier(UUID.fromString("dd956aaf-a7e7-ca28-f352-34e76c7a4ebf"), Attributes.ARMOR.getDescriptionId(), getBonus(tool, modifier), AttributeModifier.Operation.MULTIPLY_BASE));
                biConsumer.accept(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(UUID.fromString("45bbb1ce-b4ee-7ee2-2f5c-df86d24c361a"), Attributes.ARMOR_TOUGHNESS.getDescriptionId(), getBonus(tool, modifier), AttributeModifier.Operation.MULTIPLY_BASE));
                biConsumer.accept(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(UUID.fromString("4639fef7-40a3-4ecf-ff5b-0cd263d3c9c0"), Attributes.KNOCKBACK_RESISTANCE.getDescriptionId(), getBonus(tool, modifier), AttributeModifier.Operation.MULTIPLY_BASE));
            }
            else if (EquipmentUtil.HAND.contains(equipmentSlot)){
                biConsumer.accept(Attributes.ATTACK_SPEED, new AttributeModifier(UUID.fromString("ea03df0d-b06f-949a-8b4e-0a7c195df603"), Attributes.ATTACK_SPEED.getDescriptionId(), getBonus(tool, modifier), AttributeModifier.Operation.MULTIPLY_BASE));
            }
        }
    }
}
