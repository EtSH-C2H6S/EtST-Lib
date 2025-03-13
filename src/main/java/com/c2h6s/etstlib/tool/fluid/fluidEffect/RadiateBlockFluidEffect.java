package com.c2h6s.etstlib.tool.fluid.fluidEffect;

import com.c2h6s.etstlib.util.MathUtil;
import com.c2h6s.etstlib.util.ModListConstants;
import mekanism.api.Coord4D;
import mekanism.common.lib.radiation.RadiationManager;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.tconstruct.library.json.LevelingValue;
import slimeknights.tconstruct.library.modifiers.fluid.EffectLevel;
import slimeknights.tconstruct.library.modifiers.fluid.FluidEffect;
import slimeknights.tconstruct.library.modifiers.fluid.FluidEffectContext;

public record RadiateBlockFluidEffect(LevelingValue amount) implements FluidEffect<FluidEffectContext.Block> {
    public static final RecordLoadable<RadiateBlockFluidEffect> LOADER = RecordLoadable.create(
            LevelingValue.LOADABLE.requiredField("radiation_amount", RadiateBlockFluidEffect::amount),
            RadiateBlockFluidEffect::new
    );

    @Override
    public @NotNull RecordLoadable<? extends FluidEffect<FluidEffectContext.Block>> getLoader() {
        return LOADER;
    }

    @Override
    public float apply(FluidStack fluidStack, EffectLevel effectLevel, FluidEffectContext.Block context, IFluidHandler.FluidAction fluidAction) {
        if (!ModListConstants.MekLoaded) throw new IllegalStateException("Mekanism mod is needed for RadiateBlockFluidEffect to work.");
        float value = amount.compute(effectLevel.value());
        if (value==0) return 0;
        if (fluidAction.execute()){
            RadiationManager manager = RadiationManager.get();
            manager.radiate(new Coord4D(context.getBlockPos(),context.getLevel()), value);
        }
        return amount.isFlat()?1f:effectLevel.value();
    }

    @Override
    public Component getDescription(RegistryAccess registryAccess) {
        return FluidEffect.makeTranslation(LOADER, MathUtil.getUnitFloat(amount.computeForLevel(1))+"Sv");
    }
}