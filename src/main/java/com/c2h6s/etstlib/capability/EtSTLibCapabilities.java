package com.c2h6s.etstlib.capability;

import mekanism.common.content.gear.IModuleContainerItem;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class EtSTLibCapabilities {
    public static final Capability<IModuleContainerItem> MODULE_CONTAINER = CapabilityManager.get(new CapabilityToken<>() {});
}
