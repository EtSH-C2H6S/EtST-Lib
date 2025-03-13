package com.c2h6s.etstlib.tool.fluid.fluidEffect;

import com.c2h6s.etstlib.util.MathUtil;
import com.c2h6s.etstlib.util.ModListConstants;
import mekanism.api.Chunk3D;
import mekanism.common.lib.radiation.RadiationManager;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.ChunkPos;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import slimeknights.mantle.data.loadable.primitive.IntLoadable;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.tconstruct.library.modifiers.fluid.EffectLevel;
import slimeknights.tconstruct.library.modifiers.fluid.FluidEffect;
import slimeknights.tconstruct.library.modifiers.fluid.FluidEffectContext;

public record ClearChunkRadiationFluidEffect(int size) implements FluidEffect<FluidEffectContext.Block> {
    public static final RecordLoadable<ClearChunkRadiationFluidEffect> LOADER = RecordLoadable.create(
            IntLoadable.FROM_ONE.defaultField("size",0,ClearChunkRadiationFluidEffect::size),
            ClearChunkRadiationFluidEffect::new
    );


    @Override
    public @NotNull RecordLoadable<? extends FluidEffect<FluidEffectContext.Block>> getLoader() {
        return LOADER;
    }

    @Override
    public float apply(FluidStack fluidStack, EffectLevel effectLevel, FluidEffectContext.Block block, IFluidHandler.FluidAction fluidAction) {
        if (!ModListConstants.MekLoaded) throw new IllegalStateException("Mekanism mod is needed for ClearChunkRadiationFluidEffect to work.");
        if (fluidAction.execute()){
            ChunkPos centerPos = new ChunkPos(block.getBlockPos());
            RadiationManager manager = RadiationManager.get();
            int a = centerPos.x-size-1;
            do {
                int b = centerPos.z-size-1;
                do {
                    ChunkPos pos = new ChunkPos(a,b);
                    manager.removeRadiationSources(new Chunk3D(block.getLevel().dimension(), pos));
                    b++;
                }while (b< centerPos.z+size+1);
                a++;
            }while (a< centerPos.x+size+1);
        }
        return 1;
    }
    public Component getDescription(RegistryAccess registryAccess) {
        int i = size*2+1;
        return FluidEffect.makeTranslation(LOADER, i+" * "+i);
    }

}
