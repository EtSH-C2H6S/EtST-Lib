package com.c2h6s.etstlib.util;

public class MathUtil {
    public static String getUnitInt(int amount){
        int a = (int) Math.log10(amount);
        int b =a/3;
        switch (b){
            default-> {
                return String.valueOf(amount);
            }
            case 1->{
                return String.format("%.2f",(float)amount/1E+3)+" k";
            }
            case 2->{
                return String.format("%.2f",(float)amount/1E+6)+" M";
            }
            case 3->{
                return String.format("%.2f",(float)amount/1E+9)+" G";
            }
        }
    }
    public static String getEnergyString(int amount){
        return getUnitInt(amount)+"FE";
    }
}
