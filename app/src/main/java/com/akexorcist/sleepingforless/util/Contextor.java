package com.akexorcist.sleepingforless.util;

import android.content.Context;

/**
 * Created by Akexorcist on 3/10/2016 AD.
 */
public class Contextor {
    private static Context context;

    public static void init(Context context) {
        Contextor.context = context;
    }

    public static Context getContext() {
        return context;
    }
}
