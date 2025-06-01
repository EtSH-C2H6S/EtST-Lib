package com.c2h6s.etstlib.tool.fluid.fluidEffect;

import com.c2h6s.etstlib.content.misc.entityTicker.EntityTicker;
import com.c2h6s.etstlib.content.misc.entityTicker.EntityTickerInstance;
import com.c2h6s.etstlib.content.misc.entityTicker.EntityTickerManager;
import com.c2h6s.etstlib.content.register.EtSTLibRegistries;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import slimeknights.mantle.data.loadable.primitive.ResourceLocationLoadable;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.tconstruct.library.json.LevelingInt;
import slimeknights.tconstruct.library.modifiers.fluid.EffectLevel;
import slimeknights.tconstruct.library.modifiers.fluid.FluidEffect;
import slimeknights.tconstruct.library.modifiers.fluid.FluidEffectContext;

public record AddEntityTickerFluidEffect(ResourceLocation tickerId, LevelingInt duration, LevelingInt level) implements FluidEffect<FluidEffectContext.Entity> {
    public static final RecordLoadable<AddEntityTickerFluidEffect> LOADER = RecordLoadable.create(
            ResourceLocationLoadable.DEFAULT.requiredField("ticker",AddEntityTickerFluidEffect::tickerId),
            LevelingInt.LOADABLE.requiredField("duration",AddEntityTickerFluidEffect::duration),
            LevelingInt.LOADABLE.requiredField("level",AddEntityTickerFluidEffect::level),
            AddEntityTickerFluidEffect::new
    );
    @Override
    public RecordLoadable<? extends FluidEffect<FluidEffectContext.Entity>> getLoader() {
        return LOADER;
    }

    @Override
    public float apply(FluidStack fluidStack, EffectLevel effectLevel, FluidEffectContext.Entity context, IFluidHandler.FluidAction fluidAction) {
        EntityTicker ticker = EtSTLibRegistries.ENTITY_TICKER_REGISTRY.getValue(tickerId());
        if (ticker==null) throw new IllegalStateException("Unregistered Entity Ticker with id '"+tickerId()+"'");
        int lvl = level.compute(effectLevel.value());
        int d = duration.compute(effectLevel.value());
        Entity entity = context.getTarget();
        if (fluidAction.execute()){
            EntityTickerManager.EntityTickerManagerInstance managerInstance = EntityTickerManager.getInstance(entity);
            EntityTickerInstance instance = new EntityTickerInstance(ticker, lvl, d);
            managerInstance.addTicker(instance,Integer::max,Integer::sum);
        }
        return effectLevel.value();
    }

    @Override
    public Component getDescription(RegistryAccess registryAccess) {
        return FluidEffect.makeTranslation(LOADER,String.valueOf(duration.compute(1)),String.valueOf(level.compute(1))).copy().append(Component.translatable("entity_ticker."+tickerId.toLanguageKey()));
    }
}
