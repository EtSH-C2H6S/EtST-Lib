package com.c2h6s.etstlib.tool.modifiers.capabilityProvider.MekIntegration;

import com.c2h6s.etstlib.EtSTLib;
import mekanism.api.radiation.capability.IRadiationShielding;
import mekanism.common.capabilities.Capabilities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import slimeknights.tconstruct.library.tools.capability.ToolCapabilityProvider;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.function.Supplier;

public class RadiationShieldProvider implements ToolCapabilityProvider.IToolCapabilityProvider, IRadiationShielding {
    public final Supplier<? extends IToolStackView> tool;
    public final LazyOptional<IRadiationShielding> capOptional;
    public static final ResourceLocation LOCATION_RADIATION_SHIELDING = EtSTLib.getResourceLocation("radiation_shielding");
    public RadiationShieldProvider(ItemStack stack, Supplier<? extends IToolStackView> toolStack){
        tool =toolStack;
        capOptional =LazyOptional.of(()->this);
    }

    @Override
    public double getRadiationShielding() {
        return this.tool.get().getVolatileData().get(LOCATION_RADIATION_SHIELDING, CompoundTag::getDouble);
    }

    @Override
    public <T> LazyOptional<T> getCapability(IToolStackView iToolStackView, Capability<T> capability) {
        return getRadiationShielding()>0?Capabilities.RADIATION_SHIELDING.orEmpty(capability,this.capOptional):LazyOptional.empty();
    }
}
