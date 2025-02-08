package com.c2h6s.etstlib.tool.modifiers;

import com.c2h6s.etstlib.entity.specialDamageSources.LegacyDamageSource;
import com.c2h6s.etstlib.tool.modifiers.base.EtSTBaseModifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

//Only for testing,Don't use this modifier.
public class Test extends EtSTBaseModifier {
    @Override
    public float beforeMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damage, float baseKnockback, float knockback) {
        context.getAttacker().hurt(LegacyDamageSource.thorns(context.getAttacker()).setMsgId("test"),10);
        return knockback;
    }
}
