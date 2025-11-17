package com.c2h6s.etstlib.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.UUID;

public class CommonUtil {
    @Deprecated(forRemoval = true)
    public static @Nullable UUID getUuidFromTool(ToolStack toolStack){
        return ((IToolUuidGetter) toolStack).etstlib$getUuid();
    }
}
