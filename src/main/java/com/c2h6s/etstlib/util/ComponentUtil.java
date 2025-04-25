package com.c2h6s.etstlib.util;

import net.minecraft.network.chat.Component;

import java.util.List;

/**
 * 生成tooltip信息的类,更加简洁并且可以直接指定颜色
 */
public class ComponentUtil {
    /**
     * 直接根据本地化键生成
     * @param list Component对应的list
     * @param translatableText 需要本地化键的文本
     * @param color 颜色,0xffffff这种或者直接十进制颜色
     */
    public static void colorfulTranslatable(List<Component> list,String translatableText, int color){
        list.add(Component.translatable(translatableText).withStyle(style -> style.withColor(color)));
    }
    /**
     * 直接根据文本和颜色生成
     * @param list Component对应的list
     * @param literal 文本
     * @param color 颜色,0xffffff这种或者直接十进制颜色
     */
    public static void colorfulLiteral(List<Component> list,String literal,int color){
        list.add(Component.literal(literal).withStyle(style -> style.withColor(color)));
    }
}
