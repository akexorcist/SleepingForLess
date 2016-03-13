package com.akexorcist.sleepingforless.view.post.model;

import org.parceler.Parcel;

/**
 * Created by Akexorcist on 3/13/2016 AD.
 */

@Parcel(parcelsIndex = false)
public class BasePost {
    String type;
    String raw;

    public String getType() {
        return type;
    }

    public String getRaw() {
        return raw;
    }
}
