package com.c2h6s.etstlib.client.objects;

import com.c2h6s.etstlib.mixinUtil.ITinkerStationScreenMixin;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractScrollWidget;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import slimeknights.tconstruct.tables.client.inventory.widget.SideButtonsWidget;

public class TinkerStationScrollingWidget extends AbstractWidget implements Renderable, GuiEventListener {
    public final int innerHeight;
    public final ITinkerStationScreenMixin screen;
    public TinkerStationScrollingWidget(ITinkerStationScreenMixin screen, int pX, int pY, int pWidth, int pHeight, Component pMessage, int innerHeight) {
        super(pX, pY, pWidth, pHeight, pMessage);
        this.innerHeight = innerHeight;
        this.screen = screen;
    }

    public int getScrollAmount(){
        return (int) this.scrollAmount();
    }

    protected int getInnerHeight() {
        return this.innerHeight;
    }

    protected double scrollRate() {
        return Math.min( (this.getContentHeight()-this.height)/10f,15);
    }

    protected void setScrollAmount(double pScrollAmount) {
        this.scrollAmount = Mth.clamp(pScrollAmount, 0.0, this.getMaxScrollAmount());
        if (this.screen.etstlib$getButtonsScreen()!=null){
            this.screen.etstlib$getButtonsScreen().setButtonPositions();
        }
    }

    protected void renderBorder(GuiGraphics pGuiGraphics, int pX, int pY, int pWidth, int pHeight) {
        int i = this.isFocused() ? -1 : -6250336;
        pGuiGraphics.fill(pX -1, pY, pX + 9, pY + pHeight, i);
        pGuiGraphics.fill(pX , pY + 1, pX + 8, pY + pHeight - 1, -16777216);
    }

    public void renderScrollBar(GuiGraphics pGuiGraphics) {
        int barHeight = this.getScrollBarHeight();
        int startX = this.getX() ;
        int endX = this.getX() + 8;
        int barY = Math.max(this.getY(), (int)this.scrollAmount * (this.height - barHeight) / this.getMaxScrollAmount() + this.getY());
        int endY = barY + barHeight;
        pGuiGraphics.fill(startX, barY, endX, endY, -8355712);
        pGuiGraphics.fill(startX, barY, endX - 1, endY - 1, -4144960);
    }

    protected void renderContents(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {

    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {

    }

    private static final int BORDER_COLOR_FOCUSED = -1;
    private static final int BORDER_COLOR = -6250336;
    private static final int BACKGROUND_COLOR = -16777216;
    private static final int INNER_PADDING = 4;
    private double scrollAmount;
    private boolean scrolling;

    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (!this.visible) {
            return false;
        } else {
            boolean within = this.withinContentAreaPoint(pMouseX, pMouseY);
            if (within && pButton == 0) {
                this.scrolling = true;
                return true;
            } else {
                return within;
            }
        }
    }

    public boolean mouseReleased(double pMouseX, double pMouseY, int pButton) {
        if (pButton == 0) {
            this.scrolling = false;
        }

        return super.mouseReleased(pMouseX, pMouseY, pButton);
    }

    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
        if (this.visible && this.isFocused() && this.scrolling) {
            if (pMouseY < (double)this.getY()) {
                this.setScrollAmount(0.0);
            } else if (pMouseY > (double)(this.getY() + this.height)) {
                this.setScrollAmount(this.getMaxScrollAmount());
            } else {
                int $$5 = this.getScrollBarHeight();
                double $$6 = Math.max(1, this.getMaxScrollAmount() / (this.height - $$5));
                this.setScrollAmount(this.scrollAmount + pDragY * $$6);
            }

            return true;
        } else {
            return false;
        }
    }

    public boolean mouseScrolled(double pMouseX, double pMouseY, double pDelta) {
        if (!this.visible) {
            return false;
        } else {
            this.setScrollAmount(this.scrollAmount - pDelta * this.scrollRate());
            return true;
        }
    }

    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        boolean $$3 = pKeyCode == 265;
        boolean $$4 = pKeyCode == 264;
        if ($$3 || $$4) {
            double $$5 = this.scrollAmount;
            this.setScrollAmount(this.scrollAmount + (double)($$3 ? -1 : 1) * this.scrollRate());
            if ($$5 != this.scrollAmount) {
                return true;
            }
        }

        return super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }

    public void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        if (this.visible) {
            this.renderBackground(pGuiGraphics);
            pGuiGraphics.enableScissor(this.getX() + 1, this.getY() + 1, this.getX() + this.width - 1, this.getY() + this.height - 1);
            pGuiGraphics.pose().pushPose();
            pGuiGraphics.pose().translate(0.0, -this.scrollAmount, 0.0);
            this.renderContents(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
            pGuiGraphics.pose().popPose();
            pGuiGraphics.disableScissor();
            this.renderDecorations(pGuiGraphics);
        }
    }

    private int getScrollBarHeight() {
        return Mth.clamp((int)((float)(this.height * this.height) / (float)this.getContentHeight()), 32, this.height);
    }

    protected void renderDecorations(GuiGraphics pGuiGraphics) {
        if (this.scrollbarVisible()) {
            this.renderScrollBar(pGuiGraphics);
        }

    }

    protected int innerPadding() {
        return 4;
    }

    protected int totalInnerPadding() {
        return this.innerPadding() * 2;
    }

    protected double scrollAmount() {
        return this.scrollAmount;
    }

    protected int getMaxScrollAmount() {
        return Math.max(0, this.getContentHeight() - (this.height - 4));
    }

    private int getContentHeight() {
        return this.getInnerHeight() + 4;
    }

    protected void renderBackground(GuiGraphics pGuiGraphics) {
        this.renderBorder(pGuiGraphics, this.getX(), this.getY(), this.getWidth(), this.getHeight());
    }

    protected boolean withinContentAreaTopBottom(int pTop, int pBottom) {
        return (double)pBottom - this.scrollAmount >= (double)this.getY() && (double)pTop - this.scrollAmount <= (double)(this.getY() + this.height);
    }

    protected boolean withinContentAreaPoint(double pX, double pY) {
        return pX >= (double)this.getX() && pX < (double)(this.getX() + this.width) && pY >= (double)this.getY() && pY < (double)(this.getY() + this.height);
    }

    protected boolean scrollbarVisible() {
        return this.getInnerHeight() > this.getHeight();
    }

}
