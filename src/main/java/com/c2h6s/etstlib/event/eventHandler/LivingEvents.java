package com.c2h6s.etstlib.event.eventHandler;

import com.c2h6s.etstlib.EtSTLib;
import com.c2h6s.etstlib.register.EtSTLibEffects;
import com.c2h6s.etstlib.register.EtSTLibHooks;
import com.c2h6s.etstlib.util.CommonConstants;
import com.google.common.util.concurrent.AtomicDouble;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import slimeknights.tconstruct.library.modifiers.hook.armor.ProtectionModifierHook;
import slimeknights.tconstruct.library.tools.capability.EntityModifierCapability;
import slimeknights.tconstruct.library.tools.capability.PersistentDataCapability;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = EtSTLib.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LivingEvents {
    @SubscribeEvent
    public static void onLivingHeal(LivingHealEvent event){
        var livingEntity = event.getEntity();
        var amount = event.getAmount();
        if (livingEntity.hasEffect(EtSTLibEffects.FATAL_TRAUMA.get())) event.setCanceled(true);
    }
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onLivingDamageHigh(LivingDamageEvent event){
        DamageSource source = event.getSource();
        Entity entity = source.getDirectEntity();
        if (!event.isCanceled()&&entity instanceof Projectile projectile) {
            projectile.getPersistentData().putFloat(CommonConstants.KEY_PROJECTILE_DAMAGE,event.getAmount());
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onLivingHurtHigh(LivingHurtEvent event){
        DamageSource source = event.getSource();
        Entity entity = source.getDirectEntity();
        if (!event.isCanceled()&&entity instanceof Projectile projectile){
            entity.getCapability(EntityModifierCapability.CAPABILITY).ifPresent(cap-> {
                ModDataNBT nbt = PersistentDataCapability.getOrWarn(entity);
                ModifierNBT modifiers = cap.getModifiers();
                float baseDamage = event.getAmount();
                AtomicDouble atomicDouble = new AtomicDouble(baseDamage);
                if (projectile instanceof AbstractArrow arrow) {
                    modifiers.forEach(entry -> atomicDouble.set(entry.getHook(EtSTLibHooks.ARROW_DAMAGE)
                            .getArrowDamage(nbt, entry, modifiers, arrow,
                                    source.getEntity() instanceof LivingEntity living ? living : null,
                                    event.getEntity(), baseDamage, atomicDouble.floatValue())));
                    event.setAmount(atomicDouble.floatValue());
                }
                modifiers.forEach(entry -> atomicDouble.set(entry.getHook(EtSTLibHooks.PROJECTILE_DAMAGE)
                        .getProjectileDamage(nbt,entry,modifiers,projectile,projectile instanceof AbstractArrow arrow?arrow:null,
                                source.getEntity() instanceof LivingEntity living ? living : null,
                                event.getEntity(),baseDamage,atomicDouble.floatValue())));
            });
        }
    }
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onLivingHurtNormal(LivingHurtEvent event){
        var living = event.getEntity();
        var source = event.getSource();
        Map<String,Float> protectionMap = new HashMap<>();
        for (var slot: EquipmentSlot.values()){
            var itS = living.getItemBySlot(slot);
            if (!(itS.getItem() instanceof IModifiable)) continue;
            var tool = ToolStack.from(itS);
            tool.getModifierList().forEach(entry -> {
                var hook = entry.getHook(EtSTLibHooks.INDIVIDUAL_PROTECTION);
                var str = hook.getProtectionName(tool,entry,living instanceof Player player?player:null);
                if (str!=null) {
                    var ctx = EquipmentContext.withTool(living,tool,slot);
                    protectionMap.put(str,hook.getIndividualProtectionModifier(tool,entry,ctx,slot,source,protectionMap.getOrDefault(str,0f)));
                }
            });
        }
        protectionMap.values().stream().map(f-> Mth.clamp(f,-ProtectionModifierHook.getProtectionCap(living), ProtectionModifierHook.getProtectionCap(living))).map(Double::floatValue).forEach(value->{
            var amount = event.getAmount();
            amount-= amount*(0.04f*value);
            event.setAmount(amount);
        });
    }
}
