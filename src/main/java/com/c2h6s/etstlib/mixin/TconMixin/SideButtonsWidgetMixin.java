package com.c2h6s.etstlib.mixin.TconMixin;

import com.c2h6s.etstlib.EtstLibClientConfig;
import com.c2h6s.etstlib.mixinUtil.ITinkerStationScreenMixin;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import slimeknights.tconstruct.tables.client.inventory.widget.SideButtonsWidget;

@Mixin(value = SideButtonsWidget.class,remap = false)
public class SideButtonsWidgetMixin {
    @ModifyVariable(method = "setButtonPositions",at = @At(value = "STORE"),ordinal = 2)
    public int modifyButtonY(int par1) {
        if (Minecraft.getInstance().screen instanceof ITinkerStationScreenMixin screen && screen.etstlib$getScrollingWidget() != null) {
            return par1 - screen.etstlib$getScrollingWidget().getScrollAmount();
        }
        return par1;
    }
}
