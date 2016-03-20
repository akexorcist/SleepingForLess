package com.akexorcist.sleepingforless.network.sfl.model;

import org.parceler.Parcel;

/**
 * Created by Akexorcist on 3/20/2016 AD.
 */

@Parcel(parcelsIndex = false)
public class GcmTokenRequest {
    public String token;
    public String serial;

    public GcmTokenRequest() {
    }

    public GcmTokenRequest(String token, String serial) {
        this.token = token;
        this.serial = serial;
    }
}
