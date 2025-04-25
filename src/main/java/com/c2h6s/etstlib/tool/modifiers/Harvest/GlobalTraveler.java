package com.c2h6s.etstlib.tool.modifiers.Harvest;

import com.c2h6s.etstlib.EtSTLib;
import com.c2h6s.etstlib.tool.modifiers.base.EtSTBaseModifier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.behavior.ProcessLootModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.BlockInteractionModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InteractionSource;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;

import java.util.List;

public class GlobalTraveler extends EtSTBaseModifier implements ProcessLootModifierHook , BlockInteractionModifierHook , TooltipModifierHook {
    public static final ResourceLocation TRAVELER_X = EtSTLib.getResourceLocation("traveler_x");
    public static final ResourceLocation TRAVELER_Y = EtSTLib.getResourceLocation("traveler_y");
    public static final ResourceLocation TRAVELER_Z = EtSTLib.getResourceLocation("traveler_z");
    public static final ResourceLocation TRAVELER_DIM = EtSTLib.getResourceLocation("traveler_dim");
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.PROCESS_LOOT,ModifierHooks.BLOCK_INTERACT,ModifierHooks.TOOLTIP);
    }

    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public int getPriority() {
        return Integer.MIN_VALUE;
    }

    @Override
    public InteractionResult beforeBlockUse(IToolStackView tool, ModifierEntry modifier, UseOnContext context, InteractionSource source) {
        Player player = context.getPlayer();
        if (player!=null&&player.isShiftKeyDown()) {
            BlockPos blockPos = context.getClickedPos();
            ModDataNBT nbt = tool.getPersistentData();
            BlockPos legacyPos = new BlockPos(nbt.getInt(TRAVELER_X), nbt.getInt(TRAVELER_Y), nbt.getInt(TRAVELER_Z));
            BlockEntity be = context.getLevel().getBlockEntity(blockPos);
            if (blockPos.equals(legacyPos)&& context.getLevel().dimension().location().toString().equals(nbt.getString(TRAVELER_DIM))){
                nbt.remove(TRAVELER_X);
                nbt.remove(TRAVELER_Y);
                nbt.remove(TRAVELER_Z);
                nbt.remove(TRAVELER_DIM);
                player.sendSystemMessage(Component.translatable("msg.etstlib.global_traveler.container_disbound"));
                return InteractionResult.CONSUME;
            }
            if (!context.getLevel().isClientSide && be != null && be.getCapability(ForgeCapabilities.ITEM_HANDLER).isPresent()) {
                tool.getPersistentData().putInt(TRAVELER_X, blockPos.getX());
                tool.getPersistentData().putInt(TRAVELER_Y, blockPos.getY());
                tool.getPersistentData().putInt(TRAVELER_Z, blockPos.getZ());
                tool.getPersistentData().putString(TRAVELER_DIM, context.getLevel().dimension().location().toString());
                player.sendSystemMessage(Component.translatable("msg.etstlib.global_traveler.container_bind").append(" : ").append(blockPos.toShortString()).append(" @ ").append(Component.translatable(context.getLevel().dimension().location().toString())).withStyle(Style.EMPTY.withColor(0x07FF91)));
                return InteractionResult.CONSUME;
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public void processLoot(IToolStackView tool, ModifierEntry entry, List<ItemStack> list, LootContext context) {
        if (tool.getPersistentData().contains(TRAVELER_DIM, Tag.TAG_STRING)) {
            ModDataNBT nbt = tool.getPersistentData();
            BlockPos blockPos = new BlockPos(nbt.getInt(TRAVELER_X), nbt.getInt(TRAVELER_Y), nbt.getInt(TRAVELER_Z));
            ResourceKey<Level> key = ResourceKey.create(Registries.DIMENSION, ResourceLocation.of(tool.getPersistentData().getString(TRAVELER_DIM), ':'));
            Level level = context.getLevel().getServer().getLevel(key);
            if (level!=null){
                var blockEntity = level.getBlockEntity(blockPos);
                if (blockEntity!=null){
                    var optional = blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER);
                    optional.ifPresent(iItemHandler -> {
                        int i = 0;
                        do {
                            for (int j=0;j<list.size();j++){
                                var itemStack = list.get(j);
                                var count = itemStack.getCount();
                                var leftOver = iItemHandler.insertItem(i,itemStack,false);
                                if (leftOver.getCount()<count){
                                    if (leftOver.isEmpty()){
                                        list.remove(j);
                                    } else list.set(j,leftOver);
                                }
                            }
                            i++;
                        }while (i<iItemHandler.getSlots()&&!list.isEmpty());
                    });
                }
            }
        }
    }

    @Override
    public void addTooltip(IToolStackView tool, ModifierEntry modifierEntry, @Nullable Player player, List<Component> list, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        if (tool.getPersistentData().contains(TRAVELER_DIM, Tag.TAG_STRING)) {
            ModDataNBT nbt = tool.getPersistentData();
            BlockPos blockPos = new BlockPos(nbt.getInt(TRAVELER_X), nbt.getInt(TRAVELER_Y), nbt.getInt(TRAVELER_Z));
            String lang = tool.getPersistentData().getString(TRAVELER_DIM);
            list.add(Component.translatable("msg.etstlib.global_traveler.container_bind").append(" : ").append(blockPos.toShortString()).append(" @ ").append(Component.translatable(lang)));
        }
    }
}
