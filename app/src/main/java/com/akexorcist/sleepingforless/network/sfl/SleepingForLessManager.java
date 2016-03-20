package com.akexorcist.sleepingforless.network.sfl;

import com.akexorcist.sleepingforless.bus.BusProvider;
import com.akexorcist.sleepingforless.network.sfl.model.InsertTokenRequest;
import com.akexorcist.sleepingforless.network.sfl.model.InsertTokenResponse;
import com.akexorcist.sleepingforless.network.sfl.model.InsertTokenResponseFailure;
import com.akexorcist.sleepingforless.network.sfl.model.RemoveTokenRequest;
import com.akexorcist.sleepingforless.network.sfl.model.RemoveTokenResponse;

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
        InsertTokenRequest request = new InsertTokenRequest(token, serial);
        SleepingForLessConnection.getInstance().getConnection().addGcmToken(request).enqueue(new Callback<InsertTokenResponse>() {
            @Override
            public void onResponse(Call<InsertTokenResponse> call, Response<InsertTokenResponse> response) {
                BusProvider.getInstance().post(response.body());
            }

            @Override
            public void onFailure(Call<InsertTokenResponse> call, Throwable t) {
                BusProvider.getInstance().post(new InsertTokenResponseFailure(t));
            }
        });
    }

    public void removeGcmToken(String token) {
        RemoveTokenRequest request = new RemoveTokenRequest(token);
        SleepingForLessConnection.getInstance().getConnection().removeGcmToken(request).enqueue(new Callback<RemoveTokenResponse>() {
            @Override
            public void onResponse(Call<RemoveTokenResponse> call, Response<RemoveTokenResponse> response) {
            }

            @Override
            public void onFailure(Call<RemoveTokenResponse> call, Throwable t) {
            }
        });
    }
}
