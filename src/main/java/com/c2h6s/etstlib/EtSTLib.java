package com.c2h6s.etstlib;

import com.c2h6s.etstlib.data.predicate.LivingEntityWithHealth;
import com.c2h6s.etstlib.event.eventHandler.PlayerEvents;
import com.c2h6s.etstlib.network.EtSTLibPacketHandler;
import com.c2h6s.etstlib.register.EtSTLibBlockEntityTypes;
import com.c2h6s.etstlib.register.EtSTLibEntityTickers;
import com.c2h6s.etstlib.register.EtSTLibModifier;
import com.c2h6s.etstlib.tool.fluid.fluidEffect.*;
import com.c2h6s.etstlib.tool.hooks.modifierModules.AddDamageTypeTagArrowModule;
import com.c2h6s.etstlib.tool.hooks.modifierModules.AddDamageTypeTagMeleeModule;
import com.c2h6s.etstlib.tool.hooks.modifierModules.ForceDropModule;
import com.c2h6s.etstlib.tool.hooks.modifierModules.SetCriticalModule;
import com.c2h6s.etstlib.tool.modifiers.capabilityProvider.MekIntegration.RadiationShieldProvider;
import com.c2h6s.etstlib.tool.modifiers.capabilityProvider.PnCIntegration.AirStorageProvider;
import com.c2h6s.etstlib.util.CommonConstants;
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
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import org.slf4j.Logger;
import slimeknights.mantle.data.predicate.entity.LivingEntityPredicate;
import slimeknights.tconstruct.library.modifiers.fluid.FluidEffect;
import slimeknights.tconstruct.library.modifiers.modules.ModifierModule;
import slimeknights.tconstruct.library.tools.capability.ToolCapabilityProvider;

import java.util.Random;

import static com.c2h6s.etstlib.register.EtSTLibBlock.BLOCKS;
import static com.c2h6s.etstlib.register.EtSTLibEffects.EFFECTS;
import static com.c2h6s.etstlib.register.EtSTLibItem.ITEMS;

@Mod(EtSTLib.MODID)
public class EtSTLib {
    public static final Random random = new Random();
    public static final String MODID = "etstlib";
    public static ResourceLocation getResourceLocation(String string){
        return new ResourceLocation(MODID,string);
    }
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public EtSTLib() {
        FMLJavaModLoadingContext context = FMLJavaModLoadingContext.get();
        IEventBus modEventBus = context.getModEventBus();
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::registerSerializers);
        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, EtstLibClientConfig.ClientConfig, "etstlib-client.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, EtSTLibConfig.COMMON_CONFIG, "etstlib-common.toml");

        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        EFFECTS.register(modEventBus);
        EtSTLibBlockEntityTypes.BLOCK_ENTITIES.register(modEventBus);
        EtSTLibEntityTickers.ENTITY_TICKERS.register(modEventBus);
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
        if (ModListConstants.MekLoaded){
            ToolCapabilityProvider.register(RadiationShieldProvider::new);
        }
        if (ModListConstants.PnCLoaded){
            ToolCapabilityProvider.register(AirStorageProvider::new);
        }
    }

    void registerSerializers(RegisterEvent event) {
        if (event.getRegistryKey() == Registries.RECIPE_SERIALIZER) {
            ModifierModule.LOADER.register(getResourceLocation("set_critical"), SetCriticalModule.LOADER);
            ModifierModule.LOADER.register(getResourceLocation("force_drop"), ForceDropModule.LOADER);
            ModifierModule.LOADER.register(getResourceLocation("add_melee_damage_type"), AddDamageTypeTagMeleeModule.LOADER);
            ModifierModule.LOADER.register(getResourceLocation("add_arrow_damage_type"), AddDamageTypeTagArrowModule.LOADER);

            LivingEntityPredicate.LOADER.register(getResourceLocation("with_health"), LivingEntityWithHealth.LOADER);
            FluidEffect.ENTITY_EFFECTS.register(getResourceLocation("add_entity_ticker"), AddEntityTickerFluidEffect.LOADER);
            if (ModListConstants.MekLoaded){
                FluidEffect.ENTITY_EFFECTS.register(getResourceLocation("radiate_entity"), RadiateEntityFluidEffect.LOADER);
                FluidEffect.BLOCK_EFFECTS.register(getResourceLocation("radiate_block"), RadiateBlockFluidEffect.LOADER);
                FluidEffect.BLOCK_EFFECTS.register(getResourceLocation("clear_radiation"), ClearChunkRadiationFluidEffect.LOADER);
            }
        }
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }

}
