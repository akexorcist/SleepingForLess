package com.akexorcist.sleepingforless.database;

import io.realm.RealmObject;

/**
 * Created by Akexorcist on 3/15/2016 AD.
 */
public class Bookmark extends RealmObject {
    private String postId;

    public Bookmark() {
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostId() {
        return postId;
    }
}
