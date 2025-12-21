package com.c2h6s.etstlib.tool.hooks.modifierModules;

import com.c2h6s.etstlib.register.EtSTLibHooks;
import com.c2h6s.etstlib.tool.hooks.CriticalAttackModifierHook;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.mantle.data.loadable.primitive.BooleanLoadable;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.mantle.data.predicate.IJsonPredicate;
import slimeknights.mantle.data.predicate.entity.LivingEntityPredicate;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.modules.ModifierModule;
import slimeknights.tconstruct.library.modifiers.modules.util.ModifierCondition;
import slimeknights.tconstruct.library.module.HookProvider;
import slimeknights.tconstruct.library.module.ModuleHook;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.List;

public record SetCriticalModule(IJsonPredicate<LivingEntity> entity, ModifierCondition<IToolStackView> condition,boolean requireFullCharge) implements ModifierModule , CriticalAttackModifierHook , ModifierCondition.ConditionalModule<IToolStackView> {
    public static final RecordLoadable<SetCriticalModule> LOADER = RecordLoadable.create(
            LivingEntityPredicate.LOADER.defaultField("entity",SetCriticalModule::entity),
            ModifierCondition.TOOL_FIELD,
            BooleanLoadable.INSTANCE.defaultField("full_charge_only",false,SetCriticalModule::requireFullCharge),
            SetCriticalModule::new
    );

    @Override
    public boolean setCritical(IToolStackView tool, ModifierEntry entry, LivingEntity attacker, InteractionHand hand, Entity target, EquipmentSlot sourceSlot, boolean isFullyCharged, boolean isExtraAttack, boolean isCritical) {
        return target instanceof LivingEntity living&&entity.matches(living)&&(!requireFullCharge||isFullyCharged);
    }

    @Override
    public RecordLoadable<? extends ModifierModule> getLoader() {
        return LOADER;
    }

    @Override
    public List<ModuleHook<?>> getDefaultHooks() {
        return HookProvider.defaultHooks(EtSTLibHooks.CRITICAL_ATTACK);
    }
}
