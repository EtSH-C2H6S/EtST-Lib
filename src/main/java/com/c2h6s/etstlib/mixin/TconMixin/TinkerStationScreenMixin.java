package com.c2h6s.etstlib.mixin.TconMixin;

import com.c2h6s.etstlib.EtstLibClientConfig;
import com.c2h6s.etstlib.client.objects.TinkerStationScrollingWidget;
import com.c2h6s.etstlib.mixinUtil.ITinkerStationScreenMixin;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import slimeknights.mantle.client.screen.ElementScreen;
import slimeknights.mantle.client.screen.ScalableElementScreen;
import slimeknights.tconstruct.tables.block.entity.table.TinkerStationBlockEntity;
import slimeknights.tconstruct.tables.client.inventory.TinkerStationScreen;
import slimeknights.tconstruct.tables.client.inventory.ToolTableScreen;
import slimeknights.tconstruct.tables.client.inventory.widget.TinkerStationButtonsWidget;
import slimeknights.tconstruct.tables.menu.TinkerStationContainerMenu;

@Mixin(value = TinkerStationScreen.class,remap = false)
public class TinkerStationScreenMixin extends ToolTableScreen<TinkerStationBlockEntity, TinkerStationContainerMenu> implements ITinkerStationScreenMixin {
    @Shadow protected TinkerStationButtonsWidget buttonsScreen;
    @Shadow protected ScalableElementScreen centerBeam;
    @Shadow protected ElementScreen buttonDecorationTop;
    @Unique private TinkerStationScrollingWidget etstlib$scrollingWidget = null;

    public TinkerStationScreenMixin(TinkerStationContainerMenu tinkerStationContainerMenu, Inventory playerInventory, Component title) {
        super(tinkerStationContainerMenu, playerInventory, title);
    }

    @Override
    public TinkerStationScrollingWidget etstlib$getScrollingWidget() {
        return etstlib$scrollingWidget;
    }

    @Override
    public TinkerStationButtonsWidget etstlib$getButtonsScreen() {
        return buttonsScreen;
    }

    @Override
    public void etstlib$postInit() {
        if (EtstLibClientConfig.toggleScrollingBar.get()) {
            int overCount = ((this.buttonsScreen.getButtons().size() / 5) - EtstLibClientConfig.scrollingRollsThreshold.get()) * 22;
            if (overCount > 0) {
                this.etstlib$scrollingWidget = new TinkerStationScrollingWidget(this, this.buttonsScreen.getLeftPos() - 16, this.cornerY + this.centerBeam.h + this.buttonDecorationTop.h, 128, 176, Component.empty(), 176 + overCount);
                this.addRenderableWidget(this.etstlib$scrollingWidget);
            }
        }
    }
}
