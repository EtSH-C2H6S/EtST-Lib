package com.c2h6s.etstlib.tool.modifiers.capabilityProvider.PnCIntegration;

import com.c2h6s.etstlib.EtSTLib;
import me.desht.pneumaticcraft.api.PNCCapabilities;
import me.desht.pneumaticcraft.api.pressure.IPressurizableItem;
import me.desht.pneumaticcraft.api.tileentity.IAirHandlerItem;
import me.desht.pneumaticcraft.common.capabilities.AirHandlerItemStack;
import me.desht.pneumaticcraft.common.core.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import slimeknights.tconstruct.library.tools.capability.ToolCapabilityProvider;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.function.Supplier;

public class AirStorageProvider implements ToolCapabilityProvider.IToolCapabilityProvider, IPressurizableItem {
    public final Supplier<? extends IToolStackView> tool;
    public final LazyOptional<IPressurizableItem> capOptional;
    public final float maxPressure;
    public final LazyOptional<IAirHandlerItem> airHandlerItemLazyOptional;
    public static final ResourceLocation LOCATION_BASE_VOLUME =new ResourceLocation(EtSTLib.MODID,"base_volume");
    public static final ResourceLocation LOCATION_MAX_PRESSURE =new ResourceLocation(EtSTLib.MODID,"max_pressure");
    public static final ResourceLocation LOCATION_AIR_STORAGE =new ResourceLocation(EtSTLib.MODID,"air");

    public final AirHandlerItemStack airHandlerItemStack;


    public AirStorageProvider(ItemStack stack, Supplier<? extends IToolStackView> tool) {
        this.tool = tool;
        this.capOptional = LazyOptional.of(()->this);
        this.maxPressure =tool.get().getVolatileData().getInt(LOCATION_MAX_PRESSURE);
        this.airHandlerItemStack =new AirHandlerItemStack(new ItemStack(ModItems.REINFORCED_AIR_CANISTER.get())){
            @Override
            public int getAir() {
                return tool.get().getPersistentData().getInt(LOCATION_AIR_STORAGE);
            }

            @Override
            public void addAir(int amount) {
                if (tool.get().getPersistentData().contains(LOCATION_AIR_STORAGE,3)) {
                    tool.get().getPersistentData().putInt(LOCATION_AIR_STORAGE, tool.get().getPersistentData().getInt(LOCATION_AIR_STORAGE) + amount);
                }else {
                    tool.get().getPersistentData().putInt(LOCATION_AIR_STORAGE, amount);
                }
            }

            @Override
            public int getBaseVolume() {
                return tool.get().getVolatileData().getInt(LOCATION_BASE_VOLUME);
            }

            @Override
            public float getPressure() {
                return (float) this.getAir() /this.getBaseVolume();
            }

            @Override
            public void setBaseVolume(int newBaseVolume) {
            }

            @Override
            public int getVolume() {
                return this.getBaseVolume();
            }

            @Override
            public float maxPressure() {
                return tool.get().getVolatileData().getFloat(LOCATION_MAX_PRESSURE);
            }
        };
        this.airHandlerItemLazyOptional =LazyOptional.of(()->this.airHandlerItemStack);
    }

    @Override
    public <T> LazyOptional<T> getCapability(IToolStackView iToolStackView, Capability<T> capability) {
        if (tool.get().getVolatileData().getInt(LOCATION_BASE_VOLUME)>0) {
            return PNCCapabilities.AIR_HANDLER_ITEM_CAPABILITY.orEmpty(capability,airHandlerItemLazyOptional);
        }
        return LazyOptional.empty();
    }

    @Override
    public int getBaseVolume() {
        return tool.get().getVolatileData().getInt(LOCATION_BASE_VOLUME);
    }

    @Override
    public int getVolumeUpgrades(ItemStack itemStack) {
        return 0;
    }

    @Override
    public int getAir(ItemStack itemStack) {
        return tool.get().getPersistentData().getInt(LOCATION_AIR_STORAGE);
    }

    @Override
    public float getMaxPressure() {
        return getPressure(this.tool.get());
    }

    @Override
    public int getEffectiveVolume(ItemStack stack) {
        return getBaseVolume();
    }

    @Override
    public float getPressure(ItemStack stack) {
        return getPressure(this.tool.get());
    }

    public static float getPressure(IToolStackView tool){
        return (float) tool.getPersistentData().getInt(LOCATION_AIR_STORAGE) /tool.getVolatileData().getInt(LOCATION_BASE_VOLUME);
    }

    public static int getAir(IToolStackView tool){
        return tool.getPersistentData().getInt(LOCATION_AIR_STORAGE);
    }

    public static void addAir(IToolStackView tool,int amount) {
        if (tool.getPersistentData().contains(LOCATION_AIR_STORAGE,3)) {
            tool.getPersistentData().putInt(LOCATION_AIR_STORAGE, tool.getPersistentData().getInt(LOCATION_AIR_STORAGE) + amount);
            if (tool.getPersistentData().getInt(LOCATION_AIR_STORAGE)<0){
                tool.getPersistentData().putInt(LOCATION_AIR_STORAGE,0);
            }
        }else {
            tool.getPersistentData().putInt(LOCATION_AIR_STORAGE, amount);
        }
    }

    public static int getBaseVolume(IToolStackView tool){
        return tool.getVolatileData().getInt(LOCATION_BASE_VOLUME);
    }

    public static float getMaxPressure(IToolStackView tool){
        return tool.getVolatileData().getInt(LOCATION_MAX_PRESSURE);
    }
}
