package com.c2h6s.etstlib.mixinUtil;

import com.c2h6s.etstlib.client.objects.TinkerStationScrollingWidget;
import slimeknights.tconstruct.tables.client.inventory.widget.TinkerStationButtonsWidget;

public interface ITinkerStationScreenMixin {
    TinkerStationScrollingWidget etstlib$getScrollingWidget();
    TinkerStationButtonsWidget etstlib$getButtonsScreen();
    void etstlib$postInit();
}
