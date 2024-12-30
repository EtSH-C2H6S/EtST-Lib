package com.c2h6s.etstlib.tool.modifiers.capabilityProvider.MekIntegration;

import mekanism.common.content.gear.IModuleContainerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import slimeknights.tconstruct.library.tools.capability.ToolCapabilityProvider;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.function.Supplier;

public class ModuleContainerProvider implements ToolCapabilityProvider.IToolCapabilityProvider, IModuleContainerItem {
    public final Supplier<? extends IToolStackView> tool;
    public final LazyOptional<IModuleContainerItem> capOptional;
    public ModuleContainerProvider(ItemStack stack, Supplier<? extends IToolStackView> toolStack){
        tool =toolStack;
        capOptional =LazyOptional.of(()->this);
    }


    @Override
    public <T> LazyOptional<T> getCapability(IToolStackView iToolStackView, Capability<T> capability) {
        return null;
    }
}
