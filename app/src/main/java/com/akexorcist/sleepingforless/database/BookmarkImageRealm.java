package com.akexorcist.sleepingforless.database;

import io.realm.RealmObject;

/**
 * Created by Akexorcist on 3/16/2016 AD.
 */
public class BookmarkImageRealm extends RealmObject {
    private String url;

    public BookmarkImageRealm() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
