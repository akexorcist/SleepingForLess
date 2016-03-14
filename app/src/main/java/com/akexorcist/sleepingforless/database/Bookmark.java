package com.akexorcist.sleepingforless.database;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Akexorcist on 3/15/2016 AD.
 */
public class Bookmark extends RealmObject {
    @PrimaryKey
    private int id;
    private String postId;

    public Bookmark() {
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public int getId() {
        return id;
    }

    public String getPostId() {
        return postId;
    }
}
