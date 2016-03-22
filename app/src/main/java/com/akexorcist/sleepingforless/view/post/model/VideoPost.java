package com.akexorcist.sleepingforless.view.post.model;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by Akexorcist on 3/13/2016 AD.
 */

@Parcel(parcelsIndex = false)
public class VideoPost extends BasePost {
    public static final String TYPE_YOUTUBE = "type_youtube";
    public static final String TYPE_VIMEO = "type_vimeo";
    public static final String TYPE_OTHER = "type_other";

    public String url;
    public String videoType;

    public VideoPost() {
    }

    public VideoPost(String url) {
        this.url = url;
    }

    public static String getTypeYoutube() {
        return TYPE_YOUTUBE;
    }

    public static String getTypeVimeo() {
        return TYPE_VIMEO;
    }

    public String getUrl() {
        return url;
    }

    public VideoPost setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getVideoType() {
        return videoType;
    }

    public VideoPost setVideoType(String videoType) {
        this.videoType = videoType;
        return this;
    }

    public boolean isYouTube() {
        return videoType.equalsIgnoreCase(TYPE_YOUTUBE);
    }

    public boolean isVimeo() {
        return videoType.equalsIgnoreCase(TYPE_VIMEO);
    }

    public boolean isOther() {
        return videoType.equalsIgnoreCase(TYPE_OTHER);
    }
}
