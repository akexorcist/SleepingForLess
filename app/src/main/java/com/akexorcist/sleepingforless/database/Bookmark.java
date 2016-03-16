package com.akexorcist.sleepingforless.database;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Akexorcist on 3/15/2016 AD.
 */
public class Bookmark extends RealmObject {
    private String postId;
    private String published;
    private String updated;
    private String url;
    private String title;
    private String content;
    private RealmList<BookmarkLabel> labels;

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

    public RealmList<BookmarkLabel> getLabels() {
        return labels;
    }

    public void setLabels(RealmList<BookmarkLabel> labels) {
        this.labels = labels;
    }


}
