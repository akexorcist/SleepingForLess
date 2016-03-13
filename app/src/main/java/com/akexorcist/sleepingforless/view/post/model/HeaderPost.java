package com.akexorcist.sleepingforless.view.post.model;

import org.parceler.Parcel;

/**
 * Created by Akexorcist on 3/13/2016 AD.
 */

@Parcel(parcelsIndex = false)
public class HeaderPost extends BasePost {
    int size;
    String text;

    public HeaderPost() {
    }

    public HeaderPost(int size, String text) {
        this.size = size;
        this.text = text;
    }

    public int getSize() {
        return size;
    }

    public String getText() {
        return text;
    }
}
