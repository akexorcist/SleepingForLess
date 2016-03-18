package com.akexorcist.sleepingforless.network;

/**
 * Created by Akexorcist on 3/10/2016 AD.
 */
public class BloggerUrl {
    public static final String BASE = "https://www.googleapis.com";
    public static final String SUB_BASE = "/blogger/v3";
    public static final String BLOG = SUB_BASE + "/blogs/byurl" + BloggerUrl.KEY + BloggerUrl.SFL;
    public static final String POST_LIST = SUB_BASE + "/blogs/{blogId}/posts" + BloggerUrl.KEY;
    public static final String POST_BY_ID = SUB_BASE + "/blogs/{blogId}/posts/{postId}" + BloggerUrl.KEY;
    public static final String POST_BY_PATH = SUB_BASE + "/blogs/{blogId}/posts/bypath" + BloggerUrl.KEY;
    public static final String SEARCH = SUB_BASE + "/blogs/{blogId}/posts/search" + BloggerUrl.KEY;
    public static final String SFL = "&url=http://www.akexorcist.com/";
//    public static final String ORDER_BY = "&orderBy=published";
//    public static final String ORDER_BY = "&orderBy=updated";
    public static final String KEY = "?key=" + BloggerKey.KEY;
}
