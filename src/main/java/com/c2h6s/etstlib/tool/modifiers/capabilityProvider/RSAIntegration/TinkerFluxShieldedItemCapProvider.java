package com.c2h6s.etstlib.tool.modifiers.capabilityProvider.RSAIntegration;

import cofh.redstonearsenal.common.capability.CapabilityFluxShielding;
import cofh.redstonearsenal.common.capability.IFluxShieldedItem;
import com.c2h6s.etstlib.EtSTLib;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import slimeknights.tconstruct.library.tools.capability.ToolCapabilityProvider;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.function.Supplier;

public class TinkerFluxShieldedItemCapProvider implements IFluxShieldedItem, ToolCapabilityProvider.IToolCapabilityProvider {
    public static final ResourceLocation KEY_MAX_CHARGE = EtSTLib.getResourceLocation("rsa_max_charge");
    public static final ResourceLocation KEY_CUR_CHARGE = EtSTLib.getResourceLocation("rsa_cur_charge");
    public final IToolStackView tool;

    public TinkerFluxShieldedItemCapProvider(IToolStackView tool) {
        this.tool = tool;
    }
    public TinkerFluxShieldedItemCapProvider(Supplier<? extends IToolStackView> supplier){
        this.tool = supplier.get();
    }

    @Override
    public int currCharges(LivingEntity livingEntity) {
        return tool.getPersistentData().getInt(KEY_CUR_CHARGE);
    }

    @Override
    public int maxCharges(LivingEntity livingEntity) {
        return tool.getVolatileData().getInt(KEY_MAX_CHARGE);
    }

    @Override
    public boolean useCharge(LivingEntity livingEntity) {
        if (currCharges(livingEntity)>=1){
            if (livingEntity.invulnerableTime==0) tool.getPersistentData().putFloat(KEY_CUR_CHARGE,tool.getPersistentData().getInt(KEY_CUR_CHARGE)-1);
            else if (livingEntity.invulnerableTime<10) livingEntity.invulnerableTime=10;
            return true;
        }
        return false;
    }

    @Override
    public <T> LazyOptional<T> getCapability(IToolStackView tool, Capability<T> cap) {
        if (cap== CapabilityFluxShielding.FLUX_SHIELDED_ITEM_CAPABILITY){
            return LazyOptional.of(()->new TinkerFluxShieldedItemCapProvider(tool)).cast();
        }
        return LazyOptional.empty();
    }
}
