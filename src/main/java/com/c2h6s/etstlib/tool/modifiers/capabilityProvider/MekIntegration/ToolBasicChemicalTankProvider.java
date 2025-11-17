package com.c2h6s.etstlib.tool.modifiers.capabilityProvider.MekIntegration;

import com.c2h6s.etstlib.EtSTLib;
import com.c2h6s.etstlib.api.interfaces.IToolProvider;
import com.c2h6s.etstlib.register.EtSTLibToolStat;
import mekanism.api.Action;
import mekanism.api.chemical.Chemical;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.chemical.IChemicalHandler;
import mekanism.api.chemical.IEmptyStackProvider;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.gas.IGasHandler;
import mekanism.api.chemical.infuse.IInfusionHandler;
import mekanism.api.chemical.infuse.InfuseType;
import mekanism.api.chemical.infuse.InfusionStack;
import mekanism.api.chemical.pigment.IPigmentHandler;
import mekanism.api.chemical.pigment.PigmentStack;
import mekanism.api.chemical.slurry.ISlurryHandler;
import mekanism.api.chemical.slurry.SlurryStack;
import mekanism.common.capabilities.Capabilities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.tools.capability.ToolCapabilityProvider;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public abstract class ToolBasicChemicalTankProvider<CHEMICAL extends Chemical<CHEMICAL>,STACK extends ChemicalStack<CHEMICAL>> implements IToolProvider, IEmptyStackProvider<CHEMICAL,STACK>,IChemicalHandler<CHEMICAL,STACK> {
    public static ResourceLocation getKeyForTank(int tanks){
        return EtSTLib.getResourceLocation("chemical_tank_"+tanks);
    }
    public static ResourceLocation getKeyForType(int tanks){
        return EtSTLib.getResourceLocation("chemical_type_"+tanks);
    }
    public final IToolStackView tool;

    public ToolBasicChemicalTankProvider(IToolStackView tool) {
        this.tool = tool;
    }

    @Override
    public @NotNull IToolStackView getTool() {
        return this.tool;
    }
    public abstract STACK createStack(CHEMICAL chemical,long amount);

    public int getChemicalTanks(IToolStackView tool){
        return tool.getStats().getInt(EtSTLibToolStat.CHEMICAL_TANK_COUNT);
    }
    public void setChemicalForTank(int tank, ChemicalStack<?> stack){
        var data = getTool().getPersistentData();
        if (stack.isEmpty()){
            data.remove(getKeyForTank(tank));
            data.remove(getKeyForType(tank));
            return;
        }
        var nbt = data.getCompound(getKeyForTank(tank));
        stack.write(nbt);
        data.put(getKeyForTank(tank),nbt);
        ResourceLocation name = stack.getTypeRegistryName();
        data.putString(getKeyForType(tank),name.toString());
    }
    public boolean isChemicalTypeValid(int tank, ChemicalStack<?> stack){
        var data = getTool().getPersistentData();
        return data.getString(getKeyForTank(tank)).equals(stack.getTypeRegistryName().toString());
    }
    public boolean isEmpty(int tank){
        return !getTool().getPersistentData().contains(getKeyForType(tank));
    }
    public static <CHEMICAL extends Chemical<CHEMICAL>,STACK extends ChemicalStack<CHEMICAL>> STACK insertChemicalToTank(int tank, IChemicalHandler<CHEMICAL,STACK> handler,ToolBasicChemicalTankProvider<CHEMICAL,STACK> provider,STACK toInsert,Action action){
        if (tank>handler.getTanks()-1) return toInsert;
        var insertCopy = provider.createStack(toInsert.getType(),toInsert.getAmount());
        if (!toInsert.isEmpty() && handler.isValid(tank,toInsert)) {
            var existing = handler.getChemicalInTank(tank);
            long needed = handler.getTankCapacity(tank) - existing.getAmount();
            if (needed <= 0L) {
                return toInsert;
            } else {
                long toAdd = Math.min(toInsert.getAmount(), needed);
                if (action.execute()) {
                    if (!existing.isEmpty()) {
                        existing.grow(toAdd);
                        handler.setChemicalInTank(tank, existing);
                    } else {
                        var stackToCreate = provider.createStack(toInsert.getType(),toAdd);
                        handler.setChemicalInTank(tank,stackToCreate);
                    }
                }
                insertCopy.shrink(toAdd);
            }
        }
        return insertCopy;
    }
    public static <CHEMICAL extends Chemical<CHEMICAL>,STACK extends ChemicalStack<CHEMICAL>> STACK extractChemicalFromTank(int tank, IChemicalHandler<CHEMICAL,STACK> handler,ToolBasicChemicalTankProvider<CHEMICAL,STACK> provider,long toExtract,Action action){
        var existing = handler.getChemicalInTank(tank);
        if (!existing.isEmpty() && toExtract >= 1L) {
            long size = Math.min(toExtract, existing.getAmount());
            if (size == 0L) {
                return provider.getEmptyStack();
            } else {
                STACK ret = provider.createStack(existing.getType(), size);
                if (!ret.isEmpty() && action.execute()) {
                    existing.shrink(ret.getAmount());
                    handler.setChemicalInTank(tank,existing);
                }
                return ret;
            }
        } else {
            return provider.getEmptyStack();
        }
    }
    public STACK findChemical(CHEMICAL chemical){
        return findChemical(createStack(chemical,1));
    }
    public STACK findChemical(STACK stack){
        return findChemical(stack1 -> stack1.isStackIdentical(stack));
    }
    public abstract STACK findChemical(Predicate<STACK> predicate);

    public Optional<STACK> findChemicalNotEmpty(CHEMICAL chemical){
        return findChemicalNotEmpty(createStack(chemical,1));
    }
    public Optional<STACK> findChemicalNotEmpty(STACK stack){
        return findChemicalNotEmpty(stack1 -> stack1.isStackIdentical(stack));
    }
    public Optional<STACK> findChemicalNotEmpty(Predicate<STACK> predicate){
        STACK stack = findChemical(predicate);
        return Optional.ofNullable(stack.isEmpty()?null:stack);
    }
    public abstract Collection<STACK> findChemicals(Predicate<STACK> predicate);

    public static class Gas extends ToolBasicChemicalTankProvider<mekanism.api.chemical.gas.Gas,GasStack> implements IGasHandler{
        public Gas(IToolStackView tool) {
            super(tool);
        }

        @Override
        public GasStack createStack(mekanism.api.chemical.gas.Gas chemical, long amount) {
            return new GasStack(()->chemical,amount);
        }

        @Override
        public GasStack findChemical(Predicate<GasStack> predicate) {
            for (int i=0;i<getTanks();i++){
                var stack = getChemicalInTank(i);
                if (predicate.test(stack)) return stack;
            }
            return getEmptyStack();
        }

        @Override
        public Collection<GasStack> findChemicals(Predicate<GasStack> predicate) {
            List<GasStack> list = new java.util.ArrayList<>(List.of());
            for (int i=0;i<getTanks();i++){
                var stack = getChemicalInTank(i);
                if (predicate.test(stack)) list.add(stack);
            }
            return list;
        }

        @Override
        public int getTanks() {
            return getChemicalTanks(getTool());
        }
        @Override
        public @NotNull GasStack getChemicalInTank(int i) {
            CompoundTag nbt = getTool().getPersistentData().getCompound(getKeyForTank(i));
            return GasStack.readFromNBT(nbt);
        }
        @Override
        public void setChemicalInTank(int i, GasStack stack) {
            setChemicalForTank(i,stack);
        }
        @Override
        public long getTankCapacity(int i) {
            return getTool().getStats().get(EtSTLibToolStat.CHEMICAL_TANK_CAPACITY).longValue();
        }
        @Override
        public boolean isValid(int i, GasStack stack) {
            return isEmpty(i)||(isChemicalTypeValid(i,stack)&&stack.isStackIdentical(getChemicalInTank(i)));
        }
        @Override
        public @NotNull GasStack insertChemical(int i, GasStack stack, Action action) {
            return insertChemicalToTank(i,this,this,stack,action);
        }
        @Override
        public @NotNull GasStack extractChemical(int i, long l, Action action) {
            return extractChemicalFromTank(i,this,this,l,action);
        }
    }
    public static class Infuse extends ToolBasicChemicalTankProvider<InfuseType,InfusionStack> implements IInfusionHandler {
        public Infuse(IToolStackView tool) {
            super(tool);
        }

        @Override
        public InfusionStack createStack(InfuseType chemical, long amount) {
            return new InfusionStack(()->chemical,amount);
        }

        @Override
        public InfusionStack findChemical(Predicate<InfusionStack> predicate) {
            for (int i=0;i<getTanks();i++){
                var stack = getChemicalInTank(i);
                if (predicate.test(stack)) return stack;
            }
            return getEmptyStack();
        }

        @Override
        public Collection<InfusionStack> findChemicals(Predicate<InfusionStack> predicate) {
            List<InfusionStack> list = new java.util.ArrayList<>(List.of());
            for (int i=0;i<getTanks();i++){
                var stack = getChemicalInTank(i);
                if (predicate.test(stack)) list.add(stack);
            }
            return list;
        }

        @Override
        public int getTanks() {
            return getChemicalTanks(getTool());
        }
        @Override
        public @NotNull InfusionStack getChemicalInTank(int i) {
            CompoundTag nbt = getTool().getPersistentData().getCompound(getKeyForTank(i));
            return InfusionStack.readFromNBT(nbt);
        }
        @Override
        public void setChemicalInTank(int i, InfusionStack stack) {
            setChemicalForTank(i,stack);
        }
        @Override
        public long getTankCapacity(int i) {
            return getTool().getStats().get(EtSTLibToolStat.CHEMICAL_TANK_CAPACITY).longValue();
        }
        @Override
        public boolean isValid(int i, InfusionStack stack) {
            return isEmpty(i)||(isChemicalTypeValid(i,stack)&&stack.isStackIdentical(getChemicalInTank(i)));
        }
        @Override
        public @NotNull InfusionStack insertChemical(int i, InfusionStack stack, Action action) {
            return insertChemicalToTank(i,this,this,stack,action);
        }
        @Override
        public @NotNull InfusionStack extractChemical(int i, long l, Action action) {
            return extractChemicalFromTank(i,this,this,l,action);
        }
    }
    public static class Slurry extends ToolBasicChemicalTankProvider<mekanism.api.chemical.slurry.Slurry,SlurryStack> implements ISlurryHandler {
        public Slurry(IToolStackView tool) {
            super(tool);
        }

        @Override
        public SlurryStack createStack(mekanism.api.chemical.slurry.Slurry chemical, long amount) {
            return new SlurryStack(()->chemical,amount);
        }

        @Override
        public SlurryStack findChemical(Predicate<SlurryStack> predicate) {
            for (int i=0;i<getTanks();i++){
                var stack = getChemicalInTank(i);
                if (predicate.test(stack)) return stack;
            }
            return getEmptyStack();
        }

        @Override
        public Collection<SlurryStack> findChemicals(Predicate<SlurryStack> predicate) {
            List<SlurryStack> list = new java.util.ArrayList<>(List.of());
            for (int i=0;i<getTanks();i++){
                var stack = getChemicalInTank(i);
                if (predicate.test(stack)) list.add(stack);
            }
            return list;
        }


        @Override
        public int getTanks() {
            return getChemicalTanks(getTool());
        }
        @Override
        public @NotNull SlurryStack getChemicalInTank(int i) {
            CompoundTag nbt = getTool().getPersistentData().getCompound(getKeyForTank(i));
            return SlurryStack.readFromNBT(nbt);
        }
        @Override
        public void setChemicalInTank(int i, SlurryStack stack) {
            setChemicalForTank(i,stack);
        }
        @Override
        public long getTankCapacity(int i) {
            return getTool().getStats().get(EtSTLibToolStat.CHEMICAL_TANK_CAPACITY).longValue();
        }
        @Override
        public boolean isValid(int i, SlurryStack stack) {
            return isEmpty(i)||(isChemicalTypeValid(i,stack)&&stack.isStackIdentical(getChemicalInTank(i)));
        }
        @Override
        public @NotNull SlurryStack insertChemical(int i, SlurryStack stack, Action action) {
            return insertChemicalToTank(i,this,this,stack,action);
        }
        @Override
        public @NotNull SlurryStack extractChemical(int i, long l, Action action) {
            return extractChemicalFromTank(i,this,this,l,action);
        }
    }
    public static class Pigment extends ToolBasicChemicalTankProvider<mekanism.api.chemical.pigment.Pigment,PigmentStack> implements IPigmentHandler {
        public Pigment(IToolStackView tool) {
            super(tool);
        }

        @Override
        public PigmentStack createStack(mekanism.api.chemical.pigment.Pigment chemical, long amount) {
            return new PigmentStack(()->chemical,amount);
        }

        @Override
        public PigmentStack findChemical(Predicate<PigmentStack> predicate) {
            for (int i=0;i<getTanks();i++){
                var stack = getChemicalInTank(i);
                if (predicate.test(stack)) return stack;
            }
            return getEmptyStack();
        }

        @Override
        public Collection<PigmentStack> findChemicals(Predicate<PigmentStack> predicate) {
            List<PigmentStack> list = new java.util.ArrayList<>(List.of());
            for (int i=0;i<getTanks();i++){
                var stack = getChemicalInTank(i);
                if (predicate.test(stack)) list.add(stack);
            }
            return list;
        }


        @Override
        public int getTanks() {
            return getChemicalTanks(getTool());
        }
        @Override
        public @NotNull PigmentStack getChemicalInTank(int i) {
            CompoundTag nbt = getTool().getPersistentData().getCompound(getKeyForTank(i));
            return PigmentStack.readFromNBT(nbt);
        }
        @Override
        public void setChemicalInTank(int i, PigmentStack stack) {
            setChemicalForTank(i,stack);
        }
        @Override
        public long getTankCapacity(int i) {
            return getTool().getStats().get(EtSTLibToolStat.CHEMICAL_TANK_CAPACITY).longValue();
        }
        @Override
        public boolean isValid(int i, PigmentStack stack) {
            return isEmpty(i)||(isChemicalTypeValid(i,stack)&&stack.isStackIdentical(getChemicalInTank(i)));
        }
        @Override
        public @NotNull PigmentStack insertChemical(int i, PigmentStack stack, Action action) {
            return insertChemicalToTank(i,this,this,stack,action);
        }
        @Override
        public @NotNull PigmentStack extractChemical(int i, long l, Action action) {
            return extractChemicalFromTank(i,this,this,l,action);
        }
    }

    public static class CapabilityProvider implements ToolCapabilityProvider.IToolCapabilityProvider{
        @Override
        public <T> LazyOptional<T> getCapability(IToolStackView tool, Capability<T> cap) {
            if (tool.getStats().get(EtSTLibToolStat.CHEMICAL_TANK_COUNT).longValue()<=0) return LazyOptional.empty();
            if (cap== Capabilities.GAS_HANDLER){
                return LazyOptional.of(()->new Gas(tool)).cast();
            } else if (cap==Capabilities.INFUSION_HANDLER){
                return LazyOptional.of(()->new Infuse(tool)).cast();
            } else if (cap==Capabilities.SLURRY_HANDLER){
                return LazyOptional.of(()->new Slurry(tool)).cast();
            } else if (cap==Capabilities.PIGMENT_HANDLER){
                return LazyOptional.of(()->new Pigment(tool)).cast();
            }
            return LazyOptional.empty();
        }
    }
}
