package com.c2h6s.etstlib.tool.modifiers;

import com.c2h6s.etstlib.content.misc.entityTicker.EntityTickerInstance;
import com.c2h6s.etstlib.content.misc.entityTicker.EntityTickerManager;
import com.c2h6s.etstlib.register.EtSTLibEntityTickers;
import com.c2h6s.etstlib.tool.modifiers.base.EtSTBaseModifier;
import net.minecraft.network.chat.Component;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;


//Only for testing,Don't use this modifier.
public class Test extends EtSTBaseModifier {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
    }

    @Override
    public float beforeMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damage, float baseKnockback, float knockback) {
        EntityTickerManager.EntityTickerManagerInstance instance = EntityTickerManager.getInstance(context.getTarget());
        if (!instance.hasTicker(EtSTLibEntityTickers.FREEZING.get())) instance.setTicker(new EntityTickerInstance(EtSTLibEntityTickers.FREEZING.get(), 1,2000));
        else instance.getOptional(EtSTLibEntityTickers.FREEZING.get()).ifPresent(instance1 -> context.getAttacker().sendSystemMessage(Component.literal(String.valueOf(instance1.duration))));
        return knockback;
    }
}
