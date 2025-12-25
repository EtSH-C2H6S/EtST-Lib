package com.c2h6s.etstlib.tool.modifiers.Combat;

import com.c2h6s.etstlib.EtSTLib;
import com.c2h6s.etstlib.tool.modifiers.base.EtSTBaseModifier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;

public class Anisotropy extends EtSTBaseModifier {
    public static final ResourceLocation KEY_CURRENT = EtSTLib.getResourceLocation("current_durability");
    public static final ResourceLocation KEY_DAMAGE = EtSTLib.getResourceLocation("tool_damage");
    public float getBonus(IToolStackView tool,int lvl){
        return tool.getCurrentDurability()*lvl/((tool.getCurrentDurability()+tool.getDamage())*5f);
    }
    public float getBonus(int currentDurability,int toolDamage,int level){
        return currentDurability*level/((currentDurability+toolDamage)*5f);
    }
    @Override
    public float onGetMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage) {
        damage += baseDamage*getBonus(tool,modifier.getLevel())*(context.isFullyCharged()?1:-1);
        return damage;
    }
    @Override
    public void modifierProjectileLaunch(IToolStackView tool, ModifierEntry modifier, LivingEntity shooter, Projectile projectile, @Nullable AbstractArrow arrow, ModDataNBT persistentData, boolean primary) {
        if (arrow!=null){
            persistentData.putInt(KEY_CURRENT,tool.getCurrentDurability());
            persistentData.putInt(KEY_DAMAGE,tool.getDamage());
        }
    }

    @Override
    public float onGetArrowDamage(ModDataNBT persistentData, ModifierEntry entry, ModifierNBT modifiers, AbstractArrow arrow, @Nullable LivingEntity attackerk, @NotNull Entity target, float baseDamage, float damage) {
        damage += baseDamage*getBonus(persistentData.getInt(KEY_CURRENT),persistentData.getInt(KEY_DAMAGE),entry.getLevel())*(arrow.isCritArrow()?1:-1);
        return damage;
    }
}
