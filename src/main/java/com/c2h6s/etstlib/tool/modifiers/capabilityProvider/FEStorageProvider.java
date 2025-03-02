package com.c2h6s.etstlib.tool.modifiers.capabilityProvider;

import com.c2h6s.etstlib.EtSTLib;
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

import static com.c2h6s.etstlib.register.EtSTLibToolStat.*;

public class FEStorageProvider implements ToolCapabilityProvider.IToolCapabilityProvider,IEnergyStorage {
    public final Supplier<? extends IToolStackView> tool;
    public final LazyOptional<IEnergyStorage> capOptional;

    public static final ResourceLocation LOCATION_ENERGY_STORAGE =new ResourceLocation(EtSTLib.MODID,"energy_storage");

    public FEStorageProvider(ItemStack stack,Supplier<? extends IToolStackView> toolStack) {
        this.tool = toolStack;
        this.capOptional = LazyOptional.of(() -> this);
    }

    @Override
    public int receiveEnergy(int amount, boolean simulate) {
        return receiveEnergy(this.tool.get(),amount,simulate,false);
    }

    @Override
    public int extractEnergy(int amount, boolean simulate) {
        return extractEnergy(this.tool.get(),amount,simulate,false);
    }

    @Override
    public int getEnergyStored() {
        return this.tool.get().getPersistentData().getInt(LOCATION_ENERGY_STORAGE);
    }

    @Override
    public int getMaxEnergyStored() {
        return this.tool.get().getStats().getInt(MAX_ENERGY);
    }

    @Override
    public boolean canExtract() {
        return getMaxTransfer(tool.get())>0;
    }

    @Override
    public boolean canReceive() {
        return getMaxTransfer(tool.get())>0;
    }

    public static int getEnergy(IToolStackView tool){
        return tool.getPersistentData().getInt(LOCATION_ENERGY_STORAGE);
    }

    public static int getMaxEnergy(IToolStackView tool) {
        return tool.getStats().getInt(MAX_ENERGY);
    }

    public static int getMaxTransfer(IToolStackView tool){
        return tool.getStats().getInt(MAX_TRANSFER);
    }

    public static int extractEnergy(IToolStackView tool,int amount,boolean simulate,boolean bypassMaxTransfer){
        if (!bypassMaxTransfer) {
            amount = Math.min(amount, getMaxTransfer(tool));
        }
        amount = Math.min(getEnergy(tool),amount);
        if (!simulate){
            ModDataNBT nbt = tool.getPersistentData();
            nbt.putInt(LOCATION_ENERGY_STORAGE,getEnergy(tool)-amount);
        }
        return amount;
    }

    public static int receiveEnergy(IToolStackView tool,int amount,boolean simulate,boolean bypassMaxTransfer){
        if (!bypassMaxTransfer) {
            amount = Math.min(amount, getMaxTransfer(tool));
        }
        amount = Math.min(getMaxEnergy(tool) - getEnergy(tool),amount);
        if (!simulate){
            ModDataNBT nbt = tool.getPersistentData();
            nbt.putInt(LOCATION_ENERGY_STORAGE,getEnergy(tool)+amount);
        }
        return amount;
    }

    public static void setEnergy(IToolStackView tool,int amount){
        ModDataNBT nbt = tool.getPersistentData();
        nbt.putInt(LOCATION_ENERGY_STORAGE,amount);
    }

    @Override
    public <T> LazyOptional<T> getCapability(IToolStackView tool, Capability<T> capability) {
        return tool.getStats().getInt(MAX_ENERGY)>0?ForgeCapabilities.ENERGY.orEmpty(capability, this.capOptional):LazyOptional.empty();
    }
}
