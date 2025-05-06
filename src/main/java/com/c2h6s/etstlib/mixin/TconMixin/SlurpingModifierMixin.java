package com.c2h6s.etstlib.mixin.TconMixin;

import com.c2h6s.etstlib.event.CompletelyNewEvent.FluidConsumedEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import slimeknights.tconstruct.library.tools.capability.fluid.ToolTankHelper;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.tools.modifiers.ability.fluid.SlurpingModifier;

@Mixin(value = SlurpingModifier.class,remap = false)
public abstract class SlurpingModifierMixin {
    @Inject(
            method = "finishDrinking",
            at = @At(
                    value = "INVOKE",
                    target = "Lslimeknights/tconstruct/library/tools/capability/fluid/ToolTankHelper;setFluid(Lslimeknights/tconstruct/library/tools/nbt/IToolStackView;Lnet/minecraftforge/fluids/FluidStack;)Lnet/minecraftforge/fluids/FluidStack;"
            ),
            cancellable = true,
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void onSetFluid(IToolStackView tool, Player player, CallbackInfo ci, FluidStack originalFluid, int consumed) {
        if (player.isCreative() || consumed <= 0||player.level().isClientSide()) return;
        FluidStack fluid = originalFluid.copy();
        fluid.grow(consumed);
        FluidConsumedEvent event = new FluidConsumedEvent(player, fluid, consumed, fluid.copy());
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled()) {
            ci.cancel();
        } else{
            ToolTankHelper.TANK_HELPER.setFluid(tool, event.getFinalFluid());
            ci.cancel();
        }
    }
}
