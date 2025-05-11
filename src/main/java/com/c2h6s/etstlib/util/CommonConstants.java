package com.c2h6s.etstlib.util;

import com.c2h6s.etstlib.EtSTLib;
import net.minecraft.resources.ResourceLocation;

public class CommonConstants {
    //箭矢的满蓄力指示tag，只有当词条继承了EtSTBaseModifier箭矢才会带有这个标签。与isCritical()方法不同，这个标签在箭矢穿透过一次或者击中地面后依然保留。
    public static final String KEY_CRITARROW = "is_critical";
    //工具UUID在nbt中的存储键名，你用不到这个常量，获取nbt用CommonUtil中的getUuidFromTool方法。
    public static final String KEY_TOOL_UUID = "etstlib_tool_uuid";
}
