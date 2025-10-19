package com.c2h6s.etstlib.tool.hooks;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;

import java.util.Collection;
import java.util.Iterator;

/**<h3>一个用于修改"箭矢造成的伤害"的钩子</h3>
 * <h5>Hook标识符为
 * <br>"EtSTLibHooks.ARROW_DAMAGE"</h5>
 * 在后续版本中会与ProjectileDamageModifierHook合并
 * @see ProjectileDamageModifierHook
 */
@Deprecated(forRemoval = true,since = "beta.15")
public interface ArrowDamageModifierHook {
    /**
     * <h6>在箭矢伤害目标的时候调用</h6>
     *
     *
     * @param persistentData 传入箭矢的NBT信息
     * @param entry ModifierEntry实例
     * @param modifiers 传入箭矢的工具词条NBT,因为箭矢无法定位工具,所有需要这样来确保箭矢有效果
     * @param arrow 箭矢实体
     * @param attacker 攻击者
     * @param target 被攻击者
     * @param baseDamage 基础伤害
     * @param damage 总伤害
     * @return 箭矢的伤害,不会影响箭矢本体,修改这个返回值会直接修改以该箭矢为来源的伤害事件的amount值,而非箭矢的baseDamage常量
     */
    float getArrowDamage(ModDataNBT persistentData, ModifierEntry entry, ModifierNBT modifiers, AbstractArrow arrow, @Nullable LivingEntity attacker, @NotNull Entity target, float baseDamage, float damage);

    record AllMerger(Collection<ArrowDamageModifierHook> modules) implements ArrowDamageModifierHook {
        public AllMerger(Collection<ArrowDamageModifierHook> modules) {
            this.modules = modules;
        }

        public float getArrowDamage(ModDataNBT persistentData, ModifierEntry entry, ModifierNBT modifiers, AbstractArrow arrow, @Nullable LivingEntity attacker,@NotNull Entity target, float baseDamage, float damage) {
            ArrowDamageModifierHook module;
            for(Iterator<ArrowDamageModifierHook> var6 = this.modules.iterator(); var6.hasNext(); damage = module.getArrowDamage(persistentData, entry,modifiers, arrow,attacker,target, baseDamage, damage)) {
                module = var6.next();
            }
            return damage;
        }

        public Collection<ArrowDamageModifierHook> modules() {
            return this.modules;
        }
    }
}
