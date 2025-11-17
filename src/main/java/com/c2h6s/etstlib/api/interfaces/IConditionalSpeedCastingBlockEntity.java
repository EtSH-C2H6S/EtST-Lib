package com.c2h6s.etstlib.api.interfaces;

import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import slimeknights.tconstruct.library.recipe.casting.ICastingRecipe;

public interface IConditionalSpeedCastingBlockEntity {
    int modifyTotalCoolingTime(FluidStack fluidStack, ICastingRecipe recipe, int initialTime);
    int getBoost(int coolingTime);
}
