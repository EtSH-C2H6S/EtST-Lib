package com.c2h6s.etstlib.util;

public class MathUtil {
    public static String getUnitInt(int amount){
        int a = (int) Math.log10(amount);
        int b =a/3;
        switch (b){
            case 1->{
                return String.format("%.2f",(float)amount/1E+3)+" k";
            }
            case 2->{
                return String.format("%.2f",(float)amount/1E+6)+" M";
            }
            case 3->{
                return String.format("%.2f",(float)amount/1E+9)+" G";
            }
            default-> {
                return amount + " ";
            }
        }
    }
    public static String getUnitFloat(double amount){
        String unit;
        double bitRaw = Math.log10(Math.abs(amount));
        bitRaw+=bitRaw<=0?-3:0;
        bitRaw /= 3;
        int bits = (int) bitRaw;
        if (bits>5){
            unit = " P";
            amount*= 1E-15F;
        }
        else if (bits<-5){
            unit =" f";
            amount*= 1E+15F;
        }
        else {
            unit = switch (bits){
                case -4 ->" p";
                case -3 ->" n";
                case -2 ->" μ";
                case -1 ->" m";
                case -0 ->" ";
                case 1 ->" k";
                case 2 ->" M";
                case 3 ->" G";
                case 4 ->" T";
                case 5 ->" P";
                default ->" f";
            };
            amount*= (float) Math.pow(1000,-bits);
        }
        return String.format("%.2f",amount) +unit;
    }

    /**
     *
     * @param number 输入的数字
     * @return 保留的小数部分,
     * <br>如-2.3直接得到-0.3
     * <br>2.1直接得到0.1
     */
    public double getDecimal(double number) {
        return number - (int)number;
    }

    /**
     *
     * @param number 输入的数字
     * @param limit 保留的位数
     * @return 限位后的数
     * <br>如MathUtil.limitsNumber(3.14159,3),则返回3.141
     */
    public double limitsNumber(float number,float limit){
        return Math.round(number * 10 * limit) /(10 * limit);
    }
    public static String getEnergyString(int amount){
        return getUnitInt(amount)+"FE";
    }
}
