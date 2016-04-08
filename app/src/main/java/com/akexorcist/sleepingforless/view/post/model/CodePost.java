package com.akexorcist.sleepingforless.view.post.model;

import org.parceler.Parcel;

/**
 * Created by Akexorcist on 3/13/2016 AD.
 */

@Parcel(parcelsIndex = false)
public class CodePost extends BasePost {
    String code;
    String language;

    public CodePost() {
    }

    public CodePost(String raw, String code, String language) {
        setRaw(raw);
        this.code = code.replaceAll("&lt;", "<")
                .replaceAll("&gt;", ">")
                .replaceAll("&amp;", "&");
        this.language = language;
    }

    public String getCode() {
        return code;
    }

    public String getLanguage() {
        return language;
    }
}
