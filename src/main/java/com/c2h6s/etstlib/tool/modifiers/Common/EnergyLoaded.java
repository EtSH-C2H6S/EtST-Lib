package com.c2h6s.etstlib.tool.modifiers.Common;

import com.c2h6s.etstlib.tool.modifiers.base.BasicFEModifier;
import com.c2h6s.etstlib.tool.modifiers.capabilityProvider.FEStorageProvider;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class EnergyLoaded extends BasicFEModifier {
    @Override
    public int getCapacity(ModifierEntry modifier) {
        return 10000;
    }
    @Override
    public int getMaxTransfer(ModifierEntry modifier) {
        return 1000;
    }

    @Override
    public int modifierDamageTool(IToolStackView tool, ModifierEntry modifier, int amount, @Nullable LivingEntity holder) {
        amount = amount - FEStorageProvider.extractEnergy(tool, amount*200,false,true)/200;
        return amount;
    }
}
