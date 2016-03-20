package com.akexorcist.sleepingforless.network.sfl;

import com.akexorcist.sleepingforless.network.blogger.BloggerService;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Akexorcist on 3/10/2016 AD.
 */
public class SleepingForLessConnection {
    private static SleepingForLessConnection connection;

    public static SleepingForLessConnection getInstance() {
        if (connection == null) {
            connection = new SleepingForLessConnection();
        }
        return connection;
    }

    private SleepingForLessService service;

    public SleepingForLessService getConnection() {
        if (service == null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(SleepingForLessUrl.BASE)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
            service = retrofit.create(SleepingForLessService.class);
        }
        return service;
    }
}
