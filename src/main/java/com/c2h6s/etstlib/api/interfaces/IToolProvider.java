package com.c2h6s.etstlib.api.interfaces;

import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public interface IToolProvider {
    @NotNull IToolStackView getTool();
}
