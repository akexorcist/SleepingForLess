package com.akexorcist.sleepingforless.network.blogger;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Akexorcist on 3/10/2016 AD.
 */
public class BloggerConnection {
    private static BloggerConnection connection;

    public static BloggerConnection getInstance() {
        if (connection == null) {
            connection = new BloggerConnection();
        }
        return connection;
    }

    private BloggerService service;

    public BloggerService getConnection() {
        if (service == null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BloggerUrl.BASE)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
            service = retrofit.create(BloggerService.class);
        }
        return service;
    }
}
