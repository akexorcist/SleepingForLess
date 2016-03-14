package com.akexorcist.sleepingforless.database;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Akexorcist on 3/15/2016 AD.
 */
public class Bookmark extends RealmObject {
    @PrimaryKey
    int id;
    String postId;

    public Bookmark(int id, String postId) {
        this.id = id;
        this.postId = postId;
    }
}
