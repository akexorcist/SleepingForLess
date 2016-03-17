package com.akexorcist.sleepingforless.network.model;

import org.parceler.Parcel;

/**
 * Created by Akexorcist on 3/10/2016 AD.
 */

@Parcel(parcelsIndex = false)
public class PostListFailure extends Failure {
    public PostListFailure() {
    }

    public PostListFailure(Throwable throwable) {
        super(throwable);
    }
}
