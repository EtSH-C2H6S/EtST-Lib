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

/**<h3>一个用于修改"弹射物造成的伤害"的钩子</h3>
 * <h5>对应的Hook标识符为
 * <br>"EtSTLibHooks.PROJECTILE_DAMAGE"</h5>
 * 处理逻辑通过事件实现,在{@link com.c2h6s.etstlib.event.eventHandler.LivingEvents#onLivingHurt(LivingHurtEvent)}
 * 由于箭矢
 * <br>
 * <em>如果你想要在击中目标之后触发效果,可以使用<h5>ArrowHitModifierHook</h5></em>
 * @see ArrowHitModifierHook
 */
public interface ProjectileDamageModifierHook {
    /**
     * <h6>在箭矢伤害目标的时候调用</h6>
     *
     *
     * @param persistentData 传入箭矢的NBT信息
     * @param entry ModifierEntry实例
     * @param modifiers 传入箭矢的工具词条NBT,因为箭矢无法定位工具,所有需要这样来确保箭矢有效果
     * @param projectile 弹射物实体
     * @param arrow 箭矢实体（只有命中物是箭矢时不为null）
     * @param attacker 攻击者
     * @param target 被攻击者
     * @param baseDamage 基础伤害（不会变化）
     * @param damage 总伤害（会收到来自其它词条的修改）
     * @return 箭矢的伤害,不会影响箭矢本体,修改这个返回值会直接修改以该箭矢为来源的伤害事件的amount值,而非箭矢的baseDamage常量
     */
    float getProjectileDamage(ModDataNBT persistentData, ModifierEntry entry, ModifierNBT modifiers,@NotNull Projectile projectile,@Nullable AbstractArrow arrow, @Nullable LivingEntity attacker, @NotNull Entity target, float baseDamage, float damage);

    record AllMerger(Collection<ProjectileDamageModifierHook> modules) implements ProjectileDamageModifierHook {
        public AllMerger(Collection<ProjectileDamageModifierHook> modules) {
            this.modules = modules;
        }

        public float getProjectileDamage(ModDataNBT persistentData, ModifierEntry entry, ModifierNBT modifiers, @NotNull Projectile projectile,@Nullable AbstractArrow arrow, @Nullable LivingEntity attacker, @NotNull Entity target, float baseDamage, float damage) {
            ProjectileDamageModifierHook module;
            for(Iterator<ProjectileDamageModifierHook> var6 = this.modules.iterator(); var6.hasNext(); damage = module.getProjectileDamage(persistentData, entry,modifiers, projectile,arrow,attacker,target, baseDamage, damage)) {
                module = var6.next();
            }
            return damage;
        }

        public Collection<ProjectileDamageModifierHook> modules() {
            return this.modules;
        }
    }
}
