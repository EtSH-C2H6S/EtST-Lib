package com.c2h6s.etstlib.register;

import com.c2h6s.etstlib.EtSTLib;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistries;
import slimeknights.mantle.registration.deferred.SynchronizedDeferredRegister;

public class EtSTLibRecipeSerializer {
    public static SynchronizedDeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZER_AE = SynchronizedDeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, EtSTLib.MODID);

}
