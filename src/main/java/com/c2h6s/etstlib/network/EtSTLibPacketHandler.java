package com.c2h6s.etstlib.network;

import com.c2h6s.etstlib.EtSTLib;
import com.c2h6s.etstlib.network.packets.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class EtSTLibPacketHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static SimpleChannel INSTANCE ;
    static int id = 0;

    public static void init() {
        INSTANCE = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(EtSTLib.MODID, "etstlib_msg")).networkProtocolVersion(() -> "1").clientAcceptedVersions(s -> true).serverAcceptedVersions(s -> true).simpleChannel();
        INSTANCE.messageBuilder(PLeftClickEmptyC2S.class,id++, NetworkDirection.PLAY_TO_SERVER).decoder(PLeftClickEmptyC2S::new).encoder(PLeftClickEmptyC2S::toByte).consumerMainThread(PLeftClickEmptyC2S::handle).add();
    }

    public static <MSG> void sendToServer(MSG msg){
        INSTANCE.sendToServer(msg);
    }

    public static <MSG> void sendToPlayer(MSG msg, ServerPlayer player){
        INSTANCE.send(PacketDistributor.PLAYER.with(()->player),msg);
    }

    public static <MSG> void sendToClient(MSG msg){
        INSTANCE.send(PacketDistributor.ALL.noArg(), msg);
    }

}
