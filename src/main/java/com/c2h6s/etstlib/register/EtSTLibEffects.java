package com.c2h6s.etstlib.register;

import com.c2h6s.etstlib.EtSTLib;
import com.c2h6s.etstlib.content.effects.FatalTrauma;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EtSTLibEffects {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, EtSTLib.MODID);

    public static final RegistryObject<FatalTrauma> FATAL_TRAUMA = EFFECTS.register("fatal_trauma", FatalTrauma::new);
}
