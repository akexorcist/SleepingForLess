package com.akexorcist.sleepingforless.network.model;

import org.parceler.Parcel;

/**
 * Created by Akexorcist on 3/10/2016 AD.
 */

@Parcel(parcelsIndex = false)
public class PostByIdFailure extends PostFailure {
    public PostByIdFailure() {
    }

    public PostByIdFailure(Throwable throwable) {
        super(throwable);
    }
}
