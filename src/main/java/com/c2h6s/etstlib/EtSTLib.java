package com.c2h6s.etstlib;

import com.c2h6s.etstlib.event.eventHandler.PlayerEvents;
import com.c2h6s.etstlib.network.EtSTLibPacketHandler;
import com.c2h6s.etstlib.register.EtSTLibModifier;
import com.c2h6s.etstlib.tool.modifiers.capabilityProvider.FEStorageProvider;
import com.c2h6s.etstlib.tool.modifiers.capabilityProvider.MekIntegration.RadiationShieldProvider;
import com.c2h6s.etstlib.tool.modifiers.capabilityProvider.PnCIntegration.AirStorageProvider;
import com.c2h6s.etstlib.util.ModListConstants;
import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;
import slimeknights.tconstruct.library.tools.capability.ToolCapabilityProvider;

import java.util.Random;

@Mod(EtSTLib.MODID)
public class EtSTLib {
    public static final Random random = new Random();
    public static final String MODID = "etstlib";
    public static ResourceLocation getResourceLocation(String string){
        return new ResourceLocation(MODID,string);
    }
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public EtSTLib() {
        FMLJavaModLoadingContext context = FMLJavaModLoadingContext.get();
        IEventBus modEventBus = context.getModEventBus();
        modEventBus.addListener(this::commonSetup);
        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        EtSTLibModifier.MODIFIERS.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);
        forgeEventBus.addListener(PlayerEvents::onLeftClickBlock);
        forgeEventBus.addListener(PlayerEvents::onLeftClick);

        EtSTLibPacketHandler.init();

        if (ModListConstants.MekLoaded){
            EtSTLibModifier.EtSTLibModifierMek.Mek_MODIFIERS.register(modEventBus);
        }
        if (ModListConstants.PnCLoaded){
            EtSTLibModifier.EtSTLibModifierPnC.PnC_MODIFIERS.register(modEventBus);
        }
        if (ModListConstants.AE2Loaded){
            EtSTLibModifier.EtSTLibModifierAE.AE_MODIFIERS.register(modEventBus);
        }
        if (ModListConstants.BOTLoaded){
            EtSTLibModifier.EtSTLibModifierBOT.BOT_MODIFIERS.register(modEventBus);
        }

    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        ToolCapabilityProvider.register(FEStorageProvider::new);
        if (ModListConstants.MekLoaded){
            ToolCapabilityProvider.register(RadiationShieldProvider::new);
        }
        if (ModListConstants.PnCLoaded){
            ToolCapabilityProvider.register(AirStorageProvider::new);
        }
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }

}
