package com.c2h6s.etstlib.mixin.TconMixin;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import slimeknights.tconstruct.tables.client.inventory.BaseTabbedScreen;
import slimeknights.tconstruct.tables.client.inventory.ToolTableScreen;
import slimeknights.tconstruct.tables.menu.TabbedContainerMenu;

@Mixin(ToolTableScreen.class)
public abstract class ArmorStandRendererMixin <T extends BlockEntity, C extends TabbedContainerMenu<T>> extends BaseTabbedScreen<T,C> {
    public ArmorStandRendererMixin(C c, Inventory playerInventory, Component title) {
        super(c, playerInventory, title);
    }
    @Inject(method = "init",at = @At("HEAD"), cancellable = true)
    protected void init(CallbackInfo ci){
        super.init();
        ci.cancel();
    }
}
