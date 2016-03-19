package com.akexorcist.sleepingforless.view.bookmark.model;

import org.parceler.Parcel;

/**
 * Created by Akexorcist on 3/19/2016 AD.
 */

@Parcel(parcelsIndex = false)
public class BookmarkRemoveEvent {
    public String postId;

    public BookmarkRemoveEvent() {
    }

    public BookmarkRemoveEvent(String postId) {
        this.postId = postId;
    }

    public String getPostId() {
        return postId;
    }
}
