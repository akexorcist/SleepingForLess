package com.akexorcist.sleepingforless.database;

import io.realm.RealmObject;

/**
 * Created by Akexorcist on 3/15/2016 AD.
 */
public class BookmarkLabel extends RealmObject {
    private String label;

    public BookmarkLabel() {
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
