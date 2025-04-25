package com.c2h6s.etstlib.util;

public enum CommonRGBUtil{
    lightRed(0xff7e75),
    red(0xff0000),
    orange(0xffaa7f),
    yellow(0xffff00),
    lightYellow(0xffff7f),
    pink(0xffaaff);
     private final int RGB;
    CommonRGBUtil(int RGB){
        this.RGB=RGB;
    }
    public int getRGB(){
        return this.RGB;
    }
}
