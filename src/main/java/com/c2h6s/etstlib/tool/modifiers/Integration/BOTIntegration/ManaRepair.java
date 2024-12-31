package com.c2h6s.etstlib.tool.modifiers.Integration.BOTIntegration;

import com.c2h6s.etstlib.EtSTLib;
import com.c2h6s.etstlib.tool.modifiers.base.EtSTBaseModifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import vazkii.botania.api.mana.ManaItemHandler;

public class ManaRepair extends EtSTBaseModifier {
    @Override
    public void modifierOnInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity holder, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        if (tool.getDamage()>0&&world.getGameTime()%5<=modifier.getLevel()&& holder instanceof Player player&&!world.isClientSide){
            if (ManaItemHandler.INSTANCE.requestManaExactForTool(stack,player,200,false)){
                tool.setDamage(tool.getDamage()-1);
                if (EtSTLib.random.nextInt(5)<modifier.getLevel()&&tool.getDamage()>0){
                    tool.setDamage(tool.getDamage()-1);
                }
            }
        }
    }
}
