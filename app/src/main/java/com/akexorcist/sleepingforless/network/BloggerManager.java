package com.akexorcist.sleepingforless.network;

import com.akexorcist.sleepingforless.bus.BusProvider;
import com.akexorcist.sleepingforless.network.model.Blog;
import com.akexorcist.sleepingforless.network.model.Failure;

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

}
