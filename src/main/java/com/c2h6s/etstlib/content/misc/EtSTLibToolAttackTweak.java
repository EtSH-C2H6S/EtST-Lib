package com.c2h6s.etstlib.content.misc;

import com.c2h6s.etstlib.entity.specialDamageSources.LegacyDamageSource;
import com.c2h6s.etstlib.mixin.TconMixin.ToolAttackContextBuilderAccessor;
import com.c2h6s.etstlib.mixinUtil.IToolAttackContextMixin;
import com.c2h6s.etstlib.register.EtSTLibHooks;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class EtSTLibToolAttackTweak {
    private static @Nullable ToolAttackContext context = null;
    private static @Nullable IToolStackView tool = null;
    private static @Nullable ToolAttackContext.Builder contextBuilder;
    private static @Nullable LivingEntity attacker;
    private static @Nullable Entity target;
    private static boolean fullyCharged;
    @Setter
    @Getter
    private static float cachedDamage = 0;
    public static void onStart(IToolStackView tool){
        EtSTLibToolAttackTweak.tool = tool;
    }
    public static void buildCritical(ToolAttackContext.Builder builder){
        contextBuilder = builder;
    }
    public static void initCritical(LivingEntity attacker,Entity target,boolean fullyCharged){
        EtSTLibToolAttackTweak.attacker = attacker;
        EtSTLibToolAttackTweak.target = target;
        EtSTLibToolAttackTweak.fullyCharged = fullyCharged;
    }
    public static boolean processCritical(boolean isCritical){
        if (tool==null) return isCritical;
        var contextExtra = (ToolAttackContextBuilderAccessor) contextBuilder;
        for (ModifierEntry entry : tool.getModifierList()) {
            isCritical = entry.getHook(EtSTLibHooks.CRITICAL_ATTACK).setCritical(tool, entry, attacker, contextExtra.etstlib$getHand(), target, contextExtra.etstlib$getSlot(), fullyCharged, contextExtra.etstlib$getExtraAttack(), isCritical);
            if (isCritical) return true;
        }
        return isCritical;
    }
    public static void processContext(@NotNull IToolStackView tool,@NotNull ToolAttackContext context){
        EtSTLibToolAttackTweak.tool = tool;
        EtSTLibToolAttackTweak.context = context;
        var contextExtra = (IToolAttackContextMixin) context;
        if (isToolAvailable()) {
            if (context.isCritical()) {
                float originalModifier = context.getCriticalModifier();
                float modifier = originalModifier;
                for (ModifierEntry entry : tool.getModifierList()) {
                    modifier = entry.getHook(EtSTLibHooks.CRITICAL_ATTACK).getCriticalModifier(tool, entry, context,
                            originalModifier, modifier);
                }
                contextExtra.etstlib$setCriticalModifier(modifier);
            }
            var source = LegacyDamageSource.any(context.makeDamageSource());
            for (ModifierEntry entry:tool.getModifierList()){
                source = entry.getHook(EtSTLibHooks.MODIFY_DAMAGE_SOURCE).modifyDamageSource(tool,entry,context.getAttacker(),
                        context.getHand(),context.getTarget(),context.getSlotType(),context.isFullyCharged(),
                        context.isExtraAttack(),context.isCritical(),source);
            }
            contextExtra.etstlib$setLegacySource(source);
        }
    }

    public static void onEnd(){
        context = null;
        tool = null;
        contextBuilder = null;
        attacker = null;
        target = null;
        cachedDamage = 0;
    }

    public static boolean isToolAvailable(){
        return tool!=null;
    }
    public static boolean isContextAvailable(){
        return context!=null;
    }

}
