package com.c2h6s.etstlib.event.CompletelyNewEvent;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fluids.FluidStack;


public class FluidConsumedEvent extends Event {
    private final Player player;
    private final FluidStack originalFluid;
    private int consumed;
    private FluidStack finalFluid;
    private boolean canceled = false;

    public FluidConsumedEvent(Player player, FluidStack originalFluid, int consumed, FluidStack currentFluid) {
        this.player = player;
        this.originalFluid = originalFluid.copy();
        this.consumed = consumed;
        this.finalFluid = new FluidStack(currentFluid.getFluid(), currentFluid.getAmount() - consumed);
    }

    public void setConsumed(int consumed) {
        int delta = this.consumed - consumed;
        this.consumed = consumed;
        this.finalFluid.setAmount(finalFluid.getAmount() + delta);
    }
    public int getConsumed(){
        return consumed;
    }

    public FluidStack getOriginalFluid() {
        return originalFluid;
    }

    @Override
    public boolean isCanceled() {
        return canceled;
    }
    public FluidStack getFinalFluid(){
        return finalFluid;
    }

    @Override
    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    public Player getPlayer() {
        return player;
    }
}