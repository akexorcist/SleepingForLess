package com.akexorcist.sleepingforless.network.blogger.model;

import org.parceler.Parcel;

/**
 * Created by Akexorcist on 3/10/2016 AD.
 */

@Parcel(parcelsIndex = false)
public class PostByPathFailure extends PostFailure {
    public PostByPathFailure() {
    }

    public PostByPathFailure(Throwable throwable) {
        super(throwable);
    }
}
