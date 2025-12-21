package com.c2h6s.etstlib.tool.modifiers.base;

import com.c2h6s.etstlib.register.EtSTLibHooks;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.ProtectionModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IndividualProtectionDisplayModifier extends NoLevelsModifier implements TooltipModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this,ModifierHooks.TOOLTIP);
    }

    @Override
    public boolean shouldDisplay(boolean advanced) {
        return false;
    }

    @Override
    public void addTooltip(IToolStackView tool, ModifierEntry modifier, @Nullable Player player, List<Component> tooltip, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        if (player==null) return;
        Map<String,Float> protectionMap = new HashMap<>();
        Map<String,Component> transMap = new HashMap<>();
        tool.getModifierList().forEach(entry -> {
            var hook = entry.getHook(EtSTLibHooks.INDIVIDUAL_PROTECTION);
            var str = hook.getProtectionName(tool,entry,player);
            if (str!=null) {
                protectionMap.put(str,hook.getProtectionModifierForDisplay(tool,entry,player,protectionMap.getOrDefault(str,0f)));
                transMap.putIfAbsent(str,hook.getTranslationName(tool,entry,player));
            }
        });
        protectionMap.forEach((str,value)-> {
            value = (float) Math.min(value, ProtectionModifierHook.getProtectionCap(player));
            var trans = transMap.get(str);
            var comp =Component.literal((value<0?"":"+")+String.format("%.0f", value * 4)+"% ").withStyle(trans.getStyle()).append(trans);
            if (!tooltip.contains(comp)) tooltip.add(comp);
        });
    }
}
