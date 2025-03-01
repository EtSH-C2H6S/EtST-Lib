package com.c2h6s.etstlib.tool.modifiers;

import com.c2h6s.etstlib.entity.specialDamageSources.LegacyDamageSource;
import com.c2h6s.etstlib.register.EtSTLibHooks;
import com.c2h6s.etstlib.tool.hooks.LeftClickModifierHook;
import com.c2h6s.etstlib.tool.modifiers.base.EtSTBaseModifier;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

//Only for testing,Don't use this modifier.
public class Test extends Modifier implements LeftClickModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, EtSTLibHooks.LEFT_CLICK);
    }
    /*
    @Override
    public void onLeftClickEntity(IToolStackView tool, ModifierEntry entry, Player player, Level level, EquipmentSlot equipmentSlot, Entity entity) {
        player.sendSystemMessage(Component.literal("12345"));
        FallingBlockEntity.fall(player.level(), entity.blockPosition().above(2), Blocks.ANVIL.defaultBlockState());
    }

     */
}
