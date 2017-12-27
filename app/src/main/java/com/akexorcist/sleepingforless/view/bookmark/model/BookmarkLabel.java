package com.akexorcist.sleepingforless.view.bookmark.model;

import org.parceler.Parcel;

/**
 * Created by Akexorcist on 3/15/2016 AD.
 */
@Parcel
public class BookmarkLabel {
    public String label;

    public BookmarkLabel() {
    }

    public String getLabel() {
        return label;
    }

    public BookmarkLabel setLabel(String label) {
        this.label = label;
        return this;
    }
}
