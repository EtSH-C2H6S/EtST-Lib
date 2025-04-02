package com.c2h6s.etstlib.data.predicate;

import net.minecraft.world.entity.LivingEntity;
import slimeknights.mantle.data.loadable.primitive.BooleanLoadable;
import slimeknights.mantle.data.loadable.primitive.FloatLoadable;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.mantle.data.predicate.IJsonPredicate;
import slimeknights.mantle.data.predicate.entity.LivingEntityPredicate;

public record LivingEntityWithHealth(float min,float max,boolean include) implements LivingEntityPredicate {
    public static final RecordLoadable<LivingEntityWithHealth> LOADER = RecordLoadable.create(
            FloatLoadable.ANY.defaultField("min",0f,LivingEntityWithHealth::min),
            FloatLoadable.ANY.defaultField("max",-1f,LivingEntityWithHealth::max),
            BooleanLoadable.INSTANCE.defaultField("include",true,LivingEntityWithHealth::include),
            LivingEntityWithHealth::new
    );
    @Override
    public boolean matches(LivingEntity living) {
        return (living.getHealth()>min||(living.getHealth()==min&&include))&&(max<=0||living.getHealth()<max||(living.getHealth()==max&&include));
    }

    @Override
    public RecordLoadable<? extends IJsonPredicate<LivingEntity>> getLoader() {
        return LOADER;
    }
}
