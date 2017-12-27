package com.akexorcist.sleepingforless.network.sfl.model;

import org.parceler.Parcel;

/**
 * Created by Akexorcist on 3/20/2016 AD.
 */

@Parcel
public class RemoveTokenRequest {
    public String token;

    public RemoveTokenRequest() {
    }

    public RemoveTokenRequest(String token) {
        this.token = token;
    }
}
