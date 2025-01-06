package com.c2h6s.etstlib.event.eventHandler;

import com.c2h6s.etstlib.EtSTLib;
import com.c2h6s.etstlib.register.EtSTLibHooks;
import com.c2h6s.etstlib.tool.hooks.LeftClickModifierHook;
import com.c2h6s.etstlib.util.EquipmentUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

@Mod.EventBusSubscriber(modid = EtSTLib.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ToolEvents {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void EffectApply(MobEffectEvent.Applicable event){
        if (event.getEntity()!=null) {
            for (EquipmentSlot slot : EquipmentUtil.ALL) {
                if (event.getEntity().getItemBySlot(slot).getItem() instanceof IModifiable) {
                    ToolStack tool = ToolStack.from(event.getEntity().getItemBySlot(slot));
                    Boolean notApplicable = event.getResult()== Event.Result.DENY;
                    for (ModifierEntry entry:tool.getModifierList()){
                        notApplicable = entry.getHook(EtSTLibHooks.EFFECT_APPLICABLE).isApplicable(tool,entry,slot,event.getEffectInstance(),notApplicable);
                        if (notApplicable!=null){
                            if (notApplicable) {
                                event.setResult(Event.Result.DENY);
                            }
                            else event.setResult(Event.Result.ALLOW);
                            break;
                        }

                    }
                }
            }
        }
    }

}
