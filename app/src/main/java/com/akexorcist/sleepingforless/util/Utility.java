package com.akexorcist.sleepingforless.util;

/**
 * Created by Akexorcist on 3/10/2016 AD.
 */
public class Utility {
    private static Utility utility;

    public static Utility getInstance() {
        if (utility == null) {
            utility = new Utility();
        }
        return utility;
    }

//    private String
}
