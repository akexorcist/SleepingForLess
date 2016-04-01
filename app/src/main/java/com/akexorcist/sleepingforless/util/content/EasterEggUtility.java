package com.akexorcist.sleepingforless.util.content;

import java.util.Calendar;

/**
 * Created by Akexorcist on 3/31/2016 AD.
 */
public class EasterEggUtility {
    private static EasterEggUtility easterEggUtility;

    public static EasterEggUtility newInstance() {
        if (easterEggUtility == null) {
            easterEggUtility = new EasterEggUtility();
        }
        return easterEggUtility;
    }

    private boolean isAprilFoolDay;

    public void updateToday() {
        isAprilFoolDay = checkAprilFoolDay();
    }

    public boolean isAprilFoolDay() {
        return isAprilFoolDay;
    }

    private boolean checkAprilFoolDay() {
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH) + 1;
        int date = c.get(Calendar.DATE);
        return date == 1 && month == 4;
    }
}
