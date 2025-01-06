package com.c2h6s.etstlib.mixin;

import com.c2h6s.etstlib.client.objects.CustomBar;
import com.c2h6s.etstlib.register.EtSTLibHooks;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.hook.display.DurabilityDisplayModifierHook;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.HashMap;
import java.util.Map;

@Mixin(value = GuiGraphics.class)
public class GuiGraphicsMixin {
    @Inject(at = @At(value = "TAIL"),method = "renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V")
    public void renderItemDecorations(Font p_282005_, ItemStack stack, int x, int y, String p_282803_, CallbackInfo ci) {
        GuiGraphics graphics = (GuiGraphics) (Object)this;
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
                    bar.drawBar(graphics,x,y);
                }
            }
        }
    }
}
