package com.c2h6s.etstlib.tool.hooks;

import com.c2h6s.etstlib.content.misc.vibration.ToolVibrationAcceptor;
import com.c2h6s.etstlib.content.misc.vibration.ToolVibrationListener;
import com.c2h6s.etstlib.content.misc.vibration.VibrationContext;
import com.c2h6s.etstlib.register.EtSTLibHooks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.DynamicGameEventListener;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.vibrations.VibrationInfo;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.hook.armor.EquipmentChangeModifierHook;
import slimeknights.tconstruct.library.tools.context.EquipmentChangeContext;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.Collection;
import java.util.UUID;

public interface VibrationListeningModifierHook extends EquipmentChangeModifierHook {
    static void handleVibration(Player player,ServerLevel pLevel, BlockPos pPos, GameEvent pGameEvent, @Nullable Entity directEntity, @Nullable Entity projectileOwner, float pDistance, int frequency,ToolVibrationAcceptor acceptor){
        for(EquipmentSlot slot:EquipmentSlot.values()){
            ItemStack stack = player.getItemBySlot(slot);
            if (stack.getItem() instanceof IModifiable){
                ToolStack toolStack = ToolStack.from(stack);
                VibrationContext context = new VibrationContext(pLevel,pPos,pGameEvent,directEntity,projectileOwner,pDistance,frequency,acceptor);
                toolStack.getModifierList().forEach((modifier)->{
                    if (modifier.getHook(EtSTLibHooks.VIBRATION_LISTENING).canReceiveVibration(toolStack,modifier,player,pLevel,slot,context)) {
                        modifier.getHook(EtSTLibHooks.VIBRATION_LISTENING).onReceivingVibration(toolStack, modifier, player, pLevel, slot, context);
                    }
                });
            }
        }
    }
    static boolean validateVibration(Player player,ServerLevel pLevel, BlockPos pPos, GameEvent pGameEvent, GameEvent.Context pContext,float distance,ToolVibrationAcceptor acceptor){
        for(EquipmentSlot slot:EquipmentSlot.values()) {
            ItemStack stack = player.getItemBySlot(slot);
            if (stack.getItem() instanceof IModifiable) {
                ToolStack toolStack = ToolStack.from(stack);
                VibrationContext context = new VibrationContext(pLevel,pPos,pGameEvent,new VibrationInfo(pGameEvent,distance,new Vec3(pPos.getX(),pPos.getY(),pPos.getZ()),pContext.sourceEntity()),acceptor);
                for (ModifierEntry modifier:toolStack.getModifierList()){
                    if (modifier.getHook(EtSTLibHooks.VIBRATION_LISTENING).canReceiveVibration(toolStack,modifier,player,pLevel,slot,context)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    UUID getAcceptorUUID(IToolStackView tool, ModifierEntry modifier, Player player, Level level, EquipmentSlot slot);
    default void onAcceptorTick(IToolStackView tool, ModifierEntry modifier, Player player, ServerLevel level, EquipmentSlot slot,int acceptorLevel){

    }
    int listenRange(IToolStackView tool, ModifierEntry modifier, Player player, Level level, EquipmentSlot slot,int range);
    boolean canReceiveVibration(IToolStackView tool, ModifierEntry modifier, Player player, ServerLevel level, EquipmentSlot slot, VibrationContext context);
    void onReceivingVibration(IToolStackView tool, ModifierEntry modifier, Player player, ServerLevel level, EquipmentSlot slot, VibrationContext context);

    @Override
    default void onEquip(IToolStackView tool, ModifierEntry modifier, EquipmentChangeContext context) {
        if (context.getEntity() instanceof Player player&&context.getLevel() instanceof ServerLevel serverLevel) {
            UUID acceptorUUID = modifier.getHook(EtSTLibHooks.VIBRATION_LISTENING).getAcceptorUUID(tool, modifier, player,serverLevel,context.getChangedSlot());
            if (acceptorUUID!=null){
                int range = modifier.getHook(EtSTLibHooks.VIBRATION_LISTENING).listenRange(tool,modifier,player,serverLevel,context.getChangedSlot(),16);
                ToolVibrationListener listener = ToolVibrationListener.listenerMap.get(player);
                boolean noListener = listener==null;
                if (noListener){
                    listener=new ToolVibrationListener(player);
                }
                ToolVibrationAcceptor acceptor = new ToolVibrationAcceptor(range,acceptorUUID,modifier.getLevel());
                listener.addAcceptor(acceptor);
                ToolVibrationListener.listenerMap.put(player,listener);
                if (noListener) player.updateDynamicGameEventListener(DynamicGameEventListener::add);
            }
        }
    }

    @Override
    default void onUnequip(IToolStackView tool, ModifierEntry modifier, EquipmentChangeContext context) {
        if (context.getEntity() instanceof Player player&&context.getLevel() instanceof ServerLevel serverLevel) {
            UUID acceptorUUID = modifier.getHook(EtSTLibHooks.VIBRATION_LISTENING).getAcceptorUUID(tool, modifier, player, serverLevel, context.getChangedSlot());
            if (acceptorUUID != null) {
                ToolVibrationListener.decreaseLevel(player,acceptorUUID,modifier.getLevel());
            }
        }
    }

    record AllMerger(Collection<VibrationListeningModifierHook> modules) implements VibrationListeningModifierHook {

        @Override
        public UUID getAcceptorUUID(IToolStackView tool, ModifierEntry modifier, Player player, Level level, EquipmentSlot slot) {
            for (VibrationListeningModifierHook module:this.modules){
                UUID uuid = module.getAcceptorUUID(tool,modifier,player,level,slot);
                if (uuid!=null) return uuid;
            }
            return null;
        }

        @Override
        public int listenRange(IToolStackView tool, ModifierEntry modifier, Player player, Level level, EquipmentSlot slot,int range) {
            for (VibrationListeningModifierHook module:this.modules){
                range = module.listenRange(tool,modifier,player,level,slot,range);
            }
            return range;
        }

        @Override
        public boolean canReceiveVibration(IToolStackView tool, ModifierEntry modifier, Player player, ServerLevel level, EquipmentSlot slot, VibrationContext context) {
            for (VibrationListeningModifierHook module:this.modules){
                if (module.canReceiveVibration(tool,modifier,player,level,slot,context)) return true;
            }
            return false;
        }

        @Override
        public void onReceivingVibration(IToolStackView tool, ModifierEntry modifier, Player player, ServerLevel level, EquipmentSlot slot, VibrationContext context) {
            for (VibrationListeningModifierHook module:this.modules){
                if (module.canReceiveVibration(tool,modifier,player,level,slot,context)) module.onReceivingVibration(tool,modifier,player,level,slot,context);
            }
        }

        public void onAcceptorTick(IToolStackView tool, ModifierEntry modifier, Player player, ServerLevel level, EquipmentSlot slot,int acceptorLevel){
            for (VibrationListeningModifierHook module:this.modules){
                module.onAcceptorTick(tool,modifier,player,level,slot,acceptorLevel);
            }
        }
    }
}
