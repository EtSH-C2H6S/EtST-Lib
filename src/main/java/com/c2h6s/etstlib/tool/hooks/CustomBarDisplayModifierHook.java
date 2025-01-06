package com.c2h6s.etstlib.tool.hooks;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.phys.Vec2;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Collection;

public interface CustomBarDisplayModifierHook {
    String barId(IToolStackView tool, ModifierEntry entry,int barsHadBeenShown);
    boolean showBar(IToolStackView tool, ModifierEntry entry,int barsHadBeenShown);
    //Need int for xy in the Vec2.
    Vec2 getBarXYSize(IToolStackView tool, ModifierEntry entry, int barsHadBeenShown);
    default Vec2 getBarXYPos(IToolStackView tool, ModifierEntry entry,int barsHadBeenShown){
        return new Vec2(2,13-barsHadBeenShown);
    }
    //In the form of ARGB
    int getBarRGB(IToolStackView tool, ModifierEntry entry,int barsHadBeenShown);

    default RenderType getRenderType(IToolStackView tool, ModifierEntry entry,int barsHadBeenShown){
        return RenderType.guiOverlay();
    }
    default boolean showShadow(IToolStackView tool, ModifierEntry entry,int barsHadBeenShown){
        return true;
    }
    default Vec2 getShadowXYSize(IToolStackView tool, ModifierEntry entry,int barsHadBeenShown){
        return barsHadBeenShown>0?new Vec2(13,1):new Vec2(13,2);
    }
    //Place shift from the bar.
    default Vec2 getShadowXYOffset(IToolStackView tool, ModifierEntry entry,int barsHadBeenShown){
        return new Vec2(0,0);
    }

    public static record FirstMerger(Collection<CustomBarDisplayModifierHook> modules) implements CustomBarDisplayModifierHook {
        public FirstMerger(Collection<CustomBarDisplayModifierHook> modules) {
            this.modules = modules;
        }

        public Collection<CustomBarDisplayModifierHook> modules() {
            return this.modules;
        }

        @Override
        public String barId(IToolStackView tool, ModifierEntry entry, int barsHadBeenShown) {
            String s =null;
            for (CustomBarDisplayModifierHook modules:this.modules){
                s = modules.barId(tool,entry,barsHadBeenShown);
                if (s!=null) return s;
            }
            return s;
        }

        @Override
        public boolean showBar(IToolStackView tool, ModifierEntry entry, int barsHadBeenShown) {
            for (CustomBarDisplayModifierHook modules:this.modules){
                if (modules.showBar(tool,entry,barsHadBeenShown)){
                    return true;
                }
            }
            return false;
        }

        @Override
        public Vec2 getBarXYSize(IToolStackView tool, ModifierEntry entry, int barsHadBeenShown) {
            Vec2 vec2 = null;
            for (CustomBarDisplayModifierHook modules:this.modules){
                vec2 = modules.getBarXYSize(tool,entry,barsHadBeenShown);
            }
            return vec2;
        }

        @Override
        public Vec2 getBarXYPos(IToolStackView tool, ModifierEntry entry, int barsHadBeenShown) {
            Vec2 vec2 = null;
            for (CustomBarDisplayModifierHook modules:this.modules){
                vec2 = modules.getBarXYPos(tool,entry,barsHadBeenShown);
            }
            return vec2;
        }

        @Override
        public int getBarRGB(IToolStackView tool, ModifierEntry entry, int barsHadBeenShown) {
            int col =0;
            for (CustomBarDisplayModifierHook modules:this.modules){
                col = modules.getBarRGB(tool,entry,barsHadBeenShown);
            }
            return col;
        }

        @Override
        public boolean showShadow(IToolStackView tool, ModifierEntry entry, int barsHadBeenShown) {
            for (CustomBarDisplayModifierHook modules:this.modules){
                return modules.showShadow(tool, entry, barsHadBeenShown);
            }
            return true;
        }
        @Override
        public Vec2 getShadowXYSize(IToolStackView tool, ModifierEntry entry, int barsHadBeenShown) {
            Vec2 vec2 = barsHadBeenShown>0?new Vec2(13,1):new Vec2(13,2);
            for (CustomBarDisplayModifierHook modules:this.modules){
                vec2 = modules.getShadowXYSize(tool,entry,barsHadBeenShown);
            }
            return vec2;
        }

        @Override
        public Vec2 getShadowXYOffset(IToolStackView tool, ModifierEntry entry, int barsHadBeenShown) {
            Vec2 vec2 = new Vec2(0,0);
            for (CustomBarDisplayModifierHook modules:this.modules){
                vec2 = modules.getShadowXYOffset(tool,entry,barsHadBeenShown);
            }
            return vec2;
        }

        @Override
        public RenderType getRenderType(IToolStackView tool, ModifierEntry entry, int barsHadBeenShown) {
            RenderType renderType = RenderType.guiOverlay();
            for (CustomBarDisplayModifierHook modules:this.modules){
                renderType = modules.getRenderType(tool,entry,barsHadBeenShown);
            }
            return renderType;
        }
    }
}
