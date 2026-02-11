package com.c2h6s.etstlib.util;

import com.google.common.base.Preconditions;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.util.Mth;
import net.minecraftforge.fml.DistExecutor;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Optional;

/**
 * @author <h5>firefly</h5>
 * <h4>一个专门用于生成RGB颜色字的类,其他非public均为逻辑处理,无需调用</h4>
 */
public class DynamicComponentUtil {
    /**
     * <h4>滚动字类,这类为从左到右变换颜色的</h4>
     * <h6>想要调用请直接用{@link ScrollColorfulText#getColorfulText(String, String, int[], int, int, boolean)}这个静态方法</h6>
     * <h6>其他的均为逻辑处理和转化,无需在意</h6>
     */
    public static class ScrollColorfulText {
        /**
         * <h4>这个是入口方法,
         * <br>和你看到的一样,他仅在客户端上工作.在服务端的supplier会直接返回正常的translate
         * </h4>
         * @param translatableText 需要变色的文本本地化键
         * @param append 文本后缀类,如数值等等
         * @param colors 颜色数组,需要new int[]{}
         * @param step 步数,也是过渡的颜色数,这个数值越大过度越平滑,总长度也会越长
         * @param durationMs 一个颜色变换进程所需要的时间,单位毫秒
         * @param isTranslatable 是否为本地化键文本,如果为false则直接把1号形参转化为只读文本
         * @return 彩色变换字的Component文本
         * <h3>如果你还是不会用就来看个示例</h3><pre>{@code
         *@Override
         *     public void addTooltip(IToolStackView tool, ModifierEntry modifier, @Nullable Player player, List<Component> tooltip, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
         *         int[] color=new int[]{0xffea95,0xffaaff,0x55c4ff};
         *         float line=Math.round(tool.getStats().get(OrdinarytinkerStat.KILLTHRESHOLD) * 100f);
         *         tooltip.add(DynamicComponentUtil.ScrollColorfulText.getColorfulText("当前魂戈的斩杀线是",line+"%",color,40,30,false));
         *     }
         */

        public static Component getColorfulText(String translatableText, String append, int[] colors, int step, int durationMs, boolean isTranslatable) {
            return DistExecutor.unsafeRunForDist(
                    () -> () -> buildGradientText(translatableText, append, colors, step, durationMs, isTranslatable),
                    () -> () -> Component.translatable(translatableText)
            );
        }

