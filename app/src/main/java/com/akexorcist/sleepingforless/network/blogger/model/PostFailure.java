package com.akexorcist.sleepingforless.network.blogger.model;

import org.parceler.Parcel;

/**
 * Created by Akexorcist on 3/10/2016 AD.
 */

@Parcel
public class PostFailure extends Failure {
    public PostFailure() {
    }

    public PostFailure(Throwable throwable) {
        super(throwable);
    }
}
