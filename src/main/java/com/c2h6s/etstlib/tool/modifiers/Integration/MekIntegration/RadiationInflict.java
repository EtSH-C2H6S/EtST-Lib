package com.c2h6s.etstlib.tool.modifiers.Integration.MekIntegration;

import com.c2h6s.etstlib.tool.modifiers.base.EtSTBaseModifier;
import mekanism.api.radiation.IRadiationManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class RadiationInflict extends EtSTBaseModifier {
    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        if (!context.getLevel().isClientSide&&context.isFullyCharged()) {
            Entity entity = context.getTarget();
            if (entity instanceof LivingEntity living) {
                IRadiationManager.INSTANCE.radiate(living, modifier.getLevel()*0.1);
            }
        }
    }
}
