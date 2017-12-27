package com.akexorcist.sleepingforless.view.post.model;

import org.parceler.Parcel;

/**
 * Created by Akexorcist on 3/13/2016 AD.
 */

@Parcel
public class HeaderPost extends BasePost {
    int size;
    String text;

    public HeaderPost() {
    }

    public HeaderPost(String raw, int size, String text) {
        setRaw(raw);
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
