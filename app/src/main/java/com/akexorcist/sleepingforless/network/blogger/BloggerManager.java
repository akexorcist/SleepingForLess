package com.akexorcist.sleepingforless.network.blogger;

import com.akexorcist.sleepingforless.bus.BusProvider;
import com.akexorcist.sleepingforless.constant.BloggerConstant;
import com.akexorcist.sleepingforless.network.blogger.model.PostById;
import com.akexorcist.sleepingforless.network.blogger.model.PostByIdFailure;
import com.akexorcist.sleepingforless.network.blogger.model.PostByPath;
import com.akexorcist.sleepingforless.network.blogger.model.PostByPathFailure;
import com.akexorcist.sleepingforless.network.blogger.model.PostList;
import com.akexorcist.sleepingforless.network.blogger.model.PostListFailure;

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

    public void getPostById(String postId) {
        BloggerConnection.getInstance().getConnection().getPostById(BloggerConstant.BLOG_ID, postId).enqueue(new Callback<PostById>() {
            @Override
            public void onResponse(Call<PostById> call, Response<PostById> response) {
                BusProvider.getInstance().post(response.body());
            }

            @Override
            public void onFailure(Call<PostById> call, Throwable t) {
                BusProvider.getInstance().post(new PostByIdFailure(t));
            }
        });
    }

    public void getPostByPath(String postPath) {
        BloggerConnection.getInstance().getConnection().getPostByPath(BloggerConstant.BLOG_ID, postPath).enqueue(new Callback<PostByPath>() {
            @Override
            public void onResponse(Call<PostByPath> call, Response<PostByPath> response) {
                BusProvider.getInstance().post(response.body());
            }

            @Override
            public void onFailure(Call<PostByPath> call, Throwable t) {
                BusProvider.getInstance().post(new PostByPathFailure(t));
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
