package com.c2h6s.etstlib.util;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public class EffectUtil {
    /**
     * 用于快速直接操作目标药水map,会无视实体类中免疫药水效果的相关
     *
     * @param livingEntity 被施加的实体
     * @param effect 药水效果
     * @param duration 时长(刻)
     * @param Amplifier 等级,遵循原版规则0为1级
     */
    public static void directAddMobEffect(LivingEntity livingEntity, MobEffect effect,int duration,int Amplifier){
        var map=livingEntity.getActiveEffectsMap();
        map.put(effect,new MobEffectInstance(effect,duration,Amplifier));
    }
    public static void modifyEffectInstance(MobEffectInstance instance,int finalDuration,int finalAmplifier,boolean pAmbient, boolean pVisible, boolean pShowIcon){
        instance.duration=finalDuration;
        instance.amplifier=finalAmplifier;
        instance.ambient=pAmbient;
        instance.visible=pVisible;
        instance.showIcon=pShowIcon;
    }
}
