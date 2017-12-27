package com.akexorcist.sleepingforless.util.content;

import com.akexorcist.sleepingforless.view.post.model.BasePost;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by Akexorcist on 3/24/2016 AD.
 */

@Parcel
public class ContentResult {
    List<BasePost> basePostList;

    public ContentResult() {
    }

    public ContentResult(List<BasePost> basePostList) {
        this.basePostList = basePostList;
    }

    public List<BasePost> getBasePostList() {
        return basePostList;
    }
}
