package com.akexorcist.sleepingforless.view.post.model;

import org.parceler.Parcel;

/**
 * Created by Akexorcist on 3/13/2016 AD.
 */

@Parcel(parcelsIndex = false)
public class ImagePost extends BasePost {
    String postUrl;
    String fullSizeUrl;

    public ImagePost() {
    }

    public ImagePost(String raw, String fullSizeUrl, String postUrl) {
        setRaw(raw);
        this.postUrl = postUrl;
        this.fullSizeUrl = fullSizeUrl;
    }

    public String getPostUrl() {
        return postUrl;
    }

    public String getFullSizeUrl() {
        return fullSizeUrl;
    }
}
