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

    public void addGcmToken(final String token, String serial, final GcmTokenCallback callback) {
        InsertTokenRequest request = new InsertTokenRequest(token, serial);
        SleepingForLessConnection.getInstance().getConnection().addGcmToken(request).enqueue(new Callback<InsertTokenResponse>() {
            @Override
            public void onResponse(Call<InsertTokenResponse> call, Response<InsertTokenResponse> response) {
                if(callback != null) {
                    callback.onTokenAdded(token);
                }
            }

            @Override
            public void onFailure(Call<InsertTokenResponse> call, Throwable t) {
                if(callback != null) {
                    callback.onTokenFailed(token);
                }
            }
        });
    }

    public void removeGcmToken(final String token, final GcmTokenCallback callback) {
        RemoveTokenRequest request = new RemoveTokenRequest(token);
        SleepingForLessConnection.getInstance().getConnection().removeGcmToken(request).enqueue(new Callback<RemoveTokenResponse>() {
            @Override
            public void onResponse(Call<RemoveTokenResponse> call, Response<RemoveTokenResponse> response) {
                if(callback != null) {
                    callback.onTokenRemoved(token);
                }
            }

            @Override
            public void onFailure(Call<RemoveTokenResponse> call, Throwable t) {
                if(callback != null) {
                    callback.onTokenFailed(token);
                }
            }
        });
    }

    public interface GcmTokenCallback {
        void onTokenAdded(String token);
        void onTokenRemoved(String token);
        void onTokenFailed(String token);
    }
}
