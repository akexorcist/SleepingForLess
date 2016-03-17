package com.akexorcist.sleepingforless.network;

import com.akexorcist.sleepingforless.bus.BusProvider;
import com.akexorcist.sleepingforless.constant.BloggerConstant;
import com.akexorcist.sleepingforless.network.model.Post;
import com.akexorcist.sleepingforless.network.model.PostFailure;
import com.akexorcist.sleepingforless.network.model.PostList;
import com.akexorcist.sleepingforless.network.model.PostListFailure;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Akexorcist on 3/10/2016 AD.
 */
public class BloggerManager {
    public static final String SORT_PUBLISHED_DATE = "published";
    public static final String SORT_UPDATED_DATE = "updated";

    private static BloggerManager manager;

    public static BloggerManager getInstance() {
        if (manager == null) {
            manager = new BloggerManager();
        }
        return manager;
    }

    public void getPostList(String sortBy) {
        getPostList(sortBy, null, false, true);
    }

    public void getNextPostList(String sortBy, String nextPageToken) {
        getPostList(sortBy, nextPageToken, false, true);
    }

    public void getNextPostList(String nextPageToken) {
        getPostList(null, nextPageToken, false, true);
    }

    public void getNextPostList(String nextPageToken, boolean includeImage) {
        getPostList(null, nextPageToken, false, includeImage);
    }

    public void getNextPostList(String sortBy, String nextPageToken, boolean includeImage) {
        getPostList(sortBy, nextPageToken, false, includeImage);
    }

    public void getPostList(String sortBy, String nextPageToken, boolean includeBody, boolean includeImage) {
        BloggerConnection.getInstance().getConnection().getPostList(BloggerConstant.BLOG_ID, sortBy, 30, includeBody, includeImage, nextPageToken).enqueue(new Callback<PostList>() {
            @Override
            public void onResponse(Call<PostList> call, Response<PostList> response) {
                BusProvider.getInstance().post(response.body());
            }

            @Override
            public void onFailure(Call<PostList> call, Throwable t) {
                BusProvider.getInstance().post(new PostListFailure(t));
            }
        });
    }

    public void getPost(String postId) {
        BloggerConnection.getInstance().getConnection().getPost(BloggerConstant.BLOG_ID, postId).enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                BusProvider.getInstance().post(response.body());
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                BusProvider.getInstance().post(new PostFailure(t));
            }
        });
    }

    public void searchPost(String keyword) {
        BloggerConnection.getInstance().getConnection().searchPost(BloggerConstant.BLOG_ID, keyword, true).enqueue(new Callback<PostList>() {
            @Override
            public void onResponse(Call<PostList> call, Response<PostList> response) {
                BusProvider.getInstance().post(response.body());
            }

            @Override
            public void onFailure(Call<PostList> call, Throwable t) {
                BusProvider.getInstance().post(new PostListFailure(t));
            }
        });
    }

}
