package com.c2h6s.etstlib.register;

import com.c2h6s.etstlib.EtSTLib;
import com.c2h6s.etstlib.content.blockEntity.ConfigurableFaucetBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EtSTLibBlockEntityTypes {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, EtSTLib.MODID);
}
