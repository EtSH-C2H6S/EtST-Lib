package com.c2h6s.etstlib.client.gui.item;

import com.c2h6s.etstlib.client.objects.CustomBar;
import com.c2h6s.etstlib.register.EtSTLibHooks;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.client.IItemDecorator;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.hook.display.DurabilityDisplayModifierHook;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.HashMap;
import java.util.Map;

public class CustomBarDecoration implements IItemDecorator {
    @Override
    public boolean render(GuiGraphics guiGraphics, Font font, ItemStack stack, int xOffset, int yOffset) {
        if (stack.getItem() instanceof IModifiable) {
            ToolStack tool = ToolStack.from(stack);
            int barCount = DurabilityDisplayModifierHook.showDurabilityBar(stack) ? 1 : 0;
            Map<String, CustomBar> map = new HashMap<>();
            for (ModifierEntry entry : tool.getModifierList()) {
                boolean showBar = entry.getHook(EtSTLibHooks.CUSTOM_BAR).showBar(tool, entry, barCount);
                String id = entry.getHook(EtSTLibHooks.CUSTOM_BAR).barId(tool, entry, barCount);
                if (showBar && id != null) {
                    int BarCount = barCount;
                    if (map.containsKey(id)){
                        BarCount = map.get(id).barCount;
                    }
                    int col = entry.getHook(EtSTLibHooks.CUSTOM_BAR).getBarRGB(tool, entry, BarCount);
                    Vec2 Pos = entry.getHook(EtSTLibHooks.CUSTOM_BAR).getBarXYPos(tool, entry, BarCount);
                    Vec2 Size = entry.getHook(EtSTLibHooks.CUSTOM_BAR).getBarXYSize(tool, entry, BarCount);
                    boolean showShadow = entry.getHook(EtSTLibHooks.CUSTOM_BAR).showShadow(tool, entry, BarCount);
                    Vec2 ShadowPos = entry.getHook(EtSTLibHooks.CUSTOM_BAR).getShadowXYOffset(tool, entry, BarCount);
                    Vec2 ShadowSize = entry.getHook(EtSTLibHooks.CUSTOM_BAR).getShadowXYSize(tool, entry, BarCount);
                    if (!map.containsKey(id)){
                        barCount++;
                    }
                    map.put(id,new CustomBar(Pos,Size,col,showShadow,ShadowPos,ShadowSize,showBar,BarCount));
                }
            }
            if (!map.isEmpty()) {
                for (String id : map.keySet()) {
                    CustomBar bar = map.get(id);
                    bar.drawBar(guiGraphics,xOffset,yOffset);
                }
                return true;
            }
        }
        return false;
    }
}
