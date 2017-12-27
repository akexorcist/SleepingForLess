package com.akexorcist.sleepingforless.view.search;

import org.parceler.Parcel;

/**
 * Created by Akexorcist on 3/14/2016 AD.
 */

@Parcel
public class SearchRequest {
    String keyword;

    public SearchRequest() {
    }

    public SearchRequest(String keyword) {
        this.keyword = keyword;
    }

    public String getKeyword() {
        return keyword;
    }
}
