package com.c2h6s.etstlib.tool.hooks;

import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Collection;
import java.util.Objects;

public interface IndividualProtectionModifierHook {
    float getIndividualProtectionModifier(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, DamageSource source, float modifierValue);
    default String getProtectionName(IToolStackView tool, ModifierEntry modifier, Player player){
        return modifier.getModifier().getTranslationKey();
    }
    float getProtectionModifierForDisplay(IToolStackView tool, ModifierEntry modifier,Player player,float value);
    default Component getTranslationName(IToolStackView tool, ModifierEntry modifier, Player player){
        var name = getProtectionName(tool,modifier,player);
        if (name==null) return null;
        return Component.translatable( name+".resistance").withStyle(modifier.getDisplayName().getStyle());
    }

    record Merger(Collection<IndividualProtectionModifierHook> modules) implements IndividualProtectionModifierHook {
        @Override
        public float getIndividualProtectionModifier(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, DamageSource source, float modifierValue) {
            for (IndividualProtectionModifierHook module : modules) {
                modifierValue += module.getIndividualProtectionModifier(tool, modifier, context, slotType, source, modifierValue);
            }
            return modifierValue;
        }

        @Override
        public String getProtectionName(IToolStackView tool, ModifierEntry modifier, @Nullable Player player) {
            return modules.stream()
                    .map(hook->hook.getProtectionName(tool,modifier,player))
                    .filter(Objects::nonNull).findFirst().orElse(null);
        }
        @Override
        public Component getTranslationName(IToolStackView tool, ModifierEntry modifier, @Nullable Player player) {
            return modules.stream()
                    .map(hook->hook.getTranslationName(tool,modifier,player))
                    .filter(Objects::nonNull).findFirst().orElse(null);
        }

        @Override
        public float getProtectionModifierForDisplay(IToolStackView tool, ModifierEntry modifier, Player player, float value) {
            for (IndividualProtectionModifierHook module : modules) {
                value += module.getProtectionModifierForDisplay(tool, modifier, player, value);
            }
            return value;
        }
    }

}
