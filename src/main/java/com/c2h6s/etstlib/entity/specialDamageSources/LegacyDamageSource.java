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
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

//A DamageSource that can be simply used and changed similar to those in 1.19.2
public class LegacyDamageSource extends DamageSource {
    public ArrayList<ResourceKey<DamageType>> damageTypes =new ArrayList<>();
    public LegacyDamageSource(Holder<DamageType> holder, @Nullable Entity directEntity, @Nullable Entity causingEntity, @Nullable Vec3 sourcePos) {
        super(holder, directEntity, causingEntity, sourcePos);
    }
    public LegacyDamageSource(Holder<DamageType> holder, @Nullable Entity directEntity, @Nullable Entity causingEntity) {
        this(holder, directEntity, causingEntity, null);
    }
    public LegacyDamageSource(Holder<DamageType> holder, @Nullable Entity directEntity) {
        this(holder, directEntity, directEntity, null);
    }
    public LegacyDamageSource(@NotNull LivingEntity directEntity) {
        this(directEntity.damageSources().generic().typeHolder(), directEntity, directEntity, null);
    }
    public LegacyDamageSource(DamageSource source){
        this(source.typeHolder(),source.getDirectEntity(),source.getEntity(),source.sourcePositionRaw());
    }

    public static LegacyDamageSource playerAttack(@NotNull Player player){
        return new LegacyDamageSource(player.damageSources().playerAttack(player));
    }
    public static LegacyDamageSource mobAttack(@NotNull LivingEntity living){
        return new LegacyDamageSource(living.damageSources().mobAttack(living));
    }
    public static LegacyDamageSource indirectMagic(@NotNull LivingEntity living){
        return new LegacyDamageSource(living.damageSources().magic().typeHolder(),living);
    }
    public static LegacyDamageSource directMagic(@NotNull Level level){
        return new LegacyDamageSource(level.damageSources().magic());
    }
    public static LegacyDamageSource any(@NotNull DamageSource source){
        return new LegacyDamageSource(source);
    }
    public static LegacyDamageSource explosion(@NotNull Entity directEntity,Entity causingEntity){
        return new LegacyDamageSource(directEntity.damageSources().explosion(directEntity,causingEntity));
    }
    public static LegacyDamageSource explosion(@NotNull Explosion explosion){
        return new LegacyDamageSource(explosion.getDamageSource());
    }

    public LegacyDamageSource setBypassInvulnerableTime(){
        this.damageTypes.add(ResourceKey.create(Registries.DAMAGE_TYPE,DamageTypeTags.BYPASSES_COOLDOWN.location()));
        return this;
    }
    public LegacyDamageSource setThorn(){
        damageTypes.add(ResourceKey.create(Registries.DAMAGE_TYPE,DamageTypeTags.AVOIDS_GUARDIAN_THORNS.location()));
        return this;
    }
    public LegacyDamageSource setMagic(){
        damageTypes.add(DamageTypes.MAGIC);
        return this;
    }
    public LegacyDamageSource setBypassArmor(){
        damageTypes.add(ResourceKey.create(Registries.DAMAGE_TYPE,DamageTypeTags.BYPASSES_ARMOR.location()));
        return this;
    }
    public LegacyDamageSource setBypassInvul(){
        damageTypes.add(ResourceKey.create(Registries.DAMAGE_TYPE,DamageTypeTags.BYPASSES_INVULNERABILITY.location()));
        return this;
    }
    public LegacyDamageSource setBypassMagic(){
        damageTypes.add(ResourceKey.create(Registries.DAMAGE_TYPE,DamageTypeTags.BYPASSES_RESISTANCE.location()));
        damageTypes.add(ResourceKey.create(Registries.DAMAGE_TYPE,DamageTypeTags.BYPASSES_EFFECTS.location()));
        return this;
    }
    public LegacyDamageSource setBypassEnchantment(){
        damageTypes.add(ResourceKey.create(Registries.DAMAGE_TYPE,DamageTypeTags.BYPASSES_ENCHANTMENTS.location()));
        damageTypes.add(ResourceKey.create(Registries.DAMAGE_TYPE,DamageTypeTags.AVOIDS_GUARDIAN_THORNS.location()));
        return this;
    }
    public LegacyDamageSource setBypassShield(){
        damageTypes.add(ResourceKey.create(Registries.DAMAGE_TYPE,DamageTypeTags.BYPASSES_SHIELD.location()));
        return this;
    }
    public LegacyDamageSource setExplosion(){
        damageTypes.add(ResourceKey.create(Registries.DAMAGE_TYPE,DamageTypeTags.IS_EXPLOSION.location()));
        return this;
    }
    public LegacyDamageSource setFire(){
        damageTypes.add(ResourceKey.create(Registries.DAMAGE_TYPE,DamageTypeTags.IS_FIRE.location()));
        return this;
    }
    public LegacyDamageSource setProjectile(){
        damageTypes.add(ResourceKey.create(Registries.DAMAGE_TYPE,DamageTypeTags.IS_PROJECTILE.location()));
        return this;
    }
    public LegacyDamageSource setFreezing(){
        damageTypes.add(ResourceKey.create(Registries.DAMAGE_TYPE,DamageTypeTags.IS_FREEZING.location()));
        return this;
    }
    public LegacyDamageSource setFall(){
        damageTypes.add(ResourceKey.create(Registries.DAMAGE_TYPE,DamageTypeTags.IS_FALL.location()));
        return this;
    }
    public LegacyDamageSource setDrowning(){
        damageTypes.add(ResourceKey.create(Registries.DAMAGE_TYPE,DamageTypeTags.IS_DROWNING.location()));
        return this;
    }
    public LegacyDamageSource setDamageHelmet(){
        damageTypes.add(ResourceKey.create(Registries.DAMAGE_TYPE,DamageTypeTags.DAMAGES_HELMET.location()));
        return this;
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
