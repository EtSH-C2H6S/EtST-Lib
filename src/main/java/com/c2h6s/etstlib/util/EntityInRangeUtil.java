package com.c2h6s.etstlib.util;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;
import java.util.function.ToDoubleFunction;


public class EntityInRangeUtil {
    public static ToDoubleFunction<? super Entity> toManhattanDistance(Entity entity){
        return value -> Math.abs( value.getX()-entity.getX()+value.getY()-entity.getY());
    }
    public static Entity getNearestEntity(@NotNull Entity centerEntity, float range,@NotNull IntOpenHashSet ignoreEntityIds,@NotNull List<Class<? extends Entity>> classBlacklist){
        List<Entity> list = centerEntity.level().getEntitiesOfClass(Entity.class,new AABB(centerEntity.blockPosition()).inflate(range));
        list.sort(Comparator.comparingDouble(toManhattanDistance(centerEntity)));
        for (Entity entity:list){
            if (!ignoreEntityIds.contains(entity.getId())&&!classBlacklist.contains(entity.getClass())&&entity!=centerEntity){
                return entity;
            }
        }
        return null;
    }
    public static LivingEntity getNearestLivingEntity(@NotNull Entity centerEntity, float range,@NotNull IntOpenHashSet ignoreEntityIds,@NotNull List<Class<? extends Entity>> classBlacklist){
        List<LivingEntity> list = centerEntity.level().getEntitiesOfClass(LivingEntity.class,new AABB(centerEntity.blockPosition()).inflate(range));
        list.sort(Comparator.comparingDouble(toManhattanDistance(centerEntity)));
        for (LivingEntity entity:list){
            if (!ignoreEntityIds.contains(entity.getId())&&!classBlacklist.contains(entity.getClass())&&entity!=centerEntity){
                return entity;
            }
        }
        return null;
    }
    public static LivingEntity getNearestNotFriendlyLivingEntity(@NotNull Entity centerEntity, float range,@NotNull IntOpenHashSet ignoreEntityIds,@NotNull List<Class<? extends Entity>> classBlacklist){
        List<LivingEntity> list = centerEntity.level().getEntitiesOfClass(LivingEntity.class,new AABB(centerEntity.blockPosition()).inflate(range));
        list.sort(Comparator.comparingDouble(toManhattanDistance(centerEntity)));
        for (LivingEntity entity:list){
            if (!ignoreEntityIds.contains(entity.getId())&&!classBlacklist.contains(entity.getClass())&&!entity.getType().getCategory().isFriendly()&&entity!=centerEntity){
                return entity;
            }
        }
        return null;
    }
}
