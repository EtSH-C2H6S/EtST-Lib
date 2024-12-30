package com.c2h6s.etstlib.event.eventHandler;

import com.c2h6s.etstlib.EtSTLib;
import com.c2h6s.etstlib.register.EtSTLibHooks;
import com.c2h6s.etstlib.util.EquipmentUtil;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

@Mod.EventBusSubscriber(modid = EtSTLib.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LivingEvents {
    @SubscribeEvent
    public void EffectApply(MobEffectEvent.Applicable event){
        if (event.getEntity()!=null) {
            for (EquipmentSlot slot : EquipmentUtil.ALL) {
                if (event.getEntity().getItemBySlot(slot).getItem() instanceof IModifiable) {
                    ToolStack tool = ToolStack.from(event.getEntity().getItemBySlot(slot));
                    boolean notApplicable = event.getResult()== Event.Result.ALLOW||event.getResult()== Event.Result.DEFAULT;
                    for (ModifierEntry entry:tool.getModifierList()){
                        notApplicable = entry.getHook(EtSTLibHooks.EFFECT_APPLICABLE).isApplicable(tool,entry,slot,event.getEffectInstance(),notApplicable);
                        if (notApplicable){
                            event.setResult(Event.Result.DENY);
                            break;
                        }
                    }
                }
            }
        }
    }
}
