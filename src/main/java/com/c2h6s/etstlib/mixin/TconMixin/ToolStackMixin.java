package com.c2h6s.etstlib.mixin.TconMixin;


import com.c2h6s.etstlib.util.CommonConstants;
import com.c2h6s.etstlib.util.IToolUuidGetter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.*;

import java.util.UUID;

@Mixin(value = ToolStack.class,remap = false)
public abstract class ToolStackMixin implements IToolUuidGetter {
    @Shadow @Final private CompoundTag nbt;

    @Unique
    @Override
    public @Nullable UUID etstlib$getUuid() {
        if (nbt.contains(CommonConstants.KEY_TOOL_UUID,Tag.TAG_STRING)) return UUID.fromString(nbt.getString(CommonConstants.KEY_TOOL_UUID));
        return null;
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void addUuidToNbt(Item item, ToolDefinition definition, CompoundTag nbt, CallbackInfo ci){
        if (item instanceof IModifiable&&item.getMaxStackSize(new ItemStack(item))<=1){
            if (!nbt.contains(CommonConstants.KEY_TOOL_UUID,Tag.TAG_STRING)){
                nbt.putString(CommonConstants.KEY_TOOL_UUID,UUID.randomUUID().toString());
            }
        }
    }
}
