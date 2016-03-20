package com.akexorcist.sleepingforless.network.sfl.model;

import com.akexorcist.sleepingforless.network.blogger.model.Failure;

import org.parceler.Parcel;

/**
 * Created by Akexorcist on 3/20/2016 AD.
 */

@Parcel(parcelsIndex = false)
public class GcmTokenResponseFailure extends Failure {
    public GcmTokenResponseFailure() {
    }

    public GcmTokenResponseFailure(Throwable throwable) {
        super(throwable);
    }
}
