package com.akexorcist.sleepingforless.bus;

import com.squareup.otto.Bus;

/**
 * Created by Akexorcist on 3/10/2016 AD.
 */
public class BusProvider {
    private static Bus bus;

    public static Bus getInstance() {
        if (bus == null) {
            bus = new Bus();
        }
        return bus;
    }
}
