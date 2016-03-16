package com.akexorcist.sleepingforless.view.bookmark.model;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by Akexorcist on 3/15/2016 AD.
 */
@Parcel(parcelsIndex = false)
public class Bookmark {
    public String postId;
    public String published;
    public String updated;
    public String url;
    public String title;
    public String content;
    public List<BookmarkLabel> labelList;
    public List<BookmarkImage> imageList;

    public Bookmark() {
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPublished() {
        return published;
    }

    public void setPublished(String published) {
        this.published = published;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<BookmarkLabel> getLabelList() {
        return labelList;
    }

    public void setLabelList(List<BookmarkLabel> labelList) {
        this.labelList = labelList;
    }

    public List<BookmarkImage> getImageList() {
        return imageList;
    }

    public void setImageList(List<BookmarkImage> imageList) {
        this.imageList = imageList;
    }

}
