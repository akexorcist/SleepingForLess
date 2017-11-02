package com.akexorcist.sleepingforless.view.bookmark.model;

import org.parceler.Parcel;

/**
 * Created by Akexorcist on 3/16/2016 AD.
 */
@Parcel
public class BookmarkImage {
    public String url;

    public BookmarkImage() {
    }

    public String getUrl() {
        return url;
    }

    public BookmarkImage setUrl(String url) {
        this.url = url;
        return this;
    }
}
