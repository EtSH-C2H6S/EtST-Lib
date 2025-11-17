package com.c2h6s.etstlib.tool.modifiers.capabilityProvider;

import com.c2h6s.etstlib.capability.EtSTLibCapabilities;
import com.c2h6s.etstlib.util.CommonConstants;
import com.c2h6s.etstlib.util.IToolUuidGetter;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import slimeknights.tconstruct.library.tools.capability.ToolCapabilityProvider;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

public class ToolUuidCapabilityProvider implements ToolCapabilityProvider.IToolCapabilityProvider {
    @Override
    public <T> LazyOptional<T> getCapability(IToolStackView tool, Capability<T> cap) {
        if (cap== EtSTLibCapabilities.TOOL_UUID) {
            ItemStack stack = ((ToolStack) tool).createStack();
            var nbt = stack.getTag();
            if (nbt!=null&&nbt.contains(CommonConstants.KEY_TOOL_UUID, Tag.TAG_STRING))
                return LazyOptional.of(()->new IToolUuidGetter(){}).cast();
        }
        return LazyOptional.empty();
    }
}
