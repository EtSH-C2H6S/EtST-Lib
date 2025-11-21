package com.c2h6s.etstlib.mixin.TconMixin;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;

@Mixin(value = ToolAttackContext.Builder.class,remap = false)
public interface ToolAttackContextBuilderAccessor {
    @Accessor
    InteractionHand getHand();
    @Accessor
    EquipmentSlot getSlot();
    @Accessor
    boolean getExtraAttack();


}
