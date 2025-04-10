package com.c2h6s.etstlib.tool.modifiers.Combat.Defense;

import com.c2h6s.etstlib.EtSTLib;
import com.c2h6s.etstlib.mixin.LivingEntityAccessor;
import com.c2h6s.etstlib.tool.modifiers.base.EtSTBaseModifier;
import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import slimeknights.tconstruct.library.modifiers.modules.technical.ArmorLevelModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;

import static com.c2h6s.etstlib.tool.modifiers.Combat.Defense.SecondaryArmor.KEY_SECONDARY_ARMOR;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class HyperDensity extends EtSTBaseModifier {
    public static final TinkerDataCapability.TinkerDataKey<Integer> KEY_HYPER_DENSITY = TinkerDataCapability.TinkerDataKey.of(EtSTLib.getResourceLocation("hyper_density"));

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent event) {
        LivingEntity living = event.getEntity();
        if (living!=null){
            living.getCapability(TinkerDataCapability.CAPABILITY).ifPresent((cap)->{
                int level = cap.get(KEY_HYPER_DENSITY,0);
                if (level>0){
                    level = cap.get(KEY_SECONDARY_ARMOR,0);
                    if (level>0){
                        event.setAmount(CombatRules.getDamageAfterAbsorb(event.getAmount(), (float) living.getArmorValue(), (float) living.getAttributeValue(Attributes.ARMOR_TOUGHNESS)));
                    }
                    event.setAmount(((LivingEntityAccessor)living).getDamageAfterArmorAbsorb(event.getSource(),event.getAmount()));
                }
            });
        }
    }

    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addModule(new ArmorLevelModule(KEY_HYPER_DENSITY,false,null));
    }


}