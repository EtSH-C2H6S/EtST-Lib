package com.c2h6s.etstlib.event.eventHandler;

import com.c2h6s.etstlib.EtSTLib;
import com.c2h6s.etstlib.EtSTLibConfig;
import com.c2h6s.etstlib.api.interfaces.IRandomizeUuidWhenCrafted;
import com.c2h6s.etstlib.content.misc.vibration.ToolVibrationAcceptor;
import com.c2h6s.etstlib.content.misc.vibration.ToolVibrationListener;
import com.c2h6s.etstlib.event.CompletelyNewEvent.FluidConsumedEvent;
import com.c2h6s.etstlib.tool.hooks.LeftClickModifierHook;
import com.c2h6s.etstlib.util.IToolUuidGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.Optional;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = EtSTLib.MODID,bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerEvents {
    public static void onLeftClick(PlayerInteractEvent.LeftClickEmpty event){
        Player player = event.getEntity();
        if (player!=null&&player.level().isClientSide) {
            ItemStack stack = player.getItemInHand(player.getUsedItemHand());
            if (stack.getItem() instanceof IModifiable) {
                EquipmentSlot slot = stack.getEquipmentSlot();
                LeftClickModifierHook.handleLeftClick(stack,player,slot);
            }
        }
    }
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event){
        Player player = event.getEntity();
        BlockPos pos = event.getPos();
        if (player!=null) {
            BlockState state = player.level().getBlockState(pos);
            ItemStack stack = player.getItemInHand(player.getUsedItemHand());
            if (stack.getItem() instanceof IModifiable) {
                EquipmentSlot slot = stack.getEquipmentSlot();
                LeftClickModifierHook.handleLeftClickBlock(stack,player,slot,state,pos);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event){
        if (event.player instanceof ServerPlayer serverPlayer) ToolVibrationListener.tickIfPresent(serverPlayer);
    }

    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event){
        if (event.getEntity() instanceof ServerPlayer serverPlayer) ToolVibrationListener.removeAllAcceptor(serverPlayer);
    }

    @SubscribeEvent
    public static void onItemCrafted(PlayerEvent.ItemCraftedEvent event){
        ItemStack stack = event.getCrafting();
        if (stack.getItem() instanceof IRandomizeUuidWhenCrafted){
            IToolUuidGetter.getUuidOrRandomize(stack);
        } else if (stack.getItem() instanceof IModifiable&& EtSTLibConfig.ALLOW_TOOL_UUID_ON_COMMON_TOOL.get()){
            if (stack.getMaxStackSize()<=1||EtSTLibConfig.ALLOW_TOOL_UUID_ON_STACKABLE_TOOL.get()){
                IToolUuidGetter.getUuidOrRandomize(stack);
            }
        }
    }


}
