package com.akexorcist.sleepingforless.network.model;

import org.parceler.Parcel;

/**
 * Created by Akexorcist on 3/10/2016 AD.
 */

@Parcel(parcelsIndex = false)
public class Failure {
    Throwable throwable;

    public Failure() {
    }

    public Failure(Throwable throwable) {
        this.throwable = throwable;
    }
}
