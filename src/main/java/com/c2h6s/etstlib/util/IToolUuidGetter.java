package com.c2h6s.etstlib.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.Optional;
import java.util.UUID;

public interface IToolUuidGetter {
    @Nullable UUID etstlib$getUuid();

    static @NotNull Optional<UUID> getUuid(IToolStackView tool){
        return Optional.ofNullable(((IToolUuidGetter) tool).etstlib$getUuid());
    }
}
