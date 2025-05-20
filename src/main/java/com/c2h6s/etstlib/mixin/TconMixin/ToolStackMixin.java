package com.c2h6s.etstlib.mixin.TconMixin;


import com.c2h6s.etstlib.util.CommonConstants;
import com.c2h6s.etstlib.util.IToolUuidGetter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import slimeknights.tconstruct.library.tools.nbt.*;

import java.util.UUID;

@Mixin(value = ToolStack.class,remap = false)
public abstract class ToolStackMixin implements IToolUuidGetter {
    @Shadow @Final private CompoundTag nbt;

    @Unique
    @Override
    public @NotNull UUID etstlib$getUuid() {
        return UUID.fromString(nbt.getString(CommonConstants.KEY_TOOL_UUID));
    }

    @ModifyVariable(method = "<init>", at = @At("HEAD"), argsOnly = true)
    private static CompoundTag addUuidToNbt(CompoundTag nbt){
        if (!nbt.contains(CommonConstants.KEY_TOOL_UUID,Tag.TAG_STRING)){
            nbt.putString(CommonConstants.KEY_TOOL_UUID,UUID.randomUUID().toString());
        }
        return nbt;
    }
}
