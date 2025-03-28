package com.c2h6s.etstlib.tool.hooks.modifierModules;

import com.c2h6s.etstlib.register.EtSTLibHooks;
import com.c2h6s.etstlib.tool.hooks.CorrectDropModifierHook;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerEvent;
import slimeknights.mantle.data.loadable.primitive.BooleanLoadable;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.mantle.data.predicate.IJsonPredicate;
import slimeknights.mantle.data.predicate.block.BlockPredicate;
import slimeknights.mantle.data.registry.GenericLoaderRegistry;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.hook.build.ConditionalStatModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.mining.BreakSpeedModifierHook;
import slimeknights.tconstruct.library.modifiers.modules.ModifierModule;
import slimeknights.tconstruct.library.modifiers.modules.util.ModifierCondition;
import slimeknights.tconstruct.library.module.HookProvider;
import slimeknights.tconstruct.library.module.ModuleHook;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import java.util.List;

public record ForceDropModule(IJsonPredicate<BlockState> blockPredicate, ModifierCondition<IToolStackView> condition, boolean fixBreakSpeed) implements ModifierModule,CorrectDropModifierHook , BreakSpeedModifierHook, ModifierCondition.ConditionalModule<IToolStackView> {
    public static final RecordLoadable<ForceDropModule> LOADER = RecordLoadable.create(
            BlockPredicate.LOADER.defaultField("block",ForceDropModule::blockPredicate),
            ModifierCondition.TOOL_FIELD,
            BooleanLoadable.INSTANCE.defaultField("fix_break_speed",false,ForceDropModule::fixBreakSpeed),
            ForceDropModule::new
    );


    @Override
    public boolean isCorrectToolForDrop(IToolStackView tool, ModifierEntry entry, BlockState state, boolean drop) {
        return blockPredicate.matches(state)&&condition.matches(tool,entry);
    }

    @Override
    public void onBreakSpeed(IToolStackView iToolStackView, ModifierEntry modifierEntry, PlayerEvent.BreakSpeed breakSpeed, Direction direction, boolean b, float v) {
        if (fixBreakSpeed) breakSpeed.setNewSpeed(ConditionalStatModifierHook.getModifiedStat(iToolStackView,breakSpeed.getEntity(), ToolStats.MINING_SPEED));
    }

    @Override
    public RecordLoadable<? extends GenericLoaderRegistry.IHaveLoader> getLoader() {
        return LOADER;
    }

    @Override
    public List<ModuleHook<?>> getDefaultHooks() {
        return HookProvider.defaultHooks(EtSTLibHooks.CORRECT_TOOL);
    }
}
