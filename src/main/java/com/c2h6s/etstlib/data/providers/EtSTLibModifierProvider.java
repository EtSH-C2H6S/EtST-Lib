package com.c2h6s.etstlib.data.providers;

import com.c2h6s.etstlib.data.EtSTLibModifierIds;
import com.c2h6s.etstlib.data.predicate.LivingEntityWithHealth;
import com.c2h6s.etstlib.tool.hooks.modifierModules.AddDamageTypeTagArrowModule;
import com.c2h6s.etstlib.tool.hooks.modifierModules.AddDamageTypeTagMeleeModule;
import com.c2h6s.etstlib.tool.hooks.modifierModules.ForceDropModule;
import com.c2h6s.etstlib.tool.hooks.modifierModules.SetCriticalModule;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageTypes;
import slimeknights.mantle.data.predicate.block.BlockPredicate;
import slimeknights.mantle.data.predicate.entity.LivingEntityPredicate;
import slimeknights.tconstruct.library.data.tinkering.AbstractModifierProvider;
import slimeknights.tconstruct.library.modifiers.impl.BasicModifier;
import slimeknights.tconstruct.library.modifiers.modules.util.ModifierCondition;
import slimeknights.tconstruct.library.modifiers.util.ModifierLevelDisplay;

public class EtSTLibModifierProvider extends AbstractModifierProvider {
    public EtSTLibModifierProvider(PackOutput packOutput) {
        super(packOutput);
    }

    @Override
    protected void addModifiers() {
        this.buildModifier(EtSTLibModifierIds.RUDE)
                .tooltipDisplay(BasicModifier.TooltipDisplay.ALWAYS)
                .levelDisplay(ModifierLevelDisplay.SINGLE_LEVEL)
                .addModule(new SetCriticalModule(new LivingEntityWithHealth(25,-1,true), ModifierCondition.ANY_TOOL,false))
                .build();
        this.buildModifier(EtSTLibModifierIds.ATOMIC_DECOMPOSE)
                .tooltipDisplay(BasicModifier.TooltipDisplay.ALWAYS)
                .levelDisplay(ModifierLevelDisplay.SINGLE_LEVEL)
                .addModule(new ForceDropModule(BlockPredicate.ANY,ModifierCondition.ANY_TOOL,true))
                .build();
        this.buildModifier(EtSTLibModifierIds.EXECUTIONER)
                .tooltipDisplay(BasicModifier.TooltipDisplay.ALWAYS)
                .levelDisplay(ModifierLevelDisplay.SINGLE_LEVEL)
                .addModule(new AddDamageTypeTagMeleeModule(LivingEntityPredicate.ANY,ModifierCondition.ANY_TOOL, DamageTypeTags.BYPASSES_COOLDOWN,false))
                .addModule(new AddDamageTypeTagArrowModule(LivingEntityPredicate.ANY, DamageTypeTags.BYPASSES_COOLDOWN,false))
                .build();
    }

    @Override
    public String getName() {
        return "EtSTLib Modifier Provider";
    }
}
