package com.c2h6s.etstlib.content.misc;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;

import java.util.List;
import java.util.Optional;

public class SelfDroppingTieredBlock extends Block {
    private final Tier tier;
    public SelfDroppingTieredBlock(Properties pProperties,Tier tier) {
        super(pProperties);
        this.tier = tier;
    }

    //Datagen
    public Optional<TagKey<Block>> getMiningTierOptional(){
        return Optional.ofNullable(tier.getTag());
    }

    @Override
    public List<ItemStack> getDrops(BlockState pState, LootParams.Builder pParams) {
        return List.of(new ItemStack( pState.getBlock().asItem()));
    }
}
