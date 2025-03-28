package com.c2h6s.etstlib.entity.specialDamageSources;

import slimeknights.mantle.data.registry.GenericLoaderRegistry;

public interface ILoadableDamageSource extends GenericLoaderRegistry.IHaveLoader {
    GenericLoaderRegistry<ILoadableDamageSource> LOADER = new GenericLoaderRegistry("Loadable Damage Source", false);
}
