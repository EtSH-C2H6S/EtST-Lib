package com.c2h6s.etstlib.tool.modifiers.Common;

import com.c2h6s.etstlib.tool.modifiers.base.BasicFEModifier;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import static com.c2h6s.etstlib.util.ToolEnergyUtil.*;

public class EnergyLoaded extends BasicFEModifier {
    @Override
    public int getCapacity(ModifierEntry modifier) {
        return 10000*modifier.getLevel();
    }

    @Override
    public int modifierDamageTool(IToolStackView tool, ModifierEntry modifier, int amount, @Nullable LivingEntity holder) {
        amount = amount - extractEnergy(tool, amount*200,false)/200;
        return amount;
    }
}
