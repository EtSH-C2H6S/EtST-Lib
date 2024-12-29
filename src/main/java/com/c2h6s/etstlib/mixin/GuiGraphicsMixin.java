package com.c2h6s.etstlib.mixin;

import com.c2h6s.etstlib.register.EtSTLibHooks;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

@Mixin(value = GuiGraphics.class)
public class GuiGraphicsMixin {
    @Inject(at = @At(value = "TAIL"),method = "renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V")
    public void renderItemDecorations(Font p_282005_, ItemStack stack, int x, int y, String p_282803_, CallbackInfo ci) {
        GuiGraphics graphics = (GuiGraphics) (Object)this;
        if (stack.getItem() instanceof IModifiable) {
            ToolStack tool = ToolStack.from(stack);
            //draw Pressure Bar
            boolean showPressureBar =false;
            int PressureBarWidth =0;
            ModifierEntry entry0 = null;
            for (ModifierEntry entry:tool.getModifierList()){
                showPressureBar = entry.getHook(EtSTLibHooks.PRESSURE_BAR).showPressureBar(tool,entry);
                if (showPressureBar){
                    entry0 =entry;
                    break;
                }
            }
            if (entry0!=null){
                PressureBarWidth = entry0.getHook(EtSTLibHooks.PRESSURE_BAR).getPressureBarWidth(tool,entry0);
            }
            if (showPressureBar) {
                graphics.fill(RenderType.guiOverlay(),x + 2, y + 12 , x + 15, y + 13 , 0xFF000000);
                graphics.fill(RenderType.guiOverlay(),x + 2, y + 12 , x + 2 + PressureBarWidth, y + 13 , 0xFFC9C9C9);
            }


            //draw FE bar
            boolean showFEBar =false;
            int FEBarWidth =0;
            ModifierEntry entry00 = null;
            for (ModifierEntry entry:tool.getModifierList()){
                showFEBar = entry.getHook(EtSTLibHooks.ENERGY_BAR).showEnergyBar(tool,entry);
                if (showFEBar){
                    entry00 =entry;
                    break;
                }
            }
            if (entry00 !=null){
                FEBarWidth = entry00.getHook(EtSTLibHooks.ENERGY_BAR).getEnergyBarWidth(tool, entry00);
            }
            if (showFEBar) {
                if (!showPressureBar){
                    graphics.fill(RenderType.guiOverlay(),x + 2, y + 12, x + 15, y + 13, 0xFF000000);
                }
                graphics.fill(RenderType.guiOverlay(),x + 2, y + 11, x + 15, y + 12 , 0xFF000000);
                graphics.fill(RenderType.guiOverlay(),x + 2, y + 11, x + 2 + FEBarWidth, y + 12 , 0xFFFF3000);
            }
        }
    }
}
