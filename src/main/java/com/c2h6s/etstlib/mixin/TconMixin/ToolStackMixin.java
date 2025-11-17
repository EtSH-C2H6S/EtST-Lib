package com.c2h6s.etstlib.mixin.TconMixin;

import com.c2h6s.etstlib.util.IToolUuidGetter;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.UUID;

@Mixin(value = ToolStack.class,remap = false)
public class ToolStackMixin implements IToolUuidGetter {
    @Override
    public @Nullable UUID etstlib$getUuid() {
        return getUuid(((ToolStack)(Object)this).createStack());
    }
}
