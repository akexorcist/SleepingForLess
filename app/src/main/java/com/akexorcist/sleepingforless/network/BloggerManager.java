package com.akexorcist.sleepingforless.network;

import com.akexorcist.sleepingforless.bus.BusProvider;
import com.akexorcist.sleepingforless.constant.BloggerConstant;
import com.akexorcist.sleepingforless.network.model.Blog;
import com.akexorcist.sleepingforless.network.model.Failure;
import com.akexorcist.sleepingforless.network.model.Post;
import com.akexorcist.sleepingforless.network.model.PostList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Akexorcist on 3/10/2016 AD.
 */
public class BloggerManager {
    private static BloggerManager manager;

    public static BloggerManager getInstance() {
        if (manager == null) {
            manager = new BloggerManager();
        }
        return manager;
    }

    public void getBlog() {
        BloggerConnection.getInstance().getConnection().getBlog().enqueue(new Callback<Blog>() {
            @Override
            public void onResponse(Call<Blog> call, Response<Blog> response) {
                BusProvider.getInstance().post(response.body());
            }

            @Override
            public void onFailure(Call<Blog> call, Throwable t) {
                BusProvider.getInstance().post(new Failure(t));
            }
        });
    }

    public void getPostList() {
        BloggerConnection.getInstance().getConnection().getPostList(BloggerConstant.BLOG_ID, 10, false, true).enqueue(new Callback<PostList>() {
            @Override
            public void onResponse(Call<PostList> call, Response<PostList> response) {
                BusProvider.getInstance().post(response.body());
            }

            @Override
            public void onFailure(Call<PostList> call, Throwable t) {
                BusProvider.getInstance().post(new Failure(t));
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
                BusProvider.getInstance().post(new Failure(t));
            }
        });
    }

}
