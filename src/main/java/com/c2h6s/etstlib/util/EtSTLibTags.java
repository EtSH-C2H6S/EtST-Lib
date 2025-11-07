package com.c2h6s.etstlib.util;

import com.c2h6s.etstlib.EtSTLib;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

public class EtSTLibTags {
    public static class ItemTags{
        public static final TagKey<Item> UUID_SUPPORTED = TagKey.create(ForgeRegistries.ITEMS.getRegistryKey(),EtSTLib.getResourceLocation("modifiable/uuid_supported"));
    }
}
