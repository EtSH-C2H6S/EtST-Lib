package com.c2h6s.etstlib.content.misc.vibration;

import com.c2h6s.etstlib.register.EtSTLibHooks;
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

public class ToolVibrationListener implements GameEventListener.Holder<VibrationSystem.Listener>, VibrationSystem {
    public final Player playerHolder;
    public static Map<Player,ToolVibrationListener> listenerMap = new HashMap<>();
    private Map<UUID,ToolVibrationAcceptor> acceptorMap = new HashMap<>();
    private final VibrationSystem.Data vibrationData;
    private final VibrationSystem.User vibrationUser;
    private final VibrationSystem.Listener vibrationListener;
    private int listenRadius;
    private boolean cachedCanListen;

    public ToolVibrationListener(Player playerHolder) {
        this.playerHolder = playerHolder;
        this.vibrationData = new Data();
        this.vibrationUser = new VibrationUser();
        this.vibrationListener = new VibrationSystem.Listener(this);
    }

    public void setListenRadius(int radius){
        this.listenRadius = radius;
    }
    public int getListenRadius(){
        return this.listenRadius;
    }

    public void tick(){
        if (playerHolder.isDeadOrDying() || !playerHolder.isAlive()){
            removeAllAcceptor(playerHolder);
            return;
        }
        if (!playerHolder.level().isClientSide) VibrationSystem.Ticker.tick(this.playerHolder.level(),this.vibrationData,this.vibrationUser);
        this.acceptorMap.forEach(((uuid, acceptor) -> acceptor.tickAcceptor(playerHolder)));
    }

    public void addAcceptor(ToolVibrationAcceptor acceptor){
        if (!this.acceptorMap.containsKey(acceptor.acceptorUUID)){
            this.listenRadius = acceptor.listenRange;
            this.acceptorMap.put(acceptor.acceptorUUID,acceptor);
        } else {
            ToolVibrationAcceptor existingAcceptor = this.acceptorMap.get(acceptor.acceptorUUID);
            if (existingAcceptor!=null){
                this.acceptorMap.put(acceptor.acceptorUUID,existingAcceptor.mergeAcceptor(acceptor));
            }else this.acceptorMap.put(acceptor.acceptorUUID,acceptor);
        }
        this.updateRange();
    }

    public void updateRange(){
        this.listenRadius=0;
        this.acceptorMap.forEach(((uuid, acceptor) -> listenRadius=Math.max(this.listenRadius,acceptor.listenRange)));
    }

    public static void tickIfPresent(ServerPlayer player){
        ToolVibrationListener listener = listenerMap.get(player);
        if (listener!=null){
            listener.tick();
        }
    }

    public static  void validate(Player player){
        ArrayList<UUID> listExistingAcceptors = new ArrayList<>(List.of());
        for(EquipmentSlot slot:EquipmentSlot.values()) {
            ItemStack stack = player.getItemBySlot(slot);
            if (stack.getItem() instanceof IModifiable) {
                ToolStack toolStack = ToolStack.from(stack);
                toolStack.getModifierList().forEach((modifier)->{
                    UUID uuid = modifier.getHook(EtSTLibHooks.VIBRATION_LISTENING).getAcceptorUUID(toolStack,modifier,player,player.level(),slot);
                    if (uuid!=null) listExistingAcceptors.add(uuid);
                });
            }
        }
        ToolVibrationListener listener = ToolVibrationListener.listenerMap.get(player);
        if (listener==null) return;
        if (listener.acceptorMap.isEmpty()) return;
        ArrayList<UUID> uuids =new ArrayList<>( listener.acceptorMap.keySet());
        for (UUID uuid:uuids) {
            if (!listExistingAcceptors.contains(uuid)) {
                removeAcceptor(player, uuid);
            }
        }
    }


    public static void decreaseLevel(Player player,UUID uuid,int toDecrease){
        if (listenerMap.get(player)!=null){
            ToolVibrationListener listener = listenerMap.get(player);
            Map<UUID,ToolVibrationAcceptor> acceptors =new HashMap<>(listener.acceptorMap);
            ArrayList<UUID> uuids =new ArrayList<>(acceptors.keySet());
            for (UUID acceptorUUID:uuids){
                if (acceptorUUID==uuid){
                    ToolVibrationAcceptor acceptor = acceptors.get(acceptorUUID);
                    acceptor.totalLevel-=toDecrease;
                    if (acceptor.totalLevel<=0) acceptors.remove(acceptorUUID);
                }
            }
            listener.acceptorMap = acceptors;
        }
    }

