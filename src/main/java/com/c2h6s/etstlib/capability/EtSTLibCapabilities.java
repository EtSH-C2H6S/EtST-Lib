package com.c2h6s.etstlib.capability;

import com.c2h6s.etstlib.util.IToolUuidGetter;
import mekanism.common.content.gear.IModuleContainerItem;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class EtSTLibCapabilities {
    public static final Capability<IToolUuidGetter> TOOL_UUID = CapabilityManager.get(new CapabilityToken<>() {});
}