        private static MutableComponent buildGradientText(String textKey, @Nullable String append, int[] colors, int step, int durationMs, boolean isTranslatable) {
            String safeAppend = append != null ? append : "";
            String localizedText = isTranslatable
                    ? Language.getInstance().getOrDefault(textKey)
                    : textKey;
            String fullText = localizedText + safeAppend;
            int[] gradientColors = generateLinearGradient(colors, step);
            int gradientLength = gradientColors.length;
            int halfCycle = gradientLength - 1;
            int cycleLength = 2 * halfCycle;
            long timestamp = System.currentTimeMillis();
            MutableComponent result = Component.empty();
            for (int i = 0; i < fullText.length(); i++) {
                int progress = (i + (int) (timestamp / durationMs)) % cycleLength;
                int colorIndex;
                if (progress < halfCycle) {
                    colorIndex = progress;
                } else {
                    colorIndex = cycleLength - progress;
                }
                if (colorIndex < 0 || colorIndex >= gradientLength) {
                    colorIndex = 0;
                }

                result.append(Component.literal(String.valueOf(fullText.charAt(i))).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(gradientColors[colorIndex])))
                );
            }
            return result;
        }

        private static int[] generateLinearGradient(int[] colors, int totalSteps) {
            if (colors == null || colors.length == 0) {
                throw new IllegalArgumentException("数组有误,别瞎几把输入");
            }
            if (totalSteps <= 0) {
                return new int[0];
            }
            if (colors.length == 1) {
                int[] gradient = new int[totalSteps];
                Arrays.fill(gradient, colors[0]);
                return gradient;
            }
            int[] gradient = new int[totalSteps];
            int segments = colors.length - 1;
            int stepsPerSegment = totalSteps / segments;
            int remainder = totalSteps % segments;
            int startIndex = 0;
            for (int i = 0; i < segments; i++) {
                int currentSteps = stepsPerSegment + (i == segments - 1 ? remainder : 0);
                int startColor = colors[i];
                int endColor = colors[i + 1];
                float r1 = (startColor >> 16) & 0xFF;
                float g1 = (startColor >> 8) & 0xFF;
                float b1 = startColor & 0xFF;
                float r2 = (endColor >> 16) & 0xFF;
                float g2 = (endColor >> 8) & 0xFF;
                float b2 = endColor & 0xFF;
                for (int j = 0; j < currentSteps; j++) {
                    float t = (currentSteps == 1) ? 0.0f : j / (float) (currentSteps - 1);
                    int r = (int) (r1 + (r2 - r1) * t);
                    int g = (int) (g1 + (g2 - g1) * t);
                    int b = (int) (b1 + (b2 - b1) * t);
                    int index = startIndex + j;
                    if (index < totalSteps) {
                        gradient[index] = (r << 16) | (g << 8) | b;
                    }
                }
                startIndex += currentSteps;
            }
            return gradient;
        }
    }

    /**
     * <h4>呼吸灯效果的文字
     * <br>这类为从浅到深色再到浅色变换颜色的
     * <br>这是单颜色呼吸灯效果
     * </h4>
     * <h6>想要调用请直接用{@link DynamicComponentUtil.BreathColorfulText#getColorfulText(String, String, int[], int, int, boolean)}这个静态方法</h6>
     * <h6>其他的均为逻辑处理和转化,无需在意</h6>
     */
    public static class BreathColorfulText {
        /**
         * <h4>这个是入口方法,
         * <br>和你看到的一样,他仅在客户端上工作.在服务端的supplier会直接返回正常的translate
         * </h4>
         * @param textKey 需要变色的文本本地化键
         * @param append 文本后缀类,如数值等等
         * @param colors 颜色,是个int值
         * @param totalSteps 总变化步数,越长越平滑
         * @param durationMs 这是整个进程的毫秒数,而非单个变换的毫秒数
         * @param isTranslatable 是否翻译为本地化键的内容
         * @return 呼吸灯效果的Component文本
         * <h3>如果你还是不会用就来看个示例</h3><pre>{@code
         *          @Override
         *     public void addTooltip(IToolStackView tool, ModifierEntry modifier, @Nullable Player player, List<Component> list, TooltipKey key, TooltipFlag tooltipFlag) {
         *         if (player != null) {
         *             int level = modifier.getLevel();
         *             float a = (Math.max(player.getMaxHealth() * 0.2f, 1) * Math.max(player.getArmorValue() * 0.6f, 1) * Math.max(player.totalExperience * 0.001f, 1)) * 0.5f * level;
         *             list.add(DynamicComponentUtil.BreathColorfulText.getColorfulText("当前回声点数", String.valueOf(a),0x99b1ff,60,1000,false));
         *             list.add(DynamicComponentUtil.BreathColorfulText.getColorfulText("每点回声所增幅的伤害", level * 0.5f + "攻击力",0x99b1ff,60,1000,false));
         *         }
         *     }
         */
        public static Component getColorfulText(String textKey, @Nullable String append, int[] colors, int totalSteps, int durationMs, boolean isTranslatable) {
            return DistExecutor.unsafeRunForDist(
                    () -> () -> buildBreathText(textKey, append, colors, totalSteps, durationMs,isTranslatable),
                    () -> () -> Component.translatable(textKey)
            );
        }

        private static MutableComponent buildBreathText(String textKey, @Nullable String append, int[] colors, int totalSteps, int durationMs, boolean isTranslatable) {
            Preconditions.checkArgument(colors.length >= 1, "至少需要指定一个基础颜色");
            String fullText = getLocalizedText(textKey, isTranslatable) + Optional.ofNullable(append).orElse("");
            int baseColor = colors[0];
            int[] breathColors = generateRGBBreathWave(baseColor, totalSteps);
            long cyclePosition = System.currentTimeMillis() % durationMs;
            int colorIndex = (int) (cyclePosition * totalSteps / durationMs) % totalSteps;
            if (colorIndex < 0 || colorIndex >= totalSteps) {
                colorIndex = 0;
            }
            return buildColoredComponents(fullText, breathColors[colorIndex]);
        }

        private static String getLocalizedText(String key, boolean translatable) {
            return translatable ? Language.getInstance().getOrDefault(key) : key;
        }

        private static MutableComponent buildColoredComponents(String text, int color) {
            MutableComponent component = Component.empty();
            TextColor textColor = TextColor.fromRgb(color & 0xFFFFFF);
            for (char c : text.toCharArray()) {
                component.append(Component.literal(String.valueOf(c)).setStyle(Style.EMPTY.withColor(textColor)));
            }
            return component;
        }

        private static int[] generateRGBBreathWave(int baseColor, int totalSteps) {
            int r = (baseColor >> 16) & 0xFF;
            int g = (baseColor >> 8) & 0xFF;
            int b = baseColor & 0xFF;
            int[] wave = new int[totalSteps];
            for (int i = 0; i < totalSteps; i++) {
                float ratio = 0.8f * (float) Math.abs(Math.sin(Math.PI * i / totalSteps)) + 0.2f;
                int dr = Mth.clamp((int) (r * ratio), 0, 255);
                int dg = Mth.clamp((int) (g * ratio), 0, 255);
                int db = Mth.clamp((int) (b * ratio), 0, 255);
                wave[i] = (dr << 16) | (dg << 8) | db;
            }
            return wave;
        }
    }
}