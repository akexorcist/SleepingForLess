package com.akexorcist.sleepingforless.network.sfl.model;

import org.parceler.Parcel;

/**
 * Created by Akexorcist on 3/20/2016 AD.
 */

@Parcel(parcelsIndex = false)
public class GcmTokenResponse {
    public String status;
    public String message;

    public GcmTokenResponse() {
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
