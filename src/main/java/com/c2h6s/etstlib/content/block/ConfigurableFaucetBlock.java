package com.c2h6s.etstlib.content.block;

import com.c2h6s.etstlib.api.interfaces.IConfigurableFaucetBlock;
import com.c2h6s.etstlib.content.blockEntity.ConfigurableFaucetBlockEntity;
import com.c2h6s.etstlib.register.EtSTLibBlockEntityTypes;
import com.c2h6s.etstlib.util.MathUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.util.BlockEntityHelper;
import slimeknights.tconstruct.smeltery.block.FaucetBlock;
import slimeknights.tconstruct.smeltery.block.entity.FaucetBlockEntity;

import java.util.List;
import java.util.function.Supplier;

public class ConfigurableFaucetBlock extends FaucetBlock implements IConfigurableFaucetBlock {
    private final Supplier<Integer> transferConfig;
    private final Supplier<Integer> perTickConfig;
    private int extractFactor = 1;
    public ConfigurableFaucetBlock(Properties properties, Supplier<Integer> transferConfig, Supplier<Integer> perTickConfig) {
        super(properties);
        this.transferConfig = transferConfig;
        this.perTickConfig = perTickConfig;
    }
    public ConfigurableFaucetBlock(Properties properties,Supplier<Integer> perTickConfig,int extractFactor) {
        this(properties,perTickConfig,perTickConfig);
        this.extractFactor = extractFactor;
    }
    public ConfigurableFaucetBlock(Properties properties,Supplier<Integer> perTickConfig) {
        this(properties,perTickConfig,perTickConfig);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable BlockGetter pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
        pTooltip.add(Component.translatable("tooltip.etstlib.faucet_transfer").append(MathUtil.getUnitForFluid(perTickConfig.get())+"/t"));
    }

    @Override
    public int getMaxTransfer() {
        return transferConfig.get()*extractFactor;
    }

    @Override
    public int getTransferPerTick() {
        return perTickConfig.get();
    }
}
