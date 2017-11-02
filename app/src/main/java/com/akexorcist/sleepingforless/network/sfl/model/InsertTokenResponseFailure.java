package com.akexorcist.sleepingforless.network.sfl.model;

import com.akexorcist.sleepingforless.network.blogger.model.Failure;

import org.parceler.Parcel;

/**
 * Created by Akexorcist on 3/20/2016 AD.
 */

@Parcel
public class InsertTokenResponseFailure extends Failure {
    public InsertTokenResponseFailure() {
    }

    public InsertTokenResponseFailure(Throwable throwable) {
        super(throwable);
    }
}
