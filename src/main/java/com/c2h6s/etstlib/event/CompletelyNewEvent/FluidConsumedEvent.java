package com.c2h6s.etstlib.event.CompletelyNewEvent;

import lombok.Getter;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fluids.FluidStack;


public class FluidConsumedEvent extends Event {
    @Getter
    private final Player player;
    @Getter
    private final FluidStack originalFluid;
    @Getter
     private int consumed;
    @Getter
    private FluidStack finalFluid;
    private boolean canceled = false;

    public FluidConsumedEvent(Player player, FluidStack originalFluid, int consumed, FluidStack currentFluid) {
        this.player = player;
        this.originalFluid = originalFluid.copy();
        this.consumed = consumed;
        var f=currentFluid.copy();
        f.setAmount(currentFluid.getAmount() - consumed);
        this.finalFluid=f;
    }

    public void setConsumed(int consumed) {
        int delta = this.consumed - consumed;
        this.consumed = consumed;
        this.finalFluid.setAmount(finalFluid.getAmount() + delta);
    }

    @Override
    public boolean isCanceled() {
        return canceled;
    }

    @Override
    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

}