package com.akexorcist.sleepingforless.network;

import com.akexorcist.sleepingforless.network.model.Blog;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Akexorcist on 3/10/2016 AD.
 */
public interface BloggerService {
    @GET(BloggerUrl.BLOG)
    Call<Blog> getBlog();
}
