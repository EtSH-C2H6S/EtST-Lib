package com.c2h6s.etstlib.client.objects;

import com.c2h6s.etstlib.register.EtSTLibHooks;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.phys.Vec2;

public class CustomBar {
    public Vec2 xyPos;
    public Vec2 xySize;
    public int col;
    boolean showShadow;
    public Vec2 xyShadowOffset;
    public Vec2 xyShadowSize;
    public boolean showBar;
    public int barCount;
    public CustomBar(Vec2 xyPos,Vec2 xySize,int col,boolean showShadow,Vec2 xyShadowOffset,Vec2 xyShadowSize,boolean showBar,int barCount){
        this.xyPos = xyPos;
        this.xySize =xySize;
        this.col =col;
        this.xyShadowOffset =xyShadowOffset;
        this.xyShadowSize =xyShadowSize;
        this.showBar = showBar;
        this.showShadow =showShadow;
        this.barCount =barCount;
    }

    public void drawBar(GuiGraphics graphics,int x,int y){
        if (this.showBar) {
            int col = this.col;
            int xPos = x + (int) this.xyPos.x;
            int yPos = y + (int) this.xyPos.y;
            int xSize = (int) this.xySize.x;
            int ySize = (int) this.xySize.y;
            boolean showShadow = this.showShadow;
            if (showShadow) {
                int xShadowPos = (int) this.xyShadowOffset.x + xPos;
                int yShadowPos = (int) this.xyShadowOffset.y + yPos;
                int xShadowSize = (int) this.xyShadowSize.x;
                int yShadowSize = (int) this.xyShadowSize.y;
                graphics.fill(RenderType.guiOverlay(), xShadowPos, yShadowPos, xShadowPos + xShadowSize, yShadowPos + yShadowSize, 0xFF000000);
            }
            graphics.fill(RenderType.guiOverlay(), xPos, yPos, xPos + xSize, yPos + ySize, col);
        }
    }
}
