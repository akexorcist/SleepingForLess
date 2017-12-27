package com.akexorcist.sleepingforless.network.sfl.model;

import org.parceler.Parcel;

/**
 * Created by Akexorcist on 3/20/2016 AD.
 */

@Parcel
public class InsertTokenRequest {
    public String token;
    public String serial;

    public InsertTokenRequest() {
    }

    public InsertTokenRequest(String token, String serial) {
        this.token = token;
        this.serial = serial;
    }
}
