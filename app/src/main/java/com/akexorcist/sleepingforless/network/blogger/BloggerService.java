package com.akexorcist.sleepingforless.network.blogger;

import com.akexorcist.sleepingforless.network.blogger.model.Blog;
import com.akexorcist.sleepingforless.network.blogger.model.PostById;
import com.akexorcist.sleepingforless.network.blogger.model.PostByPath;
import com.akexorcist.sleepingforless.network.blogger.model.PostList;

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
    Call<PostList> getPostList(@Path("blogId") String blogId,
                               @Query("orderBy") String orderBy,
                               @Query("maxResults") int max,
                               @Query("fetchBodies") boolean includeBody,
                               @Query("fetchImages") boolean includeImage,
                               @Query("pageToken") String pageToken);

    @GET(BloggerUrl.POST_BY_ID)
    Call<PostById> getPostById(@Path("blogId") String blogId,
                               @Path("postId") String postId);

    @GET(BloggerUrl.POST_BY_PATH)
    Call<PostByPath> getPostByPath(@Path("blogId") String blogId,
                                   @Query("path") String postPath);

    @GET(BloggerUrl.SEARCH)
    Call<PostList> searchPost(@Path("blogId") String blogId,
                              @Query("q") String keyword,
                              @Query("fetchBodies") boolean includeBody);
}
