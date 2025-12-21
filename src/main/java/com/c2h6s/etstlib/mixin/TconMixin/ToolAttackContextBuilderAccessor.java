package com.c2h6s.etstlib.mixin.TconMixin;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;

@Mixin(value = ToolAttackContext.Builder.class,remap = false)
public interface ToolAttackContextBuilderAccessor {
    @Accessor("hand")
    InteractionHand etstlib$getHand();
    @Accessor("slot")
    EquipmentSlot etstlib$getSlot();
    @Accessor("extraAttack")
    boolean etstlib$getExtraAttack();


}