    public static void removeAcceptor(Player player, UUID uuid){
        if (listenerMap.get(player)!=null){
            ToolVibrationListener listener = listenerMap.get(player);
            Map<UUID,ToolVibrationAcceptor> acceptors =new HashMap<>(listener.acceptorMap);
            ArrayList<UUID> uuids =new ArrayList<>(acceptors.keySet());
            for (UUID acceptorUUID: uuids){
                if (acceptorUUID==uuid) acceptors.remove(acceptorUUID);
            }
            listener.acceptorMap = acceptors;
            if (listener.acceptorMap.isEmpty()) ToolVibrationListener.listenerMap.remove(listener.playerHolder);
        }
    }

    public static void removeAllAcceptor(Player player){
        listenerMap.remove(player);
    }

    public static void putOrMerge(@NotNull Player player, @NotNull ToolVibrationAcceptor acceptor){
        if (listenerMap.get(player)==null||listenerMap.get(player).acceptorMap.isEmpty()){
            ToolVibrationListener listener = new ToolVibrationListener(player);
            listener.addAcceptor(acceptor);
            listenerMap.put(player,listener);
        }else {
            ToolVibrationListener listener = listenerMap.get(player);
            listener.addAcceptor(acceptor);
            listenerMap.put(player,listener);
        }
    }


    @Override
    public @NotNull Listener getListener() {
        return this.vibrationListener;
    }

    @Override
    public @NotNull Data getVibrationData() {
        return vibrationData;
    }

    @Override
    public @NotNull User getVibrationUser() {
        return vibrationUser;
    }

    private void onVibrationReceived(ServerLevel pLevel, BlockPos pPos, GameEvent pGameEvent, Entity pEntity, Entity projectileOwner, float pDistance, int gameEventFrequency) {
        ArrayList<UUID> uuids =new ArrayList<>( this.acceptorMap.keySet());
        for (UUID uuid:uuids) {
            ToolVibrationAcceptor acceptor = this.acceptorMap.get(uuid);
            if (this.cachedCanListen){
                acceptor.onVibrationReceived(pLevel,playerHolder,pPos,pGameEvent,pEntity,projectileOwner,pDistance,gameEventFrequency);
                this.cachedCanListen=false;
            }
        }
    }

    private boolean canReceiveVibration(ServerLevel pLevel, BlockPos pPos, GameEvent pGameEvent, GameEvent.Context pContext) {
        BlockPos userPos = vibrationUser.getPositionSource().getPosition(pLevel).map(BlockPos::containing).orElse(pPos);
        ArrayList<UUID> uuids =new ArrayList<>( this.acceptorMap.keySet());
        for (UUID uuid:uuids){
            ToolVibrationAcceptor acceptor = this.acceptorMap.get(uuid);
            if (acceptor.canReceiveVibration(pLevel,playerHolder,userPos,pPos,pGameEvent,pContext)){
                this.cachedCanListen=true;
                return true;
            }
        }
        return false;
    }

    class VibrationUser implements VibrationSystem.User {

        @Override
        public int getListenerRadius() {
            return ToolVibrationListener.this.listenRadius;
        }

        @Override
        public PositionSource getPositionSource() {
            return new EntityPositionSource(ToolVibrationListener.this.playerHolder, ToolVibrationListener.this.playerHolder.getEyeHeight());
        }

        @Override
        public TagKey<GameEvent> getListenableEvents() {
            return GameEventTags.VIBRATIONS;
        }

        @Override
        public boolean canReceiveVibration(ServerLevel pLevel, BlockPos pPos, GameEvent pGameEvent, GameEvent.Context pContext) {
            return ToolVibrationListener.this.canReceiveVibration(pLevel,pPos,pGameEvent,pContext);
        }

        @Override
        public void onReceiveVibration(ServerLevel pLevel, BlockPos pPos, GameEvent pGameEvent, @Nullable Entity pEntity, @Nullable Entity projectileOwner, float pDistance) {
            ToolVibrationListener.this.onVibrationReceived(pLevel,pPos,pGameEvent,pEntity,projectileOwner,pDistance,VibrationSystem.getGameEventFrequency(pGameEvent));
        }
    }


}
