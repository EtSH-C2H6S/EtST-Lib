package com.c2h6s.etstlib.tool.modifiers;

import com.c2h6s.etstlib.tool.modifiers.base.EtSTBaseModifier;
import com.c2h6s.etstlib.util.AttributesModifierUtil;
import com.c2h6s.etstlib.util.CommonUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.behavior.AttributesModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.function.BiConsumer;

//Only for testing,Don't use this modifier.
public class Test extends EtSTBaseModifier implements AttributesModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.ATTRIBUTES);
    }

    @Override
    public void addAttributes(IToolStackView iToolStackView, ModifierEntry modifierEntry, EquipmentSlot equipmentSlot, BiConsumer<Attribute, AttributeModifier> biConsumer) {
        biConsumer.accept(Attributes.MAX_HEALTH,new AttributeModifier(CommonUtil.getUuidFromTool((ToolStack) iToolStackView),Attributes.MAX_HEALTH.getDescriptionId(), 10, AttributeModifier.Operation.ADDITION));
    }
//    @Override
//    public void modifierOnInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity holder, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
//        if (!world.isClientSide&&isCorrectSlot) {
//            holder.sendSystemMessage(Component.literal(AttributesModifierUtil.getUuidFromTool((ToolStack) tool).toString()));
//        }
//    }
}
