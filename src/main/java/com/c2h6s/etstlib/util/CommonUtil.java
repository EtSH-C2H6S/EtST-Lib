package com.c2h6s.etstlib.util;

import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.UUID;

public class CommonUtil {
    public static @NotNull UUID getUuidFromTool(ToolStack toolStack){
        return ((IToolUuidGetter) toolStack).etstlib$getUuid();
    }
}
