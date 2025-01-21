package com.c2h6s.etstlib.tool.hooks;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;

import java.util.Collection;
import java.util.List;

//Use to perform special effect when a projectile ticks.Projectiles needs to have EntityModifierCapability and have a modifier list.
public interface ProjectileTickModifierHook {
    //For projectiles not including abstract arrow.
    void onProjectileTick(ModifierNBT modifiers, ModifierEntry entry, Level level, @NotNull Projectile projectile,ModDataNBT persistentData,boolean hasBeenShot,boolean leftOwner);
    //Abstract arrow only.
    void onArrowTick(ModifierNBT modifiers, ModifierEntry entry, Level level, @NotNull AbstractArrow arrow, ModDataNBT persistentData, boolean hasBeenShot, boolean leftOwner,boolean inGround, @Nullable IntOpenHashSet piercingIgnoreEntityIds);
    record AllMerger(Collection<ProjectileTickModifierHook> modules) implements ProjectileTickModifierHook {
        @Override
        public void onProjectileTick(ModifierNBT modifiers, ModifierEntry entry, Level level, @NotNull Projectile projectile, ModDataNBT persistentData, boolean hasBeenShot, boolean leftOwner) {
            for (ProjectileTickModifierHook modules:this.modules){
                modules.onProjectileTick(modifiers,entry,level,projectile,persistentData,hasBeenShot,leftOwner);
            }
        }

        @Override
        public void onArrowTick(ModifierNBT modifiers, ModifierEntry entry, Level level, @NotNull AbstractArrow arrow, ModDataNBT persistentData, boolean hasBeenShot, boolean leftOwner, boolean inGround, @Nullable IntOpenHashSet piercingIgnoreEntityIds) {
            for (ProjectileTickModifierHook modules:this.modules){
                modules.onArrowTick(modifiers,entry,level,arrow,persistentData,hasBeenShot,leftOwner,inGround,piercingIgnoreEntityIds);
            }
        }
    }
}
