package com.c2h6s.etstlib.event.eventHandler;

import com.c2h6s.etstlib.EtSTLib;
import com.c2h6s.etstlib.register.EtSTLibHooks;
import com.c2h6s.etstlib.tool.hooks.OnDeathModifierHook;
import com.c2h6s.etstlib.tool.hooks.OnHoldingPreventDeathHook;
import com.c2h6s.etstlib.util.EquipmentUtil;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

@Mod.EventBusSubscriber(modid = EtSTLib.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ToolEvents {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void EffectApply(MobEffectEvent.Applicable event){
        if (event.getEntity()!=null) {
            for (EquipmentSlot slot : EquipmentUtil.ALL) {
                if (event.getEntity().getItemBySlot(slot).getItem() instanceof IModifiable) {
                    ToolStack tool = ToolStack.from(event.getEntity().getItemBySlot(slot));
                    Boolean notApplicable = event.getResult()== Event.Result.DENY;
                    for (ModifierEntry entry:tool.getModifierList()){
                        notApplicable = entry.getHook(EtSTLibHooks.EFFECT_APPLICABLE).isApplicable(tool,entry,slot,event.getEffectInstance(),notApplicable);
                        if (notApplicable!=null){
                            if (notApplicable) {
                                event.setResult(Event.Result.DENY);
                            }
                            else event.setResult(Event.Result.ALLOW);
                            break;
                        }
                    }
                }
            }
        }
    }
    public static boolean shouldPrevent(DamageSource source, boolean canIgnore){
        if(source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)){
            return canIgnore;
        }
        return true;
    }
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void ToolHoldingDeath(LivingDeathEvent event) {
        LivingEntity livingEntity = event.getEntity();
        DamageSource source = event.getSource();
        EquipmentContext context = new EquipmentContext(livingEntity);
        if (context.hasModifiableArmor()){
            for (EquipmentSlot slotType : EquipmentSlot.values()) {
                IToolStackView toolStack = context.getToolInSlot(slotType);
                if (toolStack != null && !toolStack.isBroken()) {
                    boolean canIgnore= OnHoldingPreventDeathHook.canIgnorePassInvul(EtSTLibHooks.PREVENT_DEATH,context);
                    float HealthRemain = OnHoldingPreventDeathHook.onHoldingPreventDeath(EtSTLibHooks.PREVENT_DEATH,context,source,event.getEntity());
                    if(shouldPrevent(source,canIgnore)&&HealthRemain>0){
                        event.setCanceled(true);
                        event.getEntity().setHealth(HealthRemain);
                        return;
                    }
                    if(livingEntity.isDeadOrDying()){
                        OnDeathModifierHook.handleDeath(EtSTLibHooks.ON_DEATH,context,source,livingEntity);
                    }
                }
            }
        }
    }
}
