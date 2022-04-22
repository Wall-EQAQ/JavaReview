package com.smalldolphin.shop;

/**
 * @Description:shop
 * @Created by Administrator on 2021/5/22 17:31
 * @Modified by:
 */
public class Singleton {

    private static Singleton INSTANCE;

    private Singleton() {}

    public static Singleton getINSTANCE() {
        if (INSTANCE == null) {
            synchronized (Singleton.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Singleton();
                }
            }
        }
        return INSTANCE;
    }
}
