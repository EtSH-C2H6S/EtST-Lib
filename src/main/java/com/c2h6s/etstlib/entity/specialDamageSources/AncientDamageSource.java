package com.c2h6s.etstlib.entity.specialDamageSources;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class AncientDamageSource extends DamageSource {
    public ArrayList<ResourceKey<DamageType>> damageTypes =new ArrayList<>();
    public AncientDamageSource(Holder<DamageType> holder, @Nullable Entity directEntity, @Nullable Entity causingEntity, @Nullable Vec3 sourcePos) {
        super(holder, directEntity, causingEntity, sourcePos);
    }
    public AncientDamageSource(Holder<DamageType> holder, @Nullable Entity directEntity, @Nullable Entity causingEntity) {
        this(holder, directEntity, causingEntity, null);
    }
    public AncientDamageSource(Holder<DamageType> holder, @Nullable Entity directEntity) {
        this(holder, directEntity, directEntity, null);
    }
    public AncientDamageSource(@NotNull LivingEntity directEntity) {
        this(directEntity.damageSources().generic().typeHolder(), directEntity, directEntity, null);
    }

    public static AncientDamageSource playerAttack(@NotNull Player player){
        return new AncientDamageSource(player.damageSources().playerAttack(player).typeHolder(),player);
    }
    public static AncientDamageSource mobAttack(@NotNull LivingEntity living){
        return new AncientDamageSource(living.damageSources().mobAttack(living).typeHolder(),living);
    }

    public void setBypassInvulnerableTime(){
        damageTypes.add(ResourceKey.create(Registries.DAMAGE_TYPE,DamageTypeTags.BYPASSES_COOLDOWN.location()));
    }
    public void setThorn(){
        damageTypes.add(ResourceKey.create(Registries.DAMAGE_TYPE,DamageTypeTags.AVOIDS_GUARDIAN_THORNS.location()));
    }
    public void setMagic(){
        damageTypes.add(DamageTypes.MAGIC);
    }

    @Override
    public boolean is(TagKey<DamageType> key) {
        if (!damageTypes.isEmpty()){
            return damageTypes.contains(ResourceKey.create(Registries.DAMAGE_TYPE, key.location())) || super.is(key);
        }
        return super.is(key);
    }
    @Override
    public boolean is(ResourceKey<DamageType> key) {
        if (!damageTypes.isEmpty()){
            return damageTypes.contains(key) || super.is(key);
        }
        return super.is(key);
    }
}
