package com.akexorcist.sleepingforless.view.post.model;

import org.parceler.Parcel;

/**
 * Created by Akexorcist on 3/13/2016 AD.
 */

@Parcel(parcelsIndex = false)
public class BasePost {
    int type;
    String raw;

    public int getType() {
        return type;
    }

    public BasePost setType(int type) {
        this.type = type;
        return this;
    }

    public String getRaw() {
        return raw;
    }
}
