package com.c2h6s.etstlib.content.misc.vibration;

import com.c2h6s.etstlib.register.EtSTLibHooks;
import com.c2h6s.etstlib.tool.hooks.VibrationListeningModifierHook;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.GameEventTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.gameevent.EntityPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.*;

public class ToolVibrationAcceptor implements GameEventListener.Holder<VibrationSystem.Listener>, VibrationSystem {
    public final Player playerHolder;
    private int listenRadius;
    public final UUID acceptorUUID;
    private VibrationSystem.Data vibrationData;
    private final VibrationSystem.User vibrationUser;
    private final VibrationSystem.Listener vibrationListener;
    private int totalLevel;

    public static Map<Player, List<ToolVibrationAcceptor>> acceptorMap = new HashMap<>();

    public void setListenRadius(int listenRadius){
        this.listenRadius = listenRadius;
    }

    public int getListenRadius(){
        return listenRadius;
    }


    public ToolVibrationAcceptor(@NotNull Player player, int listenRadius, UUID acceptorUUID,int level) {
        this.listenRadius = listenRadius;
        this.acceptorUUID = acceptorUUID;
        this.playerHolder = player;
        this.vibrationData = new Data();
        this.vibrationUser = new VibrationUser();
        this.vibrationListener = new VibrationSystem.Listener(this);
        this.totalLevel = level;
    }

    public static void decreaseLevel(Player player,UUID uuid,int toDecrease){
        if (acceptorMap.get(player)!=null){
            List<ToolVibrationAcceptor> acceptors =new ArrayList<>( acceptorMap.get(player));
            for (ToolVibrationAcceptor acceptor:acceptorMap.get(player)){
                if (acceptor.acceptorUUID==uuid){
                    acceptor.totalLevel-=toDecrease;
                    if (acceptor.totalLevel<=0) acceptors.remove(acceptor);
                }
            }
            acceptorMap.put(player,acceptors);
        }
    }

    public static void removeAcceptor(Player player, UUID uuid){
        if (acceptorMap.get(player)!=null){
            List<ToolVibrationAcceptor> acceptors =new ArrayList<>( acceptorMap.get(player));
            for (ToolVibrationAcceptor acceptor:acceptorMap.get(player)){
                if (acceptor.acceptorUUID==uuid) acceptors.remove(acceptor);
            }
            acceptorMap.put(player,acceptors);
        }
    }

    public static void removeAllAcceptor(Player player){
        acceptorMap.remove(player);
    }

    public static void putOrMerge(@NotNull Player player, @NotNull ToolVibrationAcceptor acceptor){
        if (acceptorMap.get(player)==null||(acceptorMap.get(player)!=null&&acceptorMap.get(player).isEmpty())){
            acceptorMap.put(player,List.of(acceptor));
        }else {
            List<ToolVibrationAcceptor> list =new ArrayList<>( acceptorMap.get(player));
            boolean merged = false;
            for (int i=0;i<list.size();i++){
                ToolVibrationAcceptor existingAcceptor = acceptorMap.get(player).get(i);
                if (existingAcceptor.shouldMerge(acceptor)){
                    list.set(i, existingAcceptor.mergeAcceptor(acceptor));
                    merged = true;
                    break;
                }
            }
            if (!merged) list.add(acceptor);
            acceptorMap.put(player,list);
        }
    }

    public void putOrMerge(){
        putOrMerge(playerHolder,this);
    }

    public boolean shouldMerge(ToolVibrationAcceptor acceptor){
        return acceptor.acceptorUUID==this.acceptorUUID;
    }

    public ToolVibrationAcceptor mergeAcceptor(ToolVibrationAcceptor acceptor){
        this.totalLevel+=acceptor.totalLevel;
        this.listenRadius = Math.max(acceptor.listenRadius,this.listenRadius);
        return this;
    }

    public void onVibrationReceived(ServerLevel pLevel, BlockPos pPos, GameEvent pGameEvent, @Nullable Entity directEntity, @Nullable Entity projectileOwner, float pDistance,int frequency){
        VibrationListeningModifierHook.handleVibration(playerHolder,pLevel,pPos,pGameEvent,directEntity,projectileOwner,pDistance,frequency,this);
    }

