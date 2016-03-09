package com.akexorcist.sleepingforless.network.model;

/**
 * Created by Akexorcist on 3/10/2016 AD.
 */
public class Failure {
    Throwable throwable;

    public Failure(Throwable throwable) {
        this.throwable = throwable;
    }
}
