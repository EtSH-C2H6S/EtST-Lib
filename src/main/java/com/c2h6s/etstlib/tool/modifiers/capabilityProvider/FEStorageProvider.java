package com.c2h6s.etstlib.tool.modifiers.capabilityProvider;

import com.c2h6s.etstlib.EtSTLib;
import com.c2h6s.etstlib.register.EtSTLibToolStat;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import slimeknights.tconstruct.library.tools.capability.ToolCapabilityProvider;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;

import java.util.function.Supplier;

public class FEStorageProvider implements ToolCapabilityProvider.IToolCapabilityProvider,IEnergyStorage {
    public final Supplier<? extends IToolStackView> tool;
    public final LazyOptional<IEnergyStorage> capOptional;

    public static final ResourceLocation LOCATION_ENERGY_STORAGE =new ResourceLocation(EtSTLib.MODID,"energy_storage");
    public static final ResourceLocation LOCATION_MAX_ENERGY =new ResourceLocation(EtSTLib.MODID,"max_energy");

    public FEStorageProvider(ItemStack stack,Supplier<? extends IToolStackView> toolStack) {
        this.tool = toolStack;
        this.capOptional = LazyOptional.of(() -> this);
    }

    @Override
    public int receiveEnergy(int amount, boolean simulate) {
        amount = Math.min(amount,this.tool.get().getStats().getInt(EtSTLibToolStat.MAX_TRANSFER));
        amount = Math.min(this.getMaxEnergyStored()-this.getEnergyStored(),amount);
        if (!simulate){
            ModDataNBT nbt = this.tool.get().getPersistentData();
            nbt.putInt(LOCATION_ENERGY_STORAGE,this.getEnergyStored()+amount);
        }
        return amount;
    }

    @Override
    public int extractEnergy(int amount, boolean simulate) {
        amount = Math.min(amount,this.tool.get().getStats().getInt(EtSTLibToolStat.MAX_TRANSFER));
        amount = Math.min(this.getEnergyStored(),amount);
        if (!simulate){
            ModDataNBT nbt = this.tool.get().getPersistentData();
            nbt.putInt(LOCATION_ENERGY_STORAGE,this.getEnergyStored()-amount);
        }
        return amount;
    }

    @Override
    public int getEnergyStored() {
        return this.tool.get().getPersistentData().getInt(LOCATION_ENERGY_STORAGE);
    }

    @Override
    public int getMaxEnergyStored() {
        return this.tool.get().getVolatileData().getInt(LOCATION_MAX_ENERGY);
    }

    @Override
    public boolean canExtract() {
        return this.tool.get().getStats().getInt(EtSTLibToolStat.MAX_TRANSFER)>0;
    }

    @Override
    public boolean canReceive() {
        return this.tool.get().getStats().getInt(EtSTLibToolStat.MAX_TRANSFER)>0;
    }

    public static int getEnergy(IToolStackView tool){
        return tool.getPersistentData().getInt(LOCATION_ENERGY_STORAGE);
    }

    public static int getMaxEnergy(IToolStackView tool) {
        return tool.getVolatileData().getInt(LOCATION_MAX_ENERGY);
    }

    public static int getMaxTransfer(IToolStackView tool){
        return tool.getStats().getInt(EtSTLibToolStat.MAX_TRANSFER);
    }

    @Override
    public <T> LazyOptional<T> getCapability(IToolStackView tool, Capability<T> capability) {
        return tool.getVolatileData().getInt(LOCATION_MAX_ENERGY)>0?ForgeCapabilities.ENERGY.orEmpty(capability, this.capOptional):LazyOptional.empty();
    }
}
