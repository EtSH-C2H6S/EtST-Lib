package com.c2h6s.etstlib.util;

import com.c2h6s.etstlib.capability.EtSTLibCapabilities;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.Optional;
import java.util.UUID;
/**
 * 用于令工具在合成时绑定UUID的接口，通过{@link EtSTLibCapabilities#TOOL_UUID}的形式作用。
 * <br>利用{@link IToolUuidGetter#getUuidOrRandomize}或{@link IToolUuidGetter#forceSetUuid}方法可以通过为工具施加初始UUID从而具备IToolUuidGetter的Capability。
 * <br>配置文件{@link com.c2h6s.etstlib.EtSTLibConfig#ALLOW_TOOL_UUID_ON_COMMON_TOOL}会影响普通工具是否在合成时拥有随机UUID。
 * <br>通过让工具继承{@link com.c2h6s.etstlib.api.interfaces.IRandomizeUuidWhenCrafted}，可以让你新增加的工具在合成时拥有一个随机的UUID，不受配置文件影响。
 * @see com.c2h6s.etstlib.event.eventHandler.PlayerEvents#onItemCrafted
 */
public interface IToolUuidGetter {
    @Nullable default UUID getUuid(ItemStack stack){
        var nbt = stack.getTag();
        if (nbt!=null&&nbt.contains(CommonConstants.KEY_TOOL_UUID, Tag.TAG_STRING))
            return UUID.fromString(nbt.getString(CommonConstants.KEY_TOOL_UUID));
        return null;
    }
    default void setUuid(ItemStack stack,UUID uuid){
        var nbt = stack.getTag();
        if (nbt!=null){
            nbt.putString(CommonConstants.KEY_TOOL_UUID,uuid.toString());
        }
    }
    static Optional<UUID> getUuidOrRandomize(ItemStack stack){
        var nbt = stack.getTag();
        if (nbt!=null&&!nbt.contains(CommonConstants.KEY_TOOL_UUID, Tag.TAG_STRING)){
            nbt.putString(CommonConstants.KEY_TOOL_UUID,UUID.randomUUID().toString());
            return Optional.of(UUID.fromString(nbt.getString(CommonConstants.KEY_TOOL_UUID)));
        }
        return Optional.empty();
    }
    static void forceSetUuid(ItemStack stack,UUID uuid){
        var nbt = stack.getTag();
        if (nbt!=null){
            nbt.putString(CommonConstants.KEY_TOOL_UUID,uuid.toString());
        }
    }
    static @NotNull Optional<UUID> getUuidForItem(ItemStack stack){
        var optional = stack.getCapability(EtSTLibCapabilities.TOOL_UUID);
        return Optional.ofNullable(
                optional.isPresent()?optional.orElse(null).getUuid(stack):null);
    }
    static @NotNull Optional<UUID> getUuidForTool(ToolStack stack){
        return getUuidForItem(stack.createStack());
    }
    static @NotNull Optional<UUID> getUuidForTool(IToolStackView tool){
        return getUuidForTool((ToolStack)tool);
    }

    @Nullable default UUID etstlib$getUuid(){ return null; }

}