    public boolean canReceiveVibration(ServerLevel pLevel, BlockPos pPos, GameEvent pGameEvent, GameEvent.Context pContext){
        if (!this.validate()) return false;
        BlockPos userPos = vibrationUser.getPositionSource().getPosition(pLevel).map(BlockPos::containing).orElse(pPos);
        return VibrationListeningModifierHook.validateVibration(playerHolder,pLevel,pPos,pGameEvent,pContext,VibrationSystem.Listener.distanceBetweenInBlocks(pPos,userPos),this);
    }

    public static void tickIfPresent(ServerPlayer player){
        List<ToolVibrationAcceptor> vibrationAcceptors = acceptorMap.get(player);
        if (vibrationAcceptors!=null&&!vibrationAcceptors.isEmpty()){
            vibrationAcceptors.forEach(ToolVibrationAcceptor::tick);
        }
    }

    public void tick(){
        if (playerHolder.isDeadOrDying() || !playerHolder.isAlive()){
            removeAllAcceptor(playerHolder);
            return;
        }
        if (!playerHolder.level().isClientSide) VibrationSystem.Ticker.tick(this.playerHolder.level(),this.vibrationData,this.vibrationUser);
        this.tickAcceptor();
    }

    public boolean validate(){
        ArrayList<UUID> listExistingAcceptors = new ArrayList<>(List.of());
        for(EquipmentSlot slot:EquipmentSlot.values()) {
            ItemStack stack = playerHolder.getItemBySlot(slot);
            if (stack.getItem() instanceof IModifiable) {
                ToolStack toolStack = ToolStack.from(stack);
                toolStack.getModifierList().forEach((modifier)->{
                    UUID uuid = modifier.getHook(EtSTLibHooks.VIBRATION_LISTENING).getAcceptorUUID(toolStack,modifier,playerHolder,playerHolder.level(),slot);
                    if (uuid!=null) listExistingAcceptors.add(uuid);
                });
            }
        }
        if (listExistingAcceptors.contains(acceptorUUID)) return true;
        else {
            removeAcceptor(playerHolder,acceptorUUID);
        }
        return false;
    }

    public void tickAcceptor(){
        for(EquipmentSlot slot:EquipmentSlot.values()) {
            ItemStack stack = playerHolder.getItemBySlot(slot);
            if (stack.getItem() instanceof IModifiable&&playerHolder.level() instanceof ServerLevel serverLevel) {
                ToolStack toolStack = ToolStack.from(stack);
                toolStack.getModifierList().forEach((modifier) -> {
                    modifier.getHook(EtSTLibHooks.VIBRATION_LISTENING).onAcceptorTick(toolStack,modifier,playerHolder,serverLevel,slot,totalLevel);
                });
            }
        }
    }

    @Override
    public Data getVibrationData() {
        return vibrationData;
    }

    @Override
    public User getVibrationUser() {
        return vibrationUser;
    }

    @Override
    public Listener getListener() {
        return vibrationListener;
    }

    class VibrationUser implements VibrationSystem.User {

        @Override
        public int getListenerRadius() {
            return ToolVibrationAcceptor.this.listenRadius;
        }

        @Override
        public PositionSource getPositionSource() {
            return new EntityPositionSource(ToolVibrationAcceptor.this.playerHolder, ToolVibrationAcceptor.this.playerHolder.getEyeHeight());
        }

        @Override
        public TagKey<GameEvent> getListenableEvents() {
            return GameEventTags.VIBRATIONS;
        }

        @Override
        public boolean canReceiveVibration(ServerLevel pLevel, BlockPos pPos, GameEvent pGameEvent, GameEvent.Context pContext) {
            return ToolVibrationAcceptor.this.canReceiveVibration(pLevel,pPos,pGameEvent,pContext);
        }

        @Override
        public void onReceiveVibration(ServerLevel pLevel, BlockPos pPos, GameEvent pGameEvent, @Nullable Entity pEntity, @Nullable Entity projectileOwner, float pDistance) {
            ToolVibrationAcceptor.this.onVibrationReceived(pLevel,pPos,pGameEvent,pEntity,projectileOwner,pDistance,VibrationSystem.getGameEventFrequency(pGameEvent));
        }
    }
}

