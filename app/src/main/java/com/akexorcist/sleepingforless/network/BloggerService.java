package com.akexorcist.sleepingforless.network;

import com.akexorcist.sleepingforless.network.model.Blog;
import com.akexorcist.sleepingforless.network.model.Post;
import com.akexorcist.sleepingforless.network.model.PostList;
import com.akexorcist.sleepingforless.network.model.SearchResultList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Akexorcist on 3/10/2016 AD.
 */
public interface BloggerService {
    @GET(BloggerUrl.BLOG)
    Call<Blog> getBlog();

    @GET(BloggerUrl.POST_LIST)
    Call<PostList> getPostList(@Path("blogId") String blogId, @Query("orderBy") String orderBy, @Query("maxResults") int max, @Query("fetchBodies") boolean includeBody, @Query("fetchImages") boolean includeImage, @Query("pageToken") String pageToken);

    @GET(BloggerUrl.POST)
    Call<Post> getPost(@Path("blogId") String blogId, @Path("postId") String postId);

    @GET(BloggerUrl.SEARCH)
    Call<PostList> searchPost(@Path("blogId") String blogId, @Query("q") String keyword, @Query("fetchBodies") boolean includeBody);
}
