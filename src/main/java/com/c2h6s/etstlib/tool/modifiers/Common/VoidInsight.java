package com.c2h6s.etstlib.tool.modifiers.Common;

import com.c2h6s.etstlib.register.EtSTLibModifier;
import com.c2h6s.etstlib.tool.modifiers.base.EtSTBaseModifier;
import com.c2h6s.etstlib.util.EquipmentUtil;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class VoidInsight extends EtSTBaseModifier {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onMobSpawn(MobSpawnEvent.PositionCheck event) {
        if (event.getLevel()!=null) {
            var players = event.getLevel().players();
            for (Player player:players){
                for (EquipmentSlot slot : EquipmentUtil.ARMOR) {
                    if (player.getItemBySlot(slot).getItem() instanceof IModifiable && ToolStack.from(player.getItemBySlot(slot)).getModifierLevel(EtSTLibModifier.VOID_INSIGHT.getId()) > 0) {
                        event.setResult(Event.Result.DENY);
                        break;
                    }
                }
            }
        }
    }
}
