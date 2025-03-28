package com.c2h6s.etstlib.tool.hooks.modifierModules;

import com.c2h6s.etstlib.entity.specialDamageSources.LegacyDamageSource;
import com.c2h6s.etstlib.register.EtSTLibHooks;
import com.c2h6s.etstlib.tool.hooks.ModifyDamageSourceModifierHook;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.data.loadable.Loadables;
import slimeknights.mantle.data.loadable.primitive.BooleanLoadable;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.mantle.data.predicate.IJsonPredicate;
import slimeknights.mantle.data.predicate.entity.LivingEntityPredicate;
import slimeknights.mantle.data.registry.GenericLoaderRegistry;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.modules.ModifierModule;
import slimeknights.tconstruct.library.modifiers.modules.util.ModifierCondition;
import slimeknights.tconstruct.library.module.ModuleHook;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;

import java.util.List;

public record AddDamageTypeTagArrowModule(IJsonPredicate<LivingEntity> entity, TagKey<DamageType> tagKey, boolean requireFullCharge) implements ModifierModule, ModifyDamageSourceModifierHook{
    public static final RecordLoadable<AddDamageTypeTagArrowModule> LOADER = RecordLoadable.create(
            LivingEntityPredicate.LOADER.defaultField("entity",AddDamageTypeTagArrowModule::entity),
            Loadables.DAMAGE_TYPE_TAG.requiredField("damage_type_tag",AddDamageTypeTagArrowModule::tagKey),
            BooleanLoadable.INSTANCE.defaultField("full_charge_only",false,AddDamageTypeTagArrowModule::requireFullCharge),
            AddDamageTypeTagArrowModule::new
    );

    @Override
    public LegacyDamageSource modifyArrowDamageSource(ModifierNBT modifiers, ModDataNBT persistentData, ModifierEntry modifier, AbstractArrow arrow, @Nullable LivingEntity attacker, @Nullable Entity target, LegacyDamageSource source) {
        if ((requireFullCharge&&!arrow.isCritArrow())||(target instanceof LivingEntity living&&!entity.matches(living))) return source;
        source.damageTypes.add(ResourceKey.create(Registries.DAMAGE_TYPE, tagKey.location()));
        return source;
    }

    @Override
    public RecordLoadable<? extends GenericLoaderRegistry.IHaveLoader> getLoader() {
        return LOADER;
    }

    @Override
    public List<ModuleHook<?>> getDefaultHooks() {
        return List.of(EtSTLibHooks.MODIFY_DAMAGE_SOURCE);
    }
}
