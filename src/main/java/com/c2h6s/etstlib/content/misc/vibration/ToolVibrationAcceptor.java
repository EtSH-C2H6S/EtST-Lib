package com.c2h6s.etstlib.content.misc.vibration;

import com.c2h6s.etstlib.register.EtSTLibHooks;
import com.c2h6s.etstlib.tool.hooks.VibrationListeningModifierHook;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.GameEventTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.*;

public class ToolVibrationAcceptor{
    public final UUID acceptorUUID;
    @Getter
    protected int totalLevel;
    public int listenRange;

    public ToolVibrationAcceptor( int listenRadius, UUID acceptorUUID,int level) {
        this.acceptorUUID = acceptorUUID;
        this.totalLevel = level;
        this.listenRange = listenRadius;
    }

    public ToolVibrationAcceptor mergeAcceptor(ToolVibrationAcceptor acceptor){
        this.totalLevel+=acceptor.totalLevel;
        this.listenRange=Math.max(acceptor.listenRange,this.listenRange);
        return this;
    }

    public void onVibrationReceived(ServerLevel pLevel,Player player, BlockPos pPos, GameEvent pGameEvent, @Nullable Entity directEntity, @Nullable Entity projectileOwner, float pDistance,int frequency){
        VibrationListeningModifierHook.handleVibration(player,pLevel,pPos,pGameEvent,directEntity,projectileOwner,pDistance,frequency,this);
    }

    public boolean canReceiveVibration(ServerLevel pLevel,Player player,BlockPos userPos, BlockPos pPos, GameEvent pGameEvent, GameEvent.Context pContext){
        return VibrationListeningModifierHook.validateVibration(player,pLevel,pPos,pGameEvent,pContext,VibrationSystem.Listener.distanceBetweenInBlocks(pPos,userPos),this);
    }

    public void tickAcceptor(Player player){
        for(EquipmentSlot slot:EquipmentSlot.values()) {
            ItemStack stack = player.getItemBySlot(slot);
            if (stack.getItem() instanceof IModifiable&&player.level() instanceof ServerLevel serverLevel) {
                ToolStack toolStack = ToolStack.from(stack);
                toolStack.getModifierList().forEach((modifier) -> modifier.getHook(EtSTLibHooks.VIBRATION_LISTENING).onAcceptorTick(toolStack,modifier,player,serverLevel,slot,totalLevel));
            }
        }
    }




}

