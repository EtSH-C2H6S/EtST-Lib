package com.c2h6s.etstlib.mixinUtil;

import com.c2h6s.etstlib.entity.specialDamageSources.LegacyDamageSource;

public interface IToolAttackContextMixin {
    void etstlib$setCriticalModifier(float f);
    void etstlib$setLegacySource(LegacyDamageSource source);
}
