package com.akexorcist.sleepingforless.network.sfl;

import com.akexorcist.sleepingforless.bus.BusProvider;
import com.akexorcist.sleepingforless.constant.BloggerConstant;
import com.akexorcist.sleepingforless.network.blogger.BloggerConnection;
import com.akexorcist.sleepingforless.network.blogger.model.PostById;
import com.akexorcist.sleepingforless.network.blogger.model.PostByIdFailure;
import com.akexorcist.sleepingforless.network.blogger.model.PostByPath;
import com.akexorcist.sleepingforless.network.blogger.model.PostByPathFailure;
import com.akexorcist.sleepingforless.network.blogger.model.PostList;
import com.akexorcist.sleepingforless.network.blogger.model.PostListFailure;
import com.akexorcist.sleepingforless.network.sfl.model.GcmTokenRequest;
import com.akexorcist.sleepingforless.network.sfl.model.GcmTokenResponse;
import com.akexorcist.sleepingforless.network.sfl.model.GcmTokenResponseFailure;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Akexorcist on 3/10/2016 AD.
 */
public class SleepingForLessManager {
    private static SleepingForLessManager manager;

    public static SleepingForLessManager getInstance() {
        if (manager == null) {
            manager = new SleepingForLessManager();
        }
        return manager;
    }

    public void addGcmToken(String token, String serial) {
        GcmTokenRequest request = new GcmTokenRequest(token, serial);
        SleepingForLessConnection.getInstance().getConnection().addGcmToken(request).enqueue(new Callback<GcmTokenResponse>() {
            @Override
            public void onResponse(Call<GcmTokenResponse> call, Response<GcmTokenResponse> response) {
                BusProvider.getInstance().post(response.body());
            }

            @Override
            public void onFailure(Call<GcmTokenResponse> call, Throwable t) {
                BusProvider.getInstance().post(new GcmTokenResponseFailure(t));
            }
        });
    }
}
